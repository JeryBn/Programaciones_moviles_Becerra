package com.tecsup.centrotareas.data

import kotlinx.coroutines.flow.Flow

interface TareasRepository {
    fun progresoTarea(): Flow<Float>
    suspend fun contarPrimos(hasta: Int, alAvanzar: (Float) -> Unit = {}): Int
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
}
