package com.prestamos.sistema

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prestamos.sistema.ui.navigation.Screen
import com.prestamos.sistema.ui.screens.CronogramaScreen
import com.prestamos.sistema.ui.screens.HistorialScreen
import com.prestamos.sistema.ui.screens.RegistroScreen
import com.prestamos.sistema.ui.screens.ResumenScreen
import com.prestamos.sistema.ui.theme.SistemaPrestamosTheme
import com.prestamos.sistema.ui.viewmodel.PrestamoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SistemaPrestamosTheme {
                val viewModel: PrestamoViewModel = viewModel()
                val uiState by viewModel.uiState.collectAsState()
                val historial by viewModel.prestamosFlow.collectAsState()
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.Registro.route) {
                    composable(Screen.Registro.route) {
                        RegistroScreen(
                            error = uiState.error,
                            onLimpiarError = { viewModel.limpiarError() },
                            onVerHistorial = { navController.navigate(Screen.Historial.route) },
                            onCalcular = { nombre, monto, cuotas, fecha ->
                                val ok = viewModel.calcularYGuardar(nombre, monto, cuotas, fecha)
                                if (ok) {
                                    val id = viewModel.uiState.value.prestamoActual?.id ?: return@RegistroScreen
                                    navController.navigate(Screen.Resumen.createRoute(id))
                                }
                            }
                        )
                    }
                    composable(Screen.Historial.route) {
                        HistorialScreen(
                            prestamos = historial,
                            onBack = { navController.popBackStack() },
                            onSelect = { id -> navController.navigate(Screen.Resumen.createRoute(id)) }
                        )
                    }
                    composable(Screen.Resumen.route) { backStack ->
                        val id = backStack.arguments?.getString("prestamoId")?.toLongOrNull()
                        val prestamo = historial.find { it.id == id } ?: uiState.prestamoActual
                        if (prestamo != null) {
                            // sincroniza selección
                            LaunchedEffect(id) { if (id != null) viewModel.seleccionarPrestamo(id) }
                            ResumenScreen(
                                prestamo = prestamo,
                                onBack = { navController.popBackStack() },
                                onVerCronograma = { navController.navigate(Screen.Cronograma.createRoute(prestamo.id)) },
                                onEliminar = {
                                    viewModel.eliminarActual()
                                    navController.popBackStack(Screen.Registro.route, false)
                                }
                            )
                        }
                    }
                    composable(Screen.Cronograma.route) { backStack ->
                        val id = backStack.arguments?.getString("prestamoId")?.toLongOrNull()
                        val prestamo = historial.find { it.id == id } ?: uiState.prestamoActual
                        if (prestamo != null) {
                            CronogramaScreen(
                                prestamo = prestamo,
                                onBack = { navController.popBackStack() },
                                onToggleCuota = { num -> viewModel.toggleCuota(num) }
                            )
                        }
                    }
                }
            }
        }
    }
}
