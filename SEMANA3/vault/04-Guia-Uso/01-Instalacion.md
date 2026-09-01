# Instalación y Requisitos

## Requisitos
- **Android Studio Ladybug+ 2024.2+** (o Hedgehog)
- **JDK 17** (Gradle toolchain lo descarga si no está; soporta 25/26 pero usa 17 para build)
- **Android SDK:** `platforms;android-34`, `build-tools;36.0.0` (ya tienes `android-35/37` + `34.0.0/36.0.0`)
- **Gradle:** `8.11.1` vía wrapper (`gradlew`)
- **Kotlin:** `2.0.21`

## Pasos
1. Abrir `File > Open` → carpeta `programacion en moviles Open Code` (root `settings.gradle.kts`)
2. `Sync Now` (descarga dependencias: Compose BOM 2024.12.01, Navigation 2.8.5, Room 2.6.1, Activity 1.9.3)
3. **Si falla por JDK 25/26:** en `Settings > Build > Gradle > Gradle JDK` selecciona `17` o `Android Studio JBR 25` con toolchain 17 (ya configurado en `app/build.gradle.kts: compileOptions VERSION_17`)
4. Verificar SDK: `File > Project Structure > SDK Location` → `C:\Users\Chory\AppData\Local\Android\Sdk`

## Vault Obsidian (opcional pero integrado)
- Abrir Obsidian → `Open folder as vault` → selecciona `vault/`
- Plugins recomendados: `Dataview` (tablas dinámicas), `Excalidraw/Canvas` para diagramas
- `00-Home.md` es índice; `Graph View` muestra arquitectura enlazada

## Agente de Arquitectura
- Definido en `.opencode/agents/architecture-agent.md`
- Permisos: editar `vault/**` + `app/src/**/domain/**` + `app/src/**/ui/**` + `docs/**`
- Uso: `opencode run agent-architecture "cambia regla a 18 cuotas 50%"` — edita código + vault + verificación

Ver [[04-Guia-Uso/03-Build-Run]] para ejecución.
