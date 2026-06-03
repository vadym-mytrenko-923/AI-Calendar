package com.agents.app.demo.ui.screens.product.details

import android.os.Parcelable
import com.agents.app.demo.ui.screens.product.details.favorite.FavoriteSvmState
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDetailsState(
    val id: Int = 0,
    val favoriteState: FavoriteSvmState = FavoriteSvmState(),
) : Parcelable

sealed interface ProductDetailsIntent {
    data object BackBtnClicked : ProductDetailsIntent
    data object ReturnToBtnClicked : ProductDetailsIntent
    data object ProductDetailsBtnClicked : ProductDetailsIntent
}
