package com.tecsup.productos.data.repository

import androidx.room.withTransaction
import com.tecsup.productos.data.local.AppDatabase
import com.tecsup.productos.data.mapper.*
import com.tecsup.productos.data.remote.ApiService
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException

class ItemRepositoryImpl(private val api: ApiService, private val db: AppDatabase) : ItemRepository {
    private val dao = db.items()
    private val operaciones = Mutex()
    override val items = dao.todos().map { lista -> lista.map { it.aModelo() } }
    override val favoritos = dao.favoritos().map { lista -> lista.map { it.aModelo() } }

    override suspend fun refrescar() = operaciones.withLock {
        resultado {
            val productos = api.listar("smartphones").products
            db.withTransaction {
                val favoritos = dao.idsFavoritos().toSet()
                dao.borrarNoFavoritos()
                dao.guardarTodos(productos.map { it.aEntity(it.id in favoritos) })
            }
            productos.size
        }
    }

    override suspend fun obtenerDetalle(id: Int) = operaciones.withLock {
        resultado {
            val guardado = dao.buscar(id)
            val detalle = try {
                api.detalle(id).aEntity(guardado?.esFavorito == true)
            } catch (error: IOException) {
                guardado ?: throw error
            }
            dao.guardarTodos(listOf(detalle))
            detalle.aModelo()
        }
    }

    override suspend fun alternarFavorito(id: Int) = operaciones.withLock { dao.alternarFavorito(id) }
    override suspend fun cantidadGuardada() = dao.contar()
    override suspend fun cantidadFavoritos() = dao.contarFavoritos()
    override suspend fun limpiarCache() = operaciones.withLock { dao.borrarTodo() }
}

internal suspend fun <T> resultado(accion: suspend () -> T): Result<T> = try {
    Result.success(accion())
} catch (cancelacion: CancellationException) {
    throw cancelacion
} catch (error: Exception) {
    Result.failure(error)
}