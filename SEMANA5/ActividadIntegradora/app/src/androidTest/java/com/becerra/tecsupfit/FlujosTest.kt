package com.becerra.tecsupfit

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import org.junit.Rule
import org.junit.Test

class FlujosTest {
    @get:Rule val compose = createComposeRule()

    private fun abrir(usarScaffold: Boolean = true, usarNavegacion: Boolean = true, lazyColumn: Boolean = true, lazyRow: Boolean = true) {
        compose.setContent { FitTheme { FitApp(usarScaffold, usarNavegacion, lazyColumn, lazyRow) } }
    }

    private fun reservarYoga() {
        compose.onNodeWithText("Yoga Flow").performClick()
        compose.onNodeWithText("Reservar cupo", useUnmergedTree = true).performClick()
        compose.onNodeWithText("Confirmar reserva").assertIsNotEnabled()
        compose.onNodeWithText("Hoy · 12:00").performClick()
        compose.onNodeWithText("Confirmar reserva").performClick()
        compose.onNodeWithText("¡Reserva confirmada!").assertIsDisplayed()
        compose.onNodeWithText("Hoy · 12:00").assertIsDisplayed()
        compose.onNodeWithText("Ver mis reservas").performClick()
        compose.onNodeWithText("Confirmada").assertIsDisplayed()
        compose.onNodeWithText("Completada").assertIsDisplayed()
    }

    @Test fun flujoCompletoConScaffold() { abrir(); reservarYoga() }
    @Test fun sinScaffoldSigueNavegandoYReservando() { abrir(usarScaffold = false); reservarYoga() }
    @Test fun sinNavegacionConservaScaffoldYFiltros() {
        abrir(usarNavegacion = false)
        compose.onNodeWithText("TECSUP Fit · Sin navegación").assertIsDisplayed()
        compose.onNodeWithText("Reservas").assertIsDisplayed()
        compose.onNodeWithText("Hoy", substring = false).performClick()
        compose.onNodeWithText("Cardio Dance").assertDoesNotExist()
        compose.onNodeWithText("Yoga Flow").assertIsDisplayed()
    }
    @Test fun sinLazyColumnNiLazyRowSigueFuncionando() {
        abrir(lazyColumn = false, lazyRow = false)
        reservarYoga()
    }
    @Test fun cuatroPestanasMuestranSuContenido() {
        abrir()
        compose.onNodeWithText("Rutinas", substring = false).performClick()
        compose.onNodeWithText("Tu ruta de entrenamiento").assertIsDisplayed()
        compose.onNodeWithText("Perfil", substring = false).performClick()
        compose.onNodeWithText("Clases completadas: 1").assertIsDisplayed()
        compose.onNodeWithText("Inicio", substring = false).performClick()
        compose.onNodeWithText("Tu próxima meta").assertIsDisplayed()
    }
}
