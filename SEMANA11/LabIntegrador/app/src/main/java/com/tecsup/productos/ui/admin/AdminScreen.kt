package com.tecsup.productos.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.Cargando

@Composable
fun AdminScreen(vm: AdminViewModel) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    var confirmar by remember { mutableStateOf(false) }
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        when (val actual = estado) {
            UiState.Cargando -> Cargando()
            is UiState.Error -> Text(actual.mensaje)
            is UiState.Exito -> {
                Text("Total guardado: ${actual.datos.guardados}")
                Text("Total de favoritos: ${actual.datos.favoritos}")
                actual.datos.mensaje?.let { Text(it) }
            }
        }
        if (estado != UiState.Cargando) {
            Button(onClick = vm::sincronizar) { Text("Sincronizar") }
            OutlinedButton(onClick = { confirmar = true }) { Text("Limpiar caché") }
        }
    }
    if (confirmar) AlertDialog(onDismissRequest = { confirmar = false },
        title = { Text("Limpiar caché") },
        text = { Text("Se eliminarán los productos guardados y sus marcas de favorito.") },
        confirmButton = { TextButton(onClick = { confirmar = false; vm.limpiarCache() }) { Text("Limpiar") } },
        dismissButton = { TextButton(onClick = { confirmar = false }) { Text("Cancelar") } })
}