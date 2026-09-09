# Laboratorio 03 - Registro de Producto
**Nombre:** Jery Becerra Ninaquispe

## Descripción

Aplicación Android desarrollada con Kotlin y Jetpack Compose para el registro de productos.

La aplicación permite ingresar el nombre de un producto, su precio y cantidad. Al presionar el botón **"AGREGAR PRODUCTO"**, se muestra un resumen con los datos ingresados y se calcula automáticamente el importe total del producto.

## Tecnologías utilizadas

* Kotlin
* Android Studio
* Jetpack Compose
* Material 3

<img width="974" height="588" alt="image" src="https://github.com/user-attachments/assets/9c722f90-cb94-416d-9b0b-4a6ece81d802" />
<img width="645" height="1344" alt="image" src="https://github.com/user-attachments/assets/46e18df3-3d52-4a75-a5f6-8c144d2f6fba" />

## ¿Qué ocurre sin `remember`?

Las variables comunes se vuelven a crear cuando Compose recompone la pantalla, por lo que no conservan el valor que el usuario escribió. `remember` mantiene el estado durante las recomposiciones; por eso los `OutlinedTextField` pueden mostrar y actualizar correctamente el nombre, el precio y la cantidad.
