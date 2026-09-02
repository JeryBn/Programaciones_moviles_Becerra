package com.prestamos.sistema.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prestamos.sistema.domain.calculator.CalculadoraPrestamo
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(
    onCalcular: (nombre: String, montoStr: String, cuotas: Int, fecha: LocalDate) -> Unit,
    onVerHistorial: () -> Unit,
    error: String? = null,
    onLimpiarError: () -> Unit = {}
) {
    var nombre by remember { mutableStateOf("") }
    var monto by remember { mutableStateOf("") }
    var cuotasSeleccionadas by remember { mutableStateOf(6) }
    var fechaInicio by remember { mutableStateOf(LocalDate.now()) }
    var showDateInfo by remember { mutableStateOf(false) }

    val porcentaje = remember(cuotasSeleccionadas) {
        CalculadoraPrestamo.obtenerInteresPorcentajeFormateado(cuotasSeleccionadas)
    }

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Sistema de Préstamos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            IconButton(onClick = onVerHistorial) {
                Icon(Icons.Default.History, contentDescription = "Historial")
            }
        }
        Text(
            "Gestión de préstamos y cronograma de pagos",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Datos del préstamo", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre del cliente") },
                    leadingIcon = { Icon(Icons.Default.AccountCircle, null) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = monto,
                    onValueChange = { monto = it },
                    label = { Text("Monto inicial (S/)") },
                    placeholder = { Text("Ej: 1200") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Text("Número de cuotas", style = MaterialTheme.typography.labelLarge)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                    listOf(6, 12, 24).forEach { opcion ->
                        FilterChip(
                            selected = cuotasSeleccionadas == opcion,
                            onClick = { cuotasSeleccionadas = opcion },
                            label = { Text("$opcion cuotas") }
                        )
                    }
                }

                // Info de interés - regla central
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Regla de interés (pizarra)", style = MaterialTheme.typography.labelMedium)
                        Text("$porcentaje de interés para $cuotasSeleccionadas cuotas", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
                        Text("6→20%  |  12→40%  |  24→60%", style = MaterialTheme.typography.bodySmall)
                    }
                }

                // Preview de cálculo en vivo
                val montoDouble = monto.toDoubleOrNull()
                if (montoDouble != null && montoDouble > 0) {
                    val interes = CalculadoraPrestamo.calcularInteres(montoDouble, cuotasSeleccionadas)
                    val total = CalculadoraPrestamo.calcularMontoTotal(montoDouble, interes)
                    val cuota = CalculadoraPrestamo.calcularPagoMensual(total, cuotasSeleccionadas)
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Vista previa", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
                            Text("Interés: S/ $interes")
                            Text("Total a pagar: S/ $total")
                            Text("Cuota mensual: S/ $cuota")
                        }
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = { showDateInfo = !showDateInfo }, label = { Text("Fecha inicio: ${fechaInicio.format(formatter)}") })
                    Text("Vencimiento: +1 mes c/cuota", style = MaterialTheme.typography.bodySmall)
                }

                if (error != null) {
                    Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                        Text(error, modifier = Modifier.padding(12.dp), color = MaterialTheme.colorScheme.onErrorContainer)
                    }
                }

                Button(
                    onClick = {
                        onCalcular(nombre, monto, cuotasSeleccionadas, fechaInicio)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = nombre.isNotBlank() && monto.isNotBlank()
                ) {
                    Text("Calcular préstamo")
                }
            }
        }

        // Tabla regla visual igual a guía
        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Tabla de referencia", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Cuotas", fontWeight = FontWeight.Bold)
                    Text("Interés", fontWeight = FontWeight.Bold)
                }
                Divider()
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("6"); Text("20%") }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("12"); Text("40%") }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text("24"); Text("60%") }
            }
        }
    }
}
