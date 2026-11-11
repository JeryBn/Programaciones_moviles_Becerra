package com.tecsup.productos.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.ui.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetalleViewModel(private val id: Int, private val repo: ItemRepository) : ViewModel() {
    private val mutable = MutableStateFlow<UiState<Item>>(UiState.Cargando)
    val estado = mutable.asStateFlow()
    init { cargar() }

    fun cargar() {
        mutable.value = UiState.Cargando
        viewModelScope.launch {
            mutable.value = repo.obtenerDetalle(id).fold(
                { UiState.Exito(it) }, { UiState.Error(it.mensajeCatalogo()) })
        }
    }
}