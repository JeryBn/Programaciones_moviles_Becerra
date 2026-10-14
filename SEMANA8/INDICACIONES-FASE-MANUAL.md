# Laboratorio 8 CafeCampus — instrucciones de trabajo personal

## Alcance y autoría

La Fase 1 de la guía exige trabajo sin IA. Este documento es una lista de ubicación, contratos y verificaciones extraída de la guía; no contiene la solución Kotlin. El estudiante debe escribir personalmente el código de esa fase. La Fase 2 se realizará con ChatGPT en lugar de Gemini, por solicitud del estudiante, y se registrará esa sustitución honestamente. Consultar al docente si acepta el cambio de herramienta.

El proyecto base suministrado por el docente todavía no se ha recibido. No se afirma que CafeCampus esté implementado, compilado o ejecutado. Los commits de estas instrucciones no cuentan como los 13 commits de implementación personal.

## Dónde colocar el proyecto

El repositorio del curso ya tiene `SEMANA8`: usar esa carpeta, sin crear otra `SEMANA 8` duplicada. Dentro colocar `CafeCampus/` con el proyecto base. Abrir esa carpeta en Android Studio. Conservar el archivo anterior `Lab08` existente. No copiar `.git` de otro proyecto ni ejecutar `git init` dentro de CafeCampus: el repositorio del curso ya existe.

Las rutas siguientes parten de `SEMANA8/CafeCampus/app/src/main/java/com/tecsup/cafecampus/`. En el panel Project seleccionar el paquete correspondiente, pulsar New > Kotlin Class/File y dar el nombre sin repetir `.kt`. Si el archivo guiado ya existe, editar sus TODO respetando las firmas. No modificar los archivos ENTREGADO salvo que el paso lo indique.

## Regla para cada avance

Trabajar en `main`. Completar y verificar un paso antes de pasar al siguiente. Desde la raíz del repositorio:

```text
git status
git add SEMANA8/CafeCampus
git commit -m "MENSAJE DEL PASO"
git push origin main
```

Usar el mensaje indicado abajo. Revisar lo que entra al commit; excluir local.properties, build/, .gradle/, credenciales y archivos del equipo. Si falla un push, resolverlo antes de continuar. No usar force push. No fabricar commits vacíos para alcanzar el número solicitado.

## Paso 0 — importar y probar la base — página 9

Descargar el proyecto base del docente y colocarlo en `CafeCampus/`. Abrirlo, sincronizar Gradle y ejecutarlo: deben aparecer Splash y Menú con productos. Leer primero Producto.kt, ProductoDao.kt, CafeRepository, MenuViewModel y MenuScreen; forman el ejemplo completo. Commit: `chore: importa proyecto base CafeCampus`.

## Paso 1 — entidades — páginas 9 y 10

En `data/local/entity/` crear Usuario.kt, Pedido.kt y DetallePedido.kt tomando Producto.kt como referencia. Usuario: id autogenerado, nombre, correo, clave y rol String. Pedido: id, clienteId, estado, horaRecojo, pago, nota, total y fecha Long. DetallePedido: id, pedidoId, productoId, nombreProducto, cantidad y precioUnitario. Declarar FK de clienteId a Usuario e Index en clienteId. Declarar FK de pedidoId a Pedido, borrado CASCADE e Index en pedidoId. Guardar nombres y precios históricos en el detalle. Compilar antes del commit. Mensaje: `feat(data): agrega entidades Usuario, Pedido y DetallePedido`.

## Paso 2 — DAOs y relaciones — páginas 10 y 11

