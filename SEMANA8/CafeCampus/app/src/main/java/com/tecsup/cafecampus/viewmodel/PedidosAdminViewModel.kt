package com.tecsup.cafecampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
import com.tecsup.cafecampus.model.EstadoPedido
@OptIn(ExperimentalCoroutinesApi::class)
class PedidosAdminViewModel(private val repo:CafeRepository):ViewModel() {
 val estadoSeleccionado=MutableStateFlow(EstadoPedido.RECIBIDO)
 val pedidos=estadoSeleccionado.flatMapLatest {repo.observarPedidosPorEstado(it)}.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 fun pedido(id:Int)=repo.observarPedido(id).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),null)
 fun avanzar(p:PedidoConDetalle) { EstadoPedido.valueOf(p.pedido.estado).siguiente()?.let { e->viewModelScope.launch {repo.cambiarEstado(p.pedido.id,e)} } }
 fun cancelar(p:PedidoConDetalle) { if(EstadoPedido.valueOf(p.pedido.estado).activo) viewModelScope.launch {repo.cambiarEstado(p.pedido.id,EstadoPedido.CANCELADO)} }
}

