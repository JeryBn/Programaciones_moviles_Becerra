package com.tecsup.cafecampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.model.ItemCarrito
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
 fun total()=_carrito.value.sumOf { it.producto.precio*it.cantidad }
 private fun normalizar(s:String)=Normalizer.normalize(s.lowercase(),Normalizer.Form.NFD).replace("\\p{Mn}+".toRegex(),"")
}

