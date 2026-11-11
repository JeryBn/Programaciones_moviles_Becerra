package com.tecsup.productos.ui.favoritos

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.*

@Composable
fun FavoritosScreen(vm: FavoritosViewModel, abrir: (Int) -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    when (val actual = estado) {
        UiState.Cargando -> Cargando()
        is UiState.Error -> androidx.compose.material3.Text(actual.mensaje)
        is UiState.Exito -> Items(actual.datos, "Todavía no hay favoritos.", abrir, vm::alternarFavorito)
    }
}