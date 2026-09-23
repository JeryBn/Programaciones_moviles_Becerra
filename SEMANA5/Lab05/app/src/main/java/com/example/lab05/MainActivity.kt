package com.example.lab05 // Asegúrate de que esto coincida con TU paquete

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
// ... tus imports ...

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Aquí es donde "enchufas" toda la aplicación
            AppNavigation()
        }
    }
}