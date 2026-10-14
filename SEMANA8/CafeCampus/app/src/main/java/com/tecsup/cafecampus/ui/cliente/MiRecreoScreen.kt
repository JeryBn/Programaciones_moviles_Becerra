package com.tecsup.cafecampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.MiRecreoViewModel
@Composable fun MiRecreoScreen(vm:MiRecreoViewModel) {
 val plan by vm.plan.collectAsState()
 Card(colors=CardDefaults.cardColors(containerColor=MaterialTheme.colorScheme.primaryContainer),modifier=Modifier.fillMaxWidth()) {
  Column(Modifier.padding(12.dp),verticalArrangement=Arrangement.spacedBy(6.dp)) {
   Text("Mi recreo · compra con tiempo",style=MaterialTheme.typography.titleMedium)
   Row(horizontalArrangement=Arrangement.spacedBy(6.dp)) {listOf(5,10,15,20).forEach {m->FilterChip(selected=plan.minutosDisponibles==m,onClick={vm.minutos.value=m},label={Text("$m min")})}}
   Text("Espera estimada: ${plan.minutosEstimados} min")
   Text(plan.mensaje,color=if(plan.cabe) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.error)
   Text("Estimación con pedidos activos y 2 estaciones. No garantiza la hora de entrega.",style=MaterialTheme.typography.bodySmall)
  }
 }
}

