# Build, Run y Tests

## Comandos (PowerShell desde root)
```powershell
# Wrapper hace download de Gradle 8.11.1 si no existe
.\gradlew.bat assembleDebug          # APK en app\build\outputs\apk\debug\app-debug.apk
.\gradlew.bat testDebugUnitTest      # tests JUnit (CalculadoraPrestamoTest)
.\gradlew.bat connectedDebugAndroidTest  # tests instrumentados (requiere emulador/dispositivo)
.\gradlew.bat installDebug           # instala en dispositivo conectado (adb)
```

## Emulador
```powershell
# Lista AVDs
& "$env:LOCALAPPDATA\Android\Sdk\emulator\emulator.exe" -list-avds
# Inicia (ej: Pixel_7_API_34)
& "$env:LOCALAPPDATA\Android\Sdk\emulator\emulator.exe" -avd Pixel_7_API_34
# adb
& "$env:LOCALAPPDATA\Android\Sdk\platform-tools\adb.exe" devices
```

## Tests incluidos
- `app/src/test/java/com/prestamos/sistema/CalculadoraPrestamoTest.kt` — 9 tests:
  - 6→20%,12→40%,24→60%
  - 1200+6→240/1440/240
  - validación nombre/monto
  - cronograma 6/12 cuotas fechas + saldo 0 final
  - toggle cuota
- Ejecutar: `.\gradlew.bat testDebugUnitTest --info` → reporte en `app\build\reports\tests\testDebugUnitTest\`

## Build troubleshooting
| Error | Fix |
|---|---|
| `JAVA_HOME invalid` | ` $env:JAVA_HOME="C:\Program Files\Android\Android Studio\jbr"` o selecciona JBR 25 en Android Studio |
| `SDK not found` | `local.properties` debe tener `sdk.dir=C\:\\Users\\Chory\\AppData\\Local\\Android\\Sdk` (se genera auto) |
| `KSP` | Ya incluido `id("com.google.devtools.ksp") version "2.0.21-1.0.28"` |
| `compileSdk 34 not found` | `sdkmanager --install "platforms;android-34"` |

## APK para entrega
- `app-debug.apk` listo para instalar o subir a Drive/Classroom
- Para release firmada: `.\gradlew.bat assembleRelease` + keystore

Ver [[04-Guia-Uso/01-Instalacion]] y `docs/INFORME-DETALLADO.md` para reevaluación post-build.
