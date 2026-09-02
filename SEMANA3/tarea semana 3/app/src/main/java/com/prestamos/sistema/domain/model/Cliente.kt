package com.prestamos.sistema.domain.model

/**
 * Modelo de cliente - contiene información básica de la persona.
 * Requisito guía §11: Modelo de cliente
 */
data class Cliente(
    val id: Long = 0,
    val nombre: String
) {
    init {
        require(nombre.isNotBlank()) { "El nombre no puede estar vacío" }
    }
}
