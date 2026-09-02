package com.prestamos.sistema.domain.calculator

import com.prestamos.sistema.domain.model.Cliente
import com.prestamos.sistema.domain.model.Cuota
import com.prestamos.sistema.domain.model.EstadoCuota
import com.prestamos.sistema.domain.model.Prestamo
import java.time.LocalDate
import kotlin.math.round

/**
 * Lógica de cálculo: concentra las reglas de interés y los cálculos del préstamo.
 * Guía §7, §8, §9
 *
 * Regla de negocio central (pizarra):
 *  6 cuotas  -> 20%
 * 12 cuotas  -> 40%
 * 24 cuotas  -> 60%
 *
 * Fórmulas:
 *  interés = montoInicial * porcentaje
 *  montoTotal = montoInicial + interés
 *  pagoMensual = montoTotal / numeroCuotas
 *  saldo decrece con cada pago hasta 0
 */
object CalculadoraPrestamo {

    private val reglaInteres: Map<Int, Double> = mapOf(
        6 to 0.20,
        12 to 0.40,
        24 to 0.60
    )

    fun obtenerPorcentaje(cuotas: Int): Double =
        reglaInteres[cuotas] ?: throw IllegalArgumentException("Cuotas no soportadas: $cuotas. Válidos: 6, 12, 24")

    fun obtenerInteresPorcentajeFormateado(cuotas: Int): String {
        val pct = obtenerPorcentaje(cuotas) * 100
        return "${pct.toInt()}%"
    }

    fun calcularInteres(montoInicial: Double, cuotas: Int): Double {
        val pct = obtenerPorcentaje(cuotas)
        return redondear(montoInicial * pct)
    }

    fun calcularMontoTotal(montoInicial: Double, interesGenerado: Double): Double =
        redondear(montoInicial + interesGenerado)

    fun calcularPagoMensual(montoTotal: Double, cuotas: Int): Double =
        redondear(montoTotal / cuotas)

    fun generarCronograma(
        montoTotal: Double,
        pagoMensual: Double,
        numeroCuotas: Int,
        fechaInicio: LocalDate
    ): List<Cuota> {
        val cuotas = mutableListOf<Cuota>()
        var saldo = montoTotal
        for (i in 1..numeroCuotas) {
            val fecha = fechaInicio.plusMonths(i.toLong() - 1)
            // Última cuota ajusta centavos por redondeo
            val montoCuota = if (i == numeroCuotas) redondear(saldo) else pagoMensual
            saldo = redondear(saldo - montoCuota)
            if (saldo < 0.01) saldo = 0.0
            cuotas.add(
                Cuota(
                    numero = i,
                    fechaVencimiento = fecha,
                    montoCuota = montoCuota,
                    saldoRestante = saldo,
                    estado = EstadoCuota.PENDIENTE
                )
            )
        }
        return cuotas
    }

    fun crearPrestamo(
        nombreCliente: String,
        montoInicial: Double,
        numeroCuotas: Int,
        fechaInicio: LocalDate = LocalDate.now()
    ): Prestamo {
        val cliente = Cliente(nombre = nombreCliente.trim())
        val porcentaje = obtenerPorcentaje(numeroCuotas)
        val interes = calcularInteres(montoInicial, numeroCuotas)
        val total = calcularMontoTotal(montoInicial, interes)
        val mensual = calcularPagoMensual(total, numeroCuotas)
        val cronograma = generarCronograma(total, mensual, numeroCuotas, fechaInicio)

        return Prestamo(
            cliente = cliente,
            montoInicial = redondear(montoInicial),
            numeroCuotas = numeroCuotas,
            porcentajeInteres = porcentaje,
            interesGenerado = interes,
            montoTotal = total,
            pagoMensual = mensual,
            fechaInicio = fechaInicio,
            cronograma = cronograma
        )
    }

    fun marcarCuotaPagada(prestamo: Prestamo, numeroCuota: Int): Prestamo {
        val nuevo = prestamo.cronograma.map {
            if (it.numero == numeroCuota) it.copy(estado = EstadoCuota.PAGADA) else it
        }
        return prestamo.copy(cronograma = nuevo)
    }

    fun toggleCuota(prestamo: Prestamo, numeroCuota: Int): Prestamo {
        val nuevo = prestamo.cronograma.map {
            if (it.numero == numeroCuota) {
                val nuevoEstado = if (it.estado == EstadoCuota.PAGADA) EstadoCuota.PENDIENTE else EstadoCuota.PAGADA
                it.copy(estado = nuevoEstado)
            } else it
        }
        return prestamo.copy(cronograma = nuevo)
    }

    private fun redondear(valor: Double): Double = round(valor * 100) / 100.0

    fun validarEntrada(nombre: String, montoStr: String): List<String> {
        val errores = mutableListOf<String>()
        if (nombre.isBlank()) errores.add("Nombre es obligatorio")
        val monto = montoStr.toDoubleOrNull()
        if (monto == null) errores.add("Monto debe ser numérico")
        else if (monto <= 0) errores.add("Monto debe ser mayor a 0")
        else if (monto > 1_000_000) errores.add("Monto excede límite S/ 1,000,000")
        return errores
    }
}
