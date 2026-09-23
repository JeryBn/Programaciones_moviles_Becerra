# TECSUP Fit · Actividad integradora de semanas 1 a 6

Aplicación Android de reserva de clases, desarrollada con Kotlin, Jetpack Compose y Material 3. Usa estado local con `mutableStateOf` y `rememberSaveable`, sin ViewModel, MVVM, servidor ni base de datos.

> **Autoría y alcance:** esta base se desarrolló desde cero con asistencia de IA autorizada y registrada en [PROMPTS.md](PROMPTS.md). No se presenta como fase realizada sin IA. Los commits corresponden a cambios reales de esta sesión y mantienen sus fechas reales. La exigencia de trabajo sin IA y distribución en varios días de la guía no queda acreditada por este desarrollo.

## Cuatro requerimientos funcionales

Los detalles de la guía se agrupan en exactamente cuatro requerimientos. Scaffold, Material 3, padding, degradados y listas son técnicas de implementación; no se enumeran como requerimientos adicionales.

| ID | Requerimiento funcional | Comprobación |
|---|---|---|
| RF-01 | Consultar las clases disponibles, filtrarlas por Hoy o Esta semana y consultar su detalle. | Esta semana muestra cuatro clases; Hoy excluye Cardio Dance y muestra tres. Tocar una clase abre su nombre, descripción y horarios. |
| RF-02 | Reservar una clase seleccionando un único horario y consultar la confirmación. | El botón permanece deshabilitado sin selección. Cada clase ofrece tres horarios. El resumen corresponde a la clase y horario elegidos; se rechazan duplicados. |
| RF-03 | Consultar las reservas y distinguir su estado. | Reservas muestra Confirmada o Completada con texto y colores diferentes. En `mejora-ia` también permite cancelar con confirmación y muestra Cancelada. |
| RF-04 | Consultar el perfil, sus estadísticas y una rutina orientativa mediante las secciones de la aplicación. | Perfil calcula las clases completadas y las reservas confirmadas; Rutinas muestra una secuencia de demostración. |

## Abrir localmente

1. Abrir **esta carpeta**, `SEMANA5/ActividadIntegradora`, desde **File > Open** en Android Studio.
2. Configurar el Gradle JDK incluido con Android Studio y esperar la sincronización.
3. Tener Android SDK 37 instalado y un emulador con Android API 24 o superior.
4. Seleccionar `app`, el dispositivo y **Run**.
5. Para el laboratorio anterior, abrir `SEMANA5/Lab05` en otra ventana.

Configuración reproducible: AGP 9.3.3, Gradle 9.5.0, compilador Compose 2.2.10, Compose BOM 2026.02.01 y Navigation Compose 2.10.1, compatibles con las dependencias del laboratorio existente. El Kotlin de Android lo gestiona AGP. `local.properties` identifica el SDK de cada PC y no se sube a GitHub.

```powershell
.\gradlew.bat assembleDebug testDebugUnitTest
.\gradlew.bat connectedDebugAndroidTest lintDebug
```

El segundo comando requiere un emulador o teléfono conectado. El APK se genera en `app/build/outputs/apk/debug/app-debug.apk` y no se versiona.

## Funcionamiento paso a paso

1. **Inicio:** observa el degradado, las tarjetas y los chips Hoy / Esta semana. Desplaza la lista para ver todas las clases.
2. **Detalle:** toca Yoga Flow. La navegación transmite `claseId=1`; la pantalla recupera la clase del catálogo.
3. **Horario:** pulsa Reservar cupo. Selecciona uno de los tres horarios. Solo se guarda un texto en `horario`; elegir otro sustituye al anterior.
4. **Confirmación:** pulsa Confirmar reserva. Se valida la selección, se añade una reserva y se navega con su ID al resumen.
5. **Reservas:** pulsa Ver mis reservas. La nueva reserva aparece junto a una reserva completada de demostración.
6. **Navegación secundaria:** usa Inicio, Reservas, Rutinas y Perfil. Cada destino tiene un icono diferente y se resalta la pestaña correspondiente.
7. **Volver:** en pantallas secundarias la flecha llama a `popBackStack()`. Tras confirmar se retira el formulario de la pila para evitar reenviarlo al volver.

