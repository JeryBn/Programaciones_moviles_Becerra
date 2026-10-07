package com.tecsup.mibodega.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val BodegaColorScheme = lightColorScheme(
    primary = VerdeBodega,
    onPrimary = Blanco,
    secondary = AzulEnlace,
    background = Blanco,
    onBackground = AzulTexto,
    surface = Blanco,
    onSurface = AzulTexto,
    surfaceVariant = GrisClaro,
    onSurfaceVariant = GrisTexto,
    outline = GrisBorde,
    error = RojoPrecio
)

@Composable
fun BodegaTheme(oscuro: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (oscuro) darkColorScheme(primary = VerdeBodega, secondary = AzulEnlace) else BodegaColorScheme,
        typography = BodegaTypography,
        content = content
    )
}