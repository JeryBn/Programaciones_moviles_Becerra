package com.becerra.tecsupfit

// Datos locales de demostración. No hay servidor, base de datos ni ViewModel.
data class Clase(
    val id: Int,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val hoy: Boolean,
    val horarios: List<String>
)

enum class EstadoReserva { CONFIRMADA, COMPLETADA, CANCELADA }

data class Reserva(
    val id: Int,
    val claseId: Int,
    val horario: String,
    val estado: EstadoReserva = EstadoReserva.CONFIRMADA
)

val clases = listOf(
    Clase(1, "Yoga Flow", "Bienestar", "Respiración, equilibrio y movilidad. Duración: 45 minutos.", true,
        listOf("Hoy · 08:00", "Hoy · 12:00", "Hoy · 18:00")),
    Clase(2, "Fuerza total", "Fuerza", "Entrenamiento de cuerpo completo. Duración: 50 minutos.", true,
        listOf("Hoy · 09:00", "Hoy · 14:00", "Hoy · 19:00")),
    Clase(3, "Cardio Dance", "Cardio", "Movimiento y música para activar tu día. Duración: 40 minutos.", false,
        listOf("Mañana · 08:00", "Mañana · 13:00", "Mañana · 18:00")),
    Clase(4, "Pilates", "Bienestar", "Control postural y fortalecimiento. Duración: 45 minutos.", true,
        listOf("Hoy · 10:00", "Hoy · 15:00", "Hoy · 20:00"))
)

fun filtrarClases(soloHoy: Boolean): List<Clase> = clases.filter { !soloHoy || it.hoy }

fun puedeReservar(reservas: List<Reserva>, claseId: Int, horario: String): Boolean =
    clases.any { it.id == claseId && horario in it.horarios } &&
        reservas.none { it.claseId == claseId && it.horario == horario && it.estado != EstadoReserva.CANCELADA }

// Una reserva completada permite demostrar estados diferentes desde el primer inicio.
fun reservasIniciales() = listOf(Reserva(1, 1, "Sesión anterior · 08:00", EstadoReserva.COMPLETADA))
