package com.prestamos.sistema.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.prestamos.sistema.data.PrestamoRepository
import com.prestamos.sistema.domain.calculator.CalculadoraPrestamo
import com.prestamos.sistema.domain.model.Prestamo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

data class PrestamoUiState(
    val prestamoActual: Prestamo? = null,
    val historial: List<Prestamo> = emptyList(),
    val error: String? = null,
    val mensaje: String? = null
)

class PrestamoViewModel(
    private val repository: PrestamoRepository = PrestamoRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrestamoUiState())
    val uiState: StateFlow<PrestamoUiState> = _uiState.asStateFlow()

    val prestamosFlow = repository.prestamos

    fun calcularYGuardar(
        nombre: String,
        montoStr: String,
        cuotas: Int,
        fechaInicio: LocalDate = LocalDate.now()
    ): Boolean {
        val errores = CalculadoraPrestamo.validarEntrada(nombre, montoStr)
        if (errores.isNotEmpty()) {
            _uiState.value = _uiState.value.copy(error = errores.joinToString("\n"))
            return false
        }
        return try {
            val monto = montoStr.toDouble()
            val prestamo = CalculadoraPrestamo.crearPrestamo(nombre, monto, cuotas, fechaInicio)
            val guardado = repository.guardar(prestamo)
            _uiState.value = _uiState.value.copy(
                prestamoActual = guardado,
                historial = repository.prestamos.value,
                error = null,
                mensaje = "Préstamo calculado y guardado"
            )
            true
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(error = e.message)
            false
        }
    }

    fun seleccionarPrestamo(id: Long) {
        val p = repository.obtenerPorId(id)
        _uiState.value = _uiState.value.copy(prestamoActual = p)
    }

    fun toggleCuota(numeroCuota: Int) {
        val actual = _uiState.value.prestamoActual ?: return
        val actualizado = CalculadoraPrestamo.toggleCuota(actual, numeroCuota)
        repository.actualizar(actualizado)
        _uiState.value = _uiState.value.copy(
            prestamoActual = actualizado,
            historial = repository.prestamos.value
        )
    }

    fun eliminarActual() {
        val actual = _uiState.value.prestamoActual ?: return
        repository.eliminar(actual.id)
        _uiState.value = _uiState.value.copy(
            prestamoActual = null,
            historial = repository.prestamos.value,
            mensaje = "Préstamo eliminado"
        )
    }

    fun limpiarError() {
        _uiState.value = _uiState.value.copy(error = null, mensaje = null)
    }
}
