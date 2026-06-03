package com.agents.app.demo.ui.core.composable.button

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.agents.app.demo.ui.theme.AppIcons
import com.agents.app.demo.ui.theme.appColorsScheme

@Composable
fun IcBtnBack(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.appColorsScheme.iconPrimary,
    onClick: () -> Unit,
    isBorder: Boolean = false
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
        color = color,
        icon = AppIcons.ArrowBack,
        isBorder = isBorder
    )
}
