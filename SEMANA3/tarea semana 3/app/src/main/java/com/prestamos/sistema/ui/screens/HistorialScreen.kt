package com.prestamos.sistema.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.prestamos.sistema.domain.model.Prestamo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistorialScreen(
    prestamos: List<Prestamo>,
    onBack: () -> Unit,
    onSelect: (Long) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Historial de préstamos") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) } }
            )
        }
    ) { padding ->
        if (prestamos.isEmpty()) {
            Box(modifier = Modifier.padding(padding).fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("No hay préstamos aún. Registra uno en la pantalla principal.")
            }
        } else {
            LazyColumn(modifier = Modifier.padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(prestamos) { p ->
                    Card(modifier = Modifier.fillMaxWidth(), onClick = { onSelect(p.id) }, elevation = CardDefaults.cardElevation(2.dp)) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(p.cliente.nombre, fontWeight = FontWeight.Bold)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("S/ ${p.montoInicial} → S/ ${p.montoTotal}")
                                Text("${p.numeroCuotas} cuotas (${(p.porcentajeInteres*100).toInt()}%)")
                            }
                            Text("Cuota: S/ ${p.pagoMensual} | Pagadas: ${p.cuotasPagadas}/${p.numeroCuotas}")
                        }
                    }
                }
            }
        }
    }
}
