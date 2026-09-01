package com.prestamos.sistema.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prestamos.sistema.domain.model.Prestamo
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    prestamo: Prestamo,
    onBack: () -> Unit,
    onVerCronograma: () -> Unit,
    onEliminar: () -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Resumen del préstamo") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                },
                actions = {
                    IconButton(onClick = onEliminar) { Icon(Icons.Default.Delete, null) }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(4.dp)) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Cliente: ${prestamo.cliente.nombre}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Divider()
                    InfoRow("Monto inicial", "S/ ${prestamo.montoInicial}")
                    InfoRow("Número de cuotas", "${prestamo.numeroCuotas}")
                    InfoRow("Interés", "${(prestamo.porcentajeInteres * 100).toInt()}%")
                    InfoRow("Interés generado", "S/ ${prestamo.interesGenerado}")
                    InfoRow("Monto total a pagar", "S/ ${prestamo.montoTotal}")
                    InfoRow("Pago mensual", "S/ ${prestamo.pagoMensual}")
                    InfoRow("Fecha inicio", prestamo.fechaInicio.format(formatter))
                    Divider()
                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Text("Pagado: S/ ${prestamo.totalPagado}")
                        Text("Pendiente: S/ ${prestamo.saldoPendiente}")
                    }
                    Text("Cuotas pagadas: ${prestamo.cuotasPagadas} / ${prestamo.numeroCuotas}")
                }
            }

            Button(onClick = onVerCronograma, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.CalendarMonth, null)
                Spacer(Modifier.width(8.dp))
                Text("Ver cronograma de pagos")
            }

            // Mini preview tabla pizarra
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text("Cronograma (primeras 3 cuotas)", style = MaterialTheme.typography.labelLarge)
                    Spacer(Modifier.height(8.dp))
                    prestamo.cronograma.take(3).forEach { c ->
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Cuota ${c.numero}")
                            Text(c.fechaVencimiento.format(formatter))
                            Text("S/ ${c.montoCuota}")
                            Text("Saldo ${c.saldoRestante}")
                        }
                    }
                    if (prestamo.numeroCuotas > 3) Text("... y ${prestamo.numeroCuotas - 3} más")
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, fontWeight = FontWeight.SemiBold)
    }
}
