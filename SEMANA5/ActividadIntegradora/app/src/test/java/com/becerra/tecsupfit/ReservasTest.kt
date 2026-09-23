package com.becerra.tecsupfit

import org.junit.Assert.*
import org.junit.Test

class ReservasTest {
    @Test fun filtrarHoyExcluyeClasesDeManana() {
        assertEquals(3, filtrarClases(true).size)
        assertTrue(filtrarClases(true).all { it.hoy })
        assertEquals(4, filtrarClases(false).size)
    }
    @Test fun rechazaHorarioVacioYClaseInexistente() {
        assertFalse(puedeReservar(emptyList(), 1, ""))
        assertFalse(puedeReservar(emptyList(), 999, "Hoy · 08:00"))
        assertFalse(puedeReservar(emptyList(), 1, "Horario inventado"))
    }
    @Test fun evitaDuplicadosSinBloquearOtrosHorarios() {
        val existentes = listOf(Reserva(1, 1, "Hoy · 08:00"))
        assertFalse(puedeReservar(existentes, 1, "Hoy · 08:00"))
        assertTrue(puedeReservar(existentes, 1, "Hoy · 12:00"))
    }
    @Test fun reservaCanceladaLiberaElHorario() {
        val existentes = listOf(Reserva(1, 1, "Hoy · 08:00", EstadoReserva.CANCELADA))
        assertTrue(puedeReservar(existentes, 1, "Hoy · 08:00"))
    }
    @Test fun cancelarConservaHistorialYSoloModificaConfirmadas() {
        val existentes = listOf(Reserva(1, 1, "Hoy · 08:00"), Reserva(2, 2, "Hoy · 09:00", EstadoReserva.COMPLETADA))
        val resultado = cancelarReserva(existentes, 1)
        assertEquals(2, resultado.size)
        assertEquals(EstadoReserva.CANCELADA, resultado.first().estado)
        assertEquals(EstadoReserva.COMPLETADA, cancelarReserva(resultado, 2)[1].estado)
        assertEquals(resultado, cancelarReserva(resultado, 999))
        assertEquals(resultado, cancelarReserva(resultado, 1))
        assertTrue(puedeReservar(resultado, 1, "Hoy · 08:00"))
    }
}
