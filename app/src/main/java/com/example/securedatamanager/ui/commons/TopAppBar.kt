package com.example.securedatamanager.ui.commons

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable

@Composable
fun AppBarWithBackButton(
    title: String,
    showBackButton: Boolean = true, // Default is true
    onBackClick: () -> Unit = {}
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = if (showBackButton) {
            {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        } else null
    )
}