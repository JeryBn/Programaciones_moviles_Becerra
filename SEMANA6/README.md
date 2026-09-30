# Semana 6 - Menu y navegacion en Android

Esta carpeta contiene los dos desarrollos solicitados por la guia GLAB-S06:

- `Laboratorio06`: TECSUP Store con menu contextual por producto y `ModalNavigationDrawer`.
- `MiBodega`: app cliente de siete pantallas con `NavigationBar`, categorias, productos, carrito, entrega y confirmacion.

## Como ejecutar

1. Abrir en Android Studio la carpeta del proyecto elegido.
2. Esperar la sincronizacion de Gradle.
3. Seleccionar un emulador Android API 24 o superior.
4. Ejecutar la configuracion `app`.

Verificacion por consola en cada proyecto:

```powershell
.\gradlew.bat testDebugUnitTest assembleDebug --console=plain
```

## Proceso realizado

1. Se preservo el proyecto de la Semana 5 y se creo una copia evolutiva para el Laboratorio 06.
2. Se agrego el icono de tres puntos dentro de cada tarjeta y un `DropdownMenu` anclado en el mismo `Box`.
3. Se incorporaron las acciones Favoritos, Compartir y Reportar, cada una con icono y separadores.
4. Se envolvio el `Scaffold` con `ModalNavigationDrawer` y se agregaron encabezado de usuario, cinco destinos y seleccion visual.
5. El estado de favoritos se elevo al contenedor principal para que el menu contextual y el badge del drawer compartan la misma fuente de verdad.
6. Se importo el esqueleto de Mi Bodega y se completaron sus siete pantallas sin servidor ni base de datos.
7. Se conecto el flujo Bienvenida -> Registro -> Inicio -> Detalle -> Carrito -> Datos de entrega -> Confirmacion.
8. Se implementaron `LazyRow` para categorias, cuadricula perezosa de productos, `NavigationBar`, paso de `productoId` y `popUpTo` al finalizar.
9. Se implemento un carrito reactivo con agregar, incrementar, disminuir, eliminar, subtotal, delivery y total.
10. Se agregaron pruebas unitarias para favoritos, filtro combinado y calculo del total; ambos proyectos compilan y generan APK debug.

## Respuestas de reflexion

El `DropdownMenu` se declara dentro de un `Box` junto al boton porque Compose usa ese contenedor como ancla visual. Si se declarara lejos del icono, su posicion ya no representaria la tarjeta que origino la accion.

Las opciones del menu contextual afectan un solo producto porque reciben su identificador. Los destinos del drawer cambian el contenido principal de toda la aplicacion y por eso su alcance es global.

El contador de favoritos funciona mediante elevacion de estado: el conjunto de identificadores vive en `TecsupStoreApp`, la tarjeta envia eventos y el drawer lee el mismo conjunto. No se duplican estados.

En Mi Bodega, el filtro de categoria y el buscador se calculan en una unica funcion pura. Por eso ambos criterios se aplican juntos en cada recomposicion.

El total no requiere un boton de recalculo: se deriva de la lista y las cantidades actuales. Al cambiar el carrito, Compose vuelve a calcular subtotal y total.

`navigate` agrega una pantalla al historial; `popUpTo` elimina las pantallas de compra ya completadas para impedir que el boton Atras regrese a una confirmacion anterior.

## Observaciones

1. El esqueleto de Mi Bodega traia las pantallas de entrega y confirmacion vacias, y la navegacion terminaba en el carrito; fue necesario completar ambos archivos y extender el `NavHost`.
2. La version inicial del esqueleto usaba un Gradle incompatible con el JDK actual de Android Studio; se actualizo la toolchain y se valido nuevamente la compilacion.

## Conclusiones

1. Los componentes locales, como `DropdownMenu`, deben mantener su estado de apertura cerca de la tarjeta, mientras que la informacion compartida, como favoritos o carrito, debe elevarse a un contenedor comun.
2. Trabajar desde un esqueleto reduce el tiempo de configuracion, pero exige revisar cada `TODO`, conectar el flujo completo y comprobar compilacion y pruebas; la fase de mejora permite aislar y revisar el cambio asistido.

## Estado de verificacion

- Laboratorio06: `testDebugUnitTest` y `assembleDebug` correctos.
- MiBodega: `testDebugUnitTest` y `assembleDebug` correctos.
- La app usa solo colecciones en memoria, tal como delimita la guia.
