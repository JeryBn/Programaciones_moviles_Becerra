# Verificación de la preparación para la evaluación

Comprobaciones realizadas el 6 de octubre de 2026 en Windows, Android Studio JBR y emulador Medium_Phone con Android 17 (API 37). Se ejecutaron assembleDebug, testDebugUnitTest, connectedDebugAndroidTest y lintDebug. Los resultados completos de Lab05 y TECSUP Store se conservaron antes de repetir una prueba para capturas.

| Proyecto | APK debug | Pruebas unitarias | Pruebas en Android | Lint |
|---|---|---|---|---|
| Lab05 | Correcta | 1 / 1 | 5 / 5 | 0 errores, 17 advertencias |
| TECSUP Fit | Correcta | 5 / 5 | 7 / 7 | 0 errores, 5 advertencias |
| TECSUP Store | Correcta | 2 / 2 | 6 / 6 | 0 errores, 17 advertencias |
| Mi Bodega | Correcta | 8 / 8 | 8 / 8 | 0 errores, 19 advertencias |

Las cantidades incluyen las pruebas de ejemplo originales. Cero errores de lint no significa cero advertencias. Las advertencias no bloquearon la compilación.

## Alcance comprobado

- Lab05: parámetro Int, navegación y regreso; variantes sin Scaffold, sin navegación y sin LazyColumn.
- TECSUP Store: favoritos compartidos entre menú/drawer, contador y destino; variantes sin Scaffold, navegación o listas Lazy. Compartir abre el selector Android; no se envió contenido a terceros.
- TECSUP Fit: reserva secuencial, una clase agendada, cancelación de mejora-ia, estadísticas y variantes de práctica.
- Mi Bodega: acceso y formularios; carrito, eliminación y recojo con total; pedido e historial; favoritos compartidos; modo oscuro y variantes sin Scaffold, navegación o listas Lazy.
- Capturas nuevas de Lab05, drawer de Store, confirmación y perfil oscuro de Mi Bodega.

## Límites que deben conservarse al sustentar

La batería completa se ejecutó sobre la preparación basada en mejora-ia. Las correcciones comunes se trasladan a main; TECSUP Fit mantiene en main su versión anterior sin cancelación. Su variante principal no se presenta como una nueva ejecución independiente de esta batería.

Los interruptores de Practica.kt prueban alternativas preparadas. Una eliminación física distinta exige volver a compilar y comprobar el flujo. La guía de práctica explica ambos procedimientos.

Las aplicaciones son demostraciones locales: no prueban servidor, persistencia definitiva, pagos, funcionamiento en todos los teléfonos ni las futuras modificaciones del alumno. No se realizó ninguna entrega en Canvas. La revisión de módulos usó Canvas y las guías locales del mismo nombre; la descarga actual del PDF en el navegador estuvo bloqueada y no se verificó identidad binaria entre ambas copias.

Los requisitos históricos de días distintos, fase sin IA, fecha de entrega y sustentación personal no se subsanan con commits de esta fecha. La asistencia y sus cambios se declaran en PROMPTS-EVALUACION.md. No se garantiza una calificación de 100 %.
