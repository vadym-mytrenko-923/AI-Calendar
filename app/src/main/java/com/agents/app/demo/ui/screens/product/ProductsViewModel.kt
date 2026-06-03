package com.agents.app.demo.ui.screens.product

import androidx.lifecycle.SavedStateHandle
import com.agents.app.demo.ui.base.BaseViewModel
import com.agents.app.demo.ui.navigation.main.MainNavigator
import com.agents.app.demo.ui.navigation.model.MainNavRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mainNavigator: MainNavigator
) : BaseViewModel<ProductsState, ProductsIntent, Nothing>(
    initialState = ProductsState(),
    savedStateHandle = savedStateHandle
) {
    override fun reduceIntent(intent: ProductsIntent) {
        launchViewModelScope {
            when (intent) {
                ProductsIntent.ProductDetailsBtnClicked -> {
                    mainNavigator.navigateTo(MainNavRoute.ProductDetails(id = 0))
                }
            }
        }
    }
}
