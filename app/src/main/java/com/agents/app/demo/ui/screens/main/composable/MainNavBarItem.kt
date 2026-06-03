package com.agents.app.demo.ui.screens.main.composable

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.agents.app.demo.ui.screens.main.BottomBarDestination
import com.agents.app.demo.ui.theme.appColorsScheme

@Composable
fun RowScope.MainNavBarItem(isSelected: Boolean, destination: BottomBarDestination, onClick: () -> Unit) {
    NavigationBarItem(
        selected = isSelected,
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(destination.icon),
                contentDescription = stringResource(destination.label),
            )
        },
        label = {
            Text(
                text = stringResource(destination.label),
                color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.appColorsScheme.textSecondary
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.secondary,
            unselectedIconColor = MaterialTheme.appColorsScheme.textSecondary,
            indicatorColor = MaterialTheme.appColorsScheme.transparent
        ),
    )
}
