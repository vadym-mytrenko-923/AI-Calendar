package com.agents.app.demo.ui.screens.product.details

import androidx.lifecycle.SavedStateHandle
import androidx.navigation.toRoute
import com.agents.app.demo.ui.base.BaseViewModel
import com.agents.app.demo.ui.base.SubViewModelEntry
import com.agents.app.demo.ui.navigation.main.MainNavigator
import com.agents.app.demo.ui.navigation.model.MainNavRoute
import com.agents.app.demo.ui.screens.product.details.favorite.FavoriteSubViewModel
import com.agents.app.demo.ui.screens.product.details.favorite.FavoriteSvmIntentHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProductDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val mainNavigator: MainNavigator,
    private val favoriteSubViewModel: FavoriteSubViewModel,
) : BaseViewModel<ProductDetailsState, ProductDetailsIntent, Nothing>(
    initialState = ProductDetailsState(),
    savedStateHandle = savedStateHandle,
    subViewModels = listOf(
        SubViewModelEntry(favoriteSubViewModel) { it.favoriteState },
    ),
),
    FavoriteSvmIntentHandler by favoriteSubViewModel {

    init {
        if (!isProcessDeathRestoration) {
            updateUiState { it.copy(id = savedStateHandle.toRoute<MainNavRoute.ProductDetails>().id) }
        }
        observeSubViewModelStates()
    }

    override fun reduceIntent(intent: ProductDetailsIntent) {
        when (intent) {
            ProductDetailsIntent.BackBtnClicked -> launchViewModelScope { mainNavigator.back() }
            ProductDetailsIntent.ReturnToBtnClicked -> launchViewModelScope {
                mainNavigator.returnTo(
                    MainNavRoute.ProductsList
                )
            }
            ProductDetailsIntent.ProductDetailsBtnClicked -> launchViewModelScope {
                mainNavigator.navigateTo(MainNavRoute.ProductDetails(id = uiState.id + 1))
            }
        }
    }

    private fun observeSubViewModelStates() {
        launchViewModelScope {
            favoriteSubViewModel.uiStateFlow.collect { favoriteState ->
                updateUiState { uiState.copy(favoriteState = favoriteState) }
            }
        }
    }
}
