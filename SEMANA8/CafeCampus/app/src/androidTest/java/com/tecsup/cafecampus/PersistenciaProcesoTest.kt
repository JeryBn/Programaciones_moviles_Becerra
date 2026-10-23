package com.tecsup.cafecampus;
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.repository.CafeRepositoryRoom
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.flow.first
import org.junit.*
import org.junit.Assert.*
import android.graphics.Bitmap
import java.io.File
/** Ejecutar después del flujo UI y tras am force-stop del proceso de la app. */
class PersistenciaProcesoTest {
 @get:Rule val rule=createAndroidComposeRule<MainActivity>()
 @Test fun conservaSesionPreferenciasYPedidoTrasNuevoProceso() { runBlocking {
  val context=InstrumentationRegistry.getInstrumentation().targetContext
  val prefs=PreferenciasStore(context)
  assertEquals("CLIENTE",prefs.perfilActivo.first())
  assertTrue(prefs.modoOscuro.first())
  assertFalse(prefs.recibirPedidos.first())
  val app=context.applicationContext as CafeApp
  app.inicializacion.await()
  assertTrue(CafeRepositoryRoom(app.db).observarPedidosCliente(1).first().isNotEmpty())
  rule.waitUntil(15000){rule.onAllNodesWithText("Menú del campus").fetchSemanticsNodes().isNotEmpty()}
  val folder=File(context.getExternalFilesDir(null),"evidencias");folder.mkdirs()
  InstrumentationRegistry.getInstrumentation().uiAutomation.takeScreenshot().let { b->File(folder,"reinicio-proceso-verificado.png").outputStream().use {b.compress(Bitmap.CompressFormat.PNG,100,it)}}
 } }
}

