package com.tecsup.campusgps.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.campusgps.BuildConfig
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.formatearDistancia
import com.tecsup.campusgps.viewmodel.MapaViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {
    val estado by vm.estado.collectAsStateWithLifecycle()
    val lugares by vm.lugares.collectAsStateWithLifecycle()
    val categoria by vm.categoria.collectAsStateWithLifecycle()
    val radio by vm.radio.collectAsStateWithLifecycle()
    val dentroDelRadio by vm.dentroDelRadio.collectAsStateWithLifecycle()
    val seleccionado by vm.seleccionado.collectAsStateWithLifecycle()
    val cercano by vm.masCercano.collectAsStateWithLifecycle()
    val contexto = LocalContext.current
    val permisos = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
    fun tienePermiso() = permisos.any {
        ContextCompat.checkSelfPermission(contexto, it) == PackageManager.PERMISSION_GRANTED
    }
    var ubicacionNativa by remember(contexto) { mutableStateOf(tienePermiso()) }
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { ubicacionNativa = tienePermiso() }
    val solicitar = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        ubicacionNativa = tienePermiso()
        if (ubicacionNativa) vm.buscar() else vm.permisoDenegado()
    }
    fun buscar() {
        if (!vm.mapaDisponible || tienePermiso()) vm.buscar() else solicitar.launch(permisos)
    }

    val colores = MaterialTheme.colorScheme
    Scaffold(
        containerColor = colores.background,
        topBar = {
            TopAppBar(title = { Text("CampusGPS") }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colores.primary, titleContentColor = colores.onPrimary
            ))
        }
    ) { insets ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(insets).padding(horizontal = 12.dp)) {
            val compacto = maxHeight < 470.dp
            val alturaLista = when {
                compacto -> 130.dp
                maxHeight >= 620.dp -> 304.dp
                else -> 228.dp
            }
            val panelMapa: @Composable (Modifier) -> Unit = { modifier ->
                MapaUbicacion(
                    estado, lugares, seleccionado, cercano, radio, BuildConfig.MAPS_CONFIGURED && vm.mapaDisponible,
                    ubicacionNativa,
                    if (vm.mapaDisponible) "Mapa pendiente de configurar" else "Mapa no disponible en este dispositivo",
                    modifier.clip(RoundedCornerShape(18.dp))
                )
            }
            val panelLugares: @Composable (Modifier) -> Unit = { modifier ->
                ListaLugares(lugares, seleccionado?.id, cercano?.id, dentroDelRadio, vm::seleccionar, modifier)
            }
            Column(Modifier.fillMaxSize().padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val buscando = estado == UbicacionUi.Buscando
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = ::buscar, enabled = !buscando) {
                        Text(if (buscando) "Buscando…" else "Obtener mi ubicación")
                    }
                    OutlinedButton(onClick = { vm.seleccionar(cercano?.id) }, enabled = cercano != null && !buscando) {
                        Text("Ir al más cercano")
                    }
                }
                Row(
                    Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    FilterChip(selected = categoria == null, onClick = { vm.filtrar(null) },
                        label = { Text("Todos") })
                    for (filtro in vm.categorias) {
                        FilterChip(selected = categoria == filtro, onClick = { vm.filtrar(filtro) },
                            label = { Text(filtro) })
                    }
                }
                val actual = estado
                if (actual is UbicacionUi.Lista) {
                    val posicion = actual.posicion
                    Text(String.format(Locale.ROOT, "%.5f°  /  %.5f°", posicion.latitud, posicion.longitud),
                        style = MaterialTheme.typography.bodySmall)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text("Radio", style = MaterialTheme.typography.labelMedium)
                        for (distancia in vm.radios) {
                            FilterChip(selected = radio == distancia, onClick = { vm.cambiarRadio(distancia) },
                                label = { Text("$distancia m") })
                        }
                    }
                    val metros = lugares.firstOrNull { it.lugar.id == cercano?.id }?.metros
                    if (cercano != null && metros != null) {
                        Text("Más cercano: ${cercano?.nombre} (${formatearDistancia(metros)})",
                            style = MaterialTheme.typography.labelMedium, color = colores.primary)
                    }
                }
                if (actual is UbicacionUi.Error) MensajeError(actual, {
                    contexto.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts("package", contexto.packageName, null)))
                }, ::buscar)
                if (compacto && LocalConfiguration.current.screenWidthDp >= 550) {
                    Row(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        panelMapa(Modifier.weight(1f).fillMaxHeight())
                        panelLugares(Modifier.weight(1f).fillMaxHeight())
                    }
                } else {
                    panelMapa(Modifier.weight(1f).fillMaxWidth())
                    Text("Lugares del campus", Modifier.align(Alignment.Start),
                        style = MaterialTheme.typography.titleMedium)
                    panelLugares(Modifier.fillMaxWidth().height(alturaLista))
                }
            }
        }
    }
}
