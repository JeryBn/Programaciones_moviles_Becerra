package com.tecsup.productos.ui

import retrofit2.HttpException
import java.io.IOException

sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

fun Throwable.mensajeCatalogo() = when (this) {
    is IOException -> "No se pudo conectar a internet."
    is HttpException -> "El servidor devolvió el código ${code()}."
    else -> "No se pudieron cargar los datos."
}