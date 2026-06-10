package com.ai.calendar.demo.ui.screens.chat.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ai.calendar.demo.R
import com.ai.calendar.demo.ui.screens.chat.AiChatIntent
import com.ai.calendar.demo.ui.screens.chat.AiChatState
import com.ai.calendar.demo.ui.screens.chat.composable.preview.AiChatPreviewData
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.chatMinHeight
import com.ai.calendar.demo.ui.theme.marginPrimary
import com.ai.calendar.demo.ui.theme.marginPrimary1_5X
import com.ai.calendar.demo.ui.theme.marginPrimary2X
import com.ai.calendar.demo.ui.theme.marginZero
import com.ai.calendar.demo.ui.theme.smallIconSize

@Composable
fun AiChatContent(
    modifier: Modifier = Modifier,
    bottomPadding: Dp = marginZero,
    state: AiChatState,
    onIntent: (AiChatIntent) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(state.messages.size, state.isLoading) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .padding(bottom = marginPrimary2X + bottomPadding),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = marginPrimary2X),
            text = stringResource(R.string.aiChatTitle),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black,
        )

        AnimatedVisibility(visible = state.isDownloading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = marginPrimary2X, vertical = marginPrimary),
            ) {
                Text(
                    text = stringResource(R.string.aiChatDownloadingModel, state.downloadProgress ?: 0),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(marginPrimary))
                LinearProgressIndicator(
                    progress = { (state.downloadProgress ?: 0) / 100f },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        Spacer(modifier = Modifier.height(marginPrimary2X))

        if (state.messages.isEmpty() && !state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = chatMinHeight),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.aiChatEmptyState),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .defaultMinSize(minHeight = chatMinHeight)
                    .fillMaxWidth()
                    .padding(horizontal = marginPrimary2X),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(marginPrimary1_5X),
                contentPadding = PaddingValues(vertical = marginPrimary),
            ) {
                items(state.messages) { message ->
                    ChatBubble(message = message)
                }

                if (state.isLoading) {
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(smallIconSize),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(marginPrimary2X))

        ChatInputBar(
            input = state.input,
            isSendEnabled = state.isSendButtonEnabled,
            onInputChanged = { onIntent(AiChatIntent.InputChanged(it)) },
            onSendClicked = { onIntent(AiChatIntent.SendClicked) },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AiChatContentEmptyPreview() {
    AiCalendarTheme {
        AiChatContent(state = AiChatPreviewData.emptyState, onIntent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun AiChatContentWithMessagesPreview() {
    AiCalendarTheme {
        AiChatContent(
            state = AiChatPreviewData.loadingState,
            onIntent = {},
        )
    }
}
