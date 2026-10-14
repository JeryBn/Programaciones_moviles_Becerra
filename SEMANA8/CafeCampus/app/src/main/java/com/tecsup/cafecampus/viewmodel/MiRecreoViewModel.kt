package com.tecsup.cafecampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.flow.*
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.model.*
class MiRecreoViewModel(repo:CafeRepository,prefs:PreferenciasStore):ViewModel() {
 val minutos=MutableStateFlow(10)
 val plan=combine(repo.observarCantidadPorEstado(EstadoPedido.RECIBIDO),repo.observarCantidadPorEstado(EstadoPedido.PREPARANDO),minutos,prefs.recibirPedidos) {n,p,m,a->calcularRecreo(n,p,m,a)}.stateIn(viewModelScope,SharingStarted.WhileSubscribed(5000),calcularRecreo(0,0,10,true))
}

