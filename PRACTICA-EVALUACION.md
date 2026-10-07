# Práctica para la evaluación de semanas 5 y 6

Preparación para el 7 de octubre de 2026, según lo indicado por el estudiante. Esta guía propone ejercicios; no afirma conocer las preguntas del docente.

## Qué proyectos entran

Semana 5: Lab05 y actividad integradora TECSUP Fit. Semana 6: Laboratorio06 TECSUP Store y tarea complementaria Mi Bodega. Son cuatro proyectos Gradle independientes: abrir la carpeta correcta, no la raíz de todo el repositorio.

## Primero entiende estas seis ideas

1. **Scaffold organiza espacio.** topBar, bottomBar y content son ranuras. No decide qué pantalla abrir. El lambda content recibe innerPadding para que el contenido no quede bajo las barras.
2. **NavHost relaciona rutas y pantallas.** nav.navigate("detalle/1") es un cambio explícito por evento. Crear un composable no navega; cambiar el ícono resaltado tampoco navega.
3. **Estado conserva la información de la interfaz.** remember/mutableStateOf produce valores que Compose observa. rememberSaveable puede restaurar valores al recrear Activity; no es base de datos. Estado compartido debe estar en el ancestro común.
4. **LazyColumn y LazyRow cambian la composición de las listas.** Son vertical y horizontal. Column/Row con scroll sirven como reemplazo para catálogos pequeños, pero componen todos los hijos. Conservar los mismos datos, tarjetas y callbacks al sustituirlas.
5. **Padding tiene dos usos.** innerPadding protege del espacio ocupado por barras. padding(16.dp) separa contenido visual. Aplicar innerPadding una sola vez. consumeWindowInsets informa a los hijos del espacio atendido; no mueve el contenido por sí mismo.
6. **Material 3 y degradado son cosas diferentes.** Los controles vienen de androidx.compose.material3. El degradado se dibuja con background(Brush.verticalGradient(...)). Un ícono importado de material.icons no cambia los controles a Material 2.

## Mapa para localizar rápidamente

| Cambio | TECSUP Fit | Mi Bodega | TECSUP Store | Lab05 |
|---|---|---|---|---|
| Scaffold y padding | Estructura.kt | Estructura.kt | StoreEstructura.kt | MarcoPantalla.kt |
| Rutas y parámetros | Navegacion.kt | ClienteApp.kt | TecsupStoreApp.kt | AppNavigation.kt / Screen.kt |
| Barra y sus íconos | Estructura.kt / destinos | Estructura.kt / secciones | AppDrawer.kt | No tiene bottomBar en la guía base |
| Listas y filtros | Listas.kt | Listas.kt / InicioScreen.kt | StoreListas.kt | ListScreen.kt |
| Datos y estado | Datos.kt / FitApp.kt | modelo / ClienteApp.kt | StoreDatos.kt / TecsupStoreApp.kt | IDs de ListScreen |
| Variante de práctica | Practica.kt | Practica.kt | Practica.kt | Practica.kt |

## Método para cualquier modificación

1. Ejecutar la versión base y describir qué hace.
2. Identificar archivo, dato que se conserva y evento que lo cambia.
3. Hacer un solo cambio pequeño. Antes de borrar una función, buscar todos sus usos.
4. Compilar; si falla, leer el primer error del archivo editado, no modificar muchos archivos a la vez.
5. Ejecutar y verificar una ruta de éxito, una cancelación/regreso y un dato inválido cuando aplique.
6. Explicar qué cambió y por qué las demás funciones siguen trabajando.
7. Guardar un commit descriptivo de TU práctica y restaurar antes del próximo ejercicio.

## Ejercicio 1 · Cambiar un ícono sin cambiar navegación (5 min)

En Estructura de TECSUP Fit, cambiar el ícono de Reservas por otro importado de material.icons. Conservar la ruta "reservas" y el título. Ejecutar, tocar la pestaña y comprobar que abre Reservas y se resalta. Explicar: el vector dibuja; el callback y la ruta navegan.

