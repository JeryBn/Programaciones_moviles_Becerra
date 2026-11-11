# Laboratorio 11–12 | Catálogo Tecsup

Programación en Móviles · Kotlin y Jetpack Compose · modalidad individual solicitada por el estudiante.

**Variante provisional G01.** Productos de la categoría smartphones en DummyJSON. Confirmar la asignación docente antes de entregar.

## Abrir y configurar

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

El proyecto se generó con IA desde cero por indicación del estudiante. La fase inicial sin IA, los aportes de una pareja y el historial exigido en la guía requieren revisión con el docente. Consultar PROMPTS.md. Pendientes de verificar con Firebase y el emulador las pruebas PA-01 a PA-14 y las capturas. El código incluye tres pruebas unitarias del catálogo.
