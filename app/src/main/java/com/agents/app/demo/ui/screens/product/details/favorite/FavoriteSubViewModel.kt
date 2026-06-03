package com.agents.app.demo.ui.screens.product.details.favorite

import com.agents.app.demo.ui.base.BaseSubViewModel
import javax.inject.Inject

class FavoriteSubViewModel @Inject constructor() :
    BaseSubViewModel<FavoriteSvmState, FavoriteSvmIntent, FavoriteSvmEffect>(initialState = FavoriteSvmState()),
    FavoriteSvmIntentHandler {

    override fun reduceIntent(intent: FavoriteSvmIntent) {
        when (intent) {
            FavoriteSvmIntent.ToggleFavorite -> onToggleFavorite()
        }
    }

    private fun onToggleFavorite() {
        val newValue = !uiState.isFavorite
        updateUiState { it.copy(isFavorite = newValue) }
        sendUiEffect(FavoriteSvmEffect.FavoriteChanged(newValue))
    }
}
