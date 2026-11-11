package com.tecsup.productos.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun ErrorView(mensaje: String, reintentar: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(mensaje, color = MaterialTheme.colorScheme.error)
        Button(onClick = reintentar) { Text("Reintentar") }
    }
}