package com.agents.app.demo.ui.screens.product.details.favorite

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteSvmState(
    val isFavorite: Boolean = false,
) : Parcelable

sealed interface FavoriteSvmIntent {
    data object ToggleFavorite : FavoriteSvmIntent
}

sealed interface FavoriteSvmEffect {
    data class FavoriteChanged(val isFavorite: Boolean) : FavoriteSvmEffect
}

fun interface FavoriteSvmIntentHandler {
    fun onUserIntent(intent: FavoriteSvmIntent)
}
