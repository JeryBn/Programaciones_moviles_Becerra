package com.becerra.tecsupfit

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.test.platform.app.InstrumentationRegistry
import android.graphics.Bitmap
import java.io.File
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

    private fun captura(nombre: String) {
        compose.waitForIdle()
        val instrumentacion = InstrumentationRegistry.getInstrumentation()
        val archivo = File(instrumentacion.targetContext.getExternalFilesDir(null), "$nombre.png")
        archivo.outputStream().use { instrumentacion.uiAutomation.takeScreenshot().compress(Bitmap.CompressFormat.PNG, 100, it) }
    }

    @Test fun cancelarRequiereConfirmacionYLiberaHorario() {
        abrir()
        reservarYoga()
        captura("reservas")
        compose.onNodeWithText("Cancelar reserva", substring = false).performClick()
        compose.onNodeWithText("¿Cancelar reserva?").assertIsDisplayed()
        captura("dialogo-cancelacion")
        compose.onNodeWithText("Mantener reserva").performClick()
        compose.onNodeWithText("Confirmada").assertIsDisplayed()
        compose.onNodeWithText("Cancelar reserva", substring = false).performClick()
        compose.onNodeWithText("Sí, cancelar").performClick()
        compose.onNodeWithText("Cancelada").assertIsDisplayed()
        compose.onNodeWithText("Cancelar reserva", substring = false).assertDoesNotExist()
        captura("reserva-cancelada")
        compose.onNodeWithText("Inicio", substring = false).performClick()
        compose.onNodeWithText("Yoga Flow").performClick()
        compose.onNodeWithText("Reservar cupo", substring = false).performClick()
        compose.onNodeWithText("Hoy · 12:00").assertIsEnabled().performClick()
        compose.onNodeWithText("Confirmar reserva").performClick()
        compose.onNodeWithText("¡Reserva confirmada!").assertIsDisplayed()
    }

    @Test fun reservasSeRestauranAlRecrearEstado() {
        val restauracion = StateRestorationTester(compose)
        restauracion.setContent { FitTheme { FitApp() } }
        reservarYoga()
        restauracion.emulateSavedInstanceStateRestore()
        compose.onNodeWithText("Hoy · 12:00").assertIsDisplayed()
        compose.onNodeWithText("Confirmada").assertIsDisplayed()
        compose.onNodeWithText("Perfil", substring = false).performClick()
        compose.onNodeWithText("Reservas confirmadas: 1").assertIsDisplayed()
    }
}
