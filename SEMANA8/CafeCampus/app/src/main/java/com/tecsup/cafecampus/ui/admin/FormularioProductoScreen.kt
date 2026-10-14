package com.tecsup.cafecampus.ui.admin
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.MenuAdminViewModel
import com.tecsup.cafecampus.ui.components.*
@Composable fun FormularioProductoScreen(vm:MenuAdminViewModel,id:Int,onVolver:()->Unit) {
 val productos by vm.productos.collectAsState(); val p=productos.find {it.id==id}; val error by vm.error.collectAsState();val guardado by vm.guardado.collectAsState()
 var nombre by rememberSaveable(id) {mutableStateOf("")}; var categoria by rememberSaveable(id) {mutableStateOf("Bebidas")};var precio by rememberSaveable(id) {mutableStateOf("")};var descripcion by rememberSaveable(id) {mutableStateOf("")};var disponible by rememberSaveable(id) {mutableStateOf(true)};var cargado by rememberSaveable(id) {mutableStateOf(false)}
 LaunchedEffect(p?.id) {if(p!=null&&!cargado) {nombre=p.nombre;categoria=p.categoria;precio=p.precio.toString();descripcion=p.descripcion;disponible=p.disponible;cargado=true}}
 LaunchedEffect(guardado) {if(guardado) {vm.guardado.value=false;onVolver()}}
 Column(Modifier.padding(16.dp).verticalScroll(rememberScrollState()),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Text(if(id==0) "Nuevo producto" else "Editar producto",style=MaterialTheme.typography.headlineMedium)
  CafeTextField(nombre,"Nombre") {nombre=it};CafeTextField(categoria,"Categoría") {categoria=it};CafeTextField(precio,"Precio") {precio=it};CafeTextField(descripcion,"Descripción") {descripcion=it}
  Row {Text("Disponible",Modifier.weight(1f));Switch(checked=disponible,onCheckedChange={disponible=it})}
  error?.let {Text(it,color=MaterialTheme.colorScheme.error)}
  CafeButton("Guardar") {vm.guardar(id,nombre,categoria,precio,descripcion,disponible,p?.icono?:"cafe")}
  TextButton(onClick=onVolver) {Text("Volver")}
 }
}

