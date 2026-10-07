package com.tecsup.mibodega.ui.cliente.screens.registro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.formularioValido

@Composable
fun RegistroScreen(onVolver: () -> Unit, onCrearCuenta: (String, String, String, String) -> Unit) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var telefono by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var referencia by rememberSaveable { mutableStateOf("") }
    var enviado by rememberSaveable { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Crear cuenta", style = MaterialTheme.typography.headlineMedium)
        OutlinedTextField(nombre, { nombre = it }, label = { Text("Nombre") }, isError = enviado && nombre.isBlank(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(telefono, { telefono = it }, label = { Text("Teléfono") }, isError = enviado && telefono.isBlank(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(direccion, { direccion = it }, label = { Text("Dirección") }, isError = enviado && direccion.isBlank(), modifier = Modifier.fillMaxWidth())
        OutlinedTextField(referencia, { referencia = it }, label = { Text("Referencia (opcional)") }, modifier = Modifier.fillMaxWidth())
        if (enviado && !formularioValido(nombre, telefono, direccion)) Text("Completa los campos marcados en rojo", color = MaterialTheme.colorScheme.error)
        Button(onClick = { enviado = true; if (formularioValido(nombre, telefono, direccion)) onCrearCuenta(nombre, telefono, direccion, referencia) }) { Text("Crear cuenta") }
        TextButton(onClick = onVolver) { Text("Volver") }
    }
}
