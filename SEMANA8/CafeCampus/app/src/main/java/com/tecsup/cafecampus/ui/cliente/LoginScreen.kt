package com.tecsup.cafecampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.SesionViewModel
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.components.*
import com.tecsup.cafecampus.ui.theme.CafeTheme
@Composable fun LoginScreen(vm:SesionViewModel) {
 var rol by rememberSaveable { mutableStateOf("CLIENTE") }; var correo by rememberSaveable { mutableStateOf("") }; var clave by rememberSaveable { mutableStateOf("") }
 val error by vm.error.collectAsState()
 val oscuro by vm.modoOscuro.collectAsState()
 CafeTheme(Rol.valueOf(rol),oscuro) {
 Column(Modifier.fillMaxSize().padding(24.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
  Spacer(Modifier.height(32.dp)); Text("CafeCampus",style=MaterialTheme.typography.headlineLarge); Text("Tu pausa sabe mejor")
  Row { Rol.entries.forEach { FilterChip(selected=rol==it.name,onClick={rol=it.name},label={Text(if(it==Rol.CLIENTE) "Estudiante" else "Administrador")}); Spacer(Modifier.width(8.dp)) } }
  CafeTextField(correo,"Correo") { correo=it }; CafeTextField(clave,"Contraseña",true) { clave=it }
  error?.let { Text(it,color=MaterialTheme.colorScheme.error) }
  CafeButton("Ingresar") { vm.login(correo,clave,Rol.valueOf(rol)) }
  Text("Modo práctica · solo datos ficticios")
 }
 }
}

