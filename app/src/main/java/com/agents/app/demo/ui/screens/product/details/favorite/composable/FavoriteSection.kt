package com.agents.app.demo.ui.screens.product.details.favorite.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.agents.app.demo.R
import com.agents.app.demo.ui.screens.product.details.favorite.FavoriteSvmIntent
import com.agents.app.demo.ui.screens.product.details.favorite.FavoriteSvmState
import com.agents.app.demo.ui.theme.AiAgenticAppTheme

@Composable
fun FavoriteSection(
    state: FavoriteSvmState,
    onIntent: (FavoriteSvmIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.productDetailsFavorite),
                style = MaterialTheme.typography.bodyMedium,
            )
            Switch(
                checked = state.isFavorite,
                onCheckedChange = { onIntent(FavoriteSvmIntent.ToggleFavorite) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteSectionPreview() {
    AiAgenticAppTheme {
        FavoriteSection(state = FavoriteSvmState(), onIntent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun FavoriteSectionCheckedPreview() {
    AiAgenticAppTheme {
        FavoriteSection(state = FavoriteSvmState(isFavorite = true), onIntent = {})
    }
}
