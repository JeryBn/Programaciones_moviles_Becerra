package com.tecsup.cafecampus.ui.admin
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tecsup.cafecampus.viewmodel.SesionViewModel
import com.tecsup.cafecampus.ui.components.*
@Composable fun AjustesScreen(vm:SesionViewModel) {
 val oscuro by vm.modoOscuro.collectAsState();val recibir by vm.recibirPedidos.collectAsState();val usuario by vm.usuarioActual.collectAsState()
 Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(16.dp)) {
  Text("Ajustes y perfil",style=MaterialTheme.typography.headlineMedium);Text(usuario?.nombre?:"");Text(usuario?.correo?:"")
  Row {Text("Modo oscuro",Modifier.weight(1f));Switch(oscuro,vm::oscuro)}
  if(usuario?.rol=="ADMIN") Row {Text("Recibir pedidos",Modifier.weight(1f));Switch(recibir,vm::recibir)}
  CafeButton("Cambiar de perfil · modo práctica",onClick=vm::cambiarPerfil)
  OutlinedButton(onClick=vm::cerrarSesion) {Text("Cerrar sesión")}
  Text("Ambos perfiles comparten cafecampus.db en este dispositivo.")
 }
}

