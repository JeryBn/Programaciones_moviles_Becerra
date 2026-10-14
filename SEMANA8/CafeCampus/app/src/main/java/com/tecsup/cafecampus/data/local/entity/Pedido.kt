package com.tecsup.cafecampus.data.local.entity
import androidx.room.*
@Entity(tableName="pedidos",foreignKeys=[ForeignKey(entity=Usuario::class,parentColumns=["id"],childColumns=["clienteId"])],indices=[Index("clienteId")])
data class Pedido(@PrimaryKey(autoGenerate=true) val id:Int=0,val clienteId:Int,val estado:String="RECIBIDO",val horaRecojo:String,val pago:String,val nota:String,val total:Double,val fecha:Long=System.currentTimeMillis())

