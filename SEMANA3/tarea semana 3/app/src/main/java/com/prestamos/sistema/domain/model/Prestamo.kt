package com.prestamos.sistema.domain.model

import java.time.LocalDate

/**
 * Modelo de préstamo - contiene información del préstamo, como monto, cuotas, interés, total y cuota.
 * Guía §1, §8, §9, §11
 */
data class Prestamo(
    val id: Long = 0,
    val cliente: Cliente,
    val montoInicial: Double,
    val numeroCuotas: Int,
    val porcentajeInteres: Double, // 0.20, 0.40, 0.60
    val interesGenerado: Double,
    val montoTotal: Double,
    val pagoMensual: Double,
    val fechaInicio: LocalDate,
    val cronograma: List<Cuota> = emptyList()
) {
    init {
        require(montoInicial > 0) { "Monto debe ser > 0" }
        require(numeroCuotas in setOf(6, 12, 24)) { "Cuotas debe ser 6, 12 o 24" }
    }

    val totalPagado: Double get() = cronograma.filter { it.estado == EstadoCuota.PAGADA }.sumOf { it.montoCuota }
    val saldoPendiente: Double get() = cronograma.lastOrNull()?.saldoRestante ?: montoTotal
    val cuotasPagadas: Int get() = cronograma.count { it.estado == EstadoCuota.PAGADA }
    val cuotasPendientes: Int get() = cronograma.count { it.estado == EstadoCuota.PENDIENTE }
}
