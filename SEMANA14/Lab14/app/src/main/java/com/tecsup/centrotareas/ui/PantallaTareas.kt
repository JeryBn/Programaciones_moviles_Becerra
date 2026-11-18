package com.tecsup.centrotareas.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    val simularError by vm.simularError.collectAsStateWithLifecycle()
    var entrada by rememberSaveable { mutableStateOf("1000") }
    val limite = entrada.toIntOrNull()?.takeIf { it >= 2 }
    val calculando = primos == PrimosUi.Calculando
    val cargando = carga == CargaUi.Cargando

    Scaffold(
        topBar = {
            Surface(color = MaterialTheme.colorScheme.surface) {
                Row(
                    Modifier.fillMaxWidth().statusBarsPadding().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("CentroTareas", Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black)
                    OutlinedButton(onClick = { toques++ }) { Text("Tócame: $toques") }
                }
            }
        }
    ) { insets ->
        Box(Modifier.fillMaxSize().padding(insets), contentAlignment = Alignment.TopCenter) {
            Column(
                Modifier.widthIn(max = 620.dp).fillMaxWidth().verticalScroll(rememberScrollState()).imePadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Panel("Tarea larga") {
                    Text(mensaje, style = MaterialTheme.typography.bodyMedium)
                    LinearProgressIndicator(progress = { progreso }, modifier = Modifier.fillMaxWidth())
                    Text("${(progreso * 100).toInt()} %", fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.primary)
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = vm::tareaLarga, enabled = !corriendo, modifier = Modifier.weight(1f)) {
                            Text("Iniciar")
                        }
                        OutlinedButton(onClick = vm::cancelar, enabled = corriendo, modifier = Modifier.weight(1f)) {
                            Text("Cancelar")
                        }
                    }
                }
                Panel("Números primos") {
                    OutlinedTextField(
                        value = entrada,
                        onValueChange = { entrada = it },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Límite del cálculo") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = { limite?.let(vm::contar) }, enabled = !calculando && limite != null,
                            modifier = Modifier.weight(1f)) { Text("Contar") }
                        OutlinedButton(onClick = vm::cancelarPrimos, enabled = calculando, modifier = Modifier.weight(1f)) {
                            Text("Cancelar")
                        }
                    }
                    when (val resultado = primos) {
                        PrimosUi.Inicial -> Unit
                        PrimosUi.Calculando -> {
                            LinearProgressIndicator(progress = { progresoPrimos }, modifier = Modifier.fillMaxWidth())
                            Text("${(progresoPrimos * 100).toInt()} % de primos")
                        }
                        PrimosUi.Cancelado -> Text("Cálculo cancelado")
                        is PrimosUi.Listo -> Resultado("${resultado.cantidad} primos", "${resultado.milisegundos} ms")
                    }
                }
                Panel("Dos cargas") {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text("Simular error")
                        Switch(checked = simularError, onCheckedChange = vm::cambiarSimularError)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        OutlinedButton(onClick = vm::cargarUnaPorUna, enabled = !cargando,
                            modifier = Modifier.weight(1f)) { Text("Una por una") }
                        Button(onClick = vm::cargarALaVez, enabled = !cargando,
                            modifier = Modifier.weight(1f)) { Text("A la vez") }
                    }
                    when (val resultado = carga) {
                        CargaUi.Inicial -> Unit
                        CargaUi.Cargando -> CircularProgressIndicator(Modifier.size(30.dp))
                        is CargaUi.Exito -> Resultado(
                            "${resultado.modo} · ${resultado.milisegundos} ms",
                            "${resultado.perfil} · ${resultado.pedidos} pedidos"
                        )
                        is CargaUi.Error -> Surface(
                            color = MaterialTheme.colorScheme.errorContainer,
                            contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(Modifier.fillMaxWidth().padding(12.dp)) {
                                Text(resultado.mensaje)
                                OutlinedButton(onClick = vm::cargarALaVez) { Text("Reintentar") }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Panel(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.surface
    )) {
        Column(Modifier.fillMaxWidth().padding(15.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            contenido()
        }
    }
}

@Composable
private fun Resultado(titulo: String, detalle: String) {
    Surface(color = Color(0xFF184533), contentColor = Color(0xFFD0FFE6), shape = MaterialTheme.shapes.medium) {
        Column(Modifier.fillMaxWidth().padding(12.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            Text(detalle, style = MaterialTheme.typography.bodySmall)
        }
    }
}
