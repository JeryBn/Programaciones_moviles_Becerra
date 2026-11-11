package com.tecsup.productos.ui.lista

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.notification.Notificador
import com.tecsup.productos.ui.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Catalogo(val items: List<Item>, val aviso: String? = null, val sincronizando: Boolean = false)
private sealed interface Actualizacion {
    data object EnCurso : Actualizacion
    data object Completa : Actualizacion
    data class Fallida(val mensaje: String) : Actualizacion
}

class ListaViewModel(private val repo: ItemRepository, private val notificador: Notificador) : ViewModel() {
    private val actualizacion = MutableStateFlow<Actualizacion>(Actualizacion.EnCurso)
    private var trabajo: Job? = null
    val estado: StateFlow<UiState<Catalogo>> = combine(repo.items, actualizacion) { items, fase ->
        when (fase) {
            Actualizacion.EnCurso -> if (items.isEmpty()) UiState.Cargando
                else UiState.Exito(Catalogo(items, sincronizando = true))
            Actualizacion.Completa -> UiState.Exito(Catalogo(items))
            is Actualizacion.Fallida -> if (items.isEmpty()) UiState.Error(fase.mensaje)
                else UiState.Exito(Catalogo(items, "${fase.mensaje} Mostrando datos guardados."))
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Cargando)

    init { refrescar() }

    fun refrescar() {
        if (trabajo?.isActive == true) return
        trabajo = viewModelScope.launch {
            actualizacion.value = Actualizacion.EnCurso
            repo.refrescar().onSuccess {
                actualizacion.value = Actualizacion.Completa
                notificador.mostrar("Datos actualizados", "Se guardaron $it elementos")
            }.onFailure { actualizacion.value = Actualizacion.Fallida(it.mensajeCatalogo()) }
        }
    }
    fun alternarFavorito(id: Int) { viewModelScope.launch { repo.alternarFavorito(id) } }
}