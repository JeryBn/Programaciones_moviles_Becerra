package com.tecsup.mibodega.ui.cliente.screens.inicio

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.FotoProducto
import com.tecsup.mibodega.ui.cliente.*
import com.tecsup.mibodega.ui.cliente.modelo.*

@Composable
fun InicioScreen(productos: List<Producto> = listaProductosFake, favoritos: Set<Int>,
    onFavorito: (Int) -> Unit, onProductoClick: (Producto) -> Unit, onAgregarProducto: (Producto) -> Unit) {
    var categoria by rememberSaveable { mutableStateOf("Todos") }
    var busqueda by rememberSaveable { mutableStateOf("") }
    var orden by rememberSaveable { mutableStateOf("Sin ordenar") }
    val filtrados = filtrarProductos(productos, categoria, busqueda)
    val visibles = when (orden) {
        "Precio ascendente" -> filtrados.sortedBy { it.precio }
        "Precio descendente" -> filtrados.sortedByDescending { it.precio }
        else -> filtrados
    }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedTextField(busqueda, { busqueda = it }, label = { Text("Buscar productos") },
            leadingIcon = { Icon(Icons.Default.Search, null) }, singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp))
        FilaFiltros(listaCategorias) { opcion -> FilterChip(categoria == opcion, { categoria = opcion }, label = { Text(opcion) }) }
        FilaFiltros(listOf("Sin ordenar", "Precio ascendente", "Precio descendente")) { opcion ->
            FilterChip(orden == opcion, { orden = opcion }, label = { Text(opcion) })
        }
        if (visibles.isEmpty()) Text("No hay productos con estos filtros", Modifier.padding(16.dp))
        ListaVertical(visibles, { it.id }, Modifier.weight(1f)) { producto ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    FotoProducto(producto, Modifier.fillMaxWidth().height(128.dp))
                    Row {
                        Text(producto.nombre, Modifier.weight(1f), style = MaterialTheme.typography.titleMedium)
                        IconButton(onClick = { onFavorito(producto.id) }) {
                            Icon(if (producto.id in favoritos) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                "Favorito ${producto.nombre}")
                        }
                    }
                    Text("${producto.categoria} Â· S/ %.2f".format(producto.precio))
                    Row {
                        TextButton(onClick = { onProductoClick(producto) }) { Text("Ver detalle") }
                        Button(onClick = { onAgregarProducto(producto) }) { Text("Agregar") }
                    }
                }
            }
        }
    }
}
