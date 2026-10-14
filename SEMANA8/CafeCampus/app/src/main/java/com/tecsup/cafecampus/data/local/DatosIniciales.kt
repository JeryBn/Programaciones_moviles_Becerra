package com.tecsup.cafecampus.data.local
import androidx.room.withTransaction
import com.tecsup.cafecampus.data.local.entity.*
object DatosIniciales {
 suspend fun sembrar(db:CafeDatabase)=db.withTransaction {
  if(db.usuarioDao().contar()==0) {
   listOf(
    Producto(nombre="Café americano",descripcion="Café recién preparado",categoria="Bebidas",precio=4.0,icono="cafe"),
    Producto(nombre="Sándwich de pollo",descripcion="Pollo y vegetales frescos",categoria="Comidas",precio=9.0,icono="pan"),
    Producto(nombre="Menú del día",descripcion="Almuerzo de la cafetería",categoria="Comidas",precio=12.0,icono="plato"),
    Producto(nombre="Jugo de naranja",descripcion="Naranja natural",categoria="Bebidas",precio=5.0,icono="jugo"),
    Producto(nombre="Empanada",descripcion="Horneada del día",categoria="Snacks",precio=4.5,icono="dulce"),
    Producto(nombre="Chicha morada",descripcion="Refresco tradicional",categoria="Bebidas",precio=3.5,icono="botella")
   ).forEach { db.productoDao().insertar(it) }
   db.usuarioDao().insertar(Usuario(nombre="Juan Campus",correo="juan@campus.test",clave="1234",rol="CLIENTE"))
   db.usuarioDao().insertar(Usuario(nombre="Admin Café",correo="admin@cafe.test",clave="admin",rol="ADMIN"))
  }
 }
}

