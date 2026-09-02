# ViewModel

**Archivo:** `ui/viewmodel/PrestamoViewModel.kt:1`

## Estado
```kotlin
data class PrestamoUiState(
  val prestamoActual: Prestamo? = null,
  val historial: List<Prestamo> = emptyList(),
  val error: String? = null,
  val mensaje: String? = null
)
```

## Dependencia
`PrestamoRepository` (in-memory `StateFlow<List<Prestamo>>`). Se inyecta por constructor, default `PrestamoRepository()`.

## Métodos
| Método | Flujo |
|---|---|
| `calcularYGuardar(nombre,montoStr,cuotas,fecha)` | valida → `crearPrestamo` → `repository.guardar` → actualiza `prestamoActual` + `historial` |
| `seleccionarPrestamo(id)` | lookup `repository.obtenerPorId` |
| `toggleCuota(numero)` | `CalculadoraPrestamo.toggleCuota(actual, num)` → `repository.actualizar` |
| `eliminarActual()` | `repository.eliminar(id)` |
| `limpiarError()` | null error/mensaje |

## Observación en UI
```kotlin
val viewModel: PrestamoViewModel = viewModel()
val uiState by viewModel.uiState.collectAsState()
val historial by viewModel.prestamosFlow.collectAsState()
```

- `RegistroScreen.onCalcular` llama `calcularYGuardar` y si `ok` navega a `Resumen`.
- `CronogramaScreen.onToggleCuota` llama `toggleCuota` y recompone por `StateFlow`.

## Testeabilidad
`PrestamoViewModel(repository)` permite inyectar repo de test y probar sin Android (ver `app/src/test`).
