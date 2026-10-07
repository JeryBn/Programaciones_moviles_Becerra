# Laboratorio 06 · TECSUP Store

Material 3, menú contextual por producto y NavigationDrawer que envuelve una estructura de pantalla. Estado local, sin MVVM ni base de datos.

## Exactamente cuatro requerimientos funcionales

| ID | Función del usuario | Comprobación |
|---|---|---|
| RF-01 | Consultar productos por categoría y abrir su detalle | Todos/Tecnología/Accesorios filtran; detalle recibe el ID seleccionado. |
| RF-02 | Marcar favoritos, compartir o reportar un producto | Menú de tres puntos con tres acciones, íconos y divisores; compartir abre el selector Android; reportar pide confirmación y muestra resultado local. |
| RF-03 | Acceder a las secciones y consultar sus favoritos y pedidos de práctica | Drawer con Inicio, Mis pedidos, Favoritos, Perfil y Cerrar sesión; selección activa y badge derivado del conjunto de favoritos. |
| RF-04 | Consultar perfil y reiniciar la sesión de demostración | Datos básicos, estadísticas y cierre con confirmación que limpia favoritos y pedidos. |

## Funcionamiento

1. Inicio presenta productos en LazyColumn y categorías en LazyRow. Una categoría cambia el conjunto visible.
2. Cada tarjeta tiene ⋮. El DropdownMenu se ancla al mismo Box del IconButton, con estado expanded local por tarjeta.
3. Favoritos añade o retira el ID de un Set elevado a TecsupStoreApp. El drawer lee el tamaño del mismo Set; no guarda otro contador. Muestra incluso cero y tiene descripción accesible.
4. Compartir crea un Intent ACTION_SEND con nombre y precio. Abre el selector: el usuario elige el destino y decide el envío.
5. Reportar abre AlertDialog. Cancelar no registra nada; confirmar muestra aviso de reporte local. No se afirma que el reporte se haya enviado a un servidor.
6. ☰ abre el drawer. El encabezado muestra JB, nombre e institución. Cada ítem llama explícitamente a navigate, cierra el drawer y resalta según la ruta actual.
7. Favoritos muestra solo productos marcados. Ver detalle transmite un productoId Int. Desde detalle puede confirmarse un pedido de práctica y consultarlo en Mis pedidos.
8. Perfil muestra datos y estadísticas. Cerrar sesión pide confirmar el reinicio de los datos de demostración. Cancelar conserva el estado.

## Mapa del código

Paquete principal: `app/src/main/java/com/example/lab05/`.

| Archivo | Uso |
|---|---|
| `TecsupStoreApp.kt` | Estado común, NavHost, acciones de compartir/reportar, pedidos y diálogos |
| `StoreDatos.kt` | Catálogo y toggleFavorito |
| `TarjetaProducto.kt` | Tarjeta, Box, ícono ⋮, DropdownMenu y callbacks |
| `AppDrawer.kt` | Encabezado, destinos, íconos, selección y badge |
| `StoreEstructura.kt` | TopBar, contenido y padding; Scaffold o Column |
| `StoreListas.kt` | Categorías y productos, alternativas Lazy/normal |
| `Practica.kt` | Cuatro variables de práctica |

Los archivos HomeScreen/ListScreen/AppNavigation anteriores se conservan como referencia del laboratorio previo; MainActivity ejecuta TecsupStoreApp.

## Retirar componentes sin romper

* USAR_SCAFFOLD = false: StoreEstructura cambia a Column. Drawer y NavHost permanecen fuera de esa decisión.
* USAR_NAVEGACION = false: se muestra StoreLista fija dentro de la estructura; no se crea NavHost. Menú contextual y favoritos locales siguen disponibles. Los destinos del drawer quedan sin cambio de pantalla durante esta práctica.
* USAR_LAZY_COLUMN = false: lista vertical con Column/verticalScroll y las mismas TarjetaProducto.
* USAR_LAZY_ROW = false: filtros con Row/horizontalScroll; se mantiene categoria y su comparación.

Retirada física: conservar la rama alternativa, quitar el bloque original y su if; revisar imports y referencias. Para quitar drawer, conservar StoreEstructura y su contenido; retirar ModalNavigationDrawer/AppDrawer y ajustar el botón ☰. Para quitar solo DropdownMenu, conservar la tarjeta y su botón de detalle.

## Reflexión

Box une ancla y menú; expanded controla solo el menú de una tarjeta. Sus acciones reciben el producto o ID, mientras el drawer cambia secciones globales. El badge se sincroniza gracias al estado elevado. La reparación corrigió acciones vacías, destinos de texto y código concentrado en un archivo. Esas correcciones deben explicarse de forma honesta como asistencia recibida.

## Observaciones y conclusiones

Observación 1: Compartir y Reportar antes cerraban el menú sin una acción visible; ahora cada uno tiene resultado comprobable.

Observación 2: las secciones antes eran textos de demostración; se añadieron rutas, detalle y datos locales de pedidos.

Conclusión 1: el estado local expanded sirve para la tarjeta; favoritos necesita estado compartido para que el badge refleje los cambios.

Conclusión 2: separar Drawer, estructura y navegación facilita retirar Scaffold conservando rutas. Los commits actuales documentan reparación asistida y no reconstruyen una fase sin IA.

## Autoría y fases de Git

La preparación del 6 de octubre de 2026 se realizó con asistencia de IA solicitada por el estudiante. Los commits registran modificaciones reales con sus fechas reales. No acreditan una fase sin IA ni reemplazan una sustentación individual. No se cambiaron fechas, autores ni el historial anterior.

La guía exige fases en `main` y `mejora-ia`. Se conserva el historial de ambas. La reparación actual no puede demostrar retroactivamente trabajo sin IA ni commits distribuidos en días anteriores. El docente debe valorar esa diferencia. Los prompts de esta preparación se registran en `PROMPTS-EVALUACION.md` en la raíz.

## Ejecutar y comprobar

Abrir la carpeta de este proyecto Gradle en Android Studio, sincronizar, seleccionar el JDK integrado y un emulador API 24 o superior. La configuración usa SDK 37, AGP 9.3.3 y Gradle 9.5.0. La ruta local del SDK se configura en `local.properties` o en `ANDROID_HOME`; no se versiona.

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest
.\gradlew.bat connectedDebugAndroidTest lintDebug
```

La segunda comprobación necesita un dispositivo. Consultar `VERIFICACION-EVALUACION.md` en la raíz para los resultados reales de esta preparación. Los APK y reportes se generan en `app/build/`; no se suben como código fuente.
