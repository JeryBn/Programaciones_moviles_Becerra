package com.becerra.lab03registroproducto

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import android.os.ParcelFileDescriptor
import org.junit.Rule
import org.junit.Test

class RegistroProductoTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    private fun campo(etiqueta: String) = compose.onNode(hasSetTextAction() and hasText(etiqueta))
    private fun llenar(nombre: String = "Cuaderno", precio: String = "12.50", cantidad: String = "2") {
        campo("Nombre del producto").performTextReplacement(nombre)
        campo("Precio (S/)").performTextReplacement(precio)
        campo("Cantidad").performTextReplacement(cantidad)
    }
    private fun agregar() = compose.onNodeWithText("AGREGAR PRODUCTO").performClick()
    private fun sinResumen() = compose.onNode(hasText("Importe: S/", substring = true)).assertDoesNotExist()
    private fun captura(nombre: String) {
        androidx.test.espresso.Espresso.closeSoftKeyboard()
        compose.waitForIdle()
        // Espera solo para la captura: el teclado y los efectos del sistema se animan fuera de Compose.
        android.os.SystemClock.sleep(1000)
        val instrumentacion = InstrumentationRegistry.getInstrumentation()
        // /data/local/tmp conserva la evidencia cuando Gradle desinstala la app de prueba.
        val salida = instrumentacion.uiAutomation.executeShellCommand(
            "screencap -p /data/local/tmp/lab03-$nombre.png"
        )
        ParcelFileDescriptor.AutoCloseInputStream(salida).use {
            it.readBytes()
        }
    }

    @Test fun vaciosMuestranErrorSinResumen() {
        agregar()
        compose.onNodeWithText("Completa nombre, precio y cantidad.").assertIsDisplayed()
        sinResumen()
        captura("campos-vacios")
    }

    @Test fun espaciosNoSonUnNombreValido() {
        llenar(nombre = "   ")
        agregar()
        compose.onNodeWithText("Completa nombre, precio y cantidad.").assertIsDisplayed()
        sinResumen()
    }

    @Test fun productoValidoCalculaImporte() {
        llenar()
        agregar()
        compose.onNodeWithText("Importe: S/ " + String.format("%.2f", 25.0)).assertIsDisplayed()
        compose.onNodeWithText("Cantidad: 2").assertIsDisplayed()
        captura("producto-valido")
    }

    @Test fun limpiarBorraFormularioYResumen() {
        llenar()
        agregar()
        compose.onNodeWithText("Limpiar").performClick()
        campo("Nombre del producto").assertTextEquals("Nombre del producto", "")
        campo("Precio (S/)").assertTextEquals("Precio (S/)", "")
        campo("Cantidad").assertTextEquals("Cantidad", "")
        sinResumen()
        captura("formulario-limpio")
    }

    @Test fun limpiarOcultaError() {
        agregar()
        compose.onNodeWithText("Limpiar").performClick()
        compose.onNodeWithText("Completa nombre, precio y cantidad.").assertDoesNotExist()
        sinResumen()
    }

    @Test fun precioConLetrasMuestraErrorSinConvertirseEnCero() {
        llenar(precio = "abc")
        agregar()
        compose.onNodeWithText("Ingresa un precio válido mayor que cero (ejemplo: 12.50).").assertIsDisplayed()
        sinResumen()
        captura("precio-invalido")
    }

    @Test fun cadaCampoEsObligatorio() {
        for (etiqueta in listOf("Nombre del producto", "Precio (S/)", "Cantidad")) {
            llenar()
            campo(etiqueta).performTextReplacement("")
            agregar()
            compose.onNodeWithText("Completa nombre, precio y cantidad.").assertIsDisplayed()
            sinResumen()
        }
    }

    @Test fun editarOcultaResumenHastaVolverAAgregar() {
        llenar()
        agregar()
        campo("Precio (S/)").performTextReplacement("20")
        sinResumen()
        agregar()
        compose.onNodeWithText("Importe: S/ " + String.format("%.2f", 40.0)).assertIsDisplayed()
    }

    @Test fun corregirEntradaOcultaErrorYPermiteRegistrar() {
        llenar(precio = "abc")
        agregar()
        campo("Precio (S/)").performTextReplacement("12.50")
        compose.onNodeWithText("Ingresa un precio válido mayor que cero (ejemplo: 12.50).").assertDoesNotExist()
        agregar()
        compose.onNodeWithText("Importe: S/ " + String.format("%.2f", 25.0)).assertIsDisplayed()
    }

    @Test fun rechazaPreciosNoPositivosYNoFinitos() {
        for (precio in listOf("0", "-1", "NaN", "Infinity", "1e309")) {
            llenar(precio = precio)
            agregar()
            compose.onNodeWithText("Ingresa un precio válido mayor que cero (ejemplo: 12.50).").assertIsDisplayed()
            sinResumen()
        }
    }

    @Test fun cantidadDebeSerEnteraPositivaDentroDeRango() {
        for (cantidad in listOf("abc", "1.5", "0", "-2", "2147483648")) {
            llenar(cantidad = cantidad)
            agregar()
            compose.onNodeWithText("Ingresa una cantidad entera mayor que cero.").assertIsDisplayed()
            sinResumen()
        }
    }

    @Test fun importeNoPuedeDesbordarse() {
        llenar(precio = "1e308", cantidad = "2")
        agregar()
        compose.onNodeWithText("El importe es demasiado grande. Reduce el precio o la cantidad.").assertIsDisplayed()
        sinResumen()
    }

    @Test fun aceptaEspaciosAlrededorDeDatosValidos() {
        llenar(nombre = " Cuaderno ", precio = " 12.50 ", cantidad = " 2 ")
        agregar()
        compose.onNodeWithText("Cuaderno", useUnmergedTree = true).assertIsDisplayed()
        compose.onNodeWithText("Importe: S/ " + String.format("%.2f", 25.0)).assertIsDisplayed()
    }
}
