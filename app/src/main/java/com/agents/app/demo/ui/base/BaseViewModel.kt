package com.agents.app.demo.ui.base

import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agents.app.demo.ui.core.error.UiErrorParser
import com.agents.app.demo.ui.core.error.model.UiError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val UI_STATE_KEY = "ui_state"
private const val INITIALIZED_KEY = "initialized"
private const val INITIALIZED_VALUE = "true"

abstract class BaseViewModel<STATE, INTENT, EFFECT>(
    initialState: STATE,
    private val savedStateHandle: SavedStateHandle,
    subViewModels: List<SubViewModelEntry<STATE, *>> = emptyList(),
) : ViewModel() {
    @Inject
    lateinit var uiErrorParser: UiErrorParser

    protected abstract fun reduceIntent(intent: INTENT)

    val uiStateFlow: StateFlow<STATE> = MutableStateFlow(restoreState() ?: initialState)
    val uiEffectFlow: SharedFlow<EFFECT> = MutableSharedFlow()
    protected val uiState get() = uiStateFlow.value

    /**
     * `true` when this ViewModel is being recreated after process death.
     * Screens whose state cannot be reconstructed (e.g. PDF viewers holding a runtime renderer)
     * can check this in `init` and navigate back instead of rendering an unusable empty state.
     */
    protected val isProcessDeathRestoration: Boolean = savedStateHandle.contains(INITIALIZED_KEY)

    init {
        savedStateHandle[INITIALIZED_KEY] = INITIALIZED_VALUE
        subViewModels.forEach { entry -> attachSubViewModel(entry) }
    }

    fun onUserIntent(intent: INTENT) = reduceIntent(intent)

    protected fun updateUiState(update: (STATE) -> STATE) {
        (uiStateFlow as MutableStateFlow).getAndUpdate(update)
        saveState(uiStateFlow.value)
    }

    protected fun sendUiEffect(effect: EFFECT) = launchViewModelScope {
        (uiEffectFlow as MutableSharedFlow).emit(effect)
    }

    protected fun launchViewModelScope(action: suspend () -> Unit) = viewModelScope.launch { action() }
    protected fun parseError(error: Throwable?): UiError = uiErrorParser.parse(error)

    private fun <SvmState> attachSubViewModel(svmEntry: SubViewModelEntry<STATE, SvmState>) {
        with(svmEntry) {
            val restoredSvmState = getSvmState?.invoke(uiState).takeIf { isProcessDeathRestoration }
            subViewModel.attach(viewModelScope, restoredSvmState)
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun restoreState(): STATE? {
        return savedStateHandle.get<Parcelable>(UI_STATE_KEY) as? STATE
    }

    private fun saveState(state: STATE) {
        savedStateHandle[UI_STATE_KEY] = state as? Parcelable ?: return
    }
}
