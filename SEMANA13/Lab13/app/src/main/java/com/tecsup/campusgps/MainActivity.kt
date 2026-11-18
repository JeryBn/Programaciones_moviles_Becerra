package com.tecsup.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModelProvider
import com.tecsup.campusgps.data.LugaresRepositoryLocal
import com.tecsup.campusgps.data.UbicacionRepositoryGoogle
import com.tecsup.campusgps.ui.PantallaMapa
import com.tecsup.campusgps.viewmodel.CampusViewModelFactory
import com.tecsup.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val barras = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        enableEdgeToEdge(statusBarStyle = barras, navigationBarStyle = barras)
        val factory = CampusViewModelFactory(
            UbicacionRepositoryGoogle(applicationContext), LugaresRepositoryLocal()
        )
        val vm = ViewModelProvider(this, factory)[MapaViewModel::class.java]
        setContent {
            MaterialTheme(colorScheme = darkColorScheme(
                primary = Color(0xFFD5F57D),
                onPrimary = Color(0xFF1D290B),
                primaryContainer = Color(0xFF263821),
                onPrimaryContainer = Color(0xFFE3F7CE),
                secondaryContainer = Color(0xFF304321),
                onSecondaryContainer = Color(0xFFE0F3C1),
                background = Color(0xFF0C1417),
                surface = Color(0xFF142125),
                surfaceVariant = Color(0xFF253438),
                onSurface = Color(0xFFF2F7F4),
                onSurfaceVariant = Color(0xFFBACBC7),
                outline = Color(0xFF839A95),
                outlineVariant = Color(0xFF34474A),
                errorContainer = Color(0xFF482927),
                onErrorContainer = Color(0xFFFFDAD3)
            )) { PantallaMapa(vm) }
        }
    }
}
