package com.tecsup.mibodega

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.tecsup.mibodega.ui.cliente.Practica
import com.tecsup.mibodega.ui.cliente.modelo.listaProductosFake
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.theme.BodegaTheme
import org.junit.*
import java.io.File

class ImagenesProductoTest {
    @get:Rule val compose = createComposeRule()
    @After fun restaurar() { Practica.USAR_LAZY_COLUMN = true }

    @Test fun fotosDeLosCincoProductosEnLazyColumn() = verificarCatalogo(true)
    @Test fun fotosDeLosCincoProductosEnColumn() = verificarCatalogo(false)

    private fun verificarCatalogo(lazy: Boolean) {
        Practica.USAR_LAZY_COLUMN = lazy
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        Assert.assertEquals(5, listaProductosFake.map { it.imagenRes }.distinct().size)
        listaProductosFake.forEach { producto ->
            val bitmap = BitmapFactory.decodeResource(contexto.resources, producto.imagenRes)
            Assert.assertNotNull("Foto de ${producto.nombre}", bitmap)
            Assert.assertTrue(bitmap.width >= 400 && bitmap.height >= 400)
            bitmap.recycle()
        }
        compose.setContent { BodegaTheme {
            InicioScreen(favoritos = emptySet(), onFavorito = {}, onProductoClick = {}, onAgregarProducto = {})
        } }
        captura(if (lazy) "mibodega-catalogo-fotos" else "mibodega-column-fotos")
        listaProductosFake.forEach { producto ->
            compose.onNodeWithText("Buscar productos").performTextClearance()
            compose.onNodeWithText("Buscar productos").performTextInput(producto.nombre)
            compose.onNodeWithContentDescription("Imagen de ${producto.nombre}").assertIsDisplayed()
            compose.onNodeWithText(producto.nombre, substring = false).assertIsDisplayed()
        }
    }

    @Test fun detalleMuestraFotoYConservaCantidadAlAgregar() {
        val producto = listaProductosFake.first { it.id == 5 }
        var cantidadAgregada = 0
        compose.setContent { BodegaTheme {
            DetalleProductoScreen(producto, onVolver = {}, onAgregarAlCarrito = { elegido, cantidad ->
                Assert.assertEquals(producto.id, elegido.id)
                cantidadAgregada = cantidad
            })
        } }
        compose.onNodeWithContentDescription("Imagen de ${producto.nombre}").assertIsDisplayed()
        captura("mibodega-detalle-foto")
        compose.onNodeWithText("Agregar al carrito").performClick()
        compose.runOnIdle { Assert.assertEquals(1, cantidadAgregada) }
    }

    private fun captura(nombre: String) {
        val instrumento = InstrumentationRegistry.getInstrumentation()
        val destino = File(instrumento.targetContext.getExternalFilesDir(null), "$nombre.png")
        compose.onRoot().captureToImage().asAndroidBitmap().let { bitmap ->
            destino.outputStream().use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        }
        instrumento.uiAutomation.executeShellCommand("cp ${destino.absolutePath} /sdcard/Download/$nombre.png").use { descriptor ->
            java.io.FileInputStream(descriptor.fileDescriptor).use { it.readBytes() }
        }
    }
}
