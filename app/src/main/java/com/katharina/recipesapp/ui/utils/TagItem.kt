package com.katharina.recipesapp.ui.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun TagItem(
    tag: String,
    onClick: (String) -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .padding(4.dp)
                .clip(RoundedCornerShape(percent = 25))
                .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(percent = 25)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary,
            modifier =
                Modifier
                    .padding(4.dp)
                    .clickable {
                        onClick(tag)
                    },
        )
    }
}

@Composable
fun TagItemSmall(
    tag: String,
    onClick: (String) -> Unit = {},
) {
    Box(
        modifier =
            Modifier
                .padding(2.dp)
                .clip(RoundedCornerShape(percent = 12))
                .background(color = MaterialTheme.colorScheme.tertiaryContainer)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.tertiary, shape = RoundedCornerShape(percent = 12)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = tag,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.tertiary,
            modifier =
                Modifier
                    .padding(2.dp)
                    .clickable {
                        onClick(tag)
                    },
        )
    }
}
