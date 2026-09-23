package com.becerra.tecsupfit

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable

// Saver convierte el estado a tipos que Android puede restaurar al recrear la Activity.
private val reservasSaver = listSaver<List<Reserva>, String>(
    save = { lista -> lista.map { "${it.id}|${it.claseId}|${it.horario}|${it.estado.name}" } },
    restore = { lista -> lista.map {
        val partes = it.split('|')
        Reserva(partes[0].toInt(), partes[1].toInt(), partes[2], EstadoReserva.valueOf(partes[3]))
    } }
)

@Composable
fun FitApp(
    usarScaffold: Boolean = Practica.USAR_SCAFFOLD,
    usarNavegacion: Boolean = Practica.USAR_NAVEGACION,
    usarLazyColumn: Boolean = Practica.USAR_LAZY_COLUMN,
    usarLazyRow: Boolean = Practica.USAR_LAZY_ROW
) {
    var reservas by rememberSaveable(stateSaver = reservasSaver) { mutableStateOf(reservasIniciales()) }
    val registrar: (Int, String) -> Int? = { claseId, horario ->
        if (puedeReservar(reservas, claseId, horario)) {
            val id = (reservas.maxOfOrNull { it.id } ?: 0) + 1
            reservas = reservas + Reserva(id, claseId, horario)
            id
        } else null
    }
    if (usarNavegacion) {
        Navegacion(reservas, registrar, { id -> reservas = cancelarReserva(reservas, id) }, usarScaffold, usarLazyColumn, usarLazyRow)
    } else {
        // Práctica: se retira el NavHost, pero el Scaffold sigue mostrando sus tres áreas.
        // Las pestañas son demostrativas: al retirar navegación no cambian de pantalla.
        Estructura("TECSUP Fit · Sin navegación", "inicio", usarScaffold, null, {}) {
            Inicio({}, usarLazyColumn, usarLazyRow)
        }
    }
}
