package com.tecsup.productos

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.rule.GrantPermissionRule
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.test.platform.app.InstrumentationRegistry
import android.app.NotificationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import org.junit.Rule
import org.junit.Test

class LaboratorioTest {
    @get:Rule(order = 0) val permiso = GrantPermissionRule.grant(android.Manifest.permission.POST_NOTIFICATIONS)
    @get:Rule(order = 1) val compose = createAndroidComposeRule<MainActivity>()

    private fun ver(texto: String, parcial: Boolean = false) {
        try {
            compose.waitUntil(40_000) {
                compose.onAllNodesWithText(texto, substring = parcial).fetchSemanticsNodes().isNotEmpty()
            }
        } catch (error: ComposeTimeoutException) {
            throw AssertionError("No apareció: $texto. " + compose.onRoot().printToString(), error)
        }
    }

    private fun pulsar(texto: String) = compose.onNodeWithText(texto).performClick()

    private fun captura(nombre: String) {
        compose.waitForIdle()
        val instrumento = InstrumentationRegistry.getInstrumentation()
        val imagen = requireNotNull(instrumento.uiAutomation.takeScreenshot())
        val archivo = java.io.File(instrumento.targetContext.getExternalFilesDir(null), "$nombre.png")
        archivo.outputStream().use { imagen.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, it) }
    }

    private fun ejecutarEnDispositivo(comando: String) {
        val instrumento = InstrumentationRegistry.getInstrumentation()
        val descriptor = instrumento.uiAutomation.executeShellCommand(comando)
        android.os.ParcelFileDescriptor.AutoCloseInputStream(descriptor).use { it.readBytes() }
    }

    private fun modoAvion(activar: Boolean) {
        ejecutarEnDispositivo("cmd connectivity airplane-mode " + if (activar) "enable" else "disable")
        val red = InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(ConnectivityManager::class.java)
        compose.waitUntil(30_000) {
            if (activar) red.activeNetwork == null
            else red.getNetworkCapabilities(red.activeNetwork)?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED) == true
        }
    }

    private fun entrar(rol: String, clave: String = "CatalogoDemo!12") {
        compose.onNodeWithText("Correo electrónico").performTextReplacement("$rol@laboratorio.example")
        compose.onNodeWithText("Contraseña").performTextReplacement(clave)
        pulsar("Ingresar")
    }

    @Test fun rolesCatalogoDetalleFavoritosYCache() {
        ver("Ingresar")
        captura("01-login")
        pulsar("Ingresar")
        ver("Completa el correo y la contraseña.")
        entrar("usuario", "incorrecta")
        ver("Correo o contraseña incorrectos.")
        entrar("usuario")
        ver("Actualizar catálogo")
        ver("Añadir a favoritos")
        captura("02-catalogo")
        compose.onNodeWithText("Admin").assertDoesNotExist()
        val notificaciones = InstrumentationRegistry.getInstrumentation().targetContext
            .getSystemService(NotificationManager::class.java)
        compose.waitUntil(10_000) {
            notificaciones.activeNotifications.any {
                it.notification.extras.getCharSequence("android.text")?.startsWith("Se guardaron ") == true
            }
        }
        ejecutarEnDispositivo("cmd statusbar expand-notifications")
        try {
            InstrumentationRegistry.getInstrumentation().uiAutomation.waitForIdle(700, 5_000)
            captura("07-notificacion")
        } finally {
            ejecutarEnDispositivo("cmd statusbar collapse")
        }
        compose.onAllNodesWithText("Añadir a favoritos").onFirst().performClick()
        pulsar("Favoritos")
        ver("Quitar de favoritos")
        captura("03-favoritos")
        compose.activityRule.scenario.recreate()
        ver("Cerrar sesión")
        pulsar("Catálogo")
        ver("Actualizar catálogo")
        pulsar("Actualizar catálogo")
        ver("Quitar de favoritos")
        pulsar("Favoritos")
        ver("Quitar de favoritos")
        pulsar("Cerrar sesión")
        ver("Ingresar")
        entrar("admin")
        ver("Admin")
        pulsar("Admin")
        ver("Total guardado: ", true)
        captura("04-panel-admin")
        pulsar("Limpiar caché")
        pulsar("Limpiar")
        ver("Total guardado: 0")
        ver("Total de favoritos: 0")
        try {
            modoAvion(true)
            pulsar("Catálogo")
            pulsar("Actualizar catálogo")
            ver("Reintentar")
        } finally {
            modoAvion(false)
        }
        pulsar("Admin")
        pulsar("Sincronizar")
        compose.waitUntil(40_000) {
            compose.onAllNodesWithText("Total guardado: ", substring = true).fetchSemanticsNodes()
                .any { nodo -> nodo.config[SemanticsProperties.Text].any { it.text.substringAfter(": ").toIntOrNull()?.let { n -> n > 0 } == true } }
        }
        pulsar("Catálogo")
        ver("Añadir a favoritos")
        compose.onNode(hasScrollAction()).performScrollToNode(hasText("iPhone 13 Pro"))
        pulsar("iPhone 13 Pro")
        ver("Detalle")
        ver("iPhone 13 Pro")
        captura("05-detalle")
        pulsar("Volver")
        try {
            modoAvion(true)
            pulsar("Actualizar catálogo")
            ver("Mostrando datos guardados.", true)
            ver("Añadir a favoritos")
            captura("06-sin-conexion")
        } finally {
            modoAvion(false)
        }
        pulsar("Cerrar sesión")
        compose.activityRule.scenario.recreate()
        ver("Ingresar")
    }
}
