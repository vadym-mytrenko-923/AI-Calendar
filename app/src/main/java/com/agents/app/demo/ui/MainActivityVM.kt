package com.agents.app.demo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agents.app.demo.domain.features.auth.usecase.IsUserLoggedInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val isUserLoggedInUseCase: IsUserLoggedInUseCase,
) : ViewModel() {
    val isUserLoggedInFlow: StateFlow<Boolean?> =
        isUserLoggedInUseCase().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )
}
