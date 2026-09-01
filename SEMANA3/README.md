# Sistema de Préstamos en Kotlin — Guía de trabajo completada
<img width="1919" height="1035" alt="image" src="https://github.com/user-attachments/assets/71aa0917-806c-4df3-bc01-f45bc2c371d1" />


> **Proyecto:** Aplicación móvil Kotlin para gestionar préstamos y cronograma de pagos (Guía §1-§18)  
> **Stack:** Kotlin 2.0.21 · AGP 8.7.3 · Gradle 8.11.1 · Compose Material3 · Navigation · Room-ready · MVVM  
> **APK:** `app/build/outputs/apk/debug/app-debug.apk` (BUILD SUCCESSFUL)  
> **Tests:** 10/10 pasados (`CalculadoraPrestamoTest`)  
> **Vault Obsidian:** `vault/00-Home.md` (Graph View) + Agente de Arquitectura

## Agentes de IA

| Agente | Archivo | Uso |
|---|---|---|
| **Architecture** ⭐ | `.opencode/agents/architecture-agent.md` | Ve y edita vault + domain + ui; re-verifica guía |
| Domain | `.opencode/agents/domain-agent.md` | Modelos y `CalculadoraPrestamo` |
| UI | `.opencode/agents/ui-agent.md` | Pantallas Compose |
| QA | `.opencode/agents/qa-agent.md` | Build + tests + informe |

**Vault:** `vault/` — abrir en Obsidian como vault. Ver `docs/INFORME-DETALLADO.md`.

## Quick start

```powershell
$env:JAVA_HOME="C:\Users\Chory\Desktop\programacion en moviles Open Code\jdk-17"
.\gradlew.bat assembleDebug        # APK debug
.\gradlew.bat testDebugUnitTest    # tests
.\gradlew.bat installDebug         # en dispositivo
```

Android Studio: `Open → raíz (settings.gradle.kts) → Sync`.

## Estructura

```
app/src/main/java/com/prestamos/sistema
  domain/model (Cliente, Prestamo, Cuota)
  domain/calculator (CalculadoraPrestamo 6→20% 12→40% 24→60%)
  data (PrestamoRepository StateFlow)
  ui/screens (Registro, Resumen, Cronograma, Historial)
  ui/viewmodel (PrestamoViewModel)
  ui/navigation (Screen), ui/theme, MainActivity
vault/ 00-Home + 01-Arquitectura + 02-Dominio + 03-UI + 04-Guia-Uso + Agente-Arquitectura
docs/INFORME-DETALLADO.md
```

Informe completo: `docs/INFORME-DETALLADO.md`. Verificación guía: `vault/01-Arquitectura/04-Verificacion-Guia.md`.
