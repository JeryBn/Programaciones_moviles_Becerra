# Calculadora de Préstamo

**Archivo:** `domain/calculator/CalculadoraPrestamo.kt:1`

## API
| Función | Firma | Descripción |
|---|---|---|
| `obtenerPorcentaje` | `(cuotas:Int)->Double` | 6→0.20,12→0.40,24→0.60 |
| `calcularInteres` | `(monto,cuotas)->Double` | `monto * pct` |
| `calcularMontoTotal` | `(monto,interes)->Double` | `monto+interes` |
| `calcularPagoMensual` | `(total,cuotas)->Double` | `total/cuotas` |
| `generarCronograma` | `(total,cuota,n,fecha)->List<Cuota>` | `plusMonths`, última cuota ajusta centavos |
| `crearPrestamo` | `(nombre,monto,cuotas,fecha)->Prestamo` | Orquesta todo |
| `toggleCuota` | `(Prestamo,numero)->Prestamo` | Pendiente↔Pagada, actualiza saldo |

## Ejemplo guía §9
```kotlin
val p = CalculadoraPrestamo.crearPrestamo("Juan", 1200.0, 6, LocalDate.of(2026,9,26))
// p.interesGenerado == 240.0
// p.montoTotal == 1440.0
// p.pagoMensual == 240.0
// p.cronograma[0] == Cuota(1, 26/09/2026, 240, 1200? -> 960, PENDIENTE)
// Real: saldo inicial 1440, tras cuota1 saldo 1200? No, lógica es total-sumaCuotas: 1440-240=1200? Corregido: cronograma saldo=total-cuota*num → 1200? Para 1440: 1440-240=1200, pero guía §10 muestra 960 si monto inicial 1200+240 total 1440? En ejemplo §10: 1440/6=240, saldo 960 (1440-240=1200? Espera: guía §10 tabla dice 960 tras primera cuota de 240 sobre total 1440 → debería ser 1200. Hay inconsistencia en guía entre §4 (1200 sin interés) y §10 (1200+20%). Se implementó lógica correcta §10: saldo = total - cuota*índice. Para §4 demo sin interés, saldo 1200-200=1000. Ambas válidas.
// Implementación genera saldo = total - pagoMensual*índice => 1440-240=1200? Pero tabla §10 dice 960 para primer saldo? Revisar: §10 N1 240 saldo 960 es incoherente con 1440-240=1200. Parece que §10 partió de 1200+20%=1440 y luego saldo 1440-480=960 asumiendo 2 cuotas? Error de guía. Se deja cálculo matemático puro correcto y se documenta divergencia en informe. La app muestra saldo correcto 1200/960 según escenario.
```

## Redondeo
`round(x*100)/100` evita `0.999999`. Última cuota = saldo restante exacto.

## Validación
`validarEntrada(nombre,montoStr): List<String>` — usada por ViewModel.

[[02-Dominio/03-Regla-Interes]] — detalle regla.
