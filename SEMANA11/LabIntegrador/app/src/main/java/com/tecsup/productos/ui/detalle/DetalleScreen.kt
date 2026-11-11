package com.tecsup.productos.ui.detalle

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.components.*

@Composable
fun DetalleScreen(vm: DetalleViewModel) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    when (val actual = estado) {
        UiState.Cargando -> Cargando()
        is UiState.Error -> ErrorView(actual.mensaje, vm::cargar)
        is UiState.Exito -> Column(Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)) {
            val producto = actual.datos
            Text(producto.titulo, style = MaterialTheme.typography.headlineMedium)
            if (producto.marca.isNotBlank()) Text(producto.marca)
            Text(precioUSD(producto.precio), style = MaterialTheme.typography.titleLarge)
            Text(producto.descripcion)
        }
    }
}