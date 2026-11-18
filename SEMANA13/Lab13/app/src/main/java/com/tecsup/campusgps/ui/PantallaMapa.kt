package com.tecsup.campusgps.ui

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tecsup.campusgps.model.Posicion
import com.tecsup.campusgps.model.UbicacionUi
import com.tecsup.campusgps.util.formatearDistancia
import com.tecsup.campusgps.viewmodel.MapaViewModel
import java.util.Locale

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
        if (!vm.ubicacionDisponible || tienePermiso()) vm.buscar() else solicitar.launch(permisos)
    }

    val colores = MaterialTheme.colorScheme
    val actual = estado
    val posicion = (actual as? UbicacionUi.Lista)?.posicion
    val buscando = actual == UbicacionUi.Buscando
    val destino = cercano
    val distanciaCercana = lugares.firstOrNull { it.lugar.id == destino?.id }?.metros
    Scaffold(
        containerColor = colores.background,
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = { CabeceraCampus(posicion) }
    ) { insets ->
        BoxWithConstraints(Modifier.fillMaxSize().padding(insets).padding(horizontal = 14.dp)) {
            val lateral = maxWidth >= 560.dp
            val alturaLista = when {
                maxHeight >= 680.dp -> 226.dp
                maxHeight >= 540.dp -> 178.dp
                else -> 104.dp
            }
            val panelMapa: @Composable (Modifier) -> Unit = { modifier ->
                MapaUbicacion(
                    estado = estado,
                    lugares = lugares,
                    seleccionado = seleccionado,
                    cercano = cercano,
                    radio = radio,
                    mostrarUbicacionNativa = ubicacionNativa,
                    seleccionar = vm::seleccionar,
                    modifier = modifier.clip(RoundedCornerShape(26.dp))
                )
            }
            val panelLugares: @Composable (Modifier, Boolean) -> Unit = { modifier, usarAltura ->
                Surface(modifier, shape = RoundedCornerShape(24.dp), color = colores.surface) {
                    Column(Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                        if (actual is UbicacionUi.Error) {
                            MensajeError(actual, {
                                contexto.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", contexto.packageName, null)))
                            }, ::buscar)
                        }

                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = ::buscar,
                                modifier = Modifier.weight(1.25f).heightIn(min = 48.dp),
                                enabled = !buscando,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 7.dp)
                            ) {
                                if (buscando) {
                                    CircularProgressIndicator(
                                        Modifier.size(15.dp),
                                        color = colores.onSurfaceVariant,
                                        strokeWidth = 2.dp
                                    )
                                    Spacer(Modifier.width(6.dp))
                                }
                                Text(if (buscando) "Buscando…" else "Obtener mi ubicación")
                            }
                            FilledTonalButton(
                                onClick = { vm.seleccionar(destino?.id) },
                                modifier = Modifier.weight(1f).heightIn(min = 48.dp),
                                enabled = destino != null && !buscando,
                                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 7.dp)
                            ) { Text("Ir al más cercano") }
                        }

                        if (destino != null && distanciaCercana != null && posicion != null) {
                            Text(
                                "Más cercano: ${destino.nombre} (${formatearDistancia(distanciaCercana)})",
                                Modifier.padding(top = 6.dp),
                                style = MaterialTheme.typography.labelMedium,
                                color = colores.primary
                            )
                        }
                        if (posicion != null) {
                            Row(
                                Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Radio", Modifier.padding(end = 4.dp), color = colores.onSurfaceVariant,
                                    style = MaterialTheme.typography.labelMedium)
                                for (distancia in vm.radios) {
                                    FiltroCampus("$distancia m", radio == distancia) { vm.cambiarRadio(distancia) }
                                }
                            }
                        }
                        HorizontalDivider(Modifier.padding(top = 4.dp), color = colores.outlineVariant)

                        Row(
                            Modifier.fillMaxWidth().padding(top = 9.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Lugares del campus", Modifier.weight(1f),
                                style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                            Text("${lugares.size}", Modifier.padding(start = 8.dp),
                                color = colores.primary, fontFamily = FontFamily.Monospace,
                                style = MaterialTheme.typography.labelLarge)
                        }
                        Row(
                            Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            FiltroCampus("Todos", categoria == null) { vm.filtrar(null) }
                            for (filtro in vm.categorias) {
                                FiltroCampus(filtro, categoria == filtro) { vm.filtrar(filtro) }
                            }
                        }
                        ListaLugares(
                            lugares, seleccionado?.id, cercano?.id, dentroDelRadio, vm::seleccionar,
                            if (usarAltura) Modifier.fillMaxWidth().height(alturaLista)
                            else Modifier.fillMaxWidth().weight(1f)
                        )
                    }
                }
            }
            if (lateral) {
                Row(Modifier.fillMaxSize().padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    panelMapa(Modifier.weight(1.3f).fillMaxHeight())
                    panelLugares(Modifier.weight(1f).fillMaxHeight(), false)
                }
            } else {
                Column(Modifier.fillMaxSize().padding(bottom = 7.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    panelMapa(Modifier.weight(1f).fillMaxWidth())
                    panelLugares(Modifier.fillMaxWidth(), true)
                }
            }
        }
    }
}

@Composable
private fun FiltroCampus(texto: String, activo: Boolean, onClick: () -> Unit) {
    val colores = MaterialTheme.colorScheme
    Surface(
        selected = activo,
        onClick = onClick,
        shape = CircleShape,
        color = if (activo) colores.primary else colores.surface,
        contentColor = if (activo) colores.onPrimary else colores.onSurfaceVariant,
        border = if (activo) null else BorderStroke(1.dp, colores.outlineVariant)
    ) {
        Box(Modifier.heightIn(min = 32.dp).padding(horizontal = 10.dp, vertical = 5.dp),
            contentAlignment = Alignment.Center) {
            Text(texto, style = MaterialTheme.typography.labelLarge, maxLines = 1)
        }
    }
}

@Composable
private fun CabeceraCampus(posicion: Posicion?) {
    val colores = MaterialTheme.colorScheme
    Row(
        Modifier.fillMaxWidth().statusBarsPadding()
            .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal))
            .padding(horizontal = 16.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Canvas(Modifier.size(40.dp).background(colores.primary, CircleShape)) {
            val punta = Path().apply {
                moveTo(size.width * .72f, size.height * .24f)
                lineTo(size.width * .57f, size.height * .72f)
                lineTo(size.width * .47f, size.height * .54f)
                lineTo(size.width * .28f, size.height * .45f)
                close()
            }
            drawPath(punta, colores.onPrimary)
            drawCircle(colores.onPrimary, radius = size.width * .035f,
                center = Offset(size.width * .31f, size.height * .68f))
        }
        Text("CampusGPS", Modifier.weight(1f), style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Black, maxLines = 1, overflow = TextOverflow.Ellipsis)
        if (posicion != null) {
            Text(
                String.format(Locale.ROOT, "%.5f°\n%.5f°", posicion.latitud, posicion.longitud),
                color = colores.onSurfaceVariant, style = MaterialTheme.typography.labelMedium,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}
