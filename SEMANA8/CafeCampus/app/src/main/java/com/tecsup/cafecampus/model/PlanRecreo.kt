package com.tecsup.cafecampus.model
/**
 * Estimación educativa: dos estaciones, 4 minutos por pedido recibido,
 * 2 minutos por pedido en preparación y 4 minutos para la nueva compra.
 * No reserva turnos ni garantiza tiempos de servicio.
 */
data class PlanRecreo(val minutosEstimados:Int,val minutosDisponibles:Int,val cabe:Boolean,val mensaje:String)
fun calcularRecreo(recibidos:Int,preparando:Int,minutosDisponibles:Int,abierto:Boolean):PlanRecreo {
 require(recibidos>=0&&preparando>=0&&minutosDisponibles>0)
 val minutos=((recibidos.toLong()*4+preparando.toLong()*2+1)/2+4).coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
 val cabe=abierto&&minutos<=minutosDisponibles
 val mensaje=when { !abierto->"La cafetería no recibe pedidos";cabe->"Según la carga actual, tu pedido podría entrar en el recreo";else->"La espera estimada supera tu recreo; considera otra hora de recojo" }
 return PlanRecreo(minutos,minutosDisponibles,cabe,mensaje)
}

