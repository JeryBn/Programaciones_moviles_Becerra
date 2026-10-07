package com.tecsup.mibodega.ui.cliente.modelo

data class Pedido(val id: Int, val items: List<ItemCarrito>, val nombre: String,
    val direccion: String, val recojo: Boolean, val total: Double)

fun credencialesValidas(usuario: String, clave: String) = usuario == "jery" && clave == "1234"
fun formularioValido(nombre: String, telefono: String, direccion: String) =
    nombre.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank()
fun totalEntrega(carrito: List<ItemCarrito>, recojo: Boolean) =
    subtotal(carrito) + if (recojo || carrito.isEmpty()) 0.0 else COSTO_DELIVERY
