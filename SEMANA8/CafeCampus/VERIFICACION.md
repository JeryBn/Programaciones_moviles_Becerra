# Verificación final del Laboratorio 08

14 octubre 2026. App y APK de pruebas: BUILD SUCCESSFUL.
Última compilación después del cambio de arquitectura: BUILD SUCCESSFUL.
Cinco pruebas unitarias de PlanRecreo: cero fallos y errores.
Ejecución final sobre Medium_Phone mediante AndroidJUnitRunner: **OK (6 tests), 42.803 s**.

1. FlujoUiTest: credencial incorrecta, ingreso cliente, dos productos, carrito conservado al recrear Activity, confirmación, administrador prepara, cliente observa Preparando y conserva ruta/datos tras recreación.
2. RoomAceptacionTest: cuentas y roles ficticios; siembra idempotente.
3. RoomAceptacionTest: creación transaccional de dos líneas, total y cadena de estados.
4. RoomAceptacionTest: precio histórico al editar/eliminar, cancelación y ventas sin cancelados.
5. RoomAceptacionTest: agotado rechaza pedido sin crear líneas parciales.

Capturas reales obtenidas de UiAutomation durante el test en docs/evidencias. No se sustituyeron por mockups.
La prueba de Activity recreation comprueba cambio de configuración; no equivale a afirmar persistencia de un carrito tras muerte del proceso.
La sexta prueba instrumentada verifica agrupación real de pedidos por hora y exclusión de cancelados.
El recorrido UI también verificó búsqueda CAFE/Café, repetición del pedido, filtro de estados, LISTO y ENTREGADO, modo oscuro persistente en recreación y confirmación deshabilitada con recepción cerrada.

Se publicaron 15 capturas reales del recorrido automatizado. Se revisaron visualmente catálogo/Mi recreo, gráfico, modo oscuro y repetición.
Después se detuvo completamente el proceso con am force-stop y se inició MainActivity. La captura reinicio-proceso.png confirma perfil estudiante y modo oscuro conservados, sin volver al login. El carrito quedó vacío, como corresponde a su almacenamiento en ViewModel; no se promete conservarlo tras muerte del proceso.

Límites: datos y credenciales ficticios, sin pago real ni servidor. Mi recreo es una estimación educativa, no garantía de entrega. El cambio de perfil es un modo de práctica, no seguridad de producción.
Por petición del usuario el desarrollo fue asistido con ChatGPT. No se afirma una primera fase sin IA, uso de Gemini ni importación del ZIP docente no recibido; véanse ORIGEN-Y-ASISTENCIA.md y PROMPTS.md.
La compilación y ejecución están verificadas. No se confirmó la selección de CafeCampus en la ventana de Android Studio, que mostraba el proyecto anterior; abrir la carpeta exacta indicada en README.
