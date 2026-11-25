package com.tecsup.avisoscampus.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.tecsup.avisoscampus.model.PermisoUi
import com.tecsup.avisoscampus.notificacion.Notificador
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.receiver.ModoAvionReceiver
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {
    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val lanzador = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission(), vm::permisoRespondido)
    LaunchedEffect(contexto) {
        if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
    }

    LaunchedEffect(contexto, vm) {
        vm.eventos.collect { aviso -> Notificador.mostrar(contexto, aviso) }
    }

    val faltan by vm.faltan.collectAsState()

    val escuchando by vm.escuchandoAvion.collectAsState()
    DisposableEffect(contexto, escuchando, vm) {
        val receptor = if (escuchando) ModoAvionReceiver(vm::alCambiarModoAvion) else null
        if (receptor != null) {
            ContextCompat.registerReceiver(
                contexto, receptor, IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )
        }
        onDispose { if (receptor != null) contexto.unregisterReceiver(receptor) }
    }

    val historial by vm.avisos.collectAsState()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text("AvisosCampus") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary
            )
        )
    }) { margenes ->
        Column(
            Modifier.padding(margenes).verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Tarjeta("Permiso de notificaciones") {
                val (etiqueta, color) = when (permiso) {
                    PermisoUi.SinPedir -> "Sin pedir" to Color(0xFF555D65)
                    PermisoUi.Concedido -> "Permitido" to Color(0xFF21633D)
                    PermisoUi.Denegado -> "Denegado" to MaterialTheme.colorScheme.error
                }
                Text(etiqueta, color = color)
                Button(onClick = {
                    if (Notificador.tienePermiso(contexto)) vm.permisoRespondido(true)
                    else if (Build.VERSION.SDK_INT >= 33) lanzador.launch(Manifest.permission.POST_NOTIFICATIONS)
                }) { Text("Permitir notificaciones") }
            }
            Tarjeta("Avisos") {
                Button(onClick = vm::avisarAhora) { Text("Mostrar aviso") }
            }
            Tarjeta("Recordatorio") {
                Button(onClick = { vm.recordarEn() }, enabled = faltan == null) { Text("Recordar en 10 s") }
                OutlinedButton(onClick = vm::cancelarRecordatorio, enabled = faltan != null) { Text("Cancelar") }
                faltan?.let { Text("Faltan $it s") }
            }
            Tarjeta("Receptor de anuncios") {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Escuchar el modo avión", Modifier.weight(1f))
                    Switch(checked = escuchando, onCheckedChange = vm::cambiarEscucha)
                }
            }
            HistorialAvisos(historial)
        }
    }
}

@Composable
internal fun Tarjeta(titulo: String, contenido: @Composable ColumnScope.() -> Unit) {
    Card(Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            contenido()
        }
    }
}
