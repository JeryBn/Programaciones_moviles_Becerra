package com.tecsup.mibodega.ui.cliente.screens.carrito

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.ListaVertical
import com.tecsup.mibodega.ui.cliente.modelo.*
import com.tecsup.mibodega.ui.componentes.SelectorCantidad

@Composable
fun CarritoScreen(carrito: List<ItemCarrito>, onVolver: () -> Unit,
    onIncrementar: (Producto) -> Unit, onDecrementar: (Producto) -> Unit,
    onEliminar: (Producto) -> Unit, onContinuarPedido: () -> Unit) {
    var pendiente by remember { mutableStateOf<Producto?>(null) }
    Column(Modifier.fillMaxSize()) {
        if (carrito.isEmpty()) Text("Tu carrito está vacío", Modifier.padding(20.dp))
        ListaVertical(carrito, { it.producto.id }, Modifier.weight(1f)) { item ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(12.dp)) {
                    Text(item.producto.nombre)
                    Text("S/ %.2f".format(item.producto.precio * item.cantidad))
                    Row {
                        SelectorCantidad(item.cantidad, { onIncrementar(item.producto) }, {
                            if (item.cantidad == 1) pendiente = item.producto else onDecrementar(item.producto)
                        })
                        IconButton(onClick = { pendiente = item.producto }) { Icon(Icons.Default.Delete, "Eliminar ${item.producto.nombre}") }
                    }
                }
            }
        }
        Column(Modifier.padding(16.dp)) {
            Text("Subtotal: S/ %.2f".format(subtotal(carrito)))
            Text("Delivery: S/ %.2f".format(if (carrito.isEmpty()) 0.0 else COSTO_DELIVERY))
            Text("Total con delivery: S/ %.2f".format(totalEntrega(carrito, false)))
            Text("Puedes elegir recojo en la siguiente pantalla")
            Button(onClick = onContinuarPedido, enabled = carrito.isNotEmpty()) { Text("Continuar pedido") }
            TextButton(onClick = onVolver) { Text("Seguir comprando") }
        }
    }
    pendiente?.let { producto ->
        AlertDialog(onDismissRequest = { pendiente = null }, title = { Text("Eliminar producto") },
            text = { Text("¿Eliminar ${producto.nombre} del carrito?") },
            confirmButton = { TextButton(onClick = { onEliminar(producto); pendiente = null }) { Text("Eliminar") } },
            dismissButton = { TextButton(onClick = { pendiente = null }) { Text("Cancelar") } })
    }
}