Posible error: cambiar el texto visible a una ruta que no existe. Nombre de pestaña y ruta pueden ser distintos.

## Ejercicio 2 · Retirar Scaffold y conservar navegación (15 min)

Primero probar USAR_SCAFFOLD=false. En TECSUP Fit o Mi Bodega abrir Inicio -> Detalle -> acción -> Confirmación. Después realizar la retirada física:

* Conservar la rama Column de Estructura.
* Retirar el bloque Scaffold y el if que lo elige.
* Mantener barra superior, Box(weight(1f)), contenido() y barra inferior.
* Mantener NavHost y estado en el contenedor. No borrar las funciones de las pantallas.
* Comprobar barras, scroll y recorrido completo.

Respuesta oral: "Scaffold distribuía las áreas de la pantalla. La navegación sigue porque NavHost se conserva dentro del contenido que la estructura recibe. Column y Box reservan ahora el espacio".

## Ejercicio 3 · Retirar navegación y conservar Scaffold (15 min)

Probar USAR_NAVEGACION=false. Luego conservar solo la rama que llama Estructura con Inicio fijo. Retirar la rama de NavController/NavHost y sus imports. Pasar callbacks vacíos a los eventos de cambio de pantalla. Conservar los eventos locales (filtro, ordenar, favorito).

Resultado esperado de la variante: topBar, contenido y bottomBar siguen dibujados; las pestañas no cambian pantalla. No equivale al producto final: es una demostración de independencia. Restaurar después.

Respuesta: "La estructura no requiere un controlador. Las acciones locales modifican estado, pero ya no existen rutas ni un grafo para cambiar de destino".

## Ejercicio 4 · Sustituir LazyColumn (15 min)

Probar USAR_LAZY_COLUMN=false. Leer ListaVertical (Mi Bodega) o ListaVertical (TECSUP Fit). Retirar la rama LazyColumn y conservar Column(verticalScroll(rememberScrollState())) más forEach. Mantener los mismos elementos y la tarjeta. No poner una lista vertical de altura infinita dentro de otra lista desplazable. Verificar último elemento, detalle y estado.

Para volver a agregarla: LazyColumn { items(elementos, key = clave) { tarjeta(it) } }. Conservar modifier y espacio disponible. Imports de items deben ser de foundation.lazy.

## Ejercicio 5 · Sustituir LazyRow (10 min)

Probar USAR_LAZY_ROW=false. Conservar Row(horizontalScroll(rememberScrollState())) y los chips. No borrar categoriaSeleccionada/soloHoy ni el callback que filtra. Verificar desplazamiento horizontal y resultado. Para restaurar LazyRow: items de los filtros y el mismo chip.

## Ejercicio 6 · Agregar una pantalla (15 min)

En TECSUP Fit crear @Composable fun Ayuda() con contenido breve. Añadir composable("ayuda") { Ayuda() } en NavHost. Añadir un botón en una pantalla con callback desde Navegacion: nav.navigate("ayuda"). Probar regreso con popBackStack. No añadir una quinta pestaña permanente si la entrega exige cuatro; el acceso puede estar dentro de Perfil. El cambio es de práctica y se restaura.

## Ejercicio 7 · Agregar o quitar una pestaña (10 min)

Si el profesor lo pide, cambiar lista de destinos y grafo juntos. Quitar Rutinas requiere quitar su Destino, su composable y referencias. Si se agrega, crear la pantalla y ruta antes de tocar la lista de destinos. En Mi Bodega secciones, nombres e iconos deben conservar igual longitud y orden. Restaurar las cuatro pestañas antes de entregar.

## Ejercicio 8 · Modificar padding y degradado (10 min)

Cambiar solo el padding visual 20.dp a 12.dp. Después observar qué ocurre si se retira temporalmente innerPadding: puede quedar contenido bajo barras. Restaurarlo y explicar la diferencia. Cambiar los colores de Brush.verticalGradient usando colorScheme.background y primaryContainer para que el modo oscuro sea legible. El orden de Modifier importa: un padding externo separa el fondo; un padding interno separa su contenido.

