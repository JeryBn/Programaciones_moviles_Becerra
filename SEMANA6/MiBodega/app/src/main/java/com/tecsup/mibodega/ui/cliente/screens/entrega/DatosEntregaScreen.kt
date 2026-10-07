package com.tecsup.mibodega.ui.cliente.screens.entrega

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.*

@Composable
fun DatosEntregaScreen(carrito: List<ItemCarrito>, onVolver: () -> Unit,
    onConfirmar: (String, String, Boolean) -> Unit) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var referencia by rememberSaveable { mutableStateOf("") }
    var pago by rememberSaveable { mutableStateOf("Efectivo") }
    var recojo by rememberSaveable { mutableStateOf(false) }
    var enviado by rememberSaveable { mutableStateOf(false) }
    val valido = nombre.isNotBlank() && telefono.isNotBlank() && (recojo || direccion.isNotBlank())
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, isError = enviado && nombre.isBlank(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(telefono, { telefono = it }, label = { Text("Teléfono") }, isError = enviado && telefono.isBlank(), modifier = Modifier.fillMaxWidth())
        Row { RadioButton(!recojo, { recojo = false }); Text("Delivery (+ S/ 4.00)") }
        Row { RadioButton(recojo, { recojo = true }); Text("Recojo en tienda (gratis)") }
        if (!recojo) {
            OutlinedTextField(direccion, { direccion = it }, label = { Text("Dirección") }, isError = enviado && direccion.isBlank(), modifier = Modifier.fillMaxWidth())
            OutlinedTextField(referencia, { referencia = it }, label = { Text("Referencia (opcional)") }, modifier = Modifier.fillMaxWidth())
        }
        Text("Método de pago (demostración)")
        Row { listOf("Efectivo", "Yape", "Plin").forEach { opcion -> FilterChip(pago == opcion, { pago = opcion }, label = { Text(opcion) }) } }
        Text("Total: S/ %.2f".format(totalEntrega(carrito, recojo)), style = MaterialTheme.typography.titleLarge)
        if (enviado && !valido) Text("Completa los campos marcados en rojo", color = MaterialTheme.colorScheme.error)
        Button(onClick = { enviado = true; if (valido && carrito.isNotEmpty()) onConfirmar(nombre, if (recojo) "Recojo en tienda" else direccion, recojo) }, enabled = carrito.isNotEmpty()) { Text("Confirmar pedido") }
        TextButton(onClick = onVolver) { Text("Volver al carrito") }
    }
}
