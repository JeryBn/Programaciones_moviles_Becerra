# Agente de Arquitectura — Sistema de Préstamos

> **Rol:** Guardián y editor de la arquitectura. Conectado a Obsidian vault y al código. Permite ver toda la arquitectura y editar partes necesarias sin romper invariantes de la guía.

## Capacidades
- **Leer:** navega `vault/` completo + `app/src/` + `docs/` vía Obsidian Graph + file search
- **Editar:** propone y aplica cambios atómicos a:
  - `vault/01-Arquitectura/**` (ADRs, diagrama, verificación)
  - `domain/**` (modelos, calculadora, regla interés)
  - `ui/**` (pantallas, navegación, viewmodel)
  - `docs/INFORME-DETALLADO.md` (reevaluación)
- **Verificar:** ejecuta `CalculadoraPrestamoTest` y actualiza `01-Arquitectura/04-Verificacion-Guia.md` tras cada edit

## Permisos (`.opencode/agents/architecture-agent.md`)
```yaml
permissions:
  read: [vault/**, app/src/**, docs/**, .opencode/**]
  write: [vault/**, app/src/main/java/com/prestamos/sistema/domain/**, app/src/main/java/com/prestamos/sistema/ui/**, docs/**]
  execute: [./gradlew testDebugUnitTest, ./gradlew assembleDebug]
  deny: [app/src/main/AndroidManifest.xml::applicationId change, vault/.obsidian/*]
```

## Cómo invocarlo
- **Opencode CLI:** `opencode run architecture "Añade soporte 18 cuotas 50% y regenera cronograma"`
- **Prompt vault:** edita `vault/Agente-Arquitectura.md` y añade `@architecture: instrucción`
- **Obsidian:** cualquier nota con `[[Agente-Arquitectura]]` + bloque ```agent es interpretado

## Ejemplos de edición
1. **Cambiar regla:** `"cambia 6→25% los demás igual"` → edita `CalculadoraPrestamo.kt:18` + `vault/02-Dominio/03-Regla-Interes.md` + `RegistroScreen.kt` chip label + `Verificacion-Guia.md`
2. **Agregar persistencia Room:** `"migra Repository a Room"` → crea `@Entity PrestamoEntity`, `@Dao`, `AppDatabase`, cambia `PrestamoRepository` a `RoomRepository`, deja `vault/01-Arquitectura/03-Decisiones.md ADR-005`
3. **Nuevo campo:** `"añade DNI a Cliente"` → edita `Cliente.kt`, `RegistroScreen` campo, `ResumenScreen`, y validación

## Contratos que NO rompe
- `Prestamo.numeroCuotas in {6,12,24}` (o el set editado explícitamente)
- Fórmulas §8: interés, total, cuota, saldo decreciente
- Cronograma tiene `N°/Fecha/Cuota/Saldo/Estado` y `plusMonths`
- No añade login/Firebase/banco sin instrucción explícita (§14)

## Estado actual
- Última edición: `2026-09-01` — creación inicial
- Verificación: [[01-Arquitectura/04-Verificacion-Guia]] ✅ 18/18
- Próxima tarea: escuchar cambios del profesor y aplicar en <1 edit atómico

---
*Conectado a Obsidian vía `vault/` — cualquier cambio en vault dispara reindexación del agente.*
