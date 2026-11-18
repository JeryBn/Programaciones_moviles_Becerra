package com.tecsup.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
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
        enableEdgeToEdge()
        val factory = CampusViewModelFactory(
            UbicacionRepositoryGoogle(applicationContext), LugaresRepositoryLocal()
        )
        val vm = ViewModelProvider(this, factory)[MapaViewModel::class.java]
        setContent {
            MaterialTheme(colorScheme = lightColorScheme(
                primary = Color(0xFF174C86),
                primaryContainer = Color(0xFFD9EAFF),
                background = Color(0xFFEDF4FB),
                surface = Color(0xFFFDFEFF),
                surfaceVariant = Color(0xFFE0EAF5),
                onSurface = Color(0xFF102840)
            )) { PantallaMapa(vm) }
        }
    }
}
