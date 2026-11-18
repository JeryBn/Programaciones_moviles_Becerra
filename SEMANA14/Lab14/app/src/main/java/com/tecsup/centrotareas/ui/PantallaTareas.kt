package com.tecsup.centrotareas.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.centrotareas.model.CargaUi
import com.tecsup.centrotareas.model.PrimosUi
import com.tecsup.centrotareas.viewmodel.TareasViewModel

@Composable
fun PantallaTareas(vm: TareasViewModel) {
    var toques by rememberSaveable { mutableIntStateOf(0) }
    val mensaje by vm.mensaje.collectAsStateWithLifecycle()
    val progreso by vm.progreso.collectAsStateWithLifecycle()
    val corriendo by vm.corriendo.collectAsStateWithLifecycle()
    val primos by vm.primos.collectAsStateWithLifecycle()
    val progresoPrimos by vm.progresoPrimos.collectAsStateWithLifecycle()
    val carga by vm.carga.collectAsStateWithLifecycle()
    val comparaciones by vm.comparaciones.collectAsStateWithLifecycle()
    val simularError by vm.simularError.collectAsStateWithLifecycle()
    var entrada by rememberSaveable { mutableStateOf("1000") }
    val limite = entrada.toIntOrNull()?.takeIf { it >= 2 }
    val calculando = primos == PrimosUi.Calculando
    val cargando = carga == CargaUi.Cargando
    val colores = MaterialTheme.colorScheme

    val panelPrimos: @Composable () -> Unit = {
        Panel("Números primos") {
            OutlinedTextField(
                value = entrada,
                onValueChange = { entrada = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                singleLine = true,
                label = { Text("Límite del cálculo") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { limite?.let(vm::contar) }, enabled = !calculando && limite != null,
                    modifier = Modifier.weight(1f)) { Text("Contar") }
                OutlinedButton(onClick = vm::cancelarPrimos, enabled = calculando,
                    modifier = Modifier.weight(1f)) { Text("Cancelar") }
            }
            when (val resultado = primos) {
                PrimosUi.Inicial -> Unit
                PrimosUi.Calculando -> {
                    LinearProgressIndicator(progress = { progresoPrimos }, modifier = Modifier.fillMaxWidth())
                    Text("${(progresoPrimos * 100).toInt()} % de primos", fontFamily = FontFamily.Monospace)
                }
                PrimosUi.Cancelado -> Text("Cálculo cancelado", color = colores.onSurfaceVariant)
                is PrimosUi.Listo -> Resultado("${resultado.cantidad} primos", "${resultado.milisegundos} ms")
            }
        }
    }
    val panelCargas: @Composable () -> Unit = {
        Panel("Dos cargas") {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = vm::cargarUnaPorUna, enabled = !cargando,
                    modifier = Modifier.weight(1f)) { Text("Una por una") }
                Button(onClick = vm::cargarALaVez, enabled = !cargando,
                    modifier = Modifier.weight(1f)) { Text("A la vez") }
            }
            Surface(color = colores.surfaceVariant, shape = RoundedCornerShape(14.dp)) {
                Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically) {
                    Text("Simular error", color = colores.onSurfaceVariant)
                    Switch(checked = simularError, onCheckedChange = vm::cambiarSimularError)
                }
            }
            when (val resultado = carga) {
                CargaUi.Inicial -> Unit
                CargaUi.Cargando -> LinearProgressIndicator(Modifier.fillMaxWidth())
                is CargaUi.Exito -> Resultado(
                    "${resultado.modo} · ${resultado.milisegundos} ms",
                    "${resultado.perfil} · ${resultado.pedidos} pedidos"
                )
                is CargaUi.Error -> Surface(
                    color = colores.errorContainer,
                    contentColor = colores.onErrorContainer,
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Column(Modifier.fillMaxWidth().padding(12.dp)) {
                        Text(resultado.mensaje)
                        TextButton(onClick = vm::cargarALaVez) { Text("Reintentar") }
                    }
                }
            }
            if (comparaciones.isNotEmpty()) ComparacionTiempos(comparaciones)
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            Row(
                Modifier.fillMaxWidth().statusBarsPadding()
                    .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("CentroTareas", Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Black)
                FilledTonalButton(onClick = { toques++ }) {
                    Text("Tócame: $toques", fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { insets ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(insets), contentAlignment = Alignment.TopCenter) {
            val columnas = maxWidth >= 650.dp
            Column(
                Modifier.widthIn(max = 1060.dp).fillMaxWidth().imePadding()
                    .verticalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PanelProgreso(mensaje, progreso, corriendo, vm::tareaLarga, vm::cancelar)
                if (columnas) {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Box(Modifier.weight(1f)) { panelPrimos() }
                        Box(Modifier.weight(1f)) { panelCargas() }
                    }
                } else {
                    panelPrimos()
                    panelCargas()
                }
            }
        }
    }
}

@Composable
private fun PanelProgreso(mensaje: String, progreso: Float, corriendo: Boolean, iniciar: () -> Unit, cancelar: () -> Unit) {
    val forma = RoundedCornerShape(24.dp)
    Surface(shape = forma, color = Color(0xFF34268F), contentColor = Color.White) {
        Column(
            Modifier.fillMaxWidth()
                .background(Brush.linearGradient(listOf(Color(0xFF2E206F), Color(0xFF5342C9))), forma)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Tarea larga", Modifier.semantics { heading() },
                        style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Surface(color = Color.White.copy(alpha = .17f), contentColor = Color.White, shape = CircleShape) {
                        Text(mensaje, Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelMedium)
                    }
                }
                Box(Modifier.size(92.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { progreso },
                        modifier = Modifier.fillMaxSize(),
                        color = Color(0xFFC8F9E4),
                        trackColor = Color.White.copy(alpha = .23f),
                        strokeWidth = 7.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Text("${(progreso * 100).toInt()} %", style = MaterialTheme.typography.titleMedium,
                        fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = iniciar,
                    enabled = !corriendo,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF35257F),
                        disabledContainerColor = Color.White.copy(alpha = .15f),
                        disabledContentColor = Color.White.copy(alpha = .65f)
                    )
                ) { Text("Iniciar") }
                OutlinedButton(
                    onClick = cancelar,
                    enabled = corriendo,
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = if (corriendo) .88f else .40f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White,
                        disabledContentColor = Color.White.copy(alpha = .65f)
                    )
                ) { Text("Cancelar") }
            }
        }
    }
}

