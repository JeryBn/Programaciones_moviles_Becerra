package com.tecsup.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tecsup.centrotareas.data.TareasRepositoryLocal
import com.tecsup.centrotareas.ui.PantallaTareas
import com.tecsup.centrotareas.viewmodel.TareasViewModel
import com.tecsup.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val vm = ViewModelProvider(this, TareasViewModelFactory(TareasRepositoryLocal()))[TareasViewModel::class.java]
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Color(0xFFFFCB80),
                onPrimary = Color(0xFF332000),
                background = Color(0xFF13131D),
                surface = Color(0xFF212130),
                surfaceVariant = Color(0xFF303043),
                outline = Color(0xFF918BA6),
                onSurface = Color(0xFFF8F1FF),
                onSurfaceVariant = Color(0xFFD6CFE5)
            )) { PantallaTareas(vm) }
        }
    }
}
