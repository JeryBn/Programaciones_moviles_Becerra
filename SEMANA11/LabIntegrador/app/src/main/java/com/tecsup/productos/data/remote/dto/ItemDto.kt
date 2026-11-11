package com.tecsup.productos.data.remote.dto

data class ItemDto(val id: Int, val title: String, val brand: String?,
    val price: Double, val description: String, val thumbnail: String?)
data class CatalogoDto(val products: List<ItemDto>)