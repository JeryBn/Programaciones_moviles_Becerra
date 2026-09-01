package com.prestamos.sistema

import com.prestamos.sistema.domain.calculator.CalculadoraPrestamo
import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDate

class CalculadoraPrestamoTest {

    @Test fun `6 cuotas 20 porciento`() {
        assertEquals(0.20, CalculadoraPrestamo.obtenerPorcentaje(6), 0.001)
    }
    @Test fun `12 cuotas 40 porciento`() {
        assertEquals(0.40, CalculadoraPrestamo.obtenerPorcentaje(12), 0.001)
    }
    @Test fun `24 cuotas 60 porciento`() {
        assertEquals(0.60, CalculadoraPrestamo.obtenerPorcentaje(24), 0.001)
    }

    @Test fun `ejemplo guia 1200 con 6 cuotas`() {
        val p = CalculadoraPrestamo.crearPrestamo("Juan", 1200.0, 6, LocalDate.of(2026, 9, 26))
        assertEquals(240.0, p.interesGenerado, 0.01)
        assertEquals(1440.0, p.montoTotal, 0.01)
        assertEquals(240.0, p.pagoMensual, 0.01)
        assertEquals(6, p.cronograma.size)
        // saldo final 0
        assertEquals(0.0, p.cronograma.last().saldoRestante, 0.01)
        // primera fecha
        assertEquals(LocalDate.of(2026, 9, 26), p.cronograma[0].fechaVencimiento)
        // segunda +1 mes
        assertEquals(LocalDate.of(2026, 10, 26), p.cronograma[1].fechaVencimiento)
    }

    @Test fun `ejemplo 12 cuotas 40 porciento`() {
        val p = CalculadoraPrestamo.crearPrestamo("Maria", 1000.0, 12, LocalDate.of(2026, 1, 1))
        assertEquals(400.0, p.interesGenerado, 0.01)
        assertEquals(1400.0, p.montoTotal, 0.01)
        assertEquals(116.67, p.pagoMensual, 0.05) // 1400/12=116.66
        assertEquals(12, p.cronograma.size)
        assertEquals(0.0, p.cronograma.last().saldoRestante, 0.01)
    }

    @Test fun `prestamo 24 cuotas 2400 calcula montos`() {
        val p = CalculadoraPrestamo.crearPrestamo("Pedro", 2400.0, 24, LocalDate.of(2026, 1, 1))
        assertEquals(1440.0, p.interesGenerado, 0.01)
        assertEquals(3840.0, p.montoTotal, 0.01)
        assertEquals(160.0, p.pagoMensual, 0.01)
    }

    @Test fun `validacion nombre vacio`() {
        val errores = CalculadoraPrestamo.validarEntrada("", "1000")
        assertTrue(errores.any { it.contains("Nombre") })
    }

    @Test fun `validacion monto no numerico`() {
        val errores = CalculadoraPrestamo.validarEntrada("Ana", "abc")
        assertTrue(errores.any { it.contains("numérico") })
    }

    @Test fun `toggle cuota cambia estado`() {
        val p = CalculadoraPrestamo.crearPrestamo("Luz", 1200.0, 6, LocalDate.of(2026, 9, 26))
        val p2 = CalculadoraPrestamo.toggleCuota(p, 1)
        assertEquals(com.prestamos.sistema.domain.model.EstadoCuota.PAGADA, p2.cronograma[0].estado)
        // totalPagado debe reflejar
        assertEquals(240.0, p2.totalPagado, 0.01)
        val p3 = CalculadoraPrestamo.toggleCuota(p2, 1)
        assertEquals(com.prestamos.sistema.domain.model.EstadoCuota.PENDIENTE, p3.cronograma[0].estado)
    }

    @Test fun `saldo decrece con cada pago ejemplo pizarra`() {
        // Guía §4: monto 1200 sin interés simulado con 6 cuotas pero usando total 1200 para validar lógica saldo
        val cronograma = CalculadoraPrestamo.generarCronograma(1200.0, 200.0, 6, LocalDate.of(2026, 9, 26))
        assertEquals(1000.0, cronograma[0].saldoRestante, 0.01)
        assertEquals(800.0, cronograma[1].saldoRestante, 0.01)
        assertEquals(600.0, cronograma[2].saldoRestante, 0.01)
        assertEquals(0.0, cronograma[5].saldoRestante, 0.01)
    }
}
