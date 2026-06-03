package com.agents.app.demo.ui.core.modifier

import android.graphics.BlurMaskFilter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import com.agents.app.demo.ui.theme.appColorsScheme
import com.agents.app.demo.ui.theme.marginPrimary
import com.agents.app.demo.ui.theme.marginPrimary2X
import com.agents.app.demo.ui.theme.marginPrimaryHalf
import com.agents.app.demo.ui.theme.marginPrimarySmallest
import com.agents.app.demo.ui.theme.marginZero
import com.agents.app.demo.ui.theme.mediumRadius
import android.graphics.Paint as AndroidPaint

// TODO: this shadow could be adjusted to the project's default shadow that is applied to cards/list items.
@Composable
fun Modifier.cardShadow(
    radius: Dp = mediumRadius,
    color: Color = MaterialTheme.appColorsScheme.cardShadow,
): Modifier = this.shadowGlow(
    color = color,
    borderRadius = radius,
    offsetY = marginPrimarySmallest,
    blurRadius = marginPrimary2X
)

@Suppress("ComplexCondition")
@Composable
fun Modifier.shadowGlow(
    color: Color,
    borderRadius: Dp = marginZero,
    blurRadius: Dp = marginPrimary,
    offsetX: Dp = marginZero,
    offsetY: Dp = marginPrimaryHalf,
    spread: Dp = marginZero,
): Modifier = composed {
    this.then(
        Modifier.drawBehind {
            val spreadPx = spread.toPx()
            val blurRadiusPx = blurRadius.toPx()

            val offsetXPx = offsetX.toPx()
            val offsetYPx = offsetY.toPx()
            val shadowBorderRadiusPx = borderRadius.toPx()

            val shadowColorArgb = color.toArgb()

            if (color.alpha == 0f && blurRadiusPx <= 0f && spreadPx == 0f && offsetXPx == 0f && offsetYPx == 0f) {
                return@drawBehind
            }

            val frameworkPaint = AndroidPaint().apply {
                isAntiAlias = true
                style = AndroidPaint.Style.FILL
                this.color = shadowColorArgb
                if (blurRadiusPx > 0f) {
                    maskFilter = BlurMaskFilter(blurRadiusPx, BlurMaskFilter.Blur.NORMAL)
                }
            }
            val left = -spreadPx + offsetXPx
            val top = -spreadPx + offsetYPx
            val right = size.width + spreadPx + offsetXPx
            val bottom = size.height + spreadPx + offsetYPx

            drawShadowShape(left, top, right, bottom, shadowBorderRadiusPx, frameworkPaint)
        }
    )
}

private fun DrawScope.drawShadowShape(left: Float, top: Float, right: Float, bottom: Float, cornerRadiusPx: Float, paint: AndroidPaint) {
    drawIntoCanvas { canvas ->
        canvas.nativeCanvas.drawRoundRect(left, top, right, bottom, cornerRadiusPx, cornerRadiusPx, paint)
    }
}
