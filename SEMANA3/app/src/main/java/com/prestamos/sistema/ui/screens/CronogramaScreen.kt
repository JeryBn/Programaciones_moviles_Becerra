package com.prestamos.sistema.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prestamos.sistema.domain.model.EstadoCuota
import com.prestamos.sistema.domain.model.Prestamo
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CronogramaScreen(
    prestamo: Prestamo,
    onBack: () -> Unit,
    onToggleCuota: (Int) -> Unit
) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cronograma - ${prestamo.cliente.nombre}") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Row(
                    modifier = Modifier.padding(12.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Total: S/ ${prestamo.montoTotal}", fontWeight = FontWeight.Bold)
                        Text("Cuota: S/ ${prestamo.pagoMensual}")
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Pagado: S/ ${prestamo.totalPagado}")
                        Text("Saldo: S/ ${prestamo.saldoPendiente}")
                    }
                }
            }
            Spacer(Modifier.height(12.dp))

            // Header tabla
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("N°", modifier = Modifier.weight(0.5f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                Text("Fecha", modifier = Modifier.weight(1.2f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                Text("Cuota", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                Text("Saldo", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
                Text("Estado", modifier = Modifier.weight(0.8f), fontWeight = FontWeight.Bold, style = MaterialTheme.typography.labelLarge)
            }
            Divider(modifier = Modifier.padding(vertical = 4.dp))

            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(prestamo.cronograma) { cuota ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (cuota.estado == EstadoCuota.PAGADA) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
                        ),
                        onClick = { onToggleCuota(cuota.numero) }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("${cuota.numero}", modifier = Modifier.weight(0.5f))
                            Text(cuota.fechaVencimiento.format(formatter), modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.bodySmall)
                            Text("S/ ${cuota.montoCuota}", modifier = Modifier.weight(1f))
                            Text("S/ ${cuota.saldoRestante}", modifier = Modifier.weight(1f))
                            Row(modifier = Modifier.weight(0.8f), verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    if (cuota.estado == EstadoCuota.PAGADA) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                                    contentDescription = null,
                                    tint = if (cuota.estado == EstadoCuota.PAGADA) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    if (cuota.estado == EstadoCuota.PAGADA) "Pagada" else "Pendiente",
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(8.dp))
            Text("Toca una cuota para marcar como pagada/pendiente. El saldo disminuye con cada pago (guía §4).", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
