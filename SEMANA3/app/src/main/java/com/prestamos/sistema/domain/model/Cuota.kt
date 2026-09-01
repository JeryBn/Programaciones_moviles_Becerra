package com.prestamos.sistema.domain.model

import java.time.LocalDate

/**
 * Modelo de cuota - representa cada pago, su fecha, importe, saldo y estado.
 * Guía §10, §11: Modelo de cuota
 */
enum class EstadoCuota {
    PENDIENTE,
    PAGADA
}

data class Cuota(
    val numero: Int,
    val fechaVencimiento: LocalDate,
    val montoCuota: Double,
    val saldoRestante: Double,
    val estado: EstadoCuota = EstadoCuota.PENDIENTE
) {
    val isLast: Boolean get() = saldoRestante == 0.0
}
