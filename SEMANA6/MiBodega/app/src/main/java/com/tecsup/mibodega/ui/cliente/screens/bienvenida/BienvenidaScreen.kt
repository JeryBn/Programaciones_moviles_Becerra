package com.tecsup.mibodega.ui.cliente.screens.bienvenida

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.tecsup.mibodega.ui.cliente.modelo.credencialesValidas

@Composable
fun BienvenidaScreen(onRegistrarse: () -> Unit, onIniciarSesion: () -> Unit, onTerminos: () -> Unit) {
    var usuario by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf(false) }
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Mi Bodega", style = MaterialTheme.typography.headlineLarge)
        Text("Ingresa para comprar. Cuenta de práctica: jery / 1234")
        OutlinedTextField(usuario, { usuario = it; error = false }, label = { Text("Usuario") },
            isError = error, singleLine = true, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(clave, { clave = it; error = false }, label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(), isError = error,
            singleLine = true, modifier = Modifier.fillMaxWidth())
        if (error) Text("Usuario o contraseña incorrectos", color = MaterialTheme.colorScheme.error)
        Button(onClick = { if (credencialesValidas(usuario, clave)) onIniciarSesion() else error = true }) { Text("Entrar en mi Bodega") }
        OutlinedButton(onClick = onRegistrarse) { Text("Crear cuenta") }
        TextButton(onClick = onTerminos) { Text("Información de la demostración") }
    }
}
