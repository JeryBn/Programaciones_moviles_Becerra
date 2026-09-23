package com.becerra.tecsupfit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class Destino(val ruta: String, val titulo: String, val icono: ImageVector)
val destinos = listOf(
    Destino("inicio", "Inicio", Icons.Default.Home),
    Destino("reservas", "Reservas", Icons.Default.DateRange),
    Destino("rutinas", "Rutinas", Icons.Default.FitnessCenter),
    Destino("perfil", "Perfil", Icons.Default.Person)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarraSuperior(titulo: String, volver: (() -> Unit)?) {
    TopAppBar(title = { Text(titulo) }, navigationIcon = {
        if (volver != null) IconButton(onClick = volver) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
        }
    })
}

@Composable
fun BarraInferior(ruta: String, navegar: (String) -> Unit) {
    NavigationBar {
        destinos.forEach { destino ->
            NavigationBarItem(
                selected = ruta == destino.ruta,
                onClick = { navegar(destino.ruta) },
                icon = { Icon(destino.icono, contentDescription = null) },
                label = { Text(destino.titulo) }
            )
        }
    }
}

// No conoce NavController. Recibe acciones; por eso Scaffold y navegación son independientes.
@Composable
fun Estructura(
    titulo: String,
    ruta: String,
    usarScaffold: Boolean,
    volver: (() -> Unit)?,
    navegar: (String) -> Unit,
    contenido: @Composable () -> Unit
) {
    val fondo = Modifier.fillMaxSize().background(
        Brush.verticalGradient(listOf(Color(0xFFEAF1FF), Color(0xFFF5F0FF), Color(0xFFE5F5EF)))
    )
    if (usarScaffold) {
        Scaffold(
            topBar = { BarraSuperior(titulo, volver) },
            bottomBar = { BarraInferior(ruta, navegar) }
        ) { innerPadding ->
            Box(Modifier.padding(innerPadding).consumeWindowInsets(innerPadding).then(fondo)) {
                contenido()
            }
        }
    } else {
        // Column distribuye las barras y reserva el espacio restante al contenido.
        // Las barras Material 3 ya atienden los insets superior/inferior del sistema.
        Column(Modifier.fillMaxSize().windowInsetsPadding(
            WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
        )) {
            BarraSuperior(titulo, volver)
            Box(Modifier.weight(1f).then(fondo)) { contenido() }
            BarraInferior(ruta, navegar)
        }
    }
}
