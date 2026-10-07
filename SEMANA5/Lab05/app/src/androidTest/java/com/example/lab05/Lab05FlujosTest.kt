package com.example.lab05
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.lab05.ui.theme.Lab05Theme
import org.junit.*
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.test.platform.app.InstrumentationRegistry
import android.graphics.Bitmap
import java.io.File
class Lab05FlujosTest {
    @get:Rule val compose = createComposeRule()
    @Before fun limpiar() { Practica.USAR_SCAFFOLD = true; Practica.USAR_NAVEGACION = true; Practica.USAR_LAZY_COLUMN = true }
    @After fun restaurar() = limpiar()
    private fun abrir() { compose.setContent { Lab05Theme { AppNavigation() } } }
    private fun detalle() {
        compose.onNodeWithText("Ver lista de elementos").performScrollTo().performClick()
        compose.onNodeWithText("Elemento número 1").performClick()
        compose.onNodeWithText("Elemento #1").assertIsDisplayed()
captura("lab05-detalle")
        compose.onNodeWithContentDescription("Volver").performClick()
        compose.onNodeWithText("Lista de elementos").assertIsDisplayed()
    }
    @Test fun argumentoEnteroYRegreso() { abrir(); detalle() }
    @Test fun sinScaffoldSigueNavegando() { Practica.USAR_SCAFFOLD = false; abrir(); detalle() }
    @Test fun sinNavegacionConservaInicio() {
        Practica.USAR_NAVEGACION = false; abrir()
        compose.onNodeWithText("Ver lista de elementos").performScrollTo().performClick()
        compose.onNodeWithText("Pantalla Tecsup").assertExists()
        compose.onNodeWithText("Lista de elementos").assertDoesNotExist()
    }
    @Test fun sinLazyColumnSigueElDetalle() { Practica.USAR_LAZY_COLUMN = false; abrir(); detalle() }

    private fun captura(nombre: String) {
        compose.waitForIdle()
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val destino = File(contexto.getExternalFilesDir(null), "$nombre.png")
        val bitmap = compose.onRoot().captureToImage().asAndroidBitmap()
        destino.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
