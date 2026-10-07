package com.tecsup.mibodega.ui.cliente.modelo

import java.text.Normalizer

const val COSTO_DELIVERY = 4.0

fun subtotal(carrito: List<ItemCarrito>): Double = carrito.sumOf { it.producto.precio * it.cantidad }

fun total(carrito: List<ItemCarrito>): Double = subtotal(carrito) + if (carrito.isEmpty()) 0.0 else COSTO_DELIVERY

fun filtrarProductos(productos: List<Producto>, categoria: String, busqueda: String): List<Producto> =
    productos.filter {
        (categoria == "Todos" || it.categoria == categoria) &&
            normalizar(it.nombre).contains(normalizar(busqueda.trim()))
    }

private fun normalizar(texto: String): String = Normalizer.normalize(texto.lowercase(), Normalizer.Form.NFD)
    .replace("\\p{Mn}+".toRegex(), "")