Los horarios Hoy / Mañana son datos didácticos, no un calendario real. Las reservas sobreviven a la recreación de la Activity mediante `rememberSaveable`; no tienen persistencia permanente tras una nueva sesión. Se incluye una reserva completada de ejemplo para mostrar los dos estados y las estadísticas. No se implementa gestión real de aforo multiusuario.

## Mapa de archivos: qué modificar

Todos los archivos Kotlin están en `app/src/main/java/com/becerra/tecsupfit/`.

| Archivo | Responsabilidad | Cambio típico del profesor |
|---|---|---|
| `MainActivity.kt` | Arranque y tema Material 3 | Cambiar colores del tema |
| `Datos.kt` | Clases, reservas y validación | Añadir una clase o evitar duplicados |
| `Estructura.kt` | TopAppBar, NavigationBar, Scaffold y alternativa con Column | Quitar Scaffold, iconos, títulos o padding |
| `Listas.kt` | Inicio, filtros y listas intercambiables | Sustituir LazyColumn / LazyRow |
| `Pantallas.kt` | Detalle, horario, confirmación, reservas, rutinas y perfil | Añadir o retirar un control |
| `Navegacion.kt` | NavHost, rutas, parámetros y pila | Añadir un destino o modificar el paso de ID |
| `FitApp.kt` | Estado compartido y elección del modo de ejecución | Explicar recomposición y conservación del estado |
| `Practica.kt` | Cuatro constantes para ensayar variaciones | Desactivar un componente y ejecutar |

## Explicación para la sustentación

**Scaffold:** organiza topBar, contenido y bottomBar. No cambia de pantalla por sí mismo. `Estructura` recibe el contenido y las acciones como parámetros; no depende de NavController.

**Navegación:** `NavHost` relaciona cada ruta con un composable. Una pulsación ejecuta explícitamente `nav.navigate("detalle/$id")`. `navArgument("claseId")` declara que el parámetro es entero. `currentBackStackEntryAsState()` permite observar la pantalla actual; al cambiar, Compose actualiza la pestaña seleccionada.

**Recorrido del dato:** tarjeta → ID de clase → `detalle/{claseId}` → `horario/{claseId}` → registro de `Reserva(claseId, horario)` → `confirmacion/{reservaId}` → búsqueda de esa reserva para mostrar el resumen. Se pasan identificadores y se consulta una sola fuente de datos.

**Padding:** `innerPadding` viene de Scaffold y reserva el espacio de las barras. `Modifier.padding(innerPadding)` lo aplica una sola vez; `consumeWindowInsets` informa a los hijos de que ese espacio ya se atendió. El padding de 20.dp es separación visual del contenido. Confundirlos puede esconder contenido o duplicar espacios.

**LazyColumn / LazyRow:** muestran elementos verticales / horizontales de forma diferida según se necesitan. `Column` / `Row` componen todos sus hijos; necesitan `verticalScroll` / `horizontalScroll` para desplazarse. Para pocos elementos sirven como sustitución didáctica. No anidar una LazyColumn sin altura limitada dentro de otra lista vertical desplazable.

**Estado y recomposición:** `mutableStateOf` produce estado observable; cuando cambia, Compose actualiza las partes que lo leen. `rememberSaveable` lo conserva al recrear la Activity. El Saver convierte las reservas a valores que Android puede guardar; no equivale a una base de datos.

**Selección única:** todos los chips comparan su valor contra la misma variable `horario`. La asignación `horario = opcion` reemplaza la selección anterior; no existen tres booleanos independientes.

