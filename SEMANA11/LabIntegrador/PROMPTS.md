# Uso de IA

## Consulta 1 · 8 de noviembre de 2026

- Herramienta. ChatGPT Work.
- Solicitud. Desarrollar el laboratorio de programación móvil con Android Studio y Jetpack Compose desde cero, para realizarlo individualmente.
- Resultado. Proyecto Kotlin con pantallas Compose, capas MVVM, consumo de DummyJSON, caché y favoritos Room, autenticación y roles Firebase, notificaciones y tres pruebas con repositorio falso.
- Decisión. Se usó provisionalmente la variante G01. Debe contrastarse con la asignación del docente.
- Aprendizaje para revisar. Comprobar por qué Room alimenta la lista y cómo una transacción evita perder favoritos al sincronizar.
- Alcance. La asistencia abarcó el proyecto desde el inicio.

## Consulta 2 · 11 de noviembre de 2026

- Herramienta. Codex.
- Solicitud. Resolver el laboratorio de la guía, revisar las indicaciones en Canvas, ejecutar las pruebas y dejar el proyecto local abierto en segundo plano.
- Resultado. Se reutilizó la base previa y se añadió una configuración de Firebase para pruebas locales. Se escribieron pruebas de integración para login, roles, catálogo, detalle, favoritos, caché y sincronización.
- Decisión. Se mantienen perfiles de prueba en Firebase Emulator Suite. La variante smartphones sigue provisional. El estudiante confirmó que trabaja solo y señaló el repositorio Programaciones_moviles_Becerra para la carpeta SEMANA11.
- Aprendizaje. Esperar la recuperación de la red evita que la prueba de sincronización mida el arranque de la conexión. Las reglas de Firestore protegen el perfil incluso cuando el cliente conoce otro UID.
