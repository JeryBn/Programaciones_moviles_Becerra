package com.example.lab05
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreEstructura(titulo: String, abrirMenu: () -> Unit, contenido: @Composable () -> Unit) {
    val superior: @Composable () -> Unit = { TopAppBar(title = { Text(titulo) },
        navigationIcon = { IconButton(onClick = abrirMenu) { Icon(Icons.Default.Menu, "Abrir menú") } }) }
    val fondo = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(
        MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.primaryContainer)))
    if (Practica.USAR_SCAFFOLD) Scaffold(topBar = superior) { padding ->
        Box(Modifier.padding(padding).consumeWindowInsets(padding).then(fondo)) { contenido() }
    } else Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))) {
        superior(); Box(Modifier.weight(1f).then(fondo)) { contenido() }
    }
}
