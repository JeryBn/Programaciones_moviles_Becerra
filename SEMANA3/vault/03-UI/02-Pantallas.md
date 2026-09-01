# Pantallas (Compose)

## Registro (`ui/screens/RegistroScreen.kt:1`) — Guía §6 Pantalla principal
- **Campos:** nombre (AccountCircle), monto (S/), selector cuotas (FilterChip 6/12/24), % visual, fecha inicio chip, vista previa en vivo (interés/total/cuota), botón Calcular, tabla referencia
- **Lógica preview:** `monto.toDoubleOrNull()?.let { calcularInteres/total/cuota }`
- **Validación:** delega a `CalculadoraPrestamo.validarEntrada`, muestra Card error

## Resumen (`ui/screens/ResumenScreen.kt:1`) — Guía §6 Sección resumen
- **Muestra:** Cliente, Monto inicial, Nº cuotas, Interés %, Interés S/, Total, Cuota, Fecha inicio, Pagado/Pendiente, cuotas pagadas
- **Acciones:** Ver cronograma, Eliminar, Back
- **Preview tabla:** primeras 3 cuotas del cronograma

## Cronograma (`ui/screens/CronogramaScreen.kt:1`) — Guía §6, §10
- **Tabla:** Header N°/Fecha/Cuota/Saldo/Estado + `LazyColumn` de `Cuota`
- **Row:** `numero | dd/MM/yyyy | S/ monto | S/ saldo | Icon Check/Radio + Pendiente/Pagada`
- **Interacción:** `onClick` → `toggleCuota(numero)` → actualiza `StateFlow` y color `secondaryContainer` vs `surfaceVariant`
- **Footer:** "Toca para marcar pagada... saldo disminuye (§4)" + header totals

## Historial (`ui/screens/HistorialScreen.kt:1`)
- **Extra:** no exigido pero útil para persistencia demo. Lista `Card` por préstamo: nombre, monto→total, cuotas (%), cuota | pagadas/total. Vacío → mensaje.

## Tema (`ui/theme/Theme.kt:1`)
`SistemaPrestamosTheme` Light: primary `#00695C` (teal), secondary teal-600, background `#F5F5F5`. Material3 `lightColorScheme/darkColorScheme`.

## Enlaces
- [[03-UI/01-Navegacion]]
- [[03-UI/03-ViewModel]]
