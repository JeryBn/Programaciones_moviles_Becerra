# Verificación del desarrollo base

14 octubre 2026. App y APK de pruebas: BUILD SUCCESSFUL.
Ejecución directa sobre Medium_Phone mediante AndroidJUnitRunner: OK (5 tests), 34.325 s.

1. FlujoUiTest: credencial incorrecta, ingreso cliente, dos productos, carrito conservado al recrear Activity, confirmación, administrador prepara, cliente observa Preparando y conserva ruta/datos tras recreación.
2. RoomAceptacionTest: cuentas y roles ficticios; siembra idempotente.
3. RoomAceptacionTest: creación transaccional de dos líneas, total y cadena de estados.
4. RoomAceptacionTest: precio histórico al editar/eliminar, cancelación y ventas sin cancelados.
5. RoomAceptacionTest: agotado rechaza pedido sin crear líneas parciales.

Capturas reales obtenidas de UiAutomation durante el test en docs/evidencias. No se sustituyeron por mockups.
La prueba de Activity recreation comprueba cambio de configuración; no equivale a afirmar persistencia de un carrito tras muerte del proceso.
Pendiente ampliar prueba visual de modo oscuro/recepción cerrada y cierre completo; sus implementaciones están presentes.
