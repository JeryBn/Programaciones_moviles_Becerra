package com.tecsup.productos.ui.lista

import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.notification.Notificador
import com.tecsup.productos.ui.UiState
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ListaViewModelTest {
    @Test fun sincronizacionExitosaNotificaYPublicaRoom() = probar { repo, notificador ->
        repo.resultado = Result.success(1)
        repo.nuevos = listOf(producto)
        val vm = ListaViewModel(repo, notificador)
        runCurrent()
        assertEquals(UiState.Exito(Catalogo(listOf(producto))), vm.estado.value)
        assertEquals(listOf("Se guardaron 1 elementos"), notificador.avisos)
    }

    @Test fun falloConCacheMantieneLosDatos() = probar { repo, notificador ->
        repo.items.value = listOf(producto)
        repo.resultado = Result.failure(IOException())
        val vm = ListaViewModel(repo, notificador)
        runCurrent()
        val catalogo = (vm.estado.value as UiState.Exito).datos
        assertEquals(listOf(producto), catalogo.items)
        assertTrue(catalogo.aviso.orEmpty().contains("Mostrando datos guardados"))
        assertTrue(notificador.avisos.isEmpty())
    }

    @Test fun falloSinCachePermiteReintentarYMostrarVacio() = probar { repo, notificador ->
        repo.resultado = Result.failure(IOException())
        val vm = ListaViewModel(repo, notificador)
        runCurrent()
        assertTrue(vm.estado.value is UiState.Error)
        repo.resultado = Result.success(0)
        vm.refrescar()
        runCurrent()
        assertEquals(UiState.Exito(Catalogo(emptyList())), vm.estado.value)
    }

    private fun probar(bloque: TestScope.(Falso, Avisos) -> Unit) {
        val dispatcher = StandardTestDispatcher()
        Dispatchers.setMain(dispatcher)
        try {
            runTest(dispatcher) { bloque(Falso(), Avisos()) }
        } finally {
            Dispatchers.resetMain()
        }
    }

    private val producto = Item(1, "Teléfono", "Marca", 123.0, "Descripción", null, false)

    private class Avisos : Notificador {
        val avisos = mutableListOf<String>()
        override fun mostrar(titulo: String, texto: String) { avisos.add(texto) }
    }

    private class Falso : ItemRepository {
        override val items = MutableStateFlow<List<Item>>(emptyList())
        override val favoritos = items.map { lista -> lista.filter { it.esFavorito } }
        var resultado = Result.success(0)
        var nuevos = emptyList<Item>()
        override suspend fun refrescar(): Result<Int> = resultado.onSuccess { items.value = nuevos }
        override suspend fun obtenerDetalle(id: Int) = Result.success(items.value.first { it.id == id })
        override suspend fun alternarFavorito(id: Int) {
            items.value = items.value.map { if (it.id == id) it.copy(esFavorito = !it.esFavorito) else it }
        }
        override suspend fun cantidadGuardada() = items.value.size
        override suspend fun cantidadFavoritos() = items.value.count { it.esFavorito }
        override suspend fun limpiarCache() { items.value = emptyList() }
    }
}
