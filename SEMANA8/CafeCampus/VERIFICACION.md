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
Esta comprobación histórica fue ampliada el 23 de octubre de 2026, según el registro siguiente.

## Verificación del 23 de octubre de 2026

Compilación de app y APK de pruebas: BUILD SUCCESSFUL.
Batería base: OK (5 tests), 64.686 s, con verificación añadida de modo oscuro en recreación y confirmación deshabilitada con recepción cerrada.
Preparación para reinicio: OK (1 test). Tras detener el proceso de la app, PersistenciaProcesoTest: OK (1 test), 8.753 s; conserva perfil CLIENTE, modo oscuro, recepción cerrada y pedido confirmado.
Las capturas actuales y las conclusiones están incorporadas en ../GLAB-S08-JLEONS-2026-1.pdf.

Para repetir el caso entre procesos: ejecutar FlujoUiTest, ejecutar PrepararPersistenciaTest, detener el proceso de la app y ejecutar PersistenciaProcesoTest. La preparación es explícita y no se presenta como un caso independiente sin precondiciones.
