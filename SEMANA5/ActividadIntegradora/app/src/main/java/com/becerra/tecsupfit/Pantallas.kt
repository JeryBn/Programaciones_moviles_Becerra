package com.becerra.tecsupfit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Pagina(contenido: @Composable ColumnScope.() -> Unit) {
    Column(
        Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp), content = contenido
    )
}

@Composable
fun Detalle(clase: Clase?, reservar: () -> Unit) {
    Pagina {
        if (clase == null) { Text("Clase no encontrada"); return@Pagina }
        Text(clase.categoria, color = MaterialTheme.colorScheme.primary)
        Text(clase.nombre, style = MaterialTheme.typography.headlineLarge)
        Text(clase.descripcion)
        Text("Horarios disponibles", style = MaterialTheme.typography.titleMedium)
        clase.horarios.forEach { Text(it) }
        Button(onClick = reservar, modifier = Modifier.fillMaxWidth()) { Text("Reservar cupo") }
    }
}

@Composable
fun ElegirHorario(clase: Clase?, reservas: List<Reserva>, confirmar: (String) -> Unit) {
    var horario by rememberSaveable(clase?.id) { mutableStateOf("") }
    Pagina {
        if (clase == null) { Text("Clase no encontrada"); return@Pagina }
        Text("Elige tu horario", style = MaterialTheme.typography.headlineMedium)
        Text(clase.nombre, style = MaterialTheme.typography.titleLarge)
        Text("Selecciona una sola opción para confirmar tu reserva.")
        clase.horarios.forEach { opcion ->
            val disponible = puedeReservar(reservas, clase.id, opcion)
            FilterChip(
                selected = horario == opcion,
                onClick = { horario = opcion },
                enabled = disponible,
                label = { Text(if (disponible) opcion else "$opcion · Ya reservado") }
            )
        }
        Button(
            onClick = { confirmar(horario) },
            enabled = puedeReservar(reservas, clase.id, horario),
            modifier = Modifier.fillMaxWidth()
        ) { Text("Confirmar reserva") }
    }
}

@Composable
fun Confirmacion(reserva: Reserva?, verReservas: () -> Unit, irInicio: () -> Unit) {
    Pagina {
        Text(if (reserva != null) "¡Reserva confirmada!" else "Reserva no encontrada", style = MaterialTheme.typography.headlineMedium)
        if (reserva != null) {
            Text(clases.firstOrNull { it.id == reserva.claseId }?.nombre.orEmpty(), style = MaterialTheme.typography.titleLarge)
            Text(reserva.horario)
            Text("Tu cupo está registrado en esta sesión.")
        }
        Button(onClick = verReservas, modifier = Modifier.fillMaxWidth()) { Text("Ver mis reservas") }
        OutlinedButton(onClick = irInicio, modifier = Modifier.fillMaxWidth()) { Text("Volver al inicio") }
    }
}

@Composable
fun Reservas(reservas: List<Reserva>) {
    if (reservas.isEmpty()) {
        Pagina { Text("Aún no tienes reservas. Elige una clase en Inicio.") }
    } else {
        ListaVertical(reservas, { it.id }) { reserva ->
            Card(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(clases.first { it.id == reserva.claseId }.nombre, style = MaterialTheme.typography.titleLarge)
                    Text(reserva.horario)
                    val color = when (reserva.estado) {
                        EstadoReserva.CONFIRMADA -> MaterialTheme.colorScheme.primary
                        EstadoReserva.COMPLETADA -> MaterialTheme.colorScheme.secondary
                        EstadoReserva.CANCELADA -> MaterialTheme.colorScheme.error
                    }
                    Text(reserva.estado.name.lowercase().replaceFirstChar { it.uppercase() }, color = color)
                }
            }
        }
    }
}

@Composable
fun Rutinas() {
    Pagina {
        Text("Tu ruta de entrenamiento", style = MaterialTheme.typography.headlineMedium)
        Text("Rutina orientativa de demostración", style = MaterialTheme.typography.bodyMedium)
        listOf("1. Activación · 5 minutos", "2. Clase a tu elección · 40–50 minutos", "3. Vuelta a la calma · 5 minutos").forEach {
            Card(Modifier.fillMaxWidth()) { Text(it, Modifier.padding(20.dp)) }
        }
    }
}

@Composable
fun Perfil(reservas: List<Reserva>) {
    Pagina {
        Text("Mi perfil", style = MaterialTheme.typography.headlineLarge)
        Text("Estudiante TECSUP", style = MaterialTheme.typography.titleLarge)
        Text("Perfil local de demostración")
        Card(Modifier.fillMaxWidth()) {
            Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Mi actividad", style = MaterialTheme.typography.titleLarge)
                Text("Clases completadas: ${reservas.count { it.estado == EstadoReserva.COMPLETADA }}")
                Text("Reservas confirmadas: ${reservas.count { it.estado == EstadoReserva.CONFIRMADA }}")
            }
        }
        Text("Los datos se conservan al girar la pantalla; no se guardan permanentemente. Se incluye una clase completada de ejemplo.")
    }
}
