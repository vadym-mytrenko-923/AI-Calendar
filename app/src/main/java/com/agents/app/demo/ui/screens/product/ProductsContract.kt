package com.agents.app.demo.ui.screens.product

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductsState(val isLoading: Boolean = false) : Parcelable

sealed interface ProductsIntent {
    data object ProductDetailsBtnClicked : ProductsIntent
}
