package com.agents.app.demo.ui.screens.envSplash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.agents.app.demo.R
import com.agents.app.demo.ui.theme.AppColorsLight
import com.agents.app.demo.ui.theme.AppImages
import com.agents.app.demo.ui.theme.AiAgenticAppTheme
import com.agents.app.demo.ui.theme.marginPrimary2X
import com.agents.app.demo.utils.AppInfo

@Composable
fun EnvironmentSplashScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = AppImages.EnvironmentBg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.envSplashTTitle, AppInfo.flavor.uppercase()),
                style = MaterialTheme.typography.headlineLarge,
                color = AppColorsLight.TextPrimary,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(top = marginPrimary2X),
                text = stringResource(R.string.envSplashTVersion, AppInfo.appVersion),
                style = MaterialTheme.typography.headlineSmall,
                color = AppColorsLight.TextPrimary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview
@Composable
fun EnvironmentSplashScreenPreview() {
    AiAgenticAppTheme {
        EnvironmentSplashScreen()
    }
}
