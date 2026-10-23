package com.tecsup.cafecampus
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.model.Rol
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.runner.RunWith
/** Preparación explícita de estado para la prueba entre dos procesos. */
@RunWith(AndroidJUnit4::class)
class PrepararPersistenciaTest {
 @Test fun prepararPreferenciasParaReinicio() { runBlocking {
  val prefs=PreferenciasStore(InstrumentationRegistry.getInstrumentation().targetContext)
  prefs.guardarPerfil(Rol.CLIENTE)
  prefs.guardarModoOscuro(true)
  prefs.guardarRecibirPedidos(false)
 } }
}
