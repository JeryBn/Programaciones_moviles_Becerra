package com.tecsup.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.campusgps.data.LugaresRepository
import com.tecsup.campusgps.data.UbicacionNoDisponible
import com.tecsup.campusgps.data.UbicacionRepository
import com.tecsup.campusgps.model.*
import com.tecsup.campusgps.util.distanciaMetros
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapaViewModel(
    private val ubicacionRepo: UbicacionRepository,
    lugaresRepo: LugaresRepository
) : ViewModel() {
    val mapaDisponible = ubicacionRepo.playServicesDisponible()
    private val catalogo = lugaresRepo.obtenerLugares()
    private val _estado = MutableStateFlow<UbicacionUi>(UbicacionUi.Inicial)
    val estado = _estado.asStateFlow()
    private val _lugares = MutableStateFlow(catalogo.map { LugarConDistancia(it, null) })
    val lugares = _lugares.asStateFlow()
    private val _seleccionado = MutableStateFlow<Seleccion?>(null)
    val seleccionado = _seleccionado.asStateFlow()
    private var evento = 0

    val masCercano = lugares.map { lista -> lista.firstOrNull { it.metros != null }?.lugar }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    fun buscar() {
        if (_estado.value == UbicacionUi.Buscando) return
        if (!ubicacionRepo.playServicesDisponible()) {
            mostrarError(TipoError.SIN_PLAY_SERVICES, "Este dispositivo no tiene Google Play Services")
            return
        }
        _seleccionado.value = null
        _estado.value = UbicacionUi.Buscando
        viewModelScope.launch {
            ubicacionRepo.obtenerPosicion().onSuccess { posicion ->
                actualizarDistancias(posicion)
                _estado.value = UbicacionUi.Lista(posicion)
            }.onFailure { error ->
                when (error) {
                    is SecurityException -> permisoDenegado()
                    is UbicacionNoDisponible -> mostrarError(TipoError.GPS_APAGADO, error.message.orEmpty())
                    else -> mostrarError(TipoError.OTRO, "No se pudo obtener la ubicación")
                }
            }
        }
    }

    fun permisoDenegado() = mostrarError(
        TipoError.SIN_PERMISO, "Concede el permiso para mostrar tu ubicación"
    )

    fun seleccionar(id: Int?) {
        _seleccionado.value = id?.let { Seleccion(it, ++evento) }
    }

    private fun actualizarDistancias(posicion: Posicion) {
        _lugares.value = catalogo.map { LugarConDistancia(it, distanciaMetros(posicion, it)) }
            .sortedBy { it.metros }
    }

    private fun mostrarError(tipo: TipoError, mensaje: String) {
        _lugares.value = catalogo.map { LugarConDistancia(it, null) }
        _estado.value = UbicacionUi.Error(mensaje, tipo)
    }
}
