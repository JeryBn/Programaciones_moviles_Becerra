package com.tecsup.productos.domain.model

data class Item(val id: Int, val titulo: String, val marca: String, val precio: Double,
    val descripcion: String, val imagenUrl: String?, val esFavorito: Boolean)