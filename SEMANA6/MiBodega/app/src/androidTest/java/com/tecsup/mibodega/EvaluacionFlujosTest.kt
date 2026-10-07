package com.tecsup.mibodega
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.tecsup.mibodega.ui.cliente.*
import org.junit.*
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.test.platform.app.InstrumentationRegistry
import android.graphics.Bitmap
import java.io.File
class EvaluacionFlujosTest {
    @get:Rule val compose = createComposeRule()
    @Before fun limpiar() { Practica.USAR_SCAFFOLD = true; Practica.USAR_NAVEGACION = true; Practica.USAR_LAZY_COLUMN = true; Practica.USAR_LAZY_ROW = true }
    @After fun restaurar() = limpiar()
    private fun abrir() { compose.setContent { ClienteApp() } }
    private fun login() {
        compose.onNodeWithText("Usuario").performTextInput("jery")
        compose.onNodeWithText("Contraseña").performTextInput("1234")
        compose.onNodeWithText("Iniciar sesión").performScrollTo().performClick()
        compose.onNodeWithText("Buscar productos", useUnmergedTree = true).assertIsDisplayed()
    }
    @Test fun rechazaLoginYRegistroVacios() {
        abrir(); compose.onNodeWithText("Iniciar sesión").performClick()
        compose.onNodeWithText("Usuario o contraseña incorrectos").assertIsDisplayed()
        compose.onNodeWithText("Crear cuenta").performClick()
        compose.onNode(hasText("Crear cuenta") and hasClickAction()).performClick()
        compose.onNodeWithText("Completa los campos marcados en rojo").assertIsDisplayed()
    }
    @Test fun compraGuardaPedidoYNavegaPorCuatroDestinos() {
        abrir(); login()
        compose.onAllNodesWithText("Agregar")[0].performClick()
        compose.onNodeWithContentDescription("Carrito").performClick()
        compose.onNodeWithText("Continuar pedido").performClick()
        compose.onNodeWithText("Confirmar pedido").performScrollTo().performClick()
        compose.onNodeWithText("Completa los campos marcados en rojo").assertIsDisplayed()
        compose.onNodeWithText("Nombre").performScrollTo().performTextInput("Jery")
        compose.onNodeWithText("Teléfono").performTextInput("987654321")
        compose.onNodeWithContentDescription("Elegir recojo").performScrollTo().performClick()
        compose.onNodeWithText("Confirmar pedido").performScrollTo().performClick()
        compose.onNodeWithText("Pedido realizado").assertIsDisplayed()
captura("mibodega-pedido-confirmado")
        compose.onNodeWithText("Volver al inicio").performClick()
        compose.onNodeWithText("Mis pedidos").performClick()
        compose.onNodeWithText("Pedido #1 · Confirmado").assertIsDisplayed()
        compose.onNodeWithText("Perfil", substring = false).performClick()
        compose.onNodeWithText("Modo oscuro").assertIsDisplayed()
        compose.onNodeWithText("Favoritos", substring = false).performClick()
        compose.onNodeWithText("Aún no tienes favoritos").assertIsDisplayed()
        compose.onNodeWithText("Inicio", substring = false).performClick()
        compose.onNodeWithContentDescription("Carrito").performClick()
        compose.onNodeWithText("Tu carrito está vacío").assertIsDisplayed()
    }
    @Test fun eliminarExigeConfirmacion() {
        abrir(); login(); compose.onAllNodesWithText("Agregar")[0].performClick()
        compose.onNodeWithContentDescription("Carrito").performClick()
        compose.onAllNodes(hasContentDescription("Eliminar", substring = true))[0].performClick()
        compose.onNodeWithText("Cancelar").performClick()
        compose.onNodeWithText("Tu carrito está vacío").assertDoesNotExist()
        compose.onAllNodes(hasContentDescription("Eliminar", substring = true))[0].performClick()
        compose.onNodeWithText("Eliminar", substring = false).performClick()
        compose.onNodeWithText("Tu carrito está vacío").assertIsDisplayed()
    }
    @Test fun favoritosCompartenDetalleYElTemaSeConservaAlNavegar() {
        abrir(); login()
        compose.onNodeWithContentDescription("Favorito Arroz Costeño").performClick()
        compose.onNodeWithText("Favoritos", substring = false).performClick()
        compose.onNodeWithText("Arroz Costeño").assertIsDisplayed()
        compose.onNodeWithText("Aceite Primor").assertDoesNotExist()
        compose.onNodeWithText("Ver detalle").performClick()
        compose.onNodeWithContentDescription("Favorito", substring = false).performClick()
        compose.onNodeWithContentDescription("Volver").performClick()
        compose.onNodeWithText("Aún no tienes favoritos").assertIsDisplayed()
        compose.onNodeWithText("Perfil", substring = false).performClick()
        compose.onNode(isToggleable()).assertIsOff().performClick().assertIsOn()
        captura("mibodega-perfil-oscuro")
        compose.onNodeWithText("Inicio", substring = false).performClick()
        compose.onNodeWithText("Perfil", substring = false).performClick()
        compose.onNode(isToggleable()).assertIsOn()
    }
    @Test fun sinScaffoldContinuaElLoginYLaNavegacion() {
        Practica.USAR_SCAFFOLD = false; abrir(); login()
        compose.onNodeWithText("Perfil", substring = false).performClick(); compose.onNodeWithText("Modo oscuro").assertIsDisplayed()
    }
    @Test fun sinNavegacionConservaScaffoldYContenido() {
        Practica.USAR_NAVEGACION = false; abrir()
        compose.onNodeWithText("Mi Bodega · práctica sin navegación").assertIsDisplayed()
        compose.onNodeWithText("Perfil", substring = false).performClick()
        compose.onNodeWithText("Buscar productos", useUnmergedTree = true).assertIsDisplayed()
    }
    @Test fun sinListasLazyConservaProductosYFiltros() {
        Practica.USAR_LAZY_COLUMN = false; Practica.USAR_LAZY_ROW = false; abrir(); login()
        compose.onNodeWithText("Bebidas", substring = false).performClick()
        compose.onNodeWithText("Buscar productos").performTextInput("Coca")
        compose.onNodeWithText("Coca-Cola Original").assertIsDisplayed()
    }

    private fun captura(nombre: String) {
        compose.waitForIdle()
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        val destino = File(contexto.getExternalFilesDir(null), "$nombre.png")
        val bitmap = compose.onRoot().captureToImage().asAndroidBitmap()
        destino.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        // UTP desinstala la app al terminar: conservar la captura fuera de sus datos.
        val instrumento = InstrumentationRegistry.getInstrumentation()
        instrumento.uiAutomation.executeShellCommand("cp ${destino.absolutePath} /sdcard/Download/$nombre.png").use { descriptor ->
            java.io.FileInputStream(descriptor.fileDescriptor).use { it.readBytes() }
        }

    }
}
