package com.Becerra.lab04carritotecsup

import android.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val icons: Any
    get() {
        TODO()
    }

// NOTA: Es recomendable mover esta data class a su propio archivo Producto.kt
data class Producto(
    val nombre: String,
    val precio: Double,
    val cantidad: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaCarrito(modifier: Modifier = Modifier) {
    var nombre by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }
    var cantidad by remember { mutableStateOf("") }

    val productos = remember { mutableStateListOf<Producto>() }

    val subtotal = productos.sumOf { it.precio * it.cantidad }
    val igv = subtotal * 0.18
    val total = subtotal + igv

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Campo Nombre
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre del producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Fila Precio y Cantidad (Faltaba la cantidad)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio (S/)") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            OutlinedTextField(
                value = cantidad,
                onValueChange = { cantidad = it },
                label = { Text("Cantidad") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón Agregar
        Button(
            onClick = {
                val precioNum = precio.toDoubleOrNull() ?: 0.0
                val cantidadNum = cantidad.toIntOrNull() ?: 0

                if (nombre.isNotBlank() && precioNum > 0 && cantidadNum > 0) {
                    productos.add(Producto(nombre.trim(), precioNum, cantidadNum))
                    nombre = ""
                    precio = ""
                    cantidad = ""
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text("AGREGAR", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Estado Vacío vs LazyColumn (Mayúscula corregida)
        if (productos.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Tu carrito está vacío",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Agrega tu primer producto",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productos) { producto ->
                    TarjetaProducto(
                        producto = producto,
                        onEliminar = { productos.remove(producto) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Panel de Totales (Estructura interna corregida)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFF2F0F7),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Productos: ${productos.size}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )

                if (productos.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Subtotal", fontSize = 14.sp)
                        Text("S/ %.2f".format(subtotal), fontSize = 14.sp)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("IGV (18%)", fontSize = 14.sp)
                        Text("S/ %.2f".format(igv), fontSize = 14.sp)
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("TOTAL", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(
                        text = "S/ %.2f".format(if (productos.isEmpty()) 0.0 else total),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
fun TarjetaProducto(
    producto : Producto,
    onEliminar: () -> Unit
){
    Card(
        modifier = Modifier.padding(16dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = producto.nombre,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "S/ %.2f x %d". format(producto.precio, producto.cantidad),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }
        Text(
            text = "S/ %.2F".format(producto.precio * producto.cantidad),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 8.dp)
        )
        IconButton(onClick = onEliminar) {
            Icon (
                imageVector = Icons.Default.Delete,
                contentDescription = "Eliminar",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

