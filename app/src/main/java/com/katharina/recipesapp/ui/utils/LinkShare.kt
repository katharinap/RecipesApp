package com.katharina.recipesapp.ui.utils

import android.content.Context
import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.katharina.recipesapp.ui.theme.RecipesAppTheme

@Composable
fun LinkShare(
    link: String,
    context: Context,
) {
    val sendIntent: Intent =
        Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, link)
            type = "text/plain"
        }

    val shareIntent = Intent.createChooser(sendIntent, null)

    IconButton(onClick = {
        context.startActivity(shareIntent)
    }) {
        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
    }
}

@Composable
fun LinkShareDisabled() {
    IconButton(onClick = {}, enabled = false) {
        Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
    }
}

@PreviewLightDark
@Composable
fun LinkSharePreview() {
    RecipesAppTheme {
        LinkShare(link = "https://www.google.com", context = LocalContext.current)
    }
}
