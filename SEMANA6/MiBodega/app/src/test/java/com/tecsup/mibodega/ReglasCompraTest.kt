package com.tecsup.mibodega

import com.tecsup.mibodega.ui.cliente.modelo.*
import org.junit.Assert.assertEquals
import org.junit.Test

class ReglasCompraTest {
    @Test fun `total cambia automaticamente con cantidad`() {
        val producto = Producto(1, "Arroz", "", 4.5, "Abarrotes")
        assertEquals(13.0, total(listOf(ItemCarrito(producto, 2))), 0.001)
    }

    @Test fun `busqueda y categoria funcionan juntas`() {
        val resultado = filtrarProductos(listaProductosFake, "Bebidas", "coca")
        assertEquals(listOf("Coca-Cola Original"), resultado.map { it.nombre })
    }
}
