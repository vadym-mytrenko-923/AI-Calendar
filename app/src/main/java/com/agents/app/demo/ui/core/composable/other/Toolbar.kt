package com.agents.app.demo.ui.core.composable.other

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.agents.app.demo.ui.core.composable.button.IcBtnBack
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.iconButtonSize
import com.agents.app.demo.ui.theme.marginPrimary2X
import kotlin.let

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(
    title: String,
    onBackClick: () -> Unit,
    rightContent: (@Composable () -> Unit)? = null,
    topPadding: Dp
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = marginPrimary2X, end = marginPrimary2X, bottom = marginPrimary2X, top = topPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IcBtnBack(onClick = onBackClick)
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        rightContent?.let { it() } ?: Spacer(modifier = Modifier.size(iconButtonSize))
    }
}

@Preview(showBackground = true)
@Composable
private fun ToolbarPreview() {
    AiAgenticAppTheme {
        Toolbar(
            title = "Test Title",
            topPadding = marginPrimary2X,
            onBackClick = {}
        )
    }
}
