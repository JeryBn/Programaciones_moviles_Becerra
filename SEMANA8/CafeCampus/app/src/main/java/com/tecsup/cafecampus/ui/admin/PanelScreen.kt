package com.tecsup.cafecampus.ui.admin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.*
import com.tecsup.cafecampus.ui.components.*
@Composable fun PanelScreen(vm:PanelAdminViewModel,pedidos:PedidosAdminViewModel,onPedido:(Int)->Unit) {
 val nuevos by vm.nuevos.collectAsState();val preparando by vm.preparando.collectAsState();val listos by vm.listos.collectAsState();val ventas by vm.ventasHoy.collectAsState();val recibidos by vm.recibidos.collectAsState()
 LazyColumn(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  item {Text("Panel de cafetería",style=MaterialTheme.typography.headlineMedium)}
  item {Row(horizontalArrangement=Arrangement.spacedBy(8.dp)) {listOf("Nuevos" to nuevos,"Preparando" to preparando,"Listos" to listos).forEach { (t,n)->Card(Modifier.weight(1f)) {Column(Modifier.padding(12.dp)) {Text("$n",style=MaterialTheme.typography.headlineSmall);Text(t)}} } }}
  item {Text("Ventas del día · S/ %.2f".format(ventas),style=MaterialTheme.typography.titleLarge)}
  item {Text("Nuevos pedidos")}
  if(recibidos.isEmpty()) item {Text("No hay pedidos nuevos")}
  items(recibidos,key={it.pedido.id}) {p->Column {PedidoCard(p) {onPedido(p.pedido.id)}; CafeButton("Preparar pedido #${p.pedido.id}") {pedidos.avanzar(p)} }}
 }
}