## Ejercicio 9 · Estado compartido y confirmación (15 min)

TECSUP Store: marcar favoritos y comprobar contador y sección; cancelar Reportar. Mi Bodega: agregar dos unidades, eliminar y cancelar, eliminar y confirmar, comprobar vacío. Explicar qué estado vive en la tarjeta, cuál vive arriba y por qué no crear un contador separado.

## Ejercicio 10 · Recorrido de un ID y total (15 min)

Lab05: index+1 -> createRoute(Int) -> navigate -> navArgument IntType -> getInt -> DetailScreen. Mi Bodega: tarjeta -> productoId -> búsqueda en catálogo -> agregar producto/cantidad -> carrito -> Pedido con copia -> confirmación e historial. Cambiar de delivery a recojo y comprobar diferencia exacta de S/ 4.

## Preguntas probables y respuesta breve

| Pregunta | Respuesta |
|---|---|
| ¿Scaffold navega? | No; organiza barras y contenido. Navegamos con rutas y eventos. |
| ¿Cómo sabe bottomBar qué resaltar? | Lee la ruta actual, compara contra el destino y aplica selected. |
| ¿Por qué el badge reacciona? | Deriva del estado compartido; cambia cuando cambia el conjunto o la cantidad. |
| ¿Dónde declaro DropdownMenu? | En Box junto a su botón de ancla; expanded es local por tarjeta. |
| ¿Por qué el drawer envuelve Scaffold? | Drawer gestiona panel/gestos; Scaffold gestiona las áreas del contenido. |
| ¿Cómo evitas confirmar dos opciones? | Una variable con un valor seleccionado; no booleanos independientes. |
| ¿Cómo evitas duplicar la compra al volver? | Crear pedido una vez, vaciar carrito y retirar formularios con popUpTo. |
| ¿Por qué copiar los items del pedido? | El historial debe conservar la compra aunque después se vacíe/modifique el carrito. |
| ¿remember guarda para siempre? | No; estado en memoria. rememberSaveable restaura ciertos valores tras recreación. |
| ¿Qué pasa si quito una función usada? | Error de referencia; buscar usos y retirar/reemplazar llamadas junto con la función. |
| ¿Cómo combino búsqueda y categoría? | Un filtro que exige ambos criterios con AND; no dos listas que se reemplazan. |
| ¿Qué hiciste con IA? | Preparación, reparación y pruebas documentadas; debo comprender y defender el resultado. |

## Plan de estudio de 2 horas y media

* 20 min: ejecutar los cuatro proyectos; localizar entrypoint, estructura y rutas.
* 35 min: ejercicios 1, 2 y 3 en TECSUP Fit o Mi Bodega; restaurar cada vez.
* 25 min: ejercicios 4 y 5, y agregar un producto/clase con ID nuevo.
* 25 min: pantalla nueva, pestaña e íconos; explicación de padding.
* 25 min: menú, favoritos, compra, cancelaciones y modalidad de entrega.
* 20 min: responder preguntas en voz alta sin leer y realizar un cambio sorteado.

## Simulacro individual

Sin copiar una solución: (1) abre TECSUP Fit, (2) quita Scaffold físicamente, (3) reserva una clase, (4) explica padding y ruta del ID, (5) restaura, (6) sustituye LazyRow, (7) verifica filtro, (8) commit describiendo el resultado. Después repite con Mi Bodega y revisa pedidos.

Ejemplo de commit de una práctica REAL: `Practica: sustituye LazyRow por Row y verifica filtro de bebidas`. No agregar commits vacíos ni fechas simuladas. Git registra cambios; la explicación y ejecución demuestran comprensión.

## Autoevaluación

Debes poder localizar la función, predecir qué cambia, editar, compilar, ejecutar y explicar sin memorizar frases. Si solo puedes activar una variable, todavía debes practicar la retirada física. Si un cambio rompe algo, restaurar ese cambio, entender la dependencia y repetirlo más pequeño.
