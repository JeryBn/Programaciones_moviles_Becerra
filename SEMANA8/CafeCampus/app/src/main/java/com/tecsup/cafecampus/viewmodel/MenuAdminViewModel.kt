package com.tecsup.cafecampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.data.local.entity.Producto
class MenuAdminViewModel(private val repo:CafeRepository):ViewModel() {
 val productos=repo.observarProductos().stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 val error=MutableStateFlow<String?>(null); val guardado=MutableStateFlow(false)
 fun guardar(id:Int,nombre:String,categoria:String,precio:String,descripcion:String,disponible:Boolean,icono:String) {
  error.value=null
  val valor=precio.replace(',','.').toDoubleOrNull()
  if(nombre.isBlank()||valor==null||!valor.isFinite()||valor<=0) {error.value="Nombre obligatorio y precio mayor que cero";return}
  viewModelScope.launch {try {repo.guardarProducto(Producto(id,nombre.trim(),descripcion,categoria,valor,disponible,icono));guardado.value=true} catch(e:Exception) {error.value=e.message}}
 }
 fun eliminar(p:Producto) {viewModelScope.launch {repo.eliminarProducto(p)}}
 fun alternarDisponibilidad(p:Producto) {viewModelScope.launch {repo.cambiarDisponibilidad(p.id,!p.disponible)}}
}

