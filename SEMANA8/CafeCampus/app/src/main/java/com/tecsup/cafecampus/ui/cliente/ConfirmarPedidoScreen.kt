package com.tecsup.cafecampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.*
import com.tecsup.cafecampus.ui.components.*
@Composable fun ConfirmarPedidoScreen(menu:MenuViewModel,vm:PedidosClienteViewModel,abierto:Boolean,onVolver:()->Unit,onCreado:(Int)->Unit) {
 val items by menu.carrito.collectAsState(); val id by vm.creado.collectAsState(); val error by vm.error.collectAsState(); val enviando by vm.enviando.collectAsState()
 var hora by rememberSaveable { mutableStateOf("Ahora") }; var pago by rememberSaveable { mutableStateOf("CAJA") }; var nota by rememberSaveable { mutableStateOf("") }
 LaunchedEffect(id) { id?.let { menu.vaciar(); vm.consumido(); onCreado(it.toInt()) } }
 Column(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Text("Confirmar pedido",style=MaterialTheme.typography.headlineMedium)
  LazyColumn(Modifier.weight(1f),verticalArrangement=Arrangement.spacedBy(8.dp)) { items(items,key={it.producto.id}) { i->OutlinedCard { Row(Modifier.fillMaxWidth().padding(12.dp)) { Text("${i.cantidad} × ${i.producto.nombre}",Modifier.weight(1f)); TextButton(onClick={menu.quitar(i.producto.id)}) {Text("Quitar")} } } } }
  Text("Hora de recojo"); Row { listOf("Ahora","12:30","13:00").forEach { h->FilterChip(selected=hora==h,onClick={hora=h},label={Text(h)}); Spacer(Modifier.width(4.dp)) } }
  Row { listOf("CAJA","YAPE").forEach { p->FilterChip(selected=pago==p,onClick={pago=p},label={Text(if(p=="CAJA") "Caja" else "Yape / Plin")}); Spacer(Modifier.width(8.dp)) } }
  CafeTextField(nota,"Nota general") {nota=it}; Text("Subtotal / Total: S/ %.2f".format(menu.total()))
  if(!abierto) Text("Cafetería cerrada",color=MaterialTheme.colorScheme.error)
  error?.let { Text(it,color=MaterialTheme.colorScheme.error) }
  CafeButton(if(enviando) "Enviando…" else "Confirmar pedido",items.isNotEmpty()&&abierto&&!enviando) {vm.confirmar(items,hora,pago,nota)}
  TextButton(onClick=onVolver) {Text("Seguir pidiendo")}
 }
}

