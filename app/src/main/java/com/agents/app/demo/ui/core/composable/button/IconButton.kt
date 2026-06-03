package com.agents.app.demo.ui.core.composable.button

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.agents.app.demo.ui.core.modifier.modifyIf
import com.agents.app.demo.ui.theme.appColorsScheme
import com.agents.app.demo.ui.theme.borderWidthDefault
import com.agents.app.demo.ui.theme.buttonIconSize
import com.agents.app.demo.ui.theme.filledIconButtonShape
import com.agents.app.demo.ui.theme.iconButtonSize
import com.agents.app.demo.ui.theme.iconButtonSizeLarge

@Composable
fun IconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color = MaterialTheme.appColorsScheme.iconPrimary,
    @DrawableRes icon: Int,
    isBorder: Boolean = false,
    isEnabled: Boolean = true,
    borderShape: RoundedCornerShape = filledIconButtonShape,
    size: Dp = if (isBorder) iconButtonSizeLarge else iconButtonSize,
    iconSize: Dp = buttonIconSize,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val pressedColor = color.copy(alpha = 0.5f)

    IconButton(
        modifier = modifier
            .size(size)
            .modifyIf(isBorder) {
                background(
                    color = MaterialTheme.colorScheme.background,
                    shape = borderShape
                ).border(
                    width = borderWidthDefault,
                    color = if (isEnabled) {
                        MaterialTheme.appColorsScheme.borderIconBtn
                    } else {
                        MaterialTheme.appColorsScheme.borderInputDisabled
                    },
                    shape = borderShape
                )
            },
        onClick = onClick,
        shape = if (isBorder) borderShape else CircleShape,
        interactionSource = interactionSource,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = if (pressed) pressedColor else color,
            containerColor = if (pressed) pressedColor else MaterialTheme.appColorsScheme.transparent,
            disabledContentColor = MaterialTheme.appColorsScheme.textPrimaryDisabled
        ),
        enabled = isEnabled
    ) {
        Icon(
            modifier = Modifier.size(iconSize),
            painter = painterResource(id = icon),
            contentDescription = null,
        )
    }
}
