package com.becerra.tecsupfit

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Ambas implementaciones comparten tarjetas y acciones: retirar lazy no cambia la función.
@Composable
fun <T> ListaVertical(
    elementos: List<T>,
    clave: (T) -> Any,
    usarLazy: Boolean = Practica.USAR_LAZY_COLUMN,
    tarjeta: @Composable (T) -> Unit
) {
    if (usarLazy) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) { items(elementos, key = clave) { tarjeta(it) } }
    } else {
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) { elementos.forEach { elemento -> key(clave(elemento)) { tarjeta(elemento) } } }
    }
}

@Composable
fun Filtros(soloHoy: Boolean, seleccionar: (Boolean) -> Unit, usarLazy: Boolean) {
    val filtros = listOf(false to "Esta semana", true to "Hoy")
    val chip: @Composable (Pair<Boolean, String>) -> Unit = { (valor, etiqueta) ->
        FilterChip(selected = soloHoy == valor, onClick = { seleccionar(valor) }, label = { Text(etiqueta) })
    }
    if (usarLazy) {
        LazyRow(contentPadding = PaddingValues(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtros, key = { it.second }) { chip(it) }
        }
    } else {
        Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 20.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            filtros.forEach { chip(it) }
        }
    }
}

@Composable
fun Inicio(
    abrirClase: (Int) -> Unit,
    usarLazyColumn: Boolean = Practica.USAR_LAZY_COLUMN,
    usarLazyRow: Boolean = Practica.USAR_LAZY_ROW
) {
    var soloHoy by rememberSaveable { mutableStateOf(false) }
    Column {
        Column(Modifier.padding(20.dp)) {
            Text("Tu próxima meta", style = MaterialTheme.typography.headlineLarge)
            Text("Muévete a tu ritmo. Elige una clase.", style = MaterialTheme.typography.bodyLarge)
        }
        Filtros(soloHoy, { soloHoy = it }, usarLazyRow)
        Box(Modifier.weight(1f)) {
            ListaVertical(filtrarClases(soloHoy), { it.id }, usarLazyColumn) { clase ->
                ElevatedCard(onClick = { abrirClase(clase.id) }, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(clase.categoria.uppercase(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                        Text(clase.nombre, style = MaterialTheme.typography.titleLarge)
                        Text(clase.horarios.first())
                        Text("Ver clase →", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
    }
}