Crear `data/local/dao/UsuarioDao.kt`, `PedidoDao.kt` y `data/local/relation/PedidoConDetalle.kt`. UsuarioDao: login, insertar y contar suspend. PedidoDao: insertarPedido, insertarDetalles y cambiarEstado suspend. CrearPedido debe ser transaccional: insertar cabecera, obtener su id y asignarlo a las líneas antes de insertarlas. Lecturas por cliente, por estado y por id con Flow y Transaction; ordenar por fecha descendente. Ventas desde una fecha con COALESCE, excluyendo CANCELADO; contador por estado con Flow. Relación: Embedded Pedido, Relation a detalles y Relation al usuario. Verificar generación KSP. Mensaje: `feat(data): agrega DAOs y relacion PedidoConDetalle`.

## Paso 3 — base y siembra — página 11

Completar `data/local/CafeDatabase.kt`, `DatosIniciales.kt` y `CafeApp.kt`. Registrar cuatro entidades, tres DAOs, singleton y nombre `cafecampus.db`. Registrar Application en AndroidManifest.xml. Sembrar en IO solo cuando no existen usuarios: café americano 4.00, sándwich de pollo 9.00, menú del día 12.00, jugo de naranja 5.00, empanada 4.50 y chicha morada 3.50. Cuentas ficticias: juan@campus.test / 1234 y admin@cafe.test / admin. Abrir View > Tool Windows > App Inspection > Database Inspector y comprobar tablas y datos. Mensaje: `feat(data): registra entidades y siembra datos iniciales en CafeDatabase`.

## Paso 4 — Repository — página 12

Completar `data/repository/CafeRepositoryRoom.kt`. Implementar todas las firmas del contrato entregado. Login delega en UsuarioDao; crearPedido copia nombre y precio en las líneas, calcula total y llama al DAO transaccional; observar delega con Flow, cambiarEstado guarda enum.name, ventas delega con el inicio del día. No dejar TODO() pendientes. Mensaje: `feat(data): implementa CafeRepositoryRoom con todas las operaciones`.

## Paso 5 — DataStore — páginas 12 y 13

Crear `data/prefs/PreferenciasStore.kt`. Declarar una sola extensión de Context para preferencias `ajustes`. Exponer Flow para perfilActivo, modoOscuro y recibirPedidos (true por defecto). Escrituras suspend: guardarPerfil, guardarModoOscuro, guardarRecibirPedidos y cerrarSesion. Room guarda pedidos y productos; DataStore solo estas preferencias. Mensaje: `feat(data): agrega PreferenciasStore con DataStore`.

## Paso 6 — sesión — páginas 13 y 14

Crear `viewmodel/SesionViewModel.kt`, `ui/cliente/LoginScreen.kt` y completar el destino en `ui/navigation/NavCafe.kt`. Exponer usuarioActual, error y modoOscuro; validar vacíos, credenciales y rol seleccionado. Al ingresar guardar perfil y navegar al grafo correspondiente. Splash recupera perfil guardado. View solo observa su VM. Probar ambos usuarios y contraseña incorrecta. Mensaje: `feat(viewmodel): agrega SesionViewModel y login con selector de rol`.

## Paso 7 — carrito y detalle — páginas 14 y 15

Completar `viewmodel/MenuViewModel.kt` y crear `ui/cliente/DetalleProductoScreen.kt`. Carrito StateFlow con agregar, quitar, vaciar y total. Sumar cantidades del mismo producto sin duplicarlo. Detalle con icono entregado, datos, disponibilidad, cantidad mínima 1, nota y botón con total. Agotado deshabilita agregar. Probar contador de bolsa y rotación. Mensaje: `feat(ui): agrega detalle de producto y carrito en MenuViewModel`.

## Paso 8 — pedido del estudiante — páginas 15 y 16

Crear PedidosClienteViewModel.kt en viewmodel y ConfirmarPedidoScreen.kt, SeguimientoScreen.kt, MisPedidosScreen.kt en ui/cliente. Confirmar crea pedido RECIBIDO, vacía carrito y abre seguimiento del id nuevo. Mostrar líneas, horas, pago CAJA/YAPE, notas y total. Stepper observa Flow; no avanzar con temporizadores. Activos incluye RECIBIDO/PREPARANDO/LISTO; otros van a historial. Probar pedido con dos productos. Mensaje: `feat(ui): agrega confirmar pedido, seguimiento y mis pedidos`.

