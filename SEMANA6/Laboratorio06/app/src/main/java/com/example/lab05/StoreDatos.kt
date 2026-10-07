package com.example.lab05
data class ProductoStore(val id: Int, val nombre: String, val precio: Double, val categoria: String)
val productosStore = listOf(
    ProductoStore(1, "Audífonos", 89.0, "Tecnología"),
    ProductoStore(2, "Smartwatch", 199.0, "Tecnología"),
    ProductoStore(3, "Funda celular", 25.0, "Accesorios"),
    ProductoStore(4, "Mochila Tecsup", 79.9, "Accesorios"))
internal fun toggleFavorito(actual: Set<Int>, id: Int) = if (id in actual) actual - id else actual + id
