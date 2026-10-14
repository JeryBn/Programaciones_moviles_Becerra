package com.tecsup.cafecampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.ui.components.*
@Composable fun DetalleProductoScreen(vm:MenuViewModel,id:Int,onVolver:()->Unit) {
 val productos by vm.productos.collectAsState(); val p=productos.find { it.id==id }
 var cantidad by rememberSaveable(id) { mutableIntStateOf(1) }; var nota by rememberSaveable(id) { mutableStateOf("") }
 Column(Modifier.padding(16.dp).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(16.dp)) {
  TextButton(onClick=onVolver) { Text("Volver") }
  if(p==null) Text("Producto no disponible") else {
   ProductoIcono(p.icono); Text(p.nombre,style=MaterialTheme.typography.headlineMedium); Text(p.descripcion); Text("S/ %.2f".format(p.precio))
   EstadoChip(if(p.disponible) "LISTO" else "CANCELADO"); Text(if(p.disponible) "Disponible" else "Agotado")
   Row { OutlinedButton(onClick={cantidad--},enabled=cantidad>1) {Text("−")}; Text(" $cantidad ",Modifier.padding(16.dp)); Button(onClick={cantidad++}) {Text("+")} }
   CafeTextField(nota,"Nota para este producto") { nota=it }
   CafeButton("Agregar al pedido · S/ %.2f".format(p.precio*cantidad),p.disponible) { vm.agregar(p,cantidad,nota); onVolver() }
  }
 }
}

