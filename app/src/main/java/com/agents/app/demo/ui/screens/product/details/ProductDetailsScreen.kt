package com.agents.app.demo.ui.screens.product.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.agents.app.demo.R
import com.agents.app.demo.ui.core.composable.button.BtnPrimary
import com.agents.app.demo.ui.core.composable.button.IcBtnBack
import com.agents.app.demo.ui.screens.product.details.favorite.FavoriteSvmIntent
import com.agents.app.demo.ui.screens.product.details.favorite.FavoriteSvmState
import com.agents.app.demo.ui.screens.product.details.favorite.composable.FavoriteSection
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.marginPrimary2X

@Composable
fun ProductDetailsScreen(viewModel: ProductDetailsViewModel = hiltViewModel()) {
    val state by viewModel.uiStateFlow.collectAsState()

    ProductDetailsContent(
        state = state,
        onIntent = viewModel::onUserIntent,
        onFavoriteIntent = viewModel::onUserIntent,
    )
}

@Composable
private fun ProductDetailsContent(
    state: ProductDetailsState,
    onIntent: (ProductDetailsIntent) -> Unit = {},
    onFavoriteIntent: (FavoriteSvmIntent) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(marginPrimary2X),
        verticalArrangement = Arrangement.spacedBy(marginPrimary2X)
    ) {
        IcBtnBack(onClick = { onIntent(ProductDetailsIntent.BackBtnClicked) })

        Text(text = stringResource(R.string.productDetailsTitleFormat, state.id))

        FavoriteSection(
            state = state.favoriteState,
            onIntent = onFavoriteIntent,
        )

        Spacer(Modifier.weight(1f))

        BtnPrimary(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.productDetailsBtnReturnToList),
            onClick = { onIntent(ProductDetailsIntent.ReturnToBtnClicked) },
        )

        BtnPrimary(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.productDetailsBtnGoToDetails),
            onClick = { onIntent(ProductDetailsIntent.ProductDetailsBtnClicked) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductDetailsContentPreview() {
    AiAgenticAppTheme {
        ProductDetailsContent(
            state = ProductDetailsState(id = 1, favoriteState = FavoriteSvmState(isFavorite = true)),
        )
    }
}
