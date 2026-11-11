package com.tecsup.productos.ui.lista

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.*

@Composable
fun ListaScreen(vm: ListaViewModel, abrir: (Int) -> Unit) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    when (val actual = estado) {
        UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorView(actual.mensaje, vm::refrescar)
        is UiState.Exito -> Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            actual.datos.aviso?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            if (actual.datos.sincronizando) LinearProgressIndicator(Modifier.fillMaxWidth())
            OutlinedButton(onClick = vm::refrescar, enabled = !actual.datos.sincronizando) { Text("Actualizar catálogo") }
            Items(actual.datos.items, "El catálogo está vacío.", abrir, vm::alternarFavorito)
        }
    }
}