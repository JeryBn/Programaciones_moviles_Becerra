package com.tecsup.mibodega.ui.cliente.screens.confirmacion
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.Pedido
@Composable
fun ConfirmacionScreen(total: Double, onVolverInicio: () -> Unit, pedido: Pedido? = null) {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Icon(Icons.Default.CheckCircle, "Pedido confirmado", tint = MaterialTheme.colorScheme.primary)
        Text("Pedido realizado", style = MaterialTheme.typography.headlineMedium)
        pedido?.let {
            Text("Pedido #${it.id}"); Text("${it.nombre} · ${it.direccion}")
            Text("Teléfono: ${it.telefono} · Pago: ${it.pago}")
            if (it.referencia.isNotBlank()) Text("Referencia: ${it.referencia}")
            it.items.forEach { item -> Text("${item.cantidad} × ${item.producto.nombre}") }
        }
        Text("Confirmación local de demostración")
        Text("Total: S/ %.2f".format(total), style = MaterialTheme.typography.titleLarge)
        Button(onClick = onVolverInicio) { Text("Volver al inicio") }
    }
}
