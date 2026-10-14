package com.tecsup.cafecampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.MenuViewModel
import com.tecsup.cafecampus.ui.components.*
@Composable fun MenuScreen(vm:MenuViewModel,onProducto:(Int)->Unit,onBolsa:()->Unit) {
 val productos by vm.visibles.collectAsState(); val carrito by vm.carrito.collectAsState(); val categoria by vm.categoria.collectAsState()
 Column(Modifier.fillMaxSize().padding(16.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) {
  Text("Menú del campus",style=MaterialTheme.typography.headlineMedium)
  LazyRow(horizontalArrangement=Arrangement.spacedBy(8.dp)) { items(listOf("Todos","Bebidas","Comidas","Snacks")) { c->FilterChip(selected=c==categoria,onClick={vm.categoria.value=c},label={Text(c)}) } }
  LazyVerticalGrid(columns=GridCells.Fixed(2),modifier=Modifier.weight(1f),horizontalArrangement=Arrangement.spacedBy(12.dp),verticalArrangement=Arrangement.spacedBy(12.dp)) { items(productos,key={it.id}) { p->ProductoCard(p) { onProducto(p.id) } } }
  if(productos.isEmpty()) Text("No hay productos en esta categoría")
  CafeButton("Mi bolsa (${carrito.sumOf { it.cantidad }}) · S/ %.2f".format(vm.total()),carrito.isNotEmpty(),onBolsa)
 }
}

