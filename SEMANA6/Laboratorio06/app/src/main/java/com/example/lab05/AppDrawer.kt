package com.example.lab05
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.*
import androidx.compose.ui.unit.dp
val rutasStore = listOf("inicio", "pedidos", "favoritos", "perfil", "salir")
@Composable
fun AppDrawer(ruta: String, favoritos: Int, navegar: (String) -> Unit) {
    val nombres = listOf("Inicio", "Mis pedidos", "Favoritos", "Perfil", "Cerrar sesión")
    val iconos = listOf(Icons.Default.Home, Icons.Default.ReceiptLong, Icons.Default.Favorite, Icons.Default.Person, Icons.Default.Logout)
    ModalDrawerSheet {
        Row(Modifier.padding(20.dp)) {
            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) { Text("JB", Modifier.padding(14.dp)) }
            Column(Modifier.padding(12.dp)) { Text("Jery Becerra"); Text("Estudiante TECSUP") }
        }
        HorizontalDivider()
        rutasStore.forEachIndexed { indice, destino ->
            NavigationDrawerItem(selected = ruta == destino, onClick = { navegar(destino) },
                label = { Text(nombres[indice]) }, icon = { Icon(iconos[indice], nombres[indice]) },
                badge = { if (destino == "favoritos") Badge(Modifier.semantics { contentDescription = "$favoritos productos favoritos" }) { Text(favoritos.toString()) } },
                modifier = Modifier.padding(horizontal = 12.dp))
        }
    }
}
