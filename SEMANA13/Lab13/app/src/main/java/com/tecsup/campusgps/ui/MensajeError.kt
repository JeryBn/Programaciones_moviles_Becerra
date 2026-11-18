package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.TipoError
import com.tecsup.campusgps.model.UbicacionUi

@Composable
fun MensajeError(error: UbicacionUi.Error, abrirAjustes: () -> Unit, reintentar: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        contentColor = MaterialTheme.colorScheme.onErrorContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Column(Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 6.dp)) {
            Text(error.mensaje, style = MaterialTheme.typography.bodySmall)
            when (error.tipo) {
                TipoError.SIN_PERMISO -> TextButton(onClick = abrirAjustes) { Text("Abrir ajustes") }
                TipoError.GPS_APAGADO, TipoError.OTRO -> TextButton(onClick = reintentar) { Text("Reintentar") }
                TipoError.SIN_PLAY_SERVICES -> Unit
            }
        }
    }
}
