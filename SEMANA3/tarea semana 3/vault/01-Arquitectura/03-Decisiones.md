# 03 — Decisiones de Arquitectura (ADR)

## ADR-001: Compose vs XML
- **Decisión:** Compose + Material3
- **Contexto:** Guía §14 dice "No asumir XML o Compose". Ambas válidas, pero Compose es canvas moderno y reduce boilerplate.
- **Consecuencia:** `ui/screens/*.kt` 100% Compose, sin layouts XML.

## ADR-002: Regla de interés hardcodeada
- **Decisión:** `mapOf(6 to 0.20, 12 to 0.40, 24 to 0.60)` en `CalculadoraPrestamo`.
- **Rationale:** Pizarra §7 es norma inmutable. Si cambia, un solo `edit` en `domain/calculator/CalculadoraPrestamo.kt:18`.
- **Agente:** [[Agente-Arquitectura]] puede editar esta tabla vía prompt: "cambia regla a 6→15%".

## ADR-003: Cálculo con redondeo a 2 decimales
- **Fórmula pizarra §8-§9:** `interes = monto * pct`, `total = monto+interes`, `cuota = total/cuotas`, `saldo -= cuota`.
- **Problema flotante:** `1200/6=200` pero `1440/6=240` exacto; con otros montos hay centavos. Se usa `round(x*100)/100` y última cuota absorbe residuo.
- **Test:** `1200,6 → 240, total 1440` coincide con guía §9.

## ADR-004: Fechas mensuales
- **Decisión:** `fechaInicio.plusMonths(i-1)` — genera cronograma §5 y §10.
- **Formato display:** `dd/MM/yyyy` (26/09/2026 ejemplo).
- **Editable:** pasar `LocalDate` distinta a `crearPrestamo`.

## ADR-005: Persistencia
- **Estado:** In-memory. Guía §11 dice "Persistencia, solo si el profesor la exige".
- **Evolución:** Añadir `room` ya está en `build.gradle.kts` (dependencias listas, KSP). Solo faltaría `@Entity @Dao @Database`. El `Repository` abstrae el cambio.

## ADR-006: Validación
- `CalculadoraPrestamo.validarEntrada` centraliza errores de nombre/monto. UI solo muestra `StateFlow.error`.

## Para editar (Agente)
Cualquier ADR puede ser revisado por el agente: edita `vault/01-Arquitectura/03-Decisiones.md` y el archivo Kotlin correspondiente en una sola transacción.
