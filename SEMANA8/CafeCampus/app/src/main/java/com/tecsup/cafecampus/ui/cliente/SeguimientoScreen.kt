package com.tecsup.cafecampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.ui.components.*
@Composable fun SeguimientoScreen(vm:PedidosClienteViewModel,id:Int,onVolver:()->Unit) {
 val flujo=remember(vm,id) {vm.pedido(id)}; val p by flujo.collectAsState()
 Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
  TextButton(onClick=onVolver) {Text("Mis pedidos")}
  Text("Seguimiento",style=MaterialTheme.typography.headlineMedium)
  p?.let { pedido->PedidoCard(pedido) {}; StepperEstado(pedido.pedido.estado); pedido.detalles.forEach { Text("${it.cantidad} × ${it.nombreProducto} · S/ %.2f".format(it.precioUnitario*it.cantidad)) } }?:Text("Cargando pedido…")
 }
}

