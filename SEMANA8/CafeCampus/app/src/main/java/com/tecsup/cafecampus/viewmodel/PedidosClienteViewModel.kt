package com.tecsup.cafecampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.model.*
class PedidosClienteViewModel(private val repo:CafeRepository,private val clienteId:Int,private val prefs:PreferenciasStore):ViewModel() {
 val misPedidos=repo.observarPedidosCliente(clienteId).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 val creado=MutableStateFlow<Long?>(null); val error=MutableStateFlow<String?>(null); val enviando=MutableStateFlow(false)
 fun pedido(id:Int)=repo.observarPedido(id).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),null)
 fun confirmar(items:List<ItemCarrito>,horaRecojo:String,pago:String,nota:String) {
  if(enviando.value) return
  enviando.value=true; error.value=null
  viewModelScope.launch { try {
   check(prefs.recibirPedidos.first()) { "Cafetería cerrada" }
   creado.value=repo.crearPedido(clienteId,items,horaRecojo,pago,nota)
  } catch(e:Exception) { error.value=e.message?:"No se pudo crear el pedido" } finally { enviando.value=false } }
 }
 fun consumido() { creado.value=null }
}

