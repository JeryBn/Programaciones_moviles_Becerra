package com.tecsup.productos.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Query("SELECT * FROM items ORDER BY titulo, id")
    fun todos(): Flow<List<ItemEntity>>
    @Query("SELECT * FROM items WHERE esFavorito = 1 ORDER BY titulo, id")
    fun favoritos(): Flow<List<ItemEntity>>
    @Query("SELECT * FROM items WHERE id = :id")
    suspend fun buscar(id: Int): ItemEntity?
    @Query("SELECT id FROM items WHERE esFavorito = 1")
    suspend fun idsFavoritos(): List<Int>
    @Query("SELECT COUNT(*) FROM items")
    suspend fun contar(): Int
    @Query("SELECT COUNT(*) FROM items WHERE esFavorito = 1")
    suspend fun contarFavoritos(): Int
    @Upsert
    suspend fun guardarTodos(items: List<ItemEntity>)
    @Query("UPDATE items SET esFavorito = NOT esFavorito WHERE id = :id")
    suspend fun alternarFavorito(id: Int)
    @Query("DELETE FROM items")
    suspend fun borrarTodo()
    @Query("DELETE FROM items WHERE esFavorito = 0")
    suspend fun borrarNoFavoritos()
}