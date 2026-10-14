package com.tecsup.cafecampus.data.repository
import androidx.room.withTransaction
import com.tecsup.cafecampus.data.local.CafeDatabase
import com.tecsup.cafecampus.data.local.entity.*
import com.tecsup.cafecampus.model.*
class CafeRepositoryRoom(private val db:CafeDatabase):CafeRepository {
 override suspend fun login(correo:String,clave:String)=db.usuarioDao().login(correo.trim(),clave)
 override suspend fun usuarioPorRol(rol:Rol)=db.usuarioDao().porRol(rol.name)
 override fun observarProductos()=db.productoDao().observarTodos()
 override suspend fun guardarProducto(p:Producto) { require(p.nombre.isNotBlank()&&p.precio.isFinite()&&p.precio>0) { "Nombre y precio válido obligatorios" }; if(p.id==0) db.productoDao().insertar(p) else db.productoDao().actualizar(p) }
 override suspend fun eliminarProducto(p:Producto)=db.productoDao().eliminar(p)
 override suspend fun cambiarDisponibilidad(id:Int,disponible:Boolean)=db.productoDao().cambiarDisponibilidad(id,disponible)
 override suspend fun crearPedido(clienteId:Int,items:List<ItemCarrito>,horaRecojo:String,pago:String,nota:String):Long = db.withTransaction {
  require(items.isNotEmpty()&&items.all { it.cantidad>0 }) { "El pedido está vacío o tiene cantidades inválidas" }
  require(horaRecojo.isNotBlank()&&pago in listOf("CAJA","YAPE")) { "Selecciona hora y pago" }
  val actuales=items.map { item ->
   val p=db.productoDao().obtener(item.producto.id)
   require(p!=null&&p.disponible) { "Un producto está agotado o fue eliminado" }
   item.copy(producto=p!!)
  }
  val detalles=actuales.map { DetallePedido(productoId=it.producto.id,nombreProducto=it.producto.nombre,cantidad=it.cantidad,precioUnitario=it.producto.precio) }
  val notas=listOf(nota.trim())+actuales.filter { it.nota.isNotBlank() }.map { "${it.producto.nombre}: ${it.nota}" }
  db.pedidoDao().crearPedido(Pedido(clienteId=clienteId,horaRecojo=horaRecojo,pago=pago,nota=notas.filter { it.isNotBlank() }.joinToString(" · "),total=actuales.sumOf { it.producto.precio*it.cantidad }),detalles)
 }
 override fun observarPedidosCliente(clienteId:Int)=db.pedidoDao().observarPorCliente(clienteId)
 override fun observarPedidosPorEstado(estado:EstadoPedido)=db.pedidoDao().observarPorEstado(estado.name)
 override fun observarPedido(id:Int)=db.pedidoDao().observarUno(id)
 override suspend fun cambiarEstado(pedidoId:Int,estado:EstadoPedido)=db.pedidoDao().cambiarEstado(pedidoId,estado.name)
 override fun observarVentasHoy(inicioDia:Long)=db.pedidoDao().ventasDesde(inicioDia)
 override fun observarCantidadPorEstado(estado:EstadoPedido)=db.pedidoDao().cantidadPorEstado(estado.name)
}

