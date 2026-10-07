package com.tecsup.mibodega

import com.tecsup.mibodega.ui.cliente.modelo.*
import org.junit.Assert.assertEquals
import org.junit.Test

class ReglasCompraTest {
    @Test fun `total cambia automaticamente con cantidad`() {
        val producto = Producto(1, "Arroz", "", 4.5, "Abarrotes", com.tecsup.mibodega.R.drawable.arroz_costeno)
        assertEquals(13.0, total(listOf(ItemCarrito(producto, 2))), 0.001)
    }

    @Test fun `busqueda y categoria funcionan juntas`() {
        val resultado = filtrarProductos(listaProductosFake, "Bebidas", "coca")
        assertEquals(listOf("Coca-Cola Original"), resultado.map { it.nombre })
    }

    @Test fun `busqueda ignora mayusculas espacios y tildes`() {
        val productos = listOf(Producto(9, "Café premium", "", 12.0, "Bebidas", com.tecsup.mibodega.R.drawable.coca_cola))
        assertEquals(1, filtrarProductos(productos, "Bebidas", "  CAFE ").size)
    }
}
