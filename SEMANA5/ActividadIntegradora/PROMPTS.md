# Registro de asistencia de IA

Esta actividad comienza desde cero con asistencia de Codex, autorizada por el estudiante el 23/09/2026. No constituye una fase realizada sin IA. Los commits registran etapas reales de esta sesión, sin alterar fechas ni atribuir trabajo autónomo no realizado.

## Solicitud inicial
Desarrollar la guía GLAB-ACTIVIDAD_SEMANA1-6 dentro de SEMANA5; utilizar cuatro requerimientos funcionales, Material 3, degradado, Scaffold, topBar, bottomBar, navegación explícita, LazyColumn y LazyRow. Documentar funcionamiento y prácticas de modificaciones para la sustentación.

## Aclaración del estudiante
Repositorio: JeryBn/Programaciones_moviles_Becerra. Mover el contenido anterior de SEMANA5 a Lab05 y separar la nueva actividad. Empezar desde cero con asistencia documentada.

## Decisiones y correcciones
- Se elige TECSUP Fit por corresponder a la bottomBar solicitada.
- Se conservan cuatro requerimientos agrupando las funciones de la guía; los componentes de interfaz son decisiones de implementación.
- El archivo vacío llamado Lab05 se conserva como Lab05-original.txt dentro de la carpeta del laboratorio.
- Se completan los archivos Gradle ausentes en el laboratorio anterior.
- Las comprobaciones y correcciones adicionales se documentan al finalizar.
- La revisión detectó un conflicto de concurrent-futures; se alineó a 1.2.0.
- Las pruebas en Android 17 fallaron con Espresso transitivo; al declarar 3.7.0 pasaron las cinco pruebas de interfaz.
- Lint detectó un atributo para API 27 y el escape de local.properties; ambos se corrigieron, conservando minSdk 24.
- Lab05 necesitó 2 GB para Gradle al compilar; se excluyeron sus volcados locales de memoria.

Las correcciones anteriores fueron realizadas por el asistente durante esta sesión. El estudiante debe revisarlas y explicar las que comprenda; no se atribuyen como correcciones manuales realizadas por él.
