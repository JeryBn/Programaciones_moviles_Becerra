package com.tecsup.cafecampus
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import android.graphics.Bitmap
import java.io.File
import org.junit.*
class FlujoUiTest {
 @get:Rule val rule=createAndroidComposeRule<MainActivity>()
 private fun esperar(texto:String) {rule.waitUntil(15000) {rule.onAllNodesWithText(texto).fetchSemanticsNodes().isNotEmpty()}}
 private fun captura(nombre:String) {rule.waitForIdle();Thread.sleep(400);val context=InstrumentationRegistry.getInstrumentation().targetContext;val dir=File(context.getExternalFilesDir(null),"evidencias");dir.mkdirs();InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot().let {b->File(dir,"$nombre.png").outputStream().use {b.compress(Bitmap.CompressFormat.PNG,100,it)}}}
 @Test fun compraAdminSeguimientoRotacionYPersistencia() {
  rule.waitUntil(15000) {rule.onAllNodesWithText("Ingresar").fetchSemanticsNodes().isNotEmpty() || rule.onAllNodesWithText("Menú del campus").fetchSemanticsNodes().isNotEmpty()}
  if(rule.onAllNodesWithText("Ingresar").fetchSemanticsNodes().isEmpty()) {
   rule.onNodeWithText("Perfil").performClick();esperar("Ajustes y perfil");rule.onNodeWithText("Cerrar sesión").performClick()
  }
  esperar("Ingresar")
  rule.onAllNodes(hasSetTextAction())[0].performTextInput("juan@campus.test")
  rule.onAllNodes(hasSetTextAction())[1].performTextInput("incorrecta")
  rule.onNodeWithText("Ingresar").performClick();esperar("Correo o contraseña incorrectos");captura("login-error")
  rule.onAllNodes(hasSetTextAction())[1].performTextClearance();rule.onAllNodes(hasSetTextAction())[1].performTextInput("1234")
  rule.onNodeWithText("Ingresar").performClick();esperar("Menú del campus");captura("cliente-menu")
  rule.onNodeWithText("Café americano").performClick();esperar("Nota para este producto");captura("cliente-detalle")
  rule.onNodeWithText("Agregar al pedido",substring=true).performClick()
  esperar("Menú del campus");rule.onNodeWithText("Chicha morada").performClick();rule.onNodeWithText("Agregar al pedido",substring=true).performClick()
  esperar("Menú del campus");rule.activityRule.scenario.recreate();esperar("Menú del campus")
  rule.onNodeWithText("Mi bolsa (2)",substring=true).assertExists().performClick();esperar("Confirmar pedido");captura("cliente-confirmar")
  rule.onNode(hasText("Confirmar pedido") and hasClickAction()).performClick();esperar("Seguimiento");captura("cliente-seguimiento")
  rule.onNodeWithText("Perfil").performClick();esperar("Ajustes y perfil");rule.onNodeWithText("Cambiar de perfil · modo práctica").performClick()
  esperar("Panel de cafetería");captura("admin-panel")
  rule.onNodeWithText("Preparar pedido",substring=true).performClick()
  rule.onNodeWithText("Pedidos",useUnmergedTree=false).performClick();esperar("Gestión de pedidos")
  rule.onNodeWithText("PREPARANDO").performClick();captura("admin-pedidos")
  rule.onNodeWithText("Menú").performClick();esperar("Gestión del menú");captura("admin-menu")
  rule.onNodeWithText("Ajustes").performClick();esperar("Ajustes y perfil");captura("admin-ajustes")
  rule.onNodeWithText("Cambiar de perfil · modo práctica").performClick();esperar("Menú del campus")
  rule.onNodeWithText("Pedidos").performClick();esperar("Mis pedidos");rule.onAllNodesWithText("PREPARANDO")[0].assertExists();captura("cliente-pedido-preparando")
  rule.activityRule.scenario.recreate();esperar("Mis pedidos");rule.onAllNodesWithText("PREPARANDO")[0].assertExists()
  rule.onNodeWithText("Perfil").performClick();esperar("Ajustes y perfil")
  rule.onNodeWithContentDescription("Activar modo oscuro").performClick();rule.waitForIdle()
  captura("cliente-modo-oscuro")
  rule.activityRule.scenario.recreate();esperar("Ajustes y perfil")
  rule.onNodeWithContentDescription("Activar modo oscuro").assertIsOn()
  rule.onNodeWithText("Cambiar de perfil · modo práctica").performClick();esperar("Panel de cafetería")
  rule.onNodeWithText("Ajustes").performClick();esperar("Ajustes y perfil")
  rule.onNodeWithContentDescription("Recibir pedidos de clientes").performClick();rule.waitForIdle();captura("admin-cafeteria-cerrada")
  rule.onNodeWithText("Cambiar de perfil · modo práctica").performClick();esperar("Menú del campus")
  rule.onNodeWithText("Café americano").performClick();esperar("Nota para este producto")
  rule.onNodeWithText("Agregar al pedido",substring=true).performClick();esperar("Menú del campus")
  rule.onNodeWithText("Mi bolsa (1)",substring=true).performClick();esperar("Confirmar pedido")
  rule.onNodeWithText("Cafetería cerrada").assertExists()
  rule.onNode(hasText("Confirmar pedido") and hasClickAction()).assertIsNotEnabled();captura("cliente-cafeteria-cerrada")

 }
}

