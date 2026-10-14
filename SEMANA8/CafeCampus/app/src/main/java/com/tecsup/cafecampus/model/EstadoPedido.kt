package com.tecsup.cafecampus.model
enum class EstadoPedido {
 RECIBIDO, PREPARANDO, LISTO, ENTREGADO, CANCELADO;
 fun siguiente():EstadoPedido? = when(this) { RECIBIDO->PREPARANDO; PREPARANDO->LISTO; LISTO->ENTREGADO; else->null }
 val activo:Boolean get() = this in listOf(RECIBIDO,PREPARANDO,LISTO)
}

