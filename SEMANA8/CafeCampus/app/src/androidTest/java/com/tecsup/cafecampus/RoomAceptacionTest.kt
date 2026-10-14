package com.tecsup.cafecampus
import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import com.tecsup.cafecampus.data.local.*
import com.tecsup.cafecampus.data.repository.*
import com.tecsup.cafecampus.model.*
@RunWith(AndroidJUnit4::class)
class RoomAceptacionTest {
 private lateinit var db:CafeDatabase
 private lateinit var repo:CafeRepositoryRoom
 @Before fun setup()=runBlocking { db=Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext<Context>(),CafeDatabase::class.java).build(); DatosIniciales.sembrar(db); repo=CafeRepositoryRoom(db) }
 @After fun close() {db.close()}
 @Test fun credencialesRolesYSiembra()=runBlocking {DatosIniciales.sembrar(db);assertEquals(6,db.productoDao().contar());assertEquals(2,db.usuarioDao().contar());assertNull(repo.login("juan@campus.test","error"));assertEquals("CLIENTE",repo.login("juan@campus.test","1234")!!.rol);assertEquals("ADMIN",repo.login("admin@cafe.test","admin")!!.rol)}
 @Test fun transaccionTotalYSeguimientoCompartido()=runBlocking {
  val u=repo.login("juan@campus.test","1234")!!;val productos=repo.observarProductos().first()
  val items=listOf(ItemCarrito(productos[0],2),ItemCarrito(productos[1],1));val esperado=items.sumOf {it.producto.precio*it.cantidad}
  val id=repo.crearPedido(u.id,items,"12:30","CAJA","Sin azúcar").toInt()
  val p=repo.observarPedido(id).first()!!;assertEquals(2,p.detalles.size);assertEquals(esperado,p.pedido.total,0.001);assertEquals("RECIBIDO",p.pedido.estado)
  repo.cambiarEstado(id,EstadoPedido.PREPARANDO);assertEquals("PREPARANDO",repo.observarPedidosCliente(u.id).first().first().pedido.estado)
  repo.cambiarEstado(id,EstadoPedido.LISTO);repo.cambiarEstado(id,EstadoPedido.ENTREGADO);assertEquals("ENTREGADO",repo.observarPedido(id).first()!!.pedido.estado)
 }
 @Test fun fotoHistoricaCancelacionVentasYEliminacion()=runBlocking {
  val u=repo.login("juan@campus.test","1234")!!;val producto=repo.observarProductos().first().first()
  val id=repo.crearPedido(u.id,listOf(ItemCarrito(producto,2)),"Ahora","YAPE","").toInt()
  repo.guardarProducto(producto.copy(precio=99.0));assertEquals(producto.precio,repo.observarPedido(id).first()!!.detalles.first().precioUnitario,0.001)
  assertEquals(producto.precio*2,repo.observarVentasHoy(0).first(),0.001)
  repo.eliminarProducto(producto);assertNotNull(repo.observarPedido(id).first())
  repo.cambiarEstado(id,EstadoPedido.CANCELADO);assertEquals(0.0,repo.observarVentasHoy(0).first(),0.001);assertEquals("CANCELADO",repo.observarPedidosCliente(u.id).first().first().pedido.estado)
 }
 @Test fun agotadoNoCreaPedidoNiLineas()=runBlocking {
  val u=repo.login("juan@campus.test","1234")!!;val p=repo.observarProductos().first().first();repo.cambiarDisponibilidad(p.id,false)
  try {repo.crearPedido(u.id,listOf(ItemCarrito(p,1)),"Ahora","CAJA","");fail("Debía rechazar agotado")}catch(e:IllegalArgumentException) {assertTrue(repo.observarPedidosCliente(u.id).first().isEmpty())}
 }
}

