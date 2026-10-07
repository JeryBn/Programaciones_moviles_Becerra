package com.tecsup.mibodega.ui.cliente.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.ListaVertical
import com.tecsup.mibodega.ui.cliente.modelo.*

@Composable
fun PedidosScreen(pedidos: List<Pedido>) {
    if (pedidos.isEmpty()) Text("Aún no tienes pedidos confirmados", Modifier.padding(20.dp))
    else ListaVertical(pedidos.reversed(), { it.id }) { pedido ->
        Card(Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp)) {
            Text("Pedido #${pedido.id} · Confirmado", style = MaterialTheme.typography.titleLarge)
            Text("${pedido.nombre} · ${pedido.direccion}")
            pedido.items.forEach { Text("${it.cantidad} × ${it.producto.nombre}") }
            Text("Total: S/ %.2f".format(pedido.total))
        } }
    }
}

