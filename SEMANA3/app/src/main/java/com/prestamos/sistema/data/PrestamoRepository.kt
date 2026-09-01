package com.prestamos.sistema.data

import com.prestamos.sistema.domain.model.Prestamo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Persistencia en memoria (suficiente per guía §14: no asumir BD si no se exige).
 * Se puede extender a Room sin cambiar UI gracias a esta capa.
 */
class PrestamoRepository {
    private val _prestamos = MutableStateFlow<List<Prestamo>>(emptyList())
    val prestamos: StateFlow<List<Prestamo>> = _prestamos.asStateFlow()

    private var nextId: Long = 1

    fun guardar(prestamo: Prestamo): Prestamo {
        val conId = prestamo.copy(id = nextId++)
        _prestamos.value = _prestamos.value + conId
        return conId
    }

    fun actualizar(prestamo: Prestamo) {
        _prestamos.value = _prestamos.value.map { if (it.id == prestamo.id) prestamo else it }
    }

    fun obtenerPorId(id: Long): Prestamo? = _prestamos.value.find { it.id == id }

    fun eliminar(id: Long) {
        _prestamos.value = _prestamos.value.filterNot { it.id == id }
    }

    fun limpiar() {
        _prestamos.value = emptyList()
        nextId = 1
    }
}
