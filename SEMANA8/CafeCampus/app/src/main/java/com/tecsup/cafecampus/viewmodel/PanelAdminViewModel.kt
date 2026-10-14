package com.tecsup.cafecampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import java.util.Calendar
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.model.EstadoPedido
class PanelAdminViewModel(repo:CafeRepository):ViewModel() {
 private val inicio=Calendar.getInstance().apply { set(Calendar.HOUR_OF_DAY,0);set(Calendar.MINUTE,0);set(Calendar.SECOND,0);set(Calendar.MILLISECOND,0) }.timeInMillis
 val nuevos=repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),0)
 val preparando=repo.observarCantidadPorEstado(EstadoPedido.PREPARANDO).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),0)
 val listos=repo.observarCantidadPorEstado(EstadoPedido.LISTO).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),0)
 val ventasHoy=repo.observarVentasHoy(inicio).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),0.0)
 val recibidos=repo.observarPedidosPorEstado(EstadoPedido.RECIBIDO).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
 private val fin=Calendar.getInstance().apply {set(Calendar.HOUR_OF_DAY,0);set(Calendar.MINUTE,0);set(Calendar.SECOND,0);set(Calendar.MILLISECOND,0);add(Calendar.DAY_OF_YEAR,1)}.timeInMillis
 val porHora=repo.observarPedidosPorHora(inicio,fin).stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),emptyList())
}

