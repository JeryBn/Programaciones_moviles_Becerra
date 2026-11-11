package com.tecsup.productos.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.data.repository.resultado
import com.tecsup.productos.domain.model.Rol
import com.tecsup.productos.notification.Notificador
import com.tecsup.productos.ui.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class Totales(val guardados: Int, val favoritos: Int, val mensaje: String? = null)

class AdminViewModel(private val repo: ItemRepository, private val notificador: Notificador, rol: Rol) : ViewModel() {
    private val mutable = MutableStateFlow<UiState<Totales>>(UiState.Cargando)
    val estado = mutable.asStateFlow()
    init {
        require(rol == Rol.ADMIN)
        ejecutar { null }
    }

    fun sincronizar() = ejecutar {
        repo.refrescar().fold(
            { notificador.mostrar("Datos actualizados", "Se guardaron $it elementos"); "Se guardaron $it elementos." },
            { it.mensajeCatalogo() })
    }

    fun limpiarCache() = ejecutar {
        repo.limpiarCache()
        "Se eliminó la caché local."
    }

    private fun ejecutar(accion: suspend () -> String?) {
        mutable.value = UiState.Cargando
        viewModelScope.launch {
            mutable.value = resultado {
                val mensaje = accion()
                Totales(repo.cantidadGuardada(), repo.cantidadFavoritos(), mensaje)
            }.fold({ UiState.Exito(it) }, { UiState.Error(it.mensajeCatalogo()) })
        }
    }
}