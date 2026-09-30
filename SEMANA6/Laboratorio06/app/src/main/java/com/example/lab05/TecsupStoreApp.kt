package com.example.lab05

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

data class ProductoStore(val id: Int, val nombre: String, val precio: Double, val categoria: String)

private val productosStore = listOf(
    ProductoStore(1, "Audifonos", 89.0, "Tecnologia"),
    ProductoStore(2, "Smartwatch", 199.0, "Tecnologia"),
    ProductoStore(3, "Funda celular", 25.0, "Accesorios"),
    ProductoStore(4, "Mochila Tecsup", 79.9, "Accesorios")
)

private enum class Destino(val etiqueta: String, val icono: ImageVector) {
    INICIO("Inicio", Icons.Default.Home), PEDIDOS("Mis pedidos", Icons.Default.ReceiptLong),
    FAVORITOS("Favoritos", Icons.Default.Favorite), PERFIL("Perfil", Icons.Default.Person),
    SALIR("Cerrar sesion", Icons.Default.Logout)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TecsupStoreApp() {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var destino by remember { mutableStateOf(Destino.INICIO) }
    var favoritos by remember { mutableStateOf(setOf<Int>()) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Row(Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer) {
                        Text("JB", Modifier.padding(14.dp), fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.width(12.dp))
                    Column { Text("Jery Becerra", fontWeight = FontWeight.Bold); Text("Estudiante TECSUP") }
                }
                HorizontalDivider()
                Destino.entries.forEach { item ->
                    NavigationDrawerItem(
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(item.etiqueta)
                                if (item == Destino.FAVORITOS && favoritos.isNotEmpty()) {
                                    Spacer(Modifier.width(8.dp)); Badge { Text(favoritos.size.toString()) }
                                }
                            }
                        },
                        icon = { Icon(item.icono, null) },
                        selected = destino == item,
                        onClick = { destino = item; scope.launch { drawerState.close() } },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Column { Text("TECSUP Store", fontWeight = FontWeight.Bold); Text(destino.etiqueta, style = MaterialTheme.typography.labelMedium) } },
                    navigationIcon = { IconButton(onClick = { scope.launch { drawerState.open() } }) { Icon(Icons.Default.Menu, "Abrir menu") } }
                )
            }
        ) { padding ->
            if (destino == Destino.FAVORITOS) {
                ListaProductos(productosStore.filter { it.id in favoritos }, favoritos, { favoritos = alternar(favoritos, it) }, Modifier.padding(padding))
            } else if (destino == Destino.INICIO) {
                ListaProductos(productosStore, favoritos, { favoritos = alternar(favoritos, it) }, Modifier.padding(padding))
            } else {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text("Seccion ${destino.etiqueta}", style = MaterialTheme.typography.headlineSmall)
                }
            }
        }
    }
}

private fun alternar(actual: Set<Int>, id: Int) = if (id in actual) actual - id else actual + id

@Composable
private fun ListaProductos(productos: List<ProductoStore>, favoritos: Set<Int>, onFavorito: (Int) -> Unit, modifier: Modifier = Modifier) {
    LazyColumn(modifier.fillMaxSize(), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(productos, key = { it.id }) { producto -> TarjetaProducto(producto, producto.id in favoritos, onFavorito) }
        if (productos.isEmpty()) item { Text("Aun no marcaste productos favoritos.") }
    }
}

@Composable
private fun TarjetaProducto(producto: ProductoStore, favorito: Boolean, onFavorito: (Int) -> Unit) {
    var expandido by remember { mutableStateOf(false) }
    ElevatedCard(shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.ShoppingBag, null, modifier = Modifier.size(42.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold)
                Text("S/ %.2f".format(producto.precio), color = MaterialTheme.colorScheme.primary)
                Text(producto.categoria, style = MaterialTheme.typography.labelSmall)
            }
            Box {
                IconButton(onClick = { expandido = true }) { Icon(Icons.Default.MoreVert, "Opciones de ${producto.nombre}") }
                DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                    DropdownMenuItem(
                        text = { Text(if (favorito) "Quitar de favoritos" else "Favoritos") },
                        leadingIcon = { Icon(if (favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null) },
                        onClick = { onFavorito(producto.id); expandido = false }
                    )
                    HorizontalDivider()
                    DropdownMenuItem(text = { Text("Compartir") }, leadingIcon = { Icon(Icons.Default.Share, null) }, onClick = { expandido = false })
                    HorizontalDivider()
                    DropdownMenuItem(text = { Text("Reportar") }, leadingIcon = { Icon(Icons.Default.Warning, null) }, onClick = { expandido = false })
                }
            }
        }
    }
}