**Material 3 y degradado:** los controles vienen de `androidx.compose.material3`. Los vectores de iconos se importan desde `androidx.compose.material.icons`; eso no convierte los controles a Material 2. El degradado se dibuja mediante `Brush.verticalGradient`, no es un parámetro especial de Material 3.

## Prácticas guiadas: cambiar sin romper

Primero ejecutar la aplicación original. Después cambiar **una sola cosa**, ejecutar, verificar y explicar. Restaurar la versión base antes de pasar al siguiente ejercicio. Las constantes son una primera comprobación; la evaluación puede exigir retirar físicamente el código.

### 1. Quitar Scaffold y conservar navegación

En `Practica.kt`, poner `USAR_SCAFFOLD = false`. Se ejecuta la alternativa de `Estructura`: `Column` → `BarraSuperior` → `Box(weight(1f))` → `BarraInferior`. Probar Inicio → Detalle → Horario → Confirmación → Reservas.

Para retirarlo físicamente: en `Estructura.kt` conservar únicamente el bloque `Column` de la rama `else`, retirar el bloque `Scaffold` y su condicional. Conservar la llamada `contenido()` y los callbacks. Se puede dejar temporalmente `usarScaffold` sin usar o retirarlo junto con sus argumentos en todas las llamadas.

**Respuesta oral:** la navegación continúa porque el NavHost está dentro del contenido recibido; Scaffold solo organizaba el espacio.

### 2. Quitar navegación y conservar Scaffold

Poner `USAR_NAVEGACION = false` y `USAR_SCAFFOLD = true`. Se ve el inicio, las barras y filtros; las pestañas y tarjetas no cambian de pantalla en este modo.

Para retirarla físicamente: en `FitApp.kt` conservar solo la llamada `Estructura` de la rama `else`, con `Inicio({}, usarLazyColumn, usarLazyRow)`. Retirar la llamada a `Navegacion`; después puede retirarse `Navegacion.kt` y la dependencia navigation-compose si ya no se usa. No borrar `Estructura`, las listas ni el tema.

**Respuesta oral:** Scaffold puede mostrar contenido fijo sin un controlador de navegación.

### 3. Quitar LazyColumn

Poner `USAR_LAZY_COLUMN = false`. La lista utiliza `Column`, `verticalScroll` y `forEach` con las mismas tarjetas. Probar desplazamiento, detalle y reservas.

Para retirarla físicamente: en `ListaVertical`, conservar únicamente la rama `Column`; retirar el bloque `LazyColumn`, el condicional y los imports sin uso. Las pantallas siguen llamando a `ListaVertical`. Mantener el alto disponible de la lista dentro de Inicio con `weight(1f)`.

### 4. Quitar LazyRow

Poner `USAR_LAZY_ROW = false`. Los chips usan `Row(horizontalScroll(...))`.

Para retirarla físicamente: en `Filtros`, conservar solo la rama `Row`. Mantener `soloHoy` y `seleccionar`, que controlan el filtrado; quitar LazyRow no debe eliminar la función del filtro.

### 5. Volver a añadir las listas Lazy

Restablecer las constantes a `true` para comparar. Si se retiraron físicamente, reconstruir `LazyColumn { items(elementos, key = clave) { tarjeta(it) } }` y `LazyRow { items(filtros, key = { it.second }) { chip(it) } }`, conservar espaciados y límites de tamaño.

### 6. Agregar una clase

Añadir un `Clase` a `clases` en `Datos.kt`, con ID distinto y tres horarios. No hace falta crear otra pantalla ni otra ruta: Detalle recibe el nuevo ID. Verificar ambos filtros y reservarla.

### 7. Agregar o retirar una pestaña

Agregar el `Destino` con su icono y una ruta única en `destinos`, añadir su `composable` en NavHost e implementar la pantalla. Para retirar Rutinas, eliminar su Destino y su `composable`; la función `Rutinas` puede retirarse cuando ya no tenga referencias. Volver a dejar las cuatro pestañas para cumplir la guía.

