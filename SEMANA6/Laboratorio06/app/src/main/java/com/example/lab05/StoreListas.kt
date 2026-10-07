package com.example.lab05
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
@Composable
fun StoreLista(productos: List<ProductoStore>, favoritos: Set<Int>, marcar: (Int) -> Unit,
    compartir: (ProductoStore) -> Unit, reportar: (ProductoStore) -> Unit, detalle: (Int) -> Unit) {
    var categoria by rememberSaveable { mutableStateOf("Todos") }
    val categorias = listOf("Todos", "Tecnología", "Accesorios")
    val visibles = productos.filter { categoria == "Todos" || it.categoria == categoria }
    val chip: @Composable (String) -> Unit = { opcion -> FilterChip(categoria == opcion, { categoria = opcion }, label = { Text(opcion) }) }
    val tarjeta: @Composable (ProductoStore) -> Unit = { producto -> TarjetaProducto(producto, producto.id in favoritos, marcar, compartir, reportar, detalle) }
    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        if (Practica.USAR_LAZY_ROW) LazyRow(contentPadding = PaddingValues(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) { items(categorias) { chip(it) } }
        else Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) { categorias.forEach { chip(it) } }
        if (visibles.isEmpty()) Text("No hay productos en esta sección", Modifier.padding(16.dp))
        if (Practica.USAR_LAZY_COLUMN) LazyColumn(Modifier.weight(1f), contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { items(visibles, key = { it.id }) { tarjeta(it) } }
        else Column(Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) { visibles.forEach { tarjeta(it) } }
    }
}
