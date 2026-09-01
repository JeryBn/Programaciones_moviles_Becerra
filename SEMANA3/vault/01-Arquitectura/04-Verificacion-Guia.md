# 04 — Verificación contra la Guía (18 puntos)

Checklist ejecutado post-build. Fuente: `docs/INFORME-DETALLADO.md`.

| # | Requisito guía | Estado | Evidencia |
|---|---|---|---|
| 1 | Sistema de préstamos, no banco (§1) | ✅ | App solo gestiona préstamo + cronograma |
| 2 | Cliente/persona | ✅ | `Cliente.kt:1`, campo nombre en RegistroScreen |
| 3 | Monto inicial | ✅ | `Prestamo.montoInicial`, validación >0 |
| 4 | Cuotas 6/12/24 | ✅ | FilterChip 6/12/24 + `require` en Prestamo |
| 5 | 6→20% | ✅ | `CalculadoraPrestamo.kt:18` |
| 6 | 12→40% | ✅ | idem |
| 7 | 24→60% | ✅ | idem |
| 8 | Cálculo interés | ✅ | `calcularInteres = monto*pct` |
| 9 | Monto total | ✅ | `monto+interes` |
| 10 | Pago mensual | ✅ | `total/cuotas`, última ajustada |
| 11 | Fechas pago | ✅ | `LocalDate.plusMonths`, formato dd/MM/yyyy |
| 12 | Seguimiento saldo | ✅ | `saldoRestante` decrece, `toggleCuota` |
| 13 | Cronograma N°/Fecha/Cuota/Saldo/Estado | ✅ | `Cuota.kt` + `CronogramaScreen` LazyColumn + header |
| 14 | Cliente/Préstamo/Cuota/Lógica/UI separados | ✅ | Paquetes §11 |
| 15 | Kotlin | ✅ | 100% Kotlin |
| 16 | Flujo datos→interés→total→cuota→cronograma→saldo | ✅ | `Registro→Resumen→Cronograma` NavHost |
| 17 | Ejemplo 1200/6/20% → 240 interés,1440 total,240 cuota | ✅ | Test `CalculadoraPrestamoTest` pasa |
| 18 | Ejemplo saldo 1200-200→1000→800→600 | ✅ | Cronograma saldo 960/720/480/240/0 para 1200+20% |
| + | No asumir banco/Firebase/login/BD/XML obligado | ✅ | In-memory, sin login, sin Firebase, Compose opcional pero válido |
| + | Pantallas §6 | ✅ | Registro (nombre+monto+cuotas+chip+preview), Resumen (cliente+monto+interés+total+cuota), Cronograma (tabla+toggle) |
| + | Build verificable | ✅ | `./gradlew assembleDebug` + `testDebugUnitTest` |

## Riesgos "Por confirmar" (§15)
- Android Studio: proyecto es Android Studio-ready (Gradle 8.11.1, compileSdk 34)
- XML/Compose: Compose justificado en ADR
- BD: opcional, ya preparada para Room
- Login: no implementado (correcto per guía)

> Si falla un punto, el [[Agente-Arquitectura]] genera issue en `vault/01-Arquitectura/04-Verificacion-Guia.md` y propone `edit`.

