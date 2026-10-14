package com.tecsup.cafecampus.ui.admin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.ui.components.*
@Composable fun MenuAdminScreen(vm:MenuAdminViewModel,onEditar:(Int)->Unit) {
 val productos by vm.productos.collectAsState(); var eliminar by remember {mutableStateOf<Producto?>(null)}
 Scaffold(floatingActionButton={FloatingActionButton(onClick={onEditar(0)}) {Icon(Icons.Default.Add,"Agregar producto")}}) {padding->
  LazyColumn(Modifier.padding(padding).padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
   item {Text("Gestión del menú",style=MaterialTheme.typography.headlineMedium)}
   items(productos,key={it.id}) {p->Column {ProductoCard(p) {onEditar(p.id)}; Row {TextButton(onClick={onEditar(p.id)}) {Text("Editar")}; TextButton(onClick={vm.alternarDisponibilidad(p)}) {Text(if(p.disponible) "Marcar agotado" else "Disponible")}; TextButton(onClick={eliminar=p}) {Text("Eliminar")}}}}
  }
 }
 eliminar?.let {p->AlertDialog(onDismissRequest={eliminar=null},title={Text("Eliminar ${p.nombre}")},text={Text("Los pedidos anteriores conservarán su nombre y precio.")},confirmButton={TextButton(onClick={vm.eliminar(p);eliminar=null}) {Text("Eliminar")}},dismissButton={TextButton(onClick={eliminar=null}) {Text("Cancelar")}})}
}