@Composable
private fun ComparacionTiempos(tiempos: Map<String, List<Long>>) {
    val colores = MaterialTheme.colorScheme
    val limite = tiempos.values.mapNotNull { it.lastOrNull() }.maxOrNull()?.coerceAtLeast(1) ?: 1
    HorizontalDivider(color = colores.outlineVariant)
    Text("Últimas cargas · ms", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (modo in listOf("Una por una", "A la vez")) {
            val mediciones = tiempos[modo].orEmpty()
            val acento = if (modo == "A la vez") colores.secondary else colores.primary
            Surface(Modifier.weight(1f), color = colores.surfaceVariant, shape = RoundedCornerShape(12.dp)) {
                Column(Modifier.padding(9.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
                    Text(modo, color = acento, style = MaterialTheme.typography.labelLarge)
                    LinearProgressIndicator(
                        progress = { (mediciones.lastOrNull() ?: 0).toFloat() / limite },
                        modifier = Modifier.fillMaxWidth(),
                        color = acento,
                        trackColor = colores.outlineVariant
                    )
                    for (medicion in mediciones.asReversed()) {
                        Text(medicion.toString(), fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodyMedium)
                    }
                    if (mediciones.isEmpty()) Text("—")
                }
            }
        }
    }
}

@Composable
private fun Panel(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Column(Modifier.fillMaxWidth().padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, Modifier.semantics { heading() }, style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold)
            contenido()
        }
    }
}

@Composable
private fun Resultado(titulo: String, detalle: String) {
    val colores = MaterialTheme.colorScheme
    Surface(color = colores.secondaryContainer, contentColor = colores.onSecondaryContainer,
        shape = RoundedCornerShape(14.dp)) {
        Column(Modifier.fillMaxWidth().padding(11.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Text(detalle, style = MaterialTheme.typography.bodySmall)
        }
    }
}
