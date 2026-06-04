package com.ai.calendar.demo.ui.screens.chat.composable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.ai.calendar.demo.ui.screens.chat.AiChatIntent
import com.ai.calendar.demo.ui.screens.chat.AiChatState
import com.ai.calendar.demo.ui.screens.chat.composable.preview.AiChatPreviewData
import com.ai.calendar.demo.ui.theme.AiCalendarTheme
import com.ai.calendar.demo.ui.theme.marginZero

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiChatBottomSheet(
    modifier: Modifier = Modifier,
    bottomPadding: Dp = marginZero,
    state: AiChatState,
    onIntent: (AiChatIntent) -> Unit,
    onDismiss: () -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = onDismiss,
        sheetState = sheetState,
    ) {
        AiChatContent(
            bottomPadding = bottomPadding,
            state = state,
            onIntent = onIntent,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AiChatBottomSheetEmptyPreview() {
    AiCalendarTheme {
        AiChatContent(state = AiChatPreviewData.emptyState, onIntent = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun AiChatBottomSheetWithMessagesPreview() {
    AiCalendarTheme {
        AiChatContent(
            state = AiChatPreviewData.withMessagesState,
            onIntent = {},
        )
    }
}
