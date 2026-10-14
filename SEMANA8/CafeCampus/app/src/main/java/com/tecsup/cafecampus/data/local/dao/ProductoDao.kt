package com.tecsup.cafecampus.data.local.dao
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.tecsup.cafecampus.data.local.entity.Producto
@Dao interface ProductoDao {
 @Query("SELECT * FROM productos ORDER BY nombre") fun observarTodos():Flow<List<Producto>>
 @Query("SELECT * FROM productos WHERE id=:id") suspend fun obtener(id:Int):Producto?
 @Query("SELECT COUNT(*) FROM productos") suspend fun contar():Int
 @Insert suspend fun insertar(p:Producto):Long
 @Update suspend fun actualizar(p:Producto)
 @Delete suspend fun eliminar(p:Producto)
 @Query("UPDATE productos SET disponible=:d WHERE id=:id") suspend fun cambiarDisponibilidad(id:Int,d:Boolean)
}

