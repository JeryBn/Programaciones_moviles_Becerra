package com.tecsup.campusgps.ui

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PointF
import android.view.Gravity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.tecsup.campusgps.model.Lugar
import com.tecsup.campusgps.model.LugarConDistancia
import com.tecsup.campusgps.model.Posicion
import com.tecsup.campusgps.model.Seleccion
import com.tecsup.campusgps.model.UbicacionUi
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.location.LocationComponentActivationOptions
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression.get
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.FillLayer
import org.maplibre.android.style.layers.LineLayer
import org.maplibre.android.style.layers.PropertyFactory.*
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point
import org.maplibre.geojson.Polygon
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos

import kotlin.math.roundToInt
import kotlin.math.sin

private const val ESTILO = "https://tiles.openfreemap.org/styles/liberty"
private const val PUNTOS = "campus-puntos"
private const val PUNTOS_CAPA = "campus-puntos-capa"

private const val RADIO = "campus-radio"
private const val USUARIO = "campus-usuario"
private const val USUARIO_CAPA = "campus-usuario-capa"

@Composable
fun MapaUbicacion(
    estado: UbicacionUi,
    lugares: List<LugarConDistancia>,
    seleccionado: Seleccion?,
    cercano: Lugar?,
    radio: Int,
    mostrarUbicacionNativa: Boolean,
    seleccionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val contexto = LocalContext.current
    val ciclo = LocalLifecycleOwner.current.lifecycle
    val seleccionarActual by rememberUpdatedState(seleccionar)
    val lugaresActuales by rememberUpdatedState(lugares)
    val vista = remember(contexto) {
        MapLibre.getInstance(contexto.applicationContext)
        MapView(contexto)
    }
    var mapa by remember(vista) { mutableStateOf<MapLibreMap?>(null) }
    var estilo by remember(vista) { mutableStateOf<Style?>(null) }
    var aviso by remember(vista) { mutableStateOf(false) }
    var mostrarPosicion by remember(vista) { mutableStateOf(false) }
    var etiquetas by remember(vista) { mutableStateOf<Map<Int, PointF>>(emptyMap()) }
    val posicion = (estado as? UbicacionUi.Lista)?.posicion
    val destino = lugares.firstOrNull { it.lugar.id == seleccionado?.id }?.lugar

    DisposableEffect(vista, ciclo) {
        var activa = true
        var iniciada = false
        var visible = false
        var reanudada = false
        val fallo = MapView.OnDidFailLoadingMapListener { if (activa) aviso = true }
        val observador = LifecycleEventObserver { _, evento ->
            when (evento) {
                Lifecycle.Event.ON_CREATE -> {
                    vista.onCreate(null)
                    iniciada = true
                }
                Lifecycle.Event.ON_START -> {
                    vista.onStart()
                    visible = true
                }
                Lifecycle.Event.ON_RESUME -> {
                    vista.onResume()
                    reanudada = true
                }
                Lifecycle.Event.ON_PAUSE -> {
                    vista.onPause()
                    reanudada = false
                }
                Lifecycle.Event.ON_STOP -> {
                    vista.onStop()
                    visible = false
                }
                Lifecycle.Event.ON_DESTROY -> {
                    activa = false
                    vista.onDestroy()
                    iniciada = false
                }
                else -> Unit
            }
        }
        ciclo.addObserver(observador)
        vista.addOnDidFailLoadingMapListener(fallo)
        vista.getMapAsync { instancia ->
            if (!activa) return@getMapAsync
            instancia.uiSettings.isScrollGesturesEnabled = true
            instancia.uiSettings.isZoomGesturesEnabled = true
            instancia.uiSettings.isLogoEnabled = false
            instancia.uiSettings.attributionGravity = Gravity.BOTTOM or Gravity.END
            instancia.cameraPosition = CameraPosition.Builder()
                .target(LatLng(-12.0464, -77.0428)).zoom(14.5).build()
            instancia.addOnCameraMoveListener { if (activa) etiquetas = instancia.proyectar(lugaresActuales) }
            instancia.addOnCameraIdleListener { if (activa) etiquetas = instancia.proyectar(lugaresActuales) }
            instancia.addOnMapClickListener { coordenada ->
                val pixel = instancia.projection.toScreenLocation(coordenada)
                val id = instancia.queryRenderedFeatures(pixel, PUNTOS_CAPA)
                    .firstOrNull()?.getNumberProperty("id")?.toInt()
                mostrarPosicion = id == null && instancia.queryRenderedFeatures(pixel, USUARIO_CAPA).isNotEmpty()
                if (id != null) seleccionarActual(id)
                id != null || mostrarPosicion
            }
            instancia.setStyle(ESTILO) { cargado ->
                if (activa) {
                    prepararCapas(cargado)
                    aviso = false
                    estilo = cargado
                }
            }
            mapa = instancia
        }
        onDispose {
            activa = false
            ciclo.removeObserver(observador)
            vista.removeOnDidFailLoadingMapListener(fallo)
            if (reanudada) vista.onPause()
            if (visible) vista.onStop()
            if (iniciada) vista.onDestroy()
        }
    }

    LaunchedEffect(estilo, lugares, cercano, destino, posicion, radio) {
        val actual = estilo ?: return@LaunchedEffect
        val puntos = lugares.map { (lugar, _) ->
            Feature.fromGeometry(Point.fromLngLat(lugar.longitud, lugar.latitud)).apply {
                addNumberProperty("id", lugar.id)
                addStringProperty("color", when (lugar.id) {
                    cercano?.id -> "#16834B"
                    else -> "#237FD1"
                })
                addNumberProperty("borde", if (lugar.id == destino?.id) 4 else 2)
            }
        }
        actual.getSourceAs<GeoJsonSource>(PUNTOS)?.setGeoJson(FeatureCollection.fromFeatures(puntos))
        val ubicacion = posicion?.let { Feature.fromGeometry(Point.fromLngLat(it.longitud, it.latitud)) }
        actual.getSourceAs<GeoJsonSource>(USUARIO)?.setGeoJson(coleccion(ubicacion))
        actual.getSourceAs<GeoJsonSource>(RADIO)?.setGeoJson(coleccion(posicion?.let {
            Feature.fromGeometry(circulo(it, radio))
        }))
    }
    LaunchedEffect(estilo, mapa, lugares) {
        etiquetas = mapa?.proyectar(lugares).orEmpty()
    }
    LaunchedEffect(estilo, mapa, mostrarUbicacionNativa) {
        val actual = estilo ?: return@LaunchedEffect
        mapa?.let { actualizarUbicacionNativa(contexto, it, actual, mostrarUbicacionNativa) }
    }
    LaunchedEffect(seleccionado, estado) { mostrarPosicion = false }
    LaunchedEffect(estilo, mapa, posicion) {
        if (estilo != null && seleccionado == null && posicion != null) mapa?.centrar(posicion)
    }
    LaunchedEffect(estilo, mapa, seleccionado, destino) {
        if (estilo != null && destino != null) {
            mapa?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(destino.latitud, destino.longitud), 17.0))
        }
    }

    Box(modifier) {
        AndroidView(factory = { vista }, modifier = Modifier.fillMaxSize().onSizeChanged {
            etiquetas = mapa?.proyectar(lugaresActuales).orEmpty()
        })
        if (estilo != null) {
            EtiquetasMapa(lugares, etiquetas, seleccionar, Modifier.matchParentSize())
        }
        mapa?.let { instancia ->
            ControlesZoom(instancia, Modifier.align(Alignment.CenterEnd).padding(end = 8.dp))
        }
        Surface(
            modifier = Modifier.align(Alignment.BottomStart).padding(start = 8.dp, bottom = 8.dp),
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
        ) {
            Text("© OpenStreetMap contributors · OpenFreeMap", Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                style = MaterialTheme.typography.labelSmall)
        }
        if (aviso || destino != null || mostrarPosicion) {
            Surface(
                modifier = Modifier.align(Alignment.TopCenter).padding(8.dp),
                shape = MaterialTheme.shapes.small
            ) {
                Text(
                    when {
                        aviso -> "No se pudo cargar el mapa. Revisa tu conexión."
                        mostrarPosicion -> "Estás aquí"
                        else -> "${destino?.nombre} · ${destino?.categoria}"
                    },
                    Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
        if (mostrarUbicacionNativa && posicion != null && estilo != null) {
            Surface(
                modifier = Modifier.align(Alignment.TopStart).padding(start = 8.dp, top = if (destino == null && !mostrarPosicion) 8.dp else 40.dp),
                shape = MaterialTheme.shapes.small
            ) {
                TextButton(onClick = {
                    mapa?.centrar(posicion)
                    mostrarPosicion = true
                }) { Text("Mi ubicación") }
            }
        }
    }
}

@Composable
private fun ControlesZoom(mapa: MapLibreMap, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 4.dp
    ) {
        Column {
            IconButton(
                onClick = { mapa.animateCamera(CameraUpdateFactory.zoomIn()) },
                modifier = Modifier.semantics { contentDescription = "Acercar mapa" }
            ) {
                Text("+", style = MaterialTheme.typography.titleLarge)
            }
            HorizontalDivider(Modifier.size(width = 48.dp, height = 1.dp))
            IconButton(
                onClick = { mapa.animateCamera(CameraUpdateFactory.zoomOut()) },
                modifier = Modifier.semantics { contentDescription = "Alejar mapa" }
            ) {
                Text("−", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
}

private fun MapLibreMap.centrar(posicion: Posicion) {
    animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(posicion.latitud, posicion.longitud), 16.0))
}

@SuppressLint("MissingPermission") // La pantalla comprueba los permisos antes de activar la capa.
private fun actualizarUbicacionNativa(contexto: Context, mapa: MapLibreMap, estilo: Style, permitido: Boolean) {
    val ubicacion = mapa.locationComponent
    try {
        if (permitido && !ubicacion.isLocationComponentActivated) {
            ubicacion.activateLocationComponent(LocationComponentActivationOptions.builder(contexto, estilo).build())
        }
        if (ubicacion.isLocationComponentActivated) ubicacion.isLocationComponentEnabled = permitido
    } catch (_: SecurityException) {
        // Android puede revocar el permiso mientras se habilita la capa.
    }
}

private fun prepararCapas(estilo: Style) {
    estilo.addSource(GeoJsonSource(RADIO))
    estilo.addSource(GeoJsonSource(PUNTOS))
    estilo.addSource(GeoJsonSource(USUARIO))
    estilo.addLayer(FillLayer("campus-radio-relleno", RADIO).withProperties(
        fillColor("#3078B7"), fillOpacity(0.20f)
    ))
    estilo.addLayer(LineLayer("campus-radio-borde", RADIO).withProperties(
        lineColor("#3078B7"), lineWidth(2f)
    ))
    estilo.addLayer(CircleLayer(PUNTOS_CAPA, PUNTOS).withProperties(
        circleColor(get("color")),
        circleRadius(8f),
        circleStrokeWidth(get("borde")),
        circleStrokeColor("#FFFFFF")
    ))
    estilo.addLayer(CircleLayer(USUARIO_CAPA, USUARIO).withProperties(
        circleColor("#CC302F"), circleRadius(6f), circleStrokeWidth(2f), circleStrokeColor("#FFFFFF")
    ))
}

private fun MapLibreMap.proyectar(lugares: List<LugarConDistancia>): Map<Int, PointF> {
    val superficie = projection
    return lugares.associate { (lugar, _) ->
        lugar.id to superficie.toScreenLocation(LatLng(lugar.latitud, lugar.longitud))
    }
}

@Composable
internal fun EtiquetasMapa(
    lugares: List<LugarConDistancia>,
    puntos: Map<Int, PointF>,
    seleccionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Layout(
        modifier = modifier,
        content = {
            for ((lugar, _) in lugares) {
                Surface(
                    modifier = Modifier.clickable { seleccionar(lugar.id) },
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    Text(lugar.nombre, Modifier.padding(horizontal = 5.dp, vertical = 2.dp),
                        style = MaterialTheme.typography.labelSmall, maxLines = 1)
                }
            }
        }
    ) { elementos, restricciones ->
        val ancho = restricciones.maxWidth
        val alto = restricciones.maxHeight
        val hijos = elementos.map { it.measure(restricciones.copy(minWidth = 0, minHeight = 0)) }
        val margen = 12.dp.roundToPx()
        layout(ancho, alto) {
            hijos.forEachIndexed { i, etiqueta ->
                val id = lugares[i].lugar.id
                val punto = puntos[id] ?: return@forEachIndexed
                if (punto.x !in 0f..ancho.toFloat() || punto.y !in 0f..alto.toFloat()) return@forEachIndexed
                val px = punto.x.roundToInt()
                val py = punto.y.roundToInt()
                val (x, y) = when (id) {
                    1, 4 -> px - etiqueta.width - margen to py - etiqueta.height / 2
                    2 -> px - etiqueta.width / 2 to py + margen
                    else -> px - etiqueta.width / 2 to py - etiqueta.height - margen
                }
                etiqueta.place(x.coerceIn(0, ancho - etiqueta.width), y.coerceIn(0, alto - etiqueta.height))
            }
        }
    }
}

private fun coleccion(punto: Feature?): FeatureCollection =
    FeatureCollection.fromFeatures(listOfNotNull(punto))

private fun circulo(centro: Posicion, metros: Int): Polygon {
    val latitud = Math.toRadians(centro.latitud)
    val longitud = Math.toRadians(centro.longitud)
    val arco = metros / 6_371_008.8
    val borde = (0 until 64).map { paso ->
        val rumbo = 2.0 * Math.PI * paso / 64
        val lat = asin(sin(latitud) * cos(arco) + cos(latitud) * sin(arco) * cos(rumbo))
        val lon = longitud + atan2(sin(rumbo) * sin(arco) * cos(latitud), cos(arco) - sin(latitud) * sin(lat))
        Point.fromLngLat((Math.toDegrees(lon) + 540) % 360 - 180, Math.toDegrees(lat))
    }
    return Polygon.fromLngLats(listOf(borde + borde.first()))
}
