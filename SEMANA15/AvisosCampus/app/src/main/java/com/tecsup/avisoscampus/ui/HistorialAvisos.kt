package com.tecsup.avisoscampus.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.tecsup.avisoscampus.model.Aviso

@Composable
fun HistorialAvisos(avisos: List<Aviso>) {
    Tarjeta("Historial") {
        if (avisos.isEmpty()) Text("Sin avisos todavía")
        avisos.take(20).forEachIndexed { indice, aviso ->
            if (indice > 0) HorizontalDivider()
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(aviso.origen.texto, color = MaterialTheme.colorScheme.primary)
                Text(aviso.hora, style = MaterialTheme.typography.labelMedium)
            }
            Text(aviso.titulo, style = MaterialTheme.typography.titleSmall)
            Text(aviso.texto)
        }
    }
}
