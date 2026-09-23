package com.becerra.tecsupfit

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument

@Composable
fun Navegacion(
    reservas: List<Reserva>,
    registrar: (Int, String) -> Int?,
    usarScaffold: Boolean,
    usarLazyColumn: Boolean,
    usarLazyRow: Boolean
) {
    val nav = rememberNavController()
    val entrada by nav.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route ?: "inicio"
    val esPrincipal = destinos.any { it.ruta == ruta }
    val pestanaActiva = when {
        esPrincipal -> ruta
        ruta.startsWith("confirmacion") -> "reservas"
        else -> "inicio"
    }
    val titulo = when {
        ruta.startsWith("detalle") -> "Detalle de clase"
        ruta.startsWith("horario") -> "Reservar cupo"
        ruta.startsWith("confirmacion") -> "Confirmación"
        else -> destinos.first { it.ruta == pestanaActiva }.titulo
    }
    val irSeccion: (String) -> Unit = { destino ->
        nav.navigate(destino) {
            popUpTo("inicio") { inclusive = false }
            launchSingleTop = true
        }
    }
    Estructura(
        "TECSUP Fit · $titulo", pestanaActiva, usarScaffold,
        if (esPrincipal) null else ({ nav.popBackStack(); Unit }), irSeccion
    ) {
        NavHost(navController = nav, startDestination = "inicio") {
            composable("inicio") { Inicio({ nav.navigate("detalle/$it") }, usarLazyColumn, usarLazyRow) }
            composable("reservas") { Reservas(reservas) }
            composable("rutinas") { Rutinas() }
            composable("perfil") { Perfil(reservas) }
            composable("detalle/{claseId}", arguments = listOf(navArgument("claseId") { type = NavType.IntType })) { entry ->
                val claseId = entry.arguments?.getInt("claseId")
                Detalle(clases.find { it.id == claseId }) { nav.navigate("horario/$claseId") }
            }
            composable("horario/{claseId}", arguments = listOf(navArgument("claseId") { type = NavType.IntType })) { entry ->
                val claseId = entry.arguments?.getInt("claseId") ?: -1
                ElegirHorario(clases.find { it.id == claseId }, reservas) { horario ->
                    registrar(claseId, horario)?.let { reservaId ->
                        nav.navigate("confirmacion/$reservaId") {
                            // Confirmar no deja el formulario en la pila: evita reenviar la reserva al volver.
                            popUpTo("inicio") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            }
            composable("confirmacion/{reservaId}", arguments = listOf(navArgument("reservaId") { type = NavType.IntType })) { entry ->
                val reserva = reservas.find { it.id == entry.arguments?.getInt("reservaId") }
                Confirmacion(reserva, { irSeccion("reservas") }, { irSeccion("inicio") })
            }
        }
    }
}
