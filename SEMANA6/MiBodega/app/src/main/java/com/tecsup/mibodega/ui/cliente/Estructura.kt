package com.tecsup.mibodega.ui.cliente

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp

val secciones = listOf("inicio", "favoritos", "pedidos", "perfil")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Estructura(titulo: String, ruta: String, cantidad: Int,
    navegar: (String) -> Unit, volver: (() -> Unit)? = null,
    contenido: @Composable () -> Unit) {
    val superior: @Composable () -> Unit = {
        TopAppBar(title = { Text(titulo) }, navigationIcon = {
            if (volver != null) IconButton(onClick = volver) { Icon(Icons.Default.ArrowBack, "Volver") }
        }, actions = {
            if (ruta != "bienvenida" && ruta != "registro") IconButton(onClick = { navegar("carrito") }) {
                BadgedBox(badge = { if (cantidad > 0) Badge { Text(cantidad.toString()) } }) {
                    Icon(Icons.Default.ShoppingCart, "Carrito")
                }
            }
        })
    }
    val inferior: @Composable () -> Unit = {
        if (ruta in secciones) NavigationBar {
            val iconos = listOf(Icons.Default.Home, Icons.Default.Favorite, Icons.Default.ReceiptLong, Icons.Default.Person)
            val nombres = listOf("Inicio", "Favoritos", "Mis pedidos", "Perfil")
            secciones.forEachIndexed { indice, destino ->
                NavigationBarItem(selected = ruta == destino, onClick = { navegar(destino) },
                    icon = { Icon(iconos[indice], nombres[indice]) }, label = { Text(nombres[indice]) })
            }
        }
    }
    val fondo = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(
        MaterialTheme.colorScheme.background, MaterialTheme.colorScheme.primaryContainer.copy(alpha = .55f))))
    if (Practica.USAR_SCAFFOLD) {
        Scaffold(topBar = superior, bottomBar = inferior) { innerPadding ->
            Box(Modifier.padding(innerPadding).consumeWindowInsets(innerPadding).then(fondo)) { contenido() }
        }
    } else {
        Column(Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))) {
            superior()
            Box(Modifier.weight(1f).then(fondo)) { contenido() }
            inferior()
        }
    }
}
