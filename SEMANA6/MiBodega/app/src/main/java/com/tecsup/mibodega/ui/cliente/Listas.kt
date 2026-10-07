package com.tecsup.mibodega.ui.cliente

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun <T> ListaVertical(elementos: List<T>, clave: (T) -> Any, modifier: Modifier = Modifier,
    tarjeta: @Composable (T) -> Unit) {
    if (Practica.USAR_LAZY_COLUMN) LazyColumn(modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(elementos, key = clave) { tarjeta(it) }
    } else Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)) { elementos.forEach { tarjeta(it) } }
}

@Composable
fun FilaFiltros(elementos: List<String>, chip: @Composable (String) -> Unit) {
    if (Practica.USAR_LAZY_ROW) LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)) { items(elementos, key = { it }) { chip(it) } }
    else Row(Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)) { elementos.forEach { chip(it) } }
}
