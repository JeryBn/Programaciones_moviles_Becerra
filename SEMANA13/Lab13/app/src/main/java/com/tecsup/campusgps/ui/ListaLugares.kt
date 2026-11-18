package com.tecsup.campusgps.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.util.formatearDistancia

@Composable
fun ListaLugares(
    lugares: List<LugarConDistancia>,
    seleccionado: Int?,
    cercano: Int?,
    dentroDelRadio: Set<Int>,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colores = MaterialTheme.colorScheme
    LazyColumn(
        modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(bottom = 4.dp)
    ) {
        items(lugares, key = { it.lugar.id }) { (lugar, metros) ->
            val activo = lugar.id == seleccionado
            val preferido = lugar.id == cercano
            val acento = when (lugar.categoria) {
                "Estudio" -> Color(0xFFABC2FB)
                "Comida" -> Color(0xFFFFBB92)
                "Eventos" -> Color(0xFFDBC1FD)
                else -> Color(0xFF9BE4DA)
            }
            Surface(
                onClick = { onClick(lugar.id) },
                color = if (activo) colores.primaryContainer else colores.surfaceVariant.copy(alpha = .62f),
                border = if (activo) BorderStroke(1.dp, colores.primary) else null,
                shape = RoundedCornerShape(15.dp)
            ) {
                Row(
                    Modifier.fillMaxWidth().padding(horizontal = 9.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconoLugar(lugar.categoria, acento)
                    Column(Modifier.weight(1f)) {
                        Text(lugar.nombre, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        val categoria = if (lugar.id in dentroDelRadio) "${lugar.categoria} · Dentro del radio"
                            else lugar.categoria
                        Text(categoria, style = MaterialTheme.typography.bodySmall, color = colores.onSurfaceVariant)
                    }
                    Surface(
                        color = if (preferido) colores.primary else colores.surface,
                        contentColor = if (preferido) colores.onPrimary else colores.onSurfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            metros?.let(::formatearDistancia) ?: "Sin calcular",
                            Modifier.padding(horizontal = 7.dp, vertical = 5.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontFamily = if (metros != null) FontFamily.Monospace else FontFamily.Default,
                            fontWeight = if (preferido) FontWeight.Bold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun IconoLugar(categoria: String, acento: Color) {
    Box(
        Modifier.size(36.dp).background(acento.copy(alpha = .14f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(Modifier.size(19.dp)) {
            val ancho = size.width
            val grosor = 1.5.dp.toPx()
            fun trazo(x1: Float, y1: Float, x2: Float, y2: Float) {
                drawLine(acento, Offset(ancho * x1, ancho * y1), Offset(ancho * x2, ancho * y2),
                    grosor, StrokeCap.Round)
            }
            when (categoria) {
                "Estudio" -> {
                    drawRoundRect(acento, Offset(ancho * .10f, ancho * .20f), Size(ancho * .80f, ancho * .59f),
                        CornerRadius(ancho * .12f), style = Stroke(grosor))
                    trazo(.50f, .22f, .50f, .77f)
                }
                "Comida" -> {
                    trazo(.25f, .14f, .25f, .46f)
                    trazo(.47f, .14f, .47f, .46f)
                    trazo(.25f, .46f, .47f, .46f)
                    trazo(.36f, .15f, .36f, .86f)
                    trazo(.73f, .16f, .73f, .86f)
                    trazo(.61f, .49f, .73f, .16f)
                    trazo(.61f, .49f, .73f, .49f)
                }
                "Eventos" -> {
                    drawRoundRect(acento, Offset(ancho * .12f, ancho * .28f), Size(ancho * .76f, ancho * .48f),
                        CornerRadius(ancho * .11f), style = Stroke(grosor))
                    trazo(.64f, .32f, .64f, .43f)
                    trazo(.64f, .59f, .64f, .70f)
                }
                else -> {
                    trazo(.34f, .17f, .34f, .85f)
                    drawRoundRect(acento, Offset(ancho * .34f, ancho * .17f), Size(ancho * .40f, ancho * .36f),
                        CornerRadius(ancho * .13f), style = Stroke(grosor))
                }
            }
        }
    }
}
