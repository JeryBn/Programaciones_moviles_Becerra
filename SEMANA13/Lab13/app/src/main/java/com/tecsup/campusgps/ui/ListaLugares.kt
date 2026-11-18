package com.tecsup.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    cercano: Int?,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    LazyColumn(modifier, verticalArrangement = Arrangement.spacedBy(6.dp)) {
        items(lugares, key = { it.lugar.id }) { (lugar, metros) ->
            val activo = lugar.id == seleccionado
            OutlinedCard(
                onClick = { onClick(lugar.id) },
                colors = CardDefaults.outlinedCardColors(
                    containerColor = if (activo) colores.primaryContainer else colores.surface
                ),
                border = BorderStroke(1.dp, if (activo) colores.primary else colores.surfaceVariant)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(Modifier.size(8.dp).background(
                        if (lugar.id == cercano) Color(0xFF177443) else Color(0xFF2684CD), CircleShape
                    ))
                    Column(Modifier.weight(1f)) {
                        Text(lugar.nombre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(lugar.categoria, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                    }
                    Text(metros?.let(::formatearDistancia) ?: "Sin calcular", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