## Paso 9 — panel administrativo — páginas 16 y 17

Crear PanelAdminViewModel.kt y PedidosAdminViewModel.kt en viewmodel, PanelScreen.kt y PedidosAdminScreen.kt en ui/admin. Contadores y ventas usan consultas Room. Filtrar por estado con flatMapLatest. Avance RECIBIDO > PREPARANDO > LISTO > ENTREGADO dentro del VM o enum, nunca en la View. Probar pedido creado por cliente y cambio por admin. Mensaje: `feat(admin): agrega panel de control y gestion de pedidos por estado`.

## Paso 10 — menú administrativo — páginas 17 y 18

Crear MenuAdminViewModel.kt y ui/admin/DetallePedidoAdminScreen.kt, MenuAdminScreen.kt, FormularioProductoScreen.kt. Detalle muestra cliente, líneas, total, nota y acciones. Menú permite editar, eliminar con confirmación, disponibilidad y FAB de alta. Formulario sirve para nuevo y editar; nombre obligatorio y precio mayor que cero. Probar agotado y nuevo producto desde cliente. Mensaje: `feat(admin): agrega detalle de pedido, gestion de menu y formulario`.

## Paso 11 — ajustes — página 19

Crear `ui/admin/AjustesScreen.kt` y completar NavCafe. Modo oscuro cambia el tema global y persiste. Recibir pedidos apagado bloquea confirmar y muestra Cafetería cerrada. Cambio de perfil limpia pila con popUpTo; cerrar sesión borra perfil. Probar cierre completo y reapertura. Mensaje: `feat(admin): agrega ajustes con DataStore y cambio de perfil`.

## Paso 12 — aceptación y README — páginas 19 y 20

Ejecutar las nueve pruebas y registrar resultados reales: login incorrecto; pedido de dos productos; agotado; cambio Preparando reflejado por Flow; cancelación en historial; precio histórico intacto; ventas sin cancelados; pedidos y perfil persistentes; rotación conserva carrito. Capturar tres vistas cliente y tres admin. En README incluir nombre, descripción, árbol de paquetes, diagrama MVVM y seis capturas. Responder la sección Para pensar con palabras propias, máximo seis líneas en total. Añadir dos observaciones y dos conclusiones técnicas para la rúbrica, basadas en problemas que realmente encontraste. Mensaje: `docs: agrega README con estructura y capturas`.

## Fase 2 — trabajo con ChatGPT

Cuando estén completos, probados y publicados los 13 pasos personales, enviar el estado del proyecto para implementar tres mejoras: búsqueda en menú (filtro en VM), repetir pedido (recargar carrito con validación de disponibilidad) y gráfico por hora (consulta Room real). Publicar un commit por mejora con prefijo ia: y después PROMPTS.md. La rama mejora-ia ya existe para semanas anteriores: preservar su historial, partir del main terminado e integrar cuidadosamente sin sobrescribirla.

Mensajes requeridos: `ia: agrega busqueda de productos en el menu`, `ia: agrega repetir pedido desde el historial`, `ia: agrega grafico real de pedidos por hora`, `docs: agrega PROMPTS.md`. Registrar texto real enviado a ChatGPT, resultado, correcciones y revisión de MVVM. No afirmar uso de Gemini ni atribuir a trabajo manual código generado con IA.

## Qué habrás realizado al terminar

Habrás construido una app local de cafetería con dos perfiles que comparten Room, relaciones y transacciones, consultas reactivas Flow, Repository interface, ViewModels, UI Compose y preferencias persistentes. Habrás demostrado pedidos cliente, gestión administrativa, conservación histórica y cambios en vivo, con 13 avances personales y tres mejoras asistidas verificadas. Por ahora solo se prepararon estas instrucciones: la aplicación y la fase personal siguen pendientes.
