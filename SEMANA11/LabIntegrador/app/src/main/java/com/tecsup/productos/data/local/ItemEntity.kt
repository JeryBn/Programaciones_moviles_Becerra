package com.tecsup.productos.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(@PrimaryKey val id: Int, val titulo: String, val marca: String,
    val precio: Double, val descripcion: String, val imagenUrl: String?,
    val esFavorito: Boolean = false)