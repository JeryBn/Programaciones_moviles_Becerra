# Lab05 · Navegación entre pantallas

Proyecto anterior conservado y separado de la actividad integradora.

## Abrir y ejecutar

1. En Android Studio, **File > Open**, seleccionar esta carpeta `Lab05`.
2. Esperar la sincronización de Gradle. Usar el JDK incluido en Android Studio.
3. Seleccionar un emulador y ejecutar `app`.

## Funcionamiento

- `MainActivity` ejecuta `AppNavigation`.
- `Screen.kt` define las rutas `home`, `list`, `profile` y `detail/{itemId}`.
- Inicio permite abrir la lista o el perfil.
- Lista presenta ocho elementos con `LazyColumn`; tocar uno navega a su detalle.
- Detalle obtiene `itemId` como entero desde la ruta y lo muestra.
- Perfil muestra un degradado y permite volver al inicio.

## Ejercicios para la evaluación

1. Cambiar `(1..8)` por `(1..12)` en `ListScreen` y comprobar la lista.
2. Cambiar el título de la barra en `DetailScreen` sin alterar la ruta.
3. Sustituir `LazyColumn` por `Column(Modifier.verticalScroll(rememberScrollState()))`, iterar con `forEachIndexed` y conservar `navigate(Screen.Detail.createRoute(index + 1))`.
4. Sustituir `Scaffold` de detalle por `Column` con `TopAppBar` y el contenido. Conservar `popBackStack()`.
5. Explicar por qué `itemId` es un entero y cómo llega desde la tarjeta hasta el detalle.

El laboratorio original solo usa Scaffold en Lista y Detalle. La aplicación integradora incluye una estructura común para todas las pantallas.
