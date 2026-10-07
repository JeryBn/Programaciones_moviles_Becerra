package com.tecsup.mibodega.ui.cliente.modelo

import com.tecsup.mibodega.R

/**
 * Datos de ejemplo (fake) para mostrar la UI sin base de datos.
 * Cuando conecten Room o una API, este archivo se reemplaza por
 * un Repository real, pero las pantallas no cambian porque ya
 * reciben una List<Producto> como parÃ¡metro.
 */
val listaCategorias = listOf("Todos", "Bebidas", "Abarrotes", "Snacks")

val listaProductosFake = listOf(
    Producto(
        id = 1,
        nombre = "Arroz CosteÃ±o",
        descripcion = "Arroz extra, grano largo, ideal para el dÃ­a a dÃ­a.",
        precio = 4.50,
        categoria = "Abarrotes",
        imagenRes = R.drawable.arroz_costeno
    ),
    Producto(
        id = 2,
        nombre = "Aceite Primor",
        descripcion = "Aceite vegetal Primor Premium, botella de 900 ml.",
        precio = 8.90,
        categoria = "Abarrotes",
        imagenRes = R.drawable.aceite_primor
    ),
    Producto(
        id = 3,
        nombre = "Leche Gloria",
        descripcion = "Leche evaporada entera Gloria, lata de 390 g.",
        precio = 5.20,
        categoria = "Abarrotes",
        imagenRes = R.drawable.leche_gloria
    ),
    Producto(
        id = 4,
        nombre = "Galleta Oreo",
        descripcion = "Galletas de chocolate con crema de vainilla, paquete de 135 g.",
        precio = 3.50,
        categoria = "Snacks",
        imagenRes = R.drawable.galletas_oreo
    ),
    Producto(
        id = 5,
        nombre = "Coca-Cola Original",
        descripcion = "Bebida gaseosa sabor cola, botella de 1,5 L.",
        precio = 6.50,
        categoria = "Bebidas",
        imagenRes = R.drawable.coca_cola
    )
)

