package com.tecsup.centrotareas.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.centrotareas.data.TareasRepository
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TareasViewModel(private val repo: TareasRepository) : ViewModel() {
    private val _mensaje = MutableStateFlow("Listo")
    val mensaje = _mensaje.asStateFlow()
    private val _progreso = MutableStateFlow(0f)
    val progreso = _progreso.asStateFlow()
    private val _corriendo = MutableStateFlow(false)
    val corriendo = _corriendo.asStateFlow()
    private var trabajo: Job? = null

    private val _primos = MutableStateFlow<PrimosUi>(PrimosUi.Inicial)
    val primos = _primos.asStateFlow()
    private var trabajoPrimos: Job? = null
    private val _progresoPrimos = MutableStateFlow(0f)
    val progresoPrimos = _progresoPrimos.asStateFlow()

    private val _carga = MutableStateFlow<CargaUi>(CargaUi.Inicial)
    val carga = _carga.asStateFlow()
    private val _simularError = MutableStateFlow(false)
    val simularError = _simularError.asStateFlow()

    fun tareaLarga() {
        if (_corriendo.value) return
        _corriendo.value = true
        _mensaje.value = "Trabajando…"
        _progreso.value = 0f
        trabajo = viewModelScope.launch(start = CoroutineStart.UNDISPATCHED) {
            try {
                Log.d("HILO", "Antes: ${Thread.currentThread().name}")
                repo.progresoTarea().collect { _progreso.value = it }
                Log.d("HILO", "Después: ${Thread.currentThread().name}")
                _mensaje.value = "Terminó"
            } catch (e: CancellationException) {
                _mensaje.value = "Cancelado"
                throw e
            } finally {
                _corriendo.value = false
            }
        }
    }

    fun cancelar() {
        trabajo?.cancel()
    }

    fun contar(hasta: Int) {
        if (hasta < 2 || _primos.value == PrimosUi.Calculando) return
        _progresoPrimos.value = 0f
        _primos.value = PrimosUi.Calculando
        trabajoPrimos = viewModelScope.launch(start = CoroutineStart.UNDISPATCHED) {
            try {
                val inicio = System.currentTimeMillis()
                val cantidad = repo.contarPrimos(hasta) { _progresoPrimos.value = it }
                _primos.value = PrimosUi.Listo(cantidad, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) {
                _primos.value = PrimosUi.Cancelado
                throw e
            }
        }
    }

    fun cancelarPrimos() {
        trabajoPrimos?.cancel()
    }

    fun cargarUnaPorUna() = cargar(enParalelo = false)
    fun cargarALaVez() = cargar(enParalelo = true)

    fun cambiarSimularError(valor: Boolean) {
        _simularError.value = valor
    }

    private fun cargar(enParalelo: Boolean) {
        if (_carga.value == CargaUi.Cargando) return
        _carga.value = CargaUi.Cargando
        val simularError = _simularError.value
        viewModelScope.launch {
            val inicio = System.currentTimeMillis()
            try {
                val (perfil, pedidos) = if (enParalelo) {
                    coroutineScope {
                        val perfil = async { repo.cargarPerfil() }
                        val pedidos = async { repo.cargarPedidos(simularError) }
                        perfil.await() to pedidos.await()
                    }
                } else {
                    repo.cargarPerfil() to repo.cargarPedidos(simularError)
                }
                val modo = if (enParalelo) "A la vez" else "Una por una"
                _carga.value = CargaUi.Exito(modo, perfil, pedidos.size, System.currentTimeMillis() - inicio)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _carga.value = CargaUi.Error("No se pudo cargar: ${e.message ?: "Error desconocido"}")
            }
        }
    }
}
