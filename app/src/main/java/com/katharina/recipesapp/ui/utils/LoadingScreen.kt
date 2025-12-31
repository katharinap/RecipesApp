package com.katharina.recipesapp.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.katharina.recipesapp.ui.theme.RecipesAppTheme

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Box(
        modifier =
            modifier.then(
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background),
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@PreviewLightDark
@Composable
fun LoadingScreenPreview() {
    RecipesAppTheme {
        LoadingScreen()
    }
}
