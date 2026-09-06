package com.becerra.lab03registroproducto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.becerra.lab03registroproducto.ui.theme.Lab03RegistroProductoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Lab03RegistroProductoTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    PantallaRegistro(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun PantallaRegistro(modifier: Modifier = Modifier) {

    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }
    var mostrarResumen by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf<String?>(null) }

    // Un cambio en los campos requiere volver a validar antes de mostrar la Card.
    fun ocultarResultado() {
        mostrarResumen = false
        mensajeError = null
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // REGLA 2: Jerarquía tipográfica
        Text(
            text = "Nuevo producto",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Completa los datos y presiona Agregar",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.height(16.dp))

        // REGLA 4: Campo largo a ancho completo
        OutlinedTextField(
            value = nombre,
            onValueChange = {
                nombre = it
                ocultarResultado()
            },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // REGLA 4: Precio y cantidad comparten una fila
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedTextField(
                value = precio,
                onValueChange = {
                    precio = it
                    ocultarResultado()
                },
                label = { Text("Precio (S/)") },
                modifier = Modifier.weight(1f)
            )

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField(
                value = cantidad,
                onValueChange = {
                    cantidad = it
                    ocultarResultado()
                },
                label = { Text("Cantidad") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // REGLA 3: Color principal del tema
        Button(
            onClick = {
                val precioNum = precio.trim().toDoubleOrNull()
                val cantidadNum = cantidad.trim().toIntOrNull()
                mensajeError = when {
                    nombre.isBlank() || precio.isBlank() || cantidad.isBlank() ->
                        "Completa nombre, precio y cantidad."
                    precioNum == null || !precioNum.isFinite() || precioNum <= 0.0 ->
                        "Ingresa un precio válido mayor que cero (ejemplo: 12.50)."
                    cantidadNum == null || cantidadNum <= 0 ->
                        "Ingresa una cantidad entera mayor que cero."
                    !(precioNum * cantidadNum).isFinite() ->
                        "El importe es demasiado grande. Reduce el precio o la cantidad."
                    else -> null
                }
                mostrarResumen = mensajeError == null
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("AGREGAR PRODUCTO")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                nombre = ""
                precio = ""
                cantidad = ""
                ocultarResultado()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Limpiar")
        }

        Spacer(modifier = Modifier.height(16.dp))

        mensajeError?.let { error ->
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        if (mostrarResumen) {

            val precioNum = precio.trim().toDoubleOrNull() ?: 0.0
            val cantidadNum = cantidad.trim().toIntOrNull() ?: 0
            val importe = precioNum * cantidadNum

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = nombre.trim(),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Precio: S/ " +
                                String.format("%.2f", precioNum),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Text(
                        text = "Cantidad: $cantidadNum",
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Importe: S/ " +
                                String.format("%.2f", importe),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
