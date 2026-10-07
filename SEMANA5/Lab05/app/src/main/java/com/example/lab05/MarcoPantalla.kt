package com.example.lab05
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
fun MarcoPantalla(topBar: @Composable () -> Unit, containerColor: Color,
    content: @Composable (PaddingValues) -> Unit) {
    if (Practica.USAR_SCAFFOLD) Scaffold(topBar = topBar, containerColor = containerColor, content = content)
    else Surface(color = containerColor) {
        Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))) {
            topBar(); Box(Modifier.weight(1f).navigationBarsPadding()) { content(PaddingValues(0.dp)) }
        }
    }
}
