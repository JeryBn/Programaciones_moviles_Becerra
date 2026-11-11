package com.tecsup.productos.ui.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.productos.data.repository.ItemRepository
import com.tecsup.productos.domain.model.Item
import com.tecsup.productos.ui.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FavoritosViewModel(private val repo: ItemRepository) : ViewModel() {
    val estado: StateFlow<UiState<List<Item>>> = repo.favoritos
        .map<List<Item>, UiState<List<Item>>> { UiState.Exito(it) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, UiState.Cargando)
    fun alternarFavorito(id: Int) { viewModelScope.launch { repo.alternarFavorito(id) } }
}