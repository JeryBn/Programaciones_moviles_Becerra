# Registro de asistencia con IA

La rama `mejora-ia` registra de forma transparente la asistencia utilizada después de consolidar la fase base en `main`.

## Prompt 1 - Busqueda en tiempo real

> Revisa el filtro de productos de Mi Bodega. Debe combinar categoria y texto en tiempo real, conservar una sola fuente de datos y ser tolerante a mayusculas, espacios y tildes. Propón una funcion pura que pueda probarse sin Android.

Resultado aplicado: se extrajo `filtrarProductos`, se normalizo el texto mediante `Normalizer` y se agregaron pruebas para la combinacion de criterios y para palabras acentuadas.

Correccion humana: se mantuvo la categoria como comparacion exacta porque sus valores provienen de una lista controlada; solo el texto libre se normaliza.

## Prompt 2 - Badge de favoritos

> Revisa el contador de Favoritos del NavigationDrawer. Debe depender del mismo estado que modifica cada DropdownMenu y comunicar su cantidad a lectores de pantalla sin duplicar el estado.

Resultado aplicado: el badge sigue leyendo el conjunto elevado de identificadores y ahora expone una descripcion accesible con la cantidad de productos favoritos.

Correccion humana: no se creo un contador independiente; el tamaño del conjunto se deriva en cada recomposicion para evitar inconsistencias.

## Prompt 3 - Verificacion de la solucion

> Audita la Semana 6 contra la guia: dos proyectos, siete pantallas de Mi Bodega, drawer, menus contextuales, filtros combinados, carrito reactivo, reflexiones, observaciones, conclusiones, pruebas y compilacion. Señala errores antes de publicar.

Resultado aplicado: se completaron las rutas de entrega y confirmacion, se actualizo la toolchain incompatible con el JDK instalado y se ejecutaron `testDebugUnitTest` y `assembleDebug` en ambos proyectos.

Correccion humana: las advertencias de iconos obsoletos no impiden compilar, pero se registran como una mejora tecnica futura; no se presentaron como errores resueltos.

## Alcance de la asistencia

La IA apoyo la implementacion, revision y documentacion. Las aplicaciones no usan servicios externos ni credenciales, y conservan todos sus datos en memoria conforme al alcance academico de la guia.
