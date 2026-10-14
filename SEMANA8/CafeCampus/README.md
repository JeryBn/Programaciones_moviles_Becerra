# CafeCampus — Laboratorio 8

Aplicación local de pedidos de cafetería con dos perfiles en el mismo dispositivo, Kotlin, Jetpack Compose, Material 3, MVVM, Repository interface, Room 2.6.1 y DataStore.

## Origen y autoría
Construida desde cero con asistencia de ChatGPT por solicitud explícita del estudiante. No se recibió el ZIP docente. El primer commit reconstruye la configuración; no es una importación de una base entregada.
La guía exige una fase inicial sin IA: esta implementación asistida **no cumple esa condición de autoría**. Se conserva main para el desarrollo base y mejora-ia para las mejoras, sin ocultar el origen.

## Abrir y ejecutar
Abrir esta carpeta CafeCampus en Android Studio, sincronizar Gradle, elegir un emulador API 24 o superior y ejecutar app. Usar JDK 21 instalado en la PC. Versiones: AGP 8.7.3, Gradle 8.13, Kotlin 2.0.21, KSP 2.0.21-1.0.28. UI Compose sin layouts XML.

Cuentas ficticias:
- Estudiante: juan@campus.test / 1234
- Administrador: admin@cafe.test / admin

## Arquitectura
```text
View → eventos → ViewModel → CafeRepository (interface)
View ← StateFlow ← ViewModel ← Flow ← CafeRepositoryRoom ← DAO ← Room
DataStore → preferencias → SesionViewModel → tema y ajustes
```
La View observa su ViewModel; no accede a DAOs ni Repository. Cada pedido conserva una foto del nombre y precio del producto. Escrituras suspend; lecturas reactivas Flow. Los perfiles comparten cafecampus.db.

## Paquetes
```text
com.tecsup.cafecampus
├── MainActivity / CafeApp
├── data
│   ├── local
│   │   ├── entity: Producto, Usuario, Pedido, DetallePedido
│   │   ├── dao: ProductoDao, UsuarioDao, PedidoDao
│   │   ├── relation: PedidoConDetalle
│   │   └── CafeDatabase / DatosIniciales
│   ├── repository: CafeRepository / CafeRepositoryRoom
│   └── prefs: PreferenciasStore
├── model: Rol / EstadoPedido / ItemCarrito
├── viewmodel
│   ├── CafeViewModelFactory / SesionViewModel / MenuViewModel
│   ├── PedidosClienteViewModel
│   └── PanelAdminViewModel / PedidosAdminViewModel / MenuAdminViewModel
└── ui
    ├── theme / components / navigation
    ├── cliente: Splash, Login, Menu, DetalleProducto, ConfirmarPedido, Seguimiento, MisPedidos
    └── admin: Panel, PedidosAdmin, DetallePedidoAdmin, MenuAdmin, FormularioProducto, Ajustes
```

## Procedimiento por avances
Cada bloque tuvo commit y push separado: configuración; entidades y enums; DAOs y relaciones; base singleton y siembra transaccional; Repository completo; DataStore; sesión y login; catálogo, detalle y carrito; confirmación, seguimiento e historial; panel y pedidos administrativos; edición de productos y detalle de pedidos; ajustes y navegación; pruebas; documentación y capturas.
Los pasos están explicados en ../INDICACIONES-FASE-MANUAL.md. Ese archivo corresponde a la petición anterior de trabajo personal; ahora la implementación está asistida y lo declara ORIGEN-Y-ASISTENCIA.md.

## Funciones
- Inicio de sesión con validación de credenciales y rol.
- Catálogo con categorías y disponibilidad.
- Carrito sin duplicar productos y con total calculado.
- Confirmación transaccional con hora, pago y notas.
- Seguimiento e historial reactivos por estudiante.
- Panel de contadores y ventas sin cancelados.
- Administración de estados y productos; confirmación antes de eliminar.
- Validación del formulario y precio positivo finito.
- Cambio de perfil en modo práctica y cierre de sesión.
- Modo oscuro y recepción de pedidos guardados con DataStore.

## Mejoras de la rama mejora-ia
1. Búsqueda de productos: combina texto y categoría en el ViewModel, tolerando tildes y mayúsculas.
2. Repetir pedido: lee productos actuales, usa sus precios y rechaza repetir si alguno fue eliminado o agotado.
3. Gráfico de pedidos por hora: consulta Room para el día actual, sin cancelados, y presenta barras y cantidades.

