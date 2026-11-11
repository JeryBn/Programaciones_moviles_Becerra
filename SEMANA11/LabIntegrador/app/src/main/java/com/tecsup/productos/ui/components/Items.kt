package com.tecsup.productos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.productos.domain.model.Item
import java.text.NumberFormat
import java.util.Locale

fun precioUSD(precio: Double): String = NumberFormat.getCurrencyInstance(Locale.US).format(precio)

@Composable
fun Items(productos: List<Item>, vacio: String, abrir: (Int) -> Unit, alternar: (Int) -> Unit) {
    if (productos.isEmpty()) {
        Text(vacio)
        return
    }
    LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        items(productos, key = { it.id }) { item ->
            Card(onClick = { abrir(item.id) }, modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(14.dp)) {
                    Text(item.titulo, style = MaterialTheme.typography.titleMedium)
                    Text(listOf(item.marca, precioUSD(item.precio)).filter { it.isNotBlank() }.joinToString(" · "))
                    TextButton(onClick = { alternar(item.id) }) {
                        Text(if (item.esFavorito) "Quitar de favoritos" else "Añadir a favoritos")
                    }
                }
            }
        }
    }
}