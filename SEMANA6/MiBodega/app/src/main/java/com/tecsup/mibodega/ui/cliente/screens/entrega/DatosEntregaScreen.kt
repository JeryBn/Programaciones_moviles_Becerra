package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.componentes.BotonPrimario

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatosEntregaScreen(total: Double, onVolver: () -> Unit, onConfirmar: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var referencia by remember { mutableStateOf("") }
    var pago by remember { mutableStateOf("Efectivo") }
    Scaffold(topBar = { TopAppBar(title = { Text("Datos de entrega") }, navigationIcon = { IconButton(onClick = onVolver) { Icon(Icons.Default.ArrowBack, "Volver") } }) }) { padding ->
        Column(Modifier.padding(padding).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(telefono, { telefono = it }, label = { Text("Telefono") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(direccion, { direccion = it }, label = { Text("Direccion") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(referencia, { referencia = it }, label = { Text("Referencia") }, modifier = Modifier.fillMaxWidth())
            Text("Metodo de pago")
            Row { listOf("Efectivo", "Yape", "Plin").forEach { opcion -> FilterChip(selected = pago == opcion, onClick = { pago = opcion }, label = { Text(opcion) }); Spacer(Modifier.width(6.dp)) } }
            Text("Total: S/ %.2f".format(total), style = MaterialTheme.typography.titleLarge)
            BotonPrimario(texto = "Confirmar pedido", onClick = onConfirmar, habilitado = nombre.isNotBlank() && telefono.isNotBlank() && direccion.isNotBlank())
        }
    }
}
