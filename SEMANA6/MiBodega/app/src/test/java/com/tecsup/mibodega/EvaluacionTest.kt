package com.tecsup.mibodega
import com.tecsup.mibodega.ui.cliente.modelo.*
import org.junit.Assert.*
import org.junit.Test
class EvaluacionTest {
    @Test fun loginRechazaVaciosYCredencialesIncorrectas() {
        assertFalse(credencialesValidas("", "")); assertFalse(credencialesValidas("jery", "mal")); assertTrue(credencialesValidas("jery", "1234"))
    }
    @Test fun formulariosExigenLosCamposObligatorios() {
        assertFalse(formularioValido(" ", "987", "Av. A", "Parque"))
        assertFalse(formularioValido("Jery", "", "Av. A", "Parque"))
        assertFalse(formularioValido("Jery", "987", "Av. A", ""))
        assertTrue(formularioValido("Jery", "987", "Av. A", "Parque"))
    }
    @Test fun recojoEliminaDeliveryYVacioNoCobra() {
        val carrito = listOf(ItemCarrito(listaProductosFake.first(), 2))
        assertEquals(subtotal(carrito), totalEntrega(carrito, true), .001)
        assertEquals(subtotal(carrito) + 4.0, totalEntrega(carrito, false), .001)
        assertEquals(0.0, totalEntrega(emptyList(), false), .001)
    }
    @Test fun pedidoConservaSusItemsAlVaciarCarrito() {
        var carrito = listOf(ItemCarrito(listaProductosFake.first(), 2))
        val pedido = Pedido(1, carrito.toList(), "Jery", "Tienda", true, totalEntrega(carrito, true))
        carrito = emptyList(); assertTrue(carrito.isEmpty()); assertEquals(2, pedido.items.first().cantidad)
    }
}
