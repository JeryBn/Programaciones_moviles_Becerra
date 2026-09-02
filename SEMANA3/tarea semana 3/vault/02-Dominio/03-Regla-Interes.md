# Regla de Interés — Pizarra §7

> **Norma central del sistema.** El porcentaje depende de la cantidad de cuotas.

| Cuotas | Interés | Decimal |
|---|---|---|
| 6 | 20% | 0.20 |
| 12 | 40% | 0.40 |
| 24 | 60% | 0.60 |

## Código
`CalculadoraPrestamo.kt:18`
```kotlin
private val reglaInteres = mapOf(6 to 0.20, 12 to 0.40, 24 to 0.60)
fun obtenerPorcentaje(cuotas:Int) = reglaInteres[cuotas] ?: error("no soportado")
```

## Edición futura
Si el profesor agrega `18 cuotas → 50%`, el [[Agente-Arquitectura]] hace:
1. Edita `vault/02-Dominio/03-Regla-Interes.md` (esta tabla)
2. Edita `domain/calculator/CalculadoraPrestamo.kt:18` → `mapOf(...,18 to 0.50)`
3. Edita `ui/screens/RegistroScreen.kt` → `listOf(6,12,18,24)` y tabla visual
4. Ejecuta `vault/01-Arquitectura/04-Verificacion-Guia.md` update + tests

No tocar otra lógica — interés/total/cuota se recalculan solos.