## Valor agregado Mi recreo
El estudiante selecciona 5, 10, 15 o 20 minutos. El ViewModel combina contadores reales de pedidos recibidos y preparando con la preferencia de recepción de pedidos.
El algoritmo estima el tiempo con dos estaciones hipotéticas: 4 minutos por recibido, 2 por preparando y 4 para el nuevo pedido. Si la carga supera el tiempo disponible, aconseja otra hora de recojo.
La pantalla aclara que no garantiza la entrega. Es una adaptación diseñada para este laboratorio; no se afirma exclusividad mundial.
Archivo de reglas: model/PlanRecreo.kt. Conexión reactiva: MiRecreoViewModel.kt. Interfaz: MiRecreoScreen.kt.
Cinco pruebas unitarias cubren cola vacía, cola larga, límite exacto, cerrado y datos inválidos.

## Cómo demostrarlo al docente
1. Entrar con la cuenta de estudiante, elegir Café americano y Chicha morada y confirmar S/ 7.50.
2. Mostrar el seguimiento Recibido.
3. Abrir Perfil > Cambiar de perfil; el panel muestra el mismo pedido.
4. Preparar, dejar listo y entregar; volver al cliente y abrir Historial.
5. Repetir el pedido: carga la bolsa sin crear todavía un nuevo pedido.
6. En Menú escribir CAFE y comprobar que aparece Café americano.
7. Mostrar el gráfico real del panel y Mi recreo con dos tiempos diferentes.
8. En Ajustes apagar Recibir pedidos y demostrar confirmación deshabilitada.
9. Activar modo oscuro, cerrar la app y reabrir; comprobar perfil y tema.
10. Mostrar git log y PROMPTS.md para explicar avances y asistencia.

## Qué explicar del código
Producto, Usuario, Pedido y DetallePedido representan las cuatro tablas. Los índices facilitan las relaciones; CASCADE elimina líneas junto a su pedido.
PedidoConDetalle reúne cabecera, usuario y líneas. La transacción impide guardar un pedido incompleto.
Repository es el contrato de datos. CafeRepositoryRoom usa los DAOs; las pantallas no los conocen.
MenuViewModel conserva carrito y filtro. Los ViewModels de pedidos observan Flow; el administrador cambia Room y el estudiante recibe la actualización.
DataStore conserva perfil, tema y recepción. El precio histórico del detalle no se recalcula con el catálogo.
Mi recreo es un modelo de estimación con supuestos documentados, no un sistema de reserva de turnos.

## Capturas reales
Tres vistas de estudiante:
![Catálogo estudiante](docs/evidencias/cliente-menu.png)
![Confirmación de pedido](docs/evidencias/cliente-confirmar.png)
![Seguimiento recibido](docs/evidencias/cliente-seguimiento.png)

Tres vistas de administrador:
![Panel de pedidos](docs/evidencias/admin-panel.png)
![Gestión del menú](docs/evidencias/admin-menu.png)
![Ajustes](docs/evidencias/admin-ajustes.png)

Cambio compartido por Flow:
![Pedido preparando observado por estudiante](docs/evidencias/cliente-pedido-preparando.png)

## Para pensar — respuestas propuestas para revisar con palabras propias
El Repository es una interface para intercambiar la fuente de datos sin cambiar los ViewModels.
crearPedido es suspend porque realiza una escritura única; observarPedidosCliente es Flow porque emite cambios.
Con bases distintas el administrador y el estudiante no verían el mismo pedido actualizado.
Para usar servidor cambiaría la implementación del Repository y la sincronización, manteniendo el contrato.

## Observaciones
1. Room necesitó índices en las claves foráneas y una transacción para que cabecera y líneas se guardaran juntas.
2. El emulador actual requirió Espresso actualizado; además, la recreación de Activity conserva la ruta y el carrito mientras el proceso vive.

## Conclusiones
1. Compartir Room y observar Flow permite reflejar cambios administrativos sin recargar manualmente las pantallas.
2. Guardar precio y nombre en el detalle conserva el historial aunque cambie o desaparezca el producto.

## Verificación
Compilación de app y APK de pruebas confirmada. Pruebas de Room: credenciales, siembra idempotente, dos productos, total, estados, cancelación, ventas, precio histórico, eliminación y rechazo de agotados verificadas.
Recorrido UI y capturas ejecutados; el resultado final se registra en VERIFICACION.md.
La persistencia del carrito está garantizada por ViewModel en rotación; el carrito no es persistente tras muerte del proceso (los pedidos confirmados sí están en Room).

## Uso de Git
```text
git log --oneline -- SEMANA8/CafeCampus
git push origin main
git push origin mejora-ia
```
No se fabricaron commits vacíos. PROMPTS.md registra solicitudes y correcciones reales; no declara participación de Gemini.

