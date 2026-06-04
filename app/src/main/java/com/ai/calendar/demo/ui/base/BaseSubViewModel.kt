package com.ai.calendar.demo.ui.base

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.getAndUpdate
import kotlinx.coroutines.launch

abstract class BaseSubViewModel<SvmState, SvmIntent, SvmEffect>(
    initialState: SvmState,
    private val subViewModels: List<BaseSubViewModel<*, *, *>> = emptyList(),
) {
    private var isAttachedToViewModel: Boolean = false
    private lateinit var _scope: CoroutineScope
    protected val scope: CoroutineScope
        get() {
            check(isAttachedToViewModel) { "SubViewModel has not been attached to a parent ViewModel" }
            return _scope
        }

    val uiStateFlow: StateFlow<SvmState> = MutableStateFlow(initialState)
    val uiEffectFlow: SharedFlow<SvmEffect> = MutableSharedFlow()
    protected val uiState: SvmState get() = uiStateFlow.value

    fun attach(scope: CoroutineScope, restoredState: SvmState? = null) {
        _scope = scope
        isAttachedToViewModel = true
        restoredState?.let { (uiStateFlow as MutableStateFlow).value = it }
        subViewModels.forEach { it.attach(scope) }
        onAttached()
    }

    open fun onUserIntent(intent: SvmIntent) = reduceIntent(intent)

    protected open fun onAttached() {}

    protected abstract fun reduceIntent(intent: SvmIntent)

    protected fun updateUiState(update: (SvmState) -> SvmState) {
        (uiStateFlow as MutableStateFlow).getAndUpdate(update)
    }

    protected fun sendUiEffect(effect: SvmEffect) {
        launchSvmScope { (uiEffectFlow as MutableSharedFlow).emit(effect) }
    }

    protected fun launchSvmScope(action: suspend () -> Unit) = scope.launch { action() }
}
