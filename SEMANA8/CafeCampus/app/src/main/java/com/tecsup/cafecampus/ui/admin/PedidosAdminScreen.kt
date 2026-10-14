package com.tecsup.cafecampus.ui.admin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.PedidosAdminViewModel
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.*
@Composable fun PedidosAdminScreen(vm:PedidosAdminViewModel,onPedido:(Int)->Unit) {
 val estado by vm.estadoSeleccionado.collectAsState(); val pedidos by vm.pedidos.collectAsState()
 Column(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Text("Gestión de pedidos",style=MaterialTheme.typography.headlineMedium)
  LazyRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) {items(EstadoPedido.entries) {e->FilterChip(selected=estado==e,onClick={vm.estadoSeleccionado.value=e},label={Text(e.name)})}}
  if(pedidos.isEmpty()) Text("No hay pedidos con este estado")
  LazyColumn(verticalArrangement=Arrangement.spacedBy(12.dp)) {items(pedidos,key={it.pedido.id}) {p->Column {PedidoCard(p) {onPedido(p.pedido.id)}; if(EstadoPedido.valueOf(p.pedido.estado).activo) {CafeButton("Pasar a ${EstadoPedido.valueOf(p.pedido.estado).siguiente()}") {vm.avanzar(p)}; TextButton(onClick={vm.cancelar(p)}) {Text("Cancelar",color=MaterialTheme.colorScheme.error)}} }}}
 }
}

