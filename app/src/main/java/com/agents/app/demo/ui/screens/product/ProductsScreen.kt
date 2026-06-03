package com.agents.app.demo.ui.screens.product

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.agents.app.demo.R
import com.agents.app.demo.ui.core.composable.button.BtnPrimary
import com.agents.app.demo.ui.theme.marginPrimary2X

@Composable
fun ProductsScreen(viewModel: ProductsViewModel = hiltViewModel()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(marginPrimary2X),
    ) {
        Text(modifier = Modifier.align(Alignment.Center), text = stringResource(R.string.mainBniProducts))

        BtnPrimary(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            text = "Go to Product Details =>",
            onClick = {
                viewModel.onUserIntent(ProductsIntent.ProductDetailsBtnClicked)
            }
        )
    }
}
