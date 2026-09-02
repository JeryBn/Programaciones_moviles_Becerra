package com.prestamos.sistema.ui.navigation

sealed class Screen(val route: String) {
    object Registro : Screen("registro")
    object Resumen : Screen("resumen/{prestamoId}") {
        fun createRoute(id: Long) = "resumen/$id"
    }
    object Cronograma : Screen("cronograma/{prestamoId}") {
        fun createRoute(id: Long) = "cronograma/$id"
    }
    object Historial : Screen("historial")
}
