package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Circle
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.Seleccion
import com.tecsup.campusgps.model.UbicacionUi

@Composable
internal fun MapaGoogle(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Seleccion?,
    cercano: Lugar?,
    radio: Int,
    permisoUbicacion: Boolean,
    seleccionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 15f)
    }
    var cargado by remember { mutableStateOf(false) }
    var area by remember { mutableStateOf(IntSize.Zero) }
    var mostrarPosicion by remember { mutableStateOf(false) }
    val ubicacion = (estado as? UbicacionUi.Lista)?.posicion?.let { LatLng(it.latitud, it.longitud) }
    val destino = lugares.firstOrNull { it.lugar.id == seleccionado?.id }?.lugar

    LaunchedEffect(cargado, ubicacion) {
        if (cargado && seleccionado == null && ubicacion != null) {
            camara.animate(CameraUpdateFactory.newLatLngZoom(ubicacion, 16f))
        }
    }
    LaunchedEffect(cargado, seleccionado, destino) {
        if (cargado && destino != null) {
            camara.animate(CameraUpdateFactory.newLatLngZoom(LatLng(destino.latitud, destino.longitud), 17f))
        }
    }
    LaunchedEffect(seleccionado, estado) { mostrarPosicion = false }

    val puntos = remember(cargado, camara.position, area, lugares) {
        val projection = camara.projection
        if (!cargado || projection == null || area == IntSize.Zero) emptyMap()
        else lugares.associate { (lugar, _) ->
            val punto = projection.toScreenLocation(LatLng(lugar.latitud, lugar.longitud))
            lugar.id to android.graphics.PointF(punto.x.toFloat(), punto.y.toFloat())
        }
    }

    Box(modifier) {
        GoogleMap(
            modifier = Modifier.fillMaxSize().onSizeChanged { area = it },
            cameraPositionState = camara,
            properties = MapProperties(isMyLocationEnabled = permisoUbicacion),
            uiSettings = MapUiSettings(
                myLocationButtonEnabled = permisoUbicacion,
                zoomControlsEnabled = true,
                mapToolbarEnabled = false
            ),
            onMapLoaded = { cargado = true }
        ) {
            if (ubicacion != null) {
                Circle(
                    center = ubicacion,
                    radius = radio.toDouble(),
                    strokeColor = Color(0xFF3078B7),
                    strokeWidth = 3f,
                    fillColor = Color(0x243078B7)
                )
                Marker(
                    state = rememberUpdatedMarkerState(ubicacion),
                    title = "Estás aquí",
                    onClick = { mostrarPosicion = true; true }
                )
            }
            for ((lugar, _) in lugares) key(lugar.id) {
                val color = if (lugar.id == cercano?.id) BitmapDescriptorFactory.HUE_GREEN
                    else BitmapDescriptorFactory.HUE_AZURE
                Marker(
                    state = rememberUpdatedMarkerState(LatLng(lugar.latitud, lugar.longitud)),
                    icon = BitmapDescriptorFactory.defaultMarker(color),
                    title = lugar.nombre,
                    snippet = lugar.categoria,
                    onClick = { seleccionar(lugar.id); true }
                )
            }
        }
        EtiquetasMapa(lugares, puntos, seleccionar, Modifier.matchParentSize())
        if (mostrarPosicion || destino != null) {
            Surface(Modifier.align(Alignment.TopCenter).padding(8.dp), shape = MaterialTheme.shapes.small) {
                Text(
                    if (mostrarPosicion) "Estás aquí" else "${destino?.nombre} · ${destino?.categoria}",
                    Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        if (permisoUbicacion && ubicacion != null && cargado) {
            Surface(
                Modifier.align(Alignment.TopStart).padding(
                    start = 8.dp, top = if (destino != null || mostrarPosicion) 40.dp else 8.dp
                ),
                shape = MaterialTheme.shapes.small
            ) {
                TextButton(onClick = {
                    camara.move(CameraUpdateFactory.newLatLngZoom(ubicacion, 16f))
                    mostrarPosicion = true
                }) { Text("Mi ubicación") }
            }
        }
    }
}
