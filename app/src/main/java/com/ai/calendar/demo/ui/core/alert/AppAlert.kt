package com.ai.calendar.demo.ui.core.alert

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import com.ai.calendar.demo.R
import com.ai.calendar.demo.ui.core.alert.model.AppAlertParams
import com.ai.calendar.demo.ui.core.alert.model.AppAlertType
import com.ai.calendar.demo.ui.core.composable.button.IconButton
import com.ai.calendar.demo.ui.core.composable.button.SecondaryTextButton
import com.ai.calendar.demo.ui.core.modifier.modifyIf
import com.ai.calendar.demo.ui.theme.AppIcons
import com.ai.calendar.demo.ui.theme.alertShapeDefault
import com.ai.calendar.demo.ui.theme.appAlertSwipeThresholdSize
import com.ai.calendar.demo.ui.theme.appColorsScheme
import com.ai.calendar.demo.ui.theme.defaultIconSize
import com.ai.calendar.demo.ui.theme.marginPrimary1_5X
import com.ai.calendar.demo.ui.theme.marginPrimary2X
import com.ai.calendar.demo.ui.theme.marginPrimary3X
import com.ai.calendar.demo.ui.theme.marginPrimaryHalf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class AppAlertController internal constructor(
    private val hostState: SnackbarHostState,
    private val scope: CoroutineScope
) {
    fun showAlert(
        title: String? = null,
        message: String? = null,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        icon: Int? = AppIcons.Warning,
        type: AppAlertType = AppAlertType.Error
    ) {
        scope.launch {
            hostState.showSnackbar(
                AppAlertParams(
                    message = "",
                    actionLabel = actionLabel,
                    withDismissAction = withDismissAction,
                    title = title,
                    description = message,
                    icon = icon,
                    type = type
                )
            )
        }
    }
}

val LocalAppAlert = staticCompositionLocalOf<AppAlertController> {
    error("No AppAlertController provided")
}

@Composable
fun AppAlertProvider(content: @Composable () -> Unit) {
    val alertHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val alertController = remember(alertHostState, scope) {
        AppAlertController(alertHostState, scope)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
    ) {
        CompositionLocalProvider(LocalAppAlert provides alertController) {
            content()
        }
        SnackbarHost(
            hostState = alertHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .systemBarsPadding(),
            snackbar = { data ->
                val alertParams = (data.visuals as? AppAlertParams) ?: return@SnackbarHost
                val title = alertParams.title ?: stringResource(R.string.errorGenericOops)
                val dismissThresholdPx = with(density) { appAlertSwipeThresholdSize.toPx() }
                val offsetY = remember(data) { Animatable(0f) }

                Row(
                    modifier = Modifier
                        .offset { IntOffset(0, offsetY.value.roundToInt()) }
                        .draggable(
                            orientation = Orientation.Vertical,
                            state = rememberDraggableState { delta ->
                                val newValue = (offsetY.value + delta).coerceAtMost(0f)
                                scope.launch { offsetY.snapTo(newValue) }
                            },
                            onDragStopped = {
                                scope.launch {
                                    if (offsetY.value <= -dismissThresholdPx) {
                                        data.dismiss()
                                    } else {
                                        offsetY.animateTo(
                                            targetValue = 0f,
                                            animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                                        )
                                    }
                                }
                            }
                        )
                        .fillMaxWidth()
                        .padding(horizontal = marginPrimary3X)
                        .background(
                            if (alertParams.type == AppAlertType.Error) {
                                MaterialTheme.colorScheme.error
                            } else {
                                MaterialTheme.appColorsScheme.backgroundSuccess
                            },
                            shape = alertShapeDefault
                        )
                        .modifyIf(alertParams.type == AppAlertType.Success) {
                            background(
                                color = Color.Black.copy(alpha = 0.2f),
                                shape = alertShapeDefault
                            )
                        }
                        .padding(horizontal = marginPrimary3X, vertical = marginPrimary2X),
                    horizontalArrangement = Arrangement.spacedBy(marginPrimary1_5X)
                ) {
                    alertParams.icon?.let {
                        Icon(
                            modifier = Modifier.size(defaultIconSize),
                            painter = painterResource(it),
                            contentDescription = null,
                            tint = MaterialTheme.appColorsScheme.iconOnDark
                        )
                    }

                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(marginPrimaryHalf)
                    ) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.appColorsScheme.textPrimaryOnDark
                        )

                        alertParams.description?.let {
                            Text(
                                text = it,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.appColorsScheme.textPrimaryOnDark
                            )
                        }
                    }

                    when {
                        alertParams.withDismissAction -> {
                            IconButton(
                                onClick = { data.dismiss() },
                                color = MaterialTheme.appColorsScheme.textPrimaryOnDark,
                                icon = AppIcons.Close
                            )
                        }

                        alertParams.actionLabel != null -> {
                            SecondaryTextButton(
                                text = alertParams.actionLabel,
                                onClick = { data.performAction() }
                            )
                        }
                    }
                }
            },
        )
    }
}
