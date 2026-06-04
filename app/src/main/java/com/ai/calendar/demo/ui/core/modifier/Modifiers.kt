package com.ai.calendar.demo.ui.core.modifier

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ai.calendar.demo.utils.Constants

@Composable
fun Modifier.modifyIf(condition: Boolean, modifier: @Composable Modifier.() -> Modifier): Modifier {
    return if (condition) this.then(modifier(Modifier)) else this
}

inline fun Modifier.noRippleClickable(
    noinline onClick: () -> Unit,
    interactionSource: MutableInteractionSource
): Modifier = composed {
    clickable(
        indication = null,
        interactionSource = interactionSource,
        onClick = onClick
    )
}

@Composable
fun Modifier.shimmer(cornerRadius: Dp = 0.dp, isLoading: Boolean): Modifier {
    return if (isLoading) this.shimmer(cornerRadius = cornerRadius) else this
}

@Composable
private fun Modifier.shimmer(cornerRadius: Dp = 0.dp): Modifier {
    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.3f),
        Color.White.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.3f)
    )

    val transition = rememberInfiniteTransition(label = "Shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -400f,
        targetValue = 1200f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1600, // slower = smoother
                easing = FastOutSlowInEasing // smoother easing
            )
        ),
        label = "Translate"
    )

    return this.drawWithCache {
        val brush = Brush.linearGradient(
            colors = shimmerColors,
            start = Offset(translateAnim, 0f),
            // wider gradient
            end = Offset(translateAnim + size.width / 1.5f, size.height)
        )
        val cornerPx = cornerRadius.toPx()
        onDrawWithContent {
            drawRoundRect(
                brush = brush,
                cornerRadius = CornerRadius(cornerPx, cornerPx),
                size = size
            )
        }
    }
}

fun Modifier.easterEggClick(
    intervalMillis: Long = Constants.EASTER_EGG_INTERVAL,
    requiredClicks: Int = Constants.EASTER_EGG_REQUIRED_CLICKS,
    onEasterEgg: () -> Unit
): Modifier = composed {
    var lastClickTime by remember { mutableStateOf(0L) }
    var clickCounter by remember { mutableStateOf(0) }

    this.pointerInput(requiredClicks, intervalMillis) {
        detectTapGestures(
            onTap = {
                val now = System.currentTimeMillis()
                if (now - lastClickTime > intervalMillis) {
                    clickCounter = 0
                }
                clickCounter++
                if (clickCounter >= requiredClicks) {
                    onEasterEgg()
                    clickCounter = 0
                }
                lastClickTime = now
            }
        )
    }
}
