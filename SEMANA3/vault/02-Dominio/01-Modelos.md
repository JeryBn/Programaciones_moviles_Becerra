# Modelos de Dominio

## Cliente (`domain/model/Cliente.kt:1`)
```kotlin
data class Cliente(val id: Long = 0, val nombre: String)
// require(nombre.isNotBlank())
```

## Cuota (`domain/model/Cuota.kt:1`)
```kotlin
enum class EstadoCuota { PENDIENTE, PAGADA }
data class Cuota(
  val numero: Int,
  val fechaVencimiento: LocalDate,
  val montoCuota: Double,
  val saldoRestante: Double,
  val estado: EstadoCuota = PENDIENTE
)
```

## Prestamo (`domain/model/Prestamo.kt:1`)
```kotlin
data class Prestamo(
  val cliente: Cliente,
  val montoInicial: Double,
  val numeroCuotas: Int, // 6|12|24
  val porcentajeInteres: Double, // 0.2/0.4/0.6
  val interesGenerado: Double,
  val montoTotal: Double,
  val pagoMensual: Double,
  val fechaInicio: LocalDate,
  val cronograma: List<Cuota>
) {
  val totalPagado: Double
  val saldoPendiente: Double
}
```

## Invariantes
- `montoInicial > 0`
- `numeroCuotas in {6,12,24}` — si no, `IllegalArgumentException`
- `cronograma.size == numeroCuotas`

Ver [[02-Dominio/02-Calculadora]] para cómo se pueblan.
