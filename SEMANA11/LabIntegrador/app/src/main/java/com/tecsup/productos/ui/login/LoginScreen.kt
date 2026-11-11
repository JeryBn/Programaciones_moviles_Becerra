package com.tecsup.productos.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(ocupado: Boolean, error: String?, ingresar: (String, String) -> Unit) {
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }
    Column(Modifier.fillMaxWidth().padding(24.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Catálogo Tecsup", style = MaterialTheme.typography.headlineLarge)
        Text("Ingresa con tu cuenta del laboratorio.")
        OutlinedTextField(correo, { correo = it }, Modifier.fillMaxWidth(), label = { Text("Correo electrónico") },
            singleLine = true, keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email))
        OutlinedTextField(clave, { clave = it }, Modifier.fillMaxWidth(), label = { Text("Contraseña") },
            singleLine = true, visualTransformation = PasswordVisualTransformation())
        error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        Button(onClick = { ingresar(correo, clave) }, enabled = !ocupado) { Text("Ingresar") }
        if (ocupado) LinearProgressIndicator(Modifier.fillMaxWidth())
    }
}