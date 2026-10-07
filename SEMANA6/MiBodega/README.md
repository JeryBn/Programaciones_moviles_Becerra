# Mi Bodega · App cliente · Semana 6

App cliente en Kotlin y Jetpack Compose Material 3. Productos, carrito, favoritos y pedidos son colecciones en memoria. No usa ViewModel, MVVM, Room, servidor ni pagos reales. Hay siete pantallas del flujo principal y tres secciones complementarias (Favoritos, Mis pedidos y Perfil).

## Exactamente cuatro requerimientos funcionales

| ID | Función que necesita el usuario | Resultado observable |
|---|---|---|
| RF-01 | Acceder a la app o registrar sus datos | Login valida `jery / 1234`; Crear cuenta exige nombre, teléfono y dirección y marca vacíos en rojo. Referencia es opcional. |
| RF-02 | Consultar, buscar, ordenar y marcar productos favoritos | Categoría y búsqueda se combinan; orden por precio; detalle por ID y favoritos compartidos entre catálogo, detalle y sección Favoritos. |
| RF-03 | Gestionar el carrito y confirmar una compra | Agregar, cambiar cantidad y confirmar eliminación; carrito vacío; contador; delivery o recojo con total reactivo; datos obligatorios y resumen de compra. |
| RF-04 | Consultar pedidos y perfil y ajustar la apariencia | Historial de pedidos confirmados, cuatro destinos inferiores, Switch oscuro y cierre de sesión que limpia datos. |

Scaffold, NavHost, íconos, padding, LazyColumn, LazyRow, Material 3 y degradado son decisiones técnicas para cumplir estas funciones; no son requerimientos funcionales adicionales.

## Funcionamiento completo, paso a paso

1. Iniciar: un login incorrecto muestra error y no entra al catálogo. Cuenta didáctica: usuario `jery`, contraseña `1234`.
2. Crear cuenta: enviar vacío muestra campos rojos. Completar nombre, teléfono y dirección permite ingresar. Es registro local; no crea una cuenta en un servidor. Referencia es opcional.
3. Inicio: LazyRow de categorías Todos/Bebidas/Abarrotes/Snacks; LazyColumn de tarjetas. Buscar actualiza la lista a medida que escribes y conserva el filtro de categoría. Tolera tildes, espacios y mayúsculas. Elegir precio ascendente o descendente reordena el resultado.
4. Cada corazón modifica el conjunto compartido de IDs favoritos. La pestaña Favoritos lista esos productos; el corazón del detalle modifica el mismo conjunto.
5. Ver detalle transmite `productoId` como Int por `detalle/{productoId}`. Elegir cantidad y agregar lleva al carrito. Un ID inexistente muestra mensaje, sin acceder a `first()` de una lista vacía.
6. Agregar repetidamente acumula cantidades. El badge de la topBar cuenta unidades, no clases distintas de producto.
7. Carrito: sumar/restar actualiza subtotal y total. Restar desde uno solicita la misma confirmación que eliminar. Cancelar conserva el producto; confirmar lo elimina. Carrito vacío muestra mensaje, costo cero e impide continuar.
8. Datos de entrega: nombre y teléfono obligatorios; dirección obligatoria para delivery. Recojo no exige dirección porque se usa la tienda. RadioButton determina una sola modalidad. Delivery añade S/ 4; recojo añade S/ 0. Los métodos Efectivo/Yape/Plin son demostración, no pagos reales.
9. Confirmar crea una copia de los items del carrito dentro de Pedido, guarda el historial y vacía el carrito. La pantalla muestra el número real, items, destinatario y total de ese pedido. `popUpTo("inicio")` retira los formularios completados para evitar reenvíos con Atrás.
10. Inicio, Favoritos, Mis pedidos y Perfil navegan de forma explícita; el destino actual determina el ícono resaltado. Perfil contiene el Switch oscuro. Cerrar sesión borra el estado de demostración y regresa al login.
11. NavHost usa transiciones de entrada y salida (su contenido animado cambia al navegar); `AnimatedContent` dentro de la ruta de confirmación presenta el resumen del pedido. No se duplica NavHost dentro de una animación.

## Qué archivo modificar

Los archivos están bajo `app/src/main/java/com/tecsup/mibodega/`.

