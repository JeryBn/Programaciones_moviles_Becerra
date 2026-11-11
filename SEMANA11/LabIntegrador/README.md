# Laboratorio 11–12 | Catálogo Tecsup

Programación en Móviles · Kotlin y Jetpack Compose · modalidad individual solicitada por el estudiante.

**Variante provisional G01.** Productos de la categoría smartphones en DummyJSON. Confirmar la asignación docente antes de entregar.

## Abrir y configurar

### Demostración local

Usar Android Emulator y la variante `debug` sin `app/google-services.json`. En la raíz iniciar Firebase Emulator Suite con `npx firebase-tools@15.35.0 emulators:start --only auth,firestore --project demo-lab-becerra`. En otra ventana, ejecutar `powershell -File scripts/Preparar-Demo.ps1`. Las cuentas ficticias son `usuario@laboratorio.example` y `admin@laboratorio.example`, ambas con la clave de demostración `CatalogoDemo!12`. Solo existen en los emuladores locales. La API pública del catálogo sigue requiriendo Internet para la primera carga.

### Proyecto Firebase en la nube

1. Abrir esta carpeta como proyecto en Android Studio. Usar el JDK incluido con Android Studio y el SDK 36.
2. Crear un proyecto de Firebase y registrar la app Android con el paquete `com.tecsup.productos`.
3. Descargar el archivo de configuración en `app/google-services.json`. Está excluido de Git.
4. Activar Correo electrónico/contraseña en Authentication. Crear dos cuentas de prueba.
5. Crear Firestore. En `usuarios/{uid}`, usar el UID exacto de Authentication. Agregar `nombre` y `rol` de tipo texto. Los roles admitidos son `admin` y `usuario`. Publicar el contenido de `firestore.rules`.
6. Sincronizar Gradle y ejecutar en un emulador con Google Play. Desde terminal usar `gradlew.bat assembleDebug testDebugUnitTest`.

## Arquitectura

- Compose observa StateFlow con collectAsStateWithLifecycle. El panel se registra en navegación exclusivamente para el rol administrador.
- Retrofit consulta /products/category/smartphones y /products/{id}. DTO, entidad y modelo de pantalla están separados.
- Room alimenta el catálogo y los favoritos. La sincronización conserva las marcas y el catálogo guardado queda disponible cuando falla la conexión. Limpiar caché borra todos los datos locales.
- Firebase Authentication conserva la sesión y Firestore determina el rol. Sus reglas permiten al usuario leer su propio perfil y bloquean escrituras desde la app.
- NotificationCompat usa un canal, permiso en Android 13 o superior y un PendingIntent que abre la actividad.

## Observación

La interfaz del repositorio permite probar ListaViewModel con datos controlados. Room evita que la lista dependa directamente de la disponibilidad de la API. Durante el refresco, una transacción conserva los favoritos y actualiza los productos. Negar notificaciones solo impide publicar el aviso.

## Evidencias y estado de entrega

El trabajo se realizó individualmente con asistencia de IA desde la base. El historial registra avances reales de esta revisión. La fase sin IA y los aportes de dos integrantes requieren revisión con el docente. El proyecto incluye tres pruebas unitarias del catálogo y un recorrido de aceptación con Android Emulator y Firebase Emulator Suite. Ejecutar `gradlew.bat testDebugUnitTest connectedDebugAndroidTest` en el emulador dedicado con las cuentas de demostración preparadas.

En Canvas, «Laboratorio 12» enlaza esta guía y muestra la tarea bloqueada desde el 8 de noviembre a las 23:59. La configuración de Firebase en la nube y el usuario del docente para dar acceso quedan sujetos a la asignación del curso.

Pruebas verificadas: 3 unitarias y 1 recorrido instrumentado. [Informe](docs/Informe-Laboratorio-11-12-Becerra.pdf) y [capturas](docs/evidencias/). El recorrido no incluye denegación del permiso de notificaciones.
