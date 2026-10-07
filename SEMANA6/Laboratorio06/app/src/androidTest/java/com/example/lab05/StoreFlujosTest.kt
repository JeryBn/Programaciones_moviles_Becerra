package com.example.lab05
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.lab05.ui.theme.Lab05Theme
import org.junit.*
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.test.platform.app.InstrumentationRegistry
import android.graphics.Bitmap
import java.io.File
class StoreFlujosTest {
    @get:Rule val compose = createComposeRule()
    @Before fun limpiar() { Practica.USAR_SCAFFOLD = true; Practica.USAR_NAVEGACION = true; Practica.USAR_LAZY_COLUMN = true; Practica.USAR_LAZY_ROW = true }
    @After fun restaurar() = limpiar()
    private fun abrir() { compose.setContent { Lab05Theme { TecsupStoreApp() } } }
    private fun marcar() {
        compose.onNodeWithContentDescription("Opciones de Audífonos").performClick()
        compose.onNodeWithText("Favoritos", substring = false).performClick()
        compose.onNodeWithContentDescription("Abrir menú").performClick()
        compose.onNodeWithContentDescription("1 productos favoritos").assertExists()
captura("store-favoritos-drawer")
        compose.onNode(hasText("Favoritos") and hasClickAction()).performClick()
        compose.onNodeWithText("Audífonos").assertIsDisplayed()
        compose.onNodeWithText("Smartwatch").assertDoesNotExist()
    }
    @Test fun menuModificaContadorYDrawerNavega() { abrir(); marcar() }
    @Test fun sinScaffoldMenuYNavegacionFuncionan() { Practica.USAR_SCAFFOLD = false; abrir(); marcar() }
    @Test fun sinNavegacionConservaScaffold() {
        Practica.USAR_NAVEGACION = false; abrir()
        compose.onNodeWithText("TECSUP Store · práctica sin navegación").assertIsDisplayed()
        compose.onNodeWithContentDescription("Abrir menú").performClick()
        compose.onNodeWithText("Perfil", substring = false).assertExists()
    }
    @Test fun sinLazyColumnNiLazyRowConservaFiltrado() {
        Practica.USAR_LAZY_COLUMN = false; Practica.USAR_LAZY_ROW = false; abrir()
        compose.onNode(hasText("Accesorios") and hasClickAction()).performClick()
        compose.onNodeWithText("Audífonos").assertDoesNotExist()
        compose.onNodeWithText("Funda celular").assertIsDisplayed()
    }
    @Test fun reportarSolicitaConfirmacionYDetalleRecibeId() {
        abrir(); compose.onNodeWithContentDescription("Opciones de Audífonos").performClick()
        compose.onNodeWithText("Reportar").performClick()
        compose.onNodeWithText("Confirmar reporte").performClick()
        compose.onNodeWithText("Reporte local registrado para Audífonos").assertIsDisplayed()
        compose.onNodeWithText("Aceptar").performClick()
        compose.onAllNodesWithText("Ver detalle")[0].performClick()
        compose.onNodeWithText("ID: 1 · Tecnología").assertIsDisplayed()
    }

    private fun captura(nombre: String) {
        compose.waitForIdle()
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val destino = File(contexto.getExternalFilesDir(null), "$nombre.png")
        val bitmap = compose.onRoot().captureToImage().asAndroidBitmap()
        destino.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
    }
}
