package com.tecsup.campusgps.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.tecsup.campusgps.model.*

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Seleccion?,
    cercano: Lugar?,
    radio: Int,
    disponible: Boolean,
    aviso: String,
    modifier: Modifier = Modifier
) {
    if (!disponible) {
        Surface(modifier, color = MaterialTheme.colorScheme.surfaceVariant) {
            Box(Modifier.padding(16.dp), contentAlignment = Alignment.Center) {
                Text(aviso, textAlign = TextAlign.Center)
            }
        }
        return
    }

    val camara = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(-12.0464, -77.0428), 12f)
    }
    val posicion = (estado as? UbicacionUi.Lista)?.posicion
    val destino = lugares.firstOrNull { it.lugar.id == seleccionado?.id }?.lugar
    val marcadores = remember { mutableMapOf<Int, MarkerState>() }
    var cargado by remember { mutableStateOf(false) }
    LaunchedEffect(cargado, estado, seleccionado, destino) {
        if (!cargado) return@LaunchedEffect
        if (destino != null) {
            camara.animate(CameraUpdateFactory.newLatLngZoom(LatLng(destino.latitud, destino.longitud), 17f))
            marcadores[destino.id]?.showInfoWindow()
        } else if (posicion != null) {
            camara.animate(CameraUpdateFactory.newLatLngZoom(LatLng(posicion.latitud, posicion.longitud), 16f))
        }
    }
    GoogleMap(modifier = modifier, cameraPositionState = camara, onMapLoaded = { cargado = true }) {
        if (posicion != null) {
            Circle(
                center = LatLng(posicion.latitud, posicion.longitud),
                radius = radio.toDouble(),
                strokeColor = Color(0xFF3078B7),
                strokeWidth = 3f,
                fillColor = Color(0x243078B7)
            )
            Marker(rememberUpdatedMarkerState(LatLng(posicion.latitud, posicion.longitud)), title = "Estás aquí")
        }
        for ((lugar, _) in lugares) key(lugar.id) {
            val tono = if (lugar.id == cercano?.id) BitmapDescriptorFactory.HUE_GREEN
                else BitmapDescriptorFactory.HUE_AZURE
            Marker(
                state = marcadores.getOrPut(lugar.id) { MarkerState(LatLng(lugar.latitud, lugar.longitud)) },
                title = lugar.nombre,
                snippet = lugar.categoria,
                icon = BitmapDescriptorFactory.defaultMarker(tono)
            )
        }
    }
}
