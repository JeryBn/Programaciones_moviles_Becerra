package com.tecsup.mibodega.ui.cliente.screens.confirmacion

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario

@Composable
fun ConfirmacionScreen(total: Double, onVolverInicio: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(28.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
        Icon(Icons.Default.CheckCircle, "Pedido confirmado", tint = Color(0xFF2E7D32), modifier = Modifier.size(96.dp))
        Spacer(Modifier.height(20.dp)); Text("Pedido realizado", style = MaterialTheme.typography.headlineMedium)
        Text("Tu pedido esta siendo preparado y sera entregado pronto.")
        Spacer(Modifier.height(12.dp)); Text("Pedido #1024"); Text("Total: S/ %.2f".format(total), style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(28.dp)); BotonPrimario(texto = "Volver al inicio", onClick = onVolverInicio)
    }
}
