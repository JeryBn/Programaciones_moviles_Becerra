package com.tecsup.centrotareas.data

import android.util.Log
import java.io.IOException
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext

class TareasRepositoryLocal : TareasRepository {
    override fun progresoTarea() = flow {
        Log.d("HILO", "Dentro: ${Thread.currentThread().name}")
        for (paso in 1..100) {
            delay(40)
            emit(paso / 100f)
        }
    }.flowOn(Dispatchers.Default)

    override suspend fun cargarPerfil(): String {
        delay(2_000)
        return "Ana Torres (ficticia)"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3_000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    override suspend fun contarPrimos(hasta: Int, alAvanzar: (Float) -> Unit): Int =
        withContext(Dispatchers.Default) {
            var cantidad = 0
            var porcentajeAnterior = 0
            val intervalo = (hasta / 100).coerceAtLeast(1)
            for (n in 2..hasta) {
                ensureActive()
                if (esPrimo(n)) cantidad++
                if (n == hasta || n % intervalo == 0) {
                    val porcentaje = (n * 100L / hasta).toInt()
                    if (porcentaje > porcentajeAnterior) {
                        porcentajeAnterior = porcentaje
                        alAvanzar(porcentaje / 100f)
                    }
                }
            }
            cantidad
        }

    private fun esPrimo(n: Int): Boolean {
        var divisor = 2
        while (divisor.toLong() * divisor <= n) {
            if (n % divisor == 0) return false
            divisor++
        }
        return true
    }
}
