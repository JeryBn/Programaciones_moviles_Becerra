package com.Becerra.lab04carritotecsup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.input.InputTransformation.Companion.keyboardOptions

data class Producto (
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
    val subtotal = productos.sumOf {it. precio * it.cantidad}
    val igv = subtotal * 0.18
    val total = subtotal + igv

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it},
            label = { Text("nombre del producto") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = precio,
                onValueChange = {precio = it},
                label = { Text("precio (S/)")},
                modifier = Modifier.weight(1f),
                keyboardOptions = keyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

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
        ){
            Text ("AGREGAR", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))
        if (productos.isEmpty()){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment . Center
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "tu carrito esta vacio",
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
        }








    }
}