| Archivo | Responsabilidad |
|---|---|
| `MainActivity.kt` | Entrada Android y contenido Compose |
| `ui/cliente/ClienteApp.kt` | Estado compartido, callbacks, rutas y paso de parámetros |
| `ui/cliente/Estructura.kt` | TopBar, bottomBar, padding y alternativa sin Scaffold |
| `ui/cliente/Listas.kt` | LazyColumn/Column y LazyRow/Row intercambiables |
| `ui/cliente/Practica.kt` | Cuatro opciones para practicar una modificación a la vez |
| `ui/cliente/modelo/ReglasCompra.kt` | Filtro combinado, subtotal y costo base |
| `ui/cliente/modelo/Pedido.kt` | Datos de pedido, validación y total según modalidad |
| `ui/cliente/screens/bienvenida/BienvenidaScreen.kt` | Login y error |
| `ui/cliente/screens/registro/RegistroScreen.kt` | Crear cuenta y campos rojos |
| `ui/cliente/screens/inicio/InicioScreen.kt` | Filtros, orden, tarjetas y corazones |
| `ui/cliente/screens/detalle/DetalleProductoScreen.kt` | Detalle y cantidad |
| `ui/cliente/screens/carrito/CarritoScreen.kt` | Lista, estado vacío y diálogo de eliminación |
| `ui/cliente/screens/entrega/DatosEntregaScreen.kt` | Formulario, modalidad y total |
| `ui/cliente/screens/confirmacion/ConfirmacionScreen.kt` | Resumen del pedido |
| `ui/cliente/screens/PedidosScreen.kt / PerfilScreen.kt` | Historial y perfil |
| `ui/theme/Theme.kt` | Esquemas Material 3 claro y oscuro |

## Prácticas seguras

Cambiar una variable de `Practica.kt` a false, ejecutar y restaurar true. Estos interruptores son ejemplos: para la sustentación hay que saber retirar físicamente el bloque correspondiente.

* **Sin Scaffold:** `USAR_SCAFFOLD = false` usa Column, barra superior, Box con `weight(1f)` y barra inferior. ClienteApp sigue proporcionando el NavHost como contenido.
* **Sin navegación:** `USAR_NAVEGACION = false` ejecuta Inicio dentro de Estructura con callbacks vacíos para cambios de destino. Se conservan filtros, favoritos y agregar al carrito; las pestañas no cambian pantalla en esta práctica. No existe NavController en esa rama.
* **Sin LazyColumn:** `USAR_LAZY_COLUMN = false` conserva tarjetas, IDs y eventos y usa Column con verticalScroll. Se mantiene altura limitada con weight para evitar medición infinita.
* **Sin LazyRow:** `USAR_LAZY_ROW = false` conserva estado de filtro y cambia solo a Row con horizontalScroll.

Para retirar Scaffold físicamente: conservar la rama Column de Estructura, quitar el if y el bloque Scaffold. Para retirar navegación: conservar la rama de práctica de ClienteApp y eliminar la rama que crea `rememberNavController`/NavHost. Para retirar listas Lazy: conservar las ramas Column/Row de Listas y quitar los bloques Lazy y sus imports. Restaurar desde Git antes del siguiente ejercicio.

## Respuestas de reflexión

El filtro reacciona porque texto y categoría son estado observable y `filtrarProductos` recibe ambos. El total se deriva de cantidades y modalidad; no hay botón recalcular. El carrito vive en ClienteApp para que todas las pantallas lean una fuente común. Un Pedido conserva una copia de los items para que vaciar el carrito no borre el historial. `navigate` agrega un destino; `popUpTo` retira destinos anteriores. NavigationBar muestra secciones frecuentes y Drawer permite más opciones globales. Los archivos de modelo y entrada necesitan menos interacción; las pantallas son el lugar donde se practican layout, controles y eventos.

## Observaciones y conclusiones

Observación 1: la barra inferior anterior solo modificaba un índice local; fue necesario conectarla a rutas reales.

Observación 2: el corazón de detalle tenía estado aislado; se elevó a ClienteApp para sincronizar todos los lugares.

Conclusión 1: conservar la misma fuente de estado evita divergencias entre catálogo, favoritos, carrito e historial.

Conclusión 2: separar estructura, navegación y listas permite modificar un componente y comprobar que los demás conservan sus funciones. Esta reparación fue asistida; no acredita la fase sin IA solicitada originalmente.

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
