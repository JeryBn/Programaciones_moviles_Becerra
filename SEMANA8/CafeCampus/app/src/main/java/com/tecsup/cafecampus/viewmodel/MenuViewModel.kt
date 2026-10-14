package com.tecsup.cafecampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.model.ItemCarrito
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import java.text.Normalizer
class MenuViewModel(private val repo:CafeRepository):ViewModel() {
 val productos=repo.observarProductos().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 private val _carrito=MutableStateFlow<List<ItemCarrito>>(emptyList()); val carrito=_carrito.asStateFlow()
 val categoria=MutableStateFlow("Todos")
 val busqueda=MutableStateFlow("")
 val visibles=combine(productos,categoria,busqueda) { lista,c,q -> lista.filter { (c=="Todos"||it.categoria==c)&&normalizar(it.nombre).contains(normalizar(q.trim())) } }.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 fun agregar(p:Producto,cantidad:Int,nota:String) { if(!p.disponible||cantidad<1) return; _carrito.update { lista -> if(lista.any { it.producto.id==p.id }) lista.map { if(it.producto.id==p.id) it.copy(cantidad=it.cantidad+cantidad,nota=nota.ifBlank { it.nota }) else it } else lista+ItemCarrito(p,cantidad,nota) } }
 fun quitar(id:Int) { _carrito.update { it.filterNot { i->i.producto.id==id } } }
 fun vaciar() { _carrito.value=emptyList() }
 val aviso=MutableStateFlow<String?>(null)
 fun repetir(pedido:PedidoConDetalle,onCargado:()->Unit) {viewModelScope.launch {
  val actuales=repo.observarProductos().first().associateBy {it.id}
  val disponibles=pedido.detalles.mapNotNull {d->actuales[d.productoId]?.takeIf {it.disponible}?.let {ItemCarrito(it,d.cantidad)}}
  if(disponibles.size!=pedido.detalles.size) {aviso.value="No se puede repetir: un producto fue eliminado o está agotado";return@launch}
  _carrito.value=disponibles;aviso.value="Pedido cargado con los precios actuales";onCargado()
 }}
 fun total()=_carrito.value.sumOf { it.producto.precio*it.cantidad }
 private fun normalizar(s:String)=Normalizer.normalize(s.lowercase(),Normalizer.Form.NFD).replace("\\p{Mn}+".toRegex(),"")
}

