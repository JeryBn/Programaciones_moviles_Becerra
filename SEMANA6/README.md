# Semana 6 · Laboratorio y tarea complementaria

| Proyecto | Trabajo solicitado | Funcionamiento y código |
|---|---|---|
| Laboratorio06 | TECSUP Store, DropdownMenu, NavigationDrawer y contador de favoritos | [README completo](Laboratorio06/README.md) |
| MiBodega | App cliente, NavigationBar, compra, filtros y mejoras indicadas en Canvas | [README completo](MiBodega/README.md) |

Cada proyecto define exactamente cuatro requerimientos funcionales. Los criterios técnicos se documentan como implementación. Cada pantalla conserva un archivo; los componentes compartidos y rutas están separados.

## Cobertura de las 11 mejoras de Canvas

| Mejora | Archivo principal en MiBodega |
|---|---|
| Login fijo y error | BienvenidaScreen.kt + Pedido.kt |
| Campos obligatorios en rojo | RegistroScreen.kt y DatosEntregaScreen.kt |
| Badge del carrito | Estructura.kt |
| Mensaje de carrito vacío | CarritoScreen.kt |
| AlertDialog antes de eliminar | CarritoScreen.kt |
| Historial Mis pedidos | ClienteApp.kt + PedidosScreen.kt / PerfilScreen.kt |
| Corazones y sección Favoritos | InicioScreen.kt, DetalleProductoScreen.kt y ClienteApp.kt |
| Orden por precio | InicioScreen.kt |
| Recojo/delivery con RadioButton | DatosEntregaScreen.kt + totalEntrega |
| Switch oscuro en Perfil | PedidosScreen.kt / PerfilScreen.kt + Theme.kt |
| Transiciones de pantalla | NavHost en ClienteApp.kt; AnimatedContent para el resumen en confirmación |

## Rúbrica y límites reales

El laboratorio tiene menú contextual, personalización, drawer, destinos, encabezado y contador compartido. Mi Bodega completa siete pantallas base, NavigationBar con cuatro destinos, LazyColumn/LazyRow, parámetros, carrito reactivo, búsqueda combinada y popUpTo. Cada README incluye reflexión y dos observaciones y dos conclusiones.

La guía exige commits mínimos en las fases sin IA/con IA. Los cambios del 6 de octubre registran asistencia y reparación; no pueden acreditar retroactivamente una fase sin IA ni cumplimiento de fechas ya vencidas. Los ocho commits históricos de Semana 6 no eran ocho commits por cada proyecto. No se rellenó el historial con commits vacíos ni se alteraron fechas.

Consultar [práctica guiada](../PRACTICA-EVALUACION.md), [registro de asistencia](../PROMPTS-EVALUACION.md) y [verificación](../VERIFICACION-EVALUACION.md).
