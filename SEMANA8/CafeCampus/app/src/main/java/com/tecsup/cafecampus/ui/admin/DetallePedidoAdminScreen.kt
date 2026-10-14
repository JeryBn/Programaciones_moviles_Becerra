package com.tecsup.cafecampus.ui.admin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.*
@Composable fun DetallePedidoAdminScreen(vm:PedidosAdminViewModel,id:Int,onVolver:()->Unit) {
 val flujo=remember(vm,id) {vm.pedido(id)};val p by flujo.collectAsState()
 Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  TextButton(onClick=onVolver) {Text("Volver")}
  p?.let {p->Text("Pedido #$id",style=MaterialTheme.typography.headlineMedium);PedidoCard(p) {};p.detalles.forEach {Text("${it.cantidad} × ${it.nombreProducto} · S/ %.2f".format(it.precioUnitario*it.cantidad))}; if(p.pedido.nota.isNotBlank()) Card {Text("Nota: ${p.pedido.nota}",Modifier.padding(16.dp))}
   val estado=EstadoPedido.valueOf(p.pedido.estado)
   StepperEstado(estado.name)
   if(estado.activo) {CafeButton("Pasar a ${estado.siguiente()}") {vm.avanzar(p)};TextButton(onClick={vm.cancelar(p)}) {Text("Cancelar pedido")}}
  }?:Text("Cargando…")
 }
}

