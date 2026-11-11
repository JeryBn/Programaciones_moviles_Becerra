package com.tecsup.productos.data.remote

import com.tecsup.productos.data.remote.dto.*
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("products/category/{categoria}")
    suspend fun listar(@Path("categoria") categoria: String): CatalogoDto

    @GET("products/{id}")
    suspend fun detalle(@Path("id") id: Int): ItemDto
}