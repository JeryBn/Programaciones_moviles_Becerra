package com.example.lab05
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
@Composable
fun TarjetaProducto(producto: ProductoStore, favorito: Boolean, marcar: (Int) -> Unit,
    compartir: (ProductoStore) -> Unit, reportar: (ProductoStore) -> Unit, detalle: (Int) -> Unit) {
    var expandido by remember { mutableStateOf(false) }
    ElevatedCard(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row {
                Column(Modifier.weight(1f)) { Text(producto.nombre, style = MaterialTheme.typography.titleLarge); Text("S/ %.2f".format(producto.precio)); Text(producto.categoria) }
                Box {
                    IconButton(onClick = { expandido = true }) { Icon(Icons.Default.MoreVert, "Opciones de ${producto.nombre}") }
                    DropdownMenu(expandido, { expandido = false }) {
                        DropdownMenuItem(text = { Text(if (favorito) "Quitar de favoritos" else "Favoritos") },
                            modifier = Modifier.testTag("favorito-${producto.id}"),
                            leadingIcon = { Icon(if (favorito) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null) },
                            onClick = { marcar(producto.id); expandido = false })
                        HorizontalDivider()
                        DropdownMenuItem(text = { Text("Compartir") }, leadingIcon = { Icon(Icons.Default.Share, null) }, onClick = { compartir(producto); expandido = false })
                        HorizontalDivider()
                        DropdownMenuItem(text = { Text("Reportar") }, leadingIcon = { Icon(Icons.Default.Warning, null) }, onClick = { reportar(producto); expandido = false })
                    }
                }
            }
            TextButton(onClick = { detalle(producto.id) }) { Text("Ver detalle") }
        }
    }
}
