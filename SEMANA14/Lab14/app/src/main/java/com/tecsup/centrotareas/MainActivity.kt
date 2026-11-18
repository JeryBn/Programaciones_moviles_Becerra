package com.tecsup.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tecsup.centrotareas.data.TareasRepositoryLocal
import com.tecsup.centrotareas.ui.PantallaTareas
import com.tecsup.centrotareas.viewmodel.TareasViewModel
import com.tecsup.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val transparente = android.graphics.Color.TRANSPARENT
        val barras = SystemBarStyle.light(transparente, transparente)
        enableEdgeToEdge(statusBarStyle = barras, navigationBarStyle = barras)
        val vm = ViewModelProvider(this, TareasViewModelFactory(TareasRepositoryLocal()))[TareasViewModel::class.java]
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(
                primary = Color(0xFF4532BF),
                onPrimary = Color(0xFFFFFFFF),
                primaryContainer = Color(0xFFEAE5FF),
                onPrimaryContainer = Color(0xFF291B76),
                secondary = Color(0xFF006F64),
                secondaryContainer = Color(0xFFDFF5EF),
                onSecondaryContainer = Color(0xFF114C42),
                background = Color(0xFFF3F2F8),
                surface = Color(0xFFFFFFFF),
                surfaceVariant = Color(0xFFEFEDF7),
                outline = Color(0xFF776F8C),
                outlineVariant = Color(0xFFDCD8EA),
                onSurface = Color(0xFF1C1832),
                onSurfaceVariant = Color(0xFF5D5671)
            )) { PantallaTareas(vm) }
        }
    }
}
