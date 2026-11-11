package com.tecsup.productos.data.mapper

import com.tecsup.productos.data.local.ItemEntity
import com.tecsup.productos.data.remote.dto.ItemDto
import com.tecsup.productos.domain.model.Item

fun ItemDto.aEntity(esFavorito: Boolean = false) =
    ItemEntity(id, title, brand.orEmpty(), price, description, thumbnail, esFavorito)

fun ItemEntity.aModelo() = Item(id, titulo, marca, precio, descripcion, imagenUrl, esFavorito)