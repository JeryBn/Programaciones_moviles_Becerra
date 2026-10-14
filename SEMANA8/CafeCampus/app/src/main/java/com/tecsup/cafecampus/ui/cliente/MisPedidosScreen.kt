package com.tecsup.cafecampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.PedidosClienteViewModel
import com.tecsup.cafecampus.model.EstadoPedido
import com.tecsup.cafecampus.ui.components.*
@Composable fun MisPedidosScreen(vm:PedidosClienteViewModel,onPedido:(Int)->Unit) {
 val pedidos by vm.misPedidos.collectAsState(); var historial by rememberSaveable { mutableStateOf(false) }
 Column(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Text("Mis pedidos",style=MaterialTheme.typography.headlineMedium)
  Row { FilterChip(selected=!historial,onClick={historial=false},label={Text("Activos")}); Spacer(Modifier.width(8.dp)); FilterChip(selected=historial,onClick={historial=true},label={Text("Historial")}) }
  val visibles=pedidos.filter { EstadoPedido.valueOf(it.pedido.estado).activo!=historial }
  if(visibles.isEmpty()) Text("No hay pedidos en esta sección")
  LazyColumn(verticalArrangement=Arrangement.spacedBy(12.dp)) {items(visibles,key={it.pedido.id}) {p->PedidoCard(p) {onPedido(p.pedido.id)} }}
 }
}

