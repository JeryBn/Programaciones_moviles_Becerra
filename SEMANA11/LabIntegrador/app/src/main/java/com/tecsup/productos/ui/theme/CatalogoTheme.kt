package com.tecsup.productos.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun CatalogoTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = lightColorScheme(primary = Color(0xFF164D84),
        secondary = Color(0xFF3C6179), background = Color(0xFFF6F8FC)), content = content)
}