### 8. Retirar una barra

Quitar `topBar` o `bottomBar` de Scaffold y ejecutar. El contenido recibe un `innerPadding` diferente. La navegación secuencial seguirá funcionando mientras existan sus botones y NavHost. En el modo sin Scaffold, retirar también la llamada correspondiente de la Column si se busca el mismo resultado.

## Posibles pedidos del profesor

Son ejercicios probables por los temas indicados, no preguntas confirmadas.

| Pedido | Qué demostrar |
|---|---|
| Cambiar el icono de Reservas | Cambiar solo `Icons.Default.DateRange` y mantener su ruta |
| Cambiar el orden de las pestañas | Reordenar `destinos`, sin confundir nombre con ruta |
| Añadir un botón de inicio en Detalle | Pasar un callback y navegar a inicio controlando la pila |
| Cambiar el título según pantalla | Modificar el `when` que calcula `titulo` |
| Mostrar ID recibido en Detalle | Añadir un Text con `clase.id` y verificar varias tarjetas |
| Impedir confirmar sin horario | Explicar y conservar `enabled = puedeReservar(...)` |
| Permitir solo una selección | Una variable con el valor elegido, no varios booleanos |
| Corregir contenido oculto bajo barras | Aplicar innerPadding una sola vez |
| Cambiar orientación | Comprobar selección y reservas conservadas |
| Quitar un filtro | Cambiar lista de chips y revisar el estado inicial |
| Vaciar la lista de reservas | Mostrar estado vacío sin acceder a una posición inexistente |
| Retirar Scaffold o NavHost | Aplicar los ejercicios independientes anteriores |
| Añadir confirmación de cancelación | Separar solicitar, descartar y confirmar la acción |
| Explicar remember frente a rememberSaveable | Recomposición frente a recreación de la Activity |

## Preparación paso a paso para mañana

1. **15 minutos:** ejecutar los dos proyectos e identificar MainActivity y las rutas.
2. **20 minutos:** explicar cada archivo de TECSUP Fit con el mapa anterior; seguir un ID desde Inicio hasta Confirmación.
3. **25 minutos:** realizar los cuatro ejercicios de retirada, uno a la vez, y restaurar cada cambio.
4. **20 minutos:** añadir una clase, cambiar un icono y quitar/restaurar Rutinas.
5. **15 minutos:** practicar selección única, padding, giro de pantalla y prevención de duplicados.
6. **10 minutos:** explicar qué ayuda dio la IA y qué se verificó. No memorizar frases sobre código que no puedas localizar.

## Git y evidencia de desarrollo

`main` contiene la base asistida. `mejora-ia` se deriva de ella y añade cancelación con AlertDialog dentro de RF-03. El historial documenta organización, configuración, datos, interfaz, pantallas, navegación, pruebas y documentación, no simula trabajo sin IA ni fechas anteriores.

Para tus prácticas, crear una rama propia y realizar commits después de cada cambio entendido y comprobado:

```powershell
git switch -c practica-sustentacion
# Hacer un cambio, ejecutar y verificar.
git add SEMANA5/ActividadIntegradora
git commit -m "Practica: sustituye LazyRow por Row y comprueba filtros"
git log --oneline --graph --all
```

No subir `.gradle`, `build`, `local.properties`, credenciales ni claves. Consultar [VERIFICACION.md](VERIFICACION.md) para resultados reales de esta entrega.

## Documentación oficial

- [Scaffold y sus áreas](https://developer.android.com/develop/ui/compose/components/scaffold)
- [Insets y padding de Material 3](https://developer.android.com/develop/ui/compose/system/material-insets)
- [Listas y cuadrículas](https://developer.android.com/develop/ui/compose/lists)
- [Navegación con Compose](https://developer.android.com/develop/ui/compose/navigation)
- [Estado en Compose](https://developer.android.com/develop/ui/compose/state)
