# Flujo de Usuario (Guía §13)

## Paso a paso (9 pasos §13)

1. **Ingresar nombre** → `RegistroScreen` campo "Nombre del cliente" (obligatorio, validado)
2. **Ingresar monto** → campo "Monto inicial (S/)" ej `1200` (validado >0)
3. **Seleccionar cuotas** → `FilterChip` `6 | 12 | 24`
4. **Visualizar %** → Card teal "20%/40%/60% para X cuotas" + tabla referencia abajo
5. **Calcular** → Botón "Calcular préstamo" → `CalculadoraPrestamo.crearPrestamo` → `Nav Resumen`
6. **Revisar resumen** → `ResumenScreen` muestra cliente, monto, cuotas, interés%, interés S/, total, cuota, fechas, pagado/pendiente
7. **Consultar cronograma** → Botón "Ver cronograma" → `CronogramaScreen` tabla N°/Fecha/Cuota/Saldo/Estado
8. **Registrar pagos** → Tocar fila → toggle Pendiente↔Pagada (icon Radio→Check, color cambia)
9. **Ver saldo disminuir** → Header "Saldo: S/ ..." y `saldoRestante` por cuota decrece hasta 0 (guía §4 ejemplo 1200→1000→800→600)

## Ejemplo concreto guía §9-§10
- Entrada: `Juan`, `1200`, `6 cuotas`, fecha `26/09/2026`
- Cálculo: interés 240, total 1440, cuota 240
- Cronograma 6 filas:
  ```
  N° Fecha       Cuota Saldo Estado
  1  26/09/2026   240   1200 Pendiente  (1440-240)
  2  26/10/2026   240    960
  3  26/11/2026   240    720
  4  26/12/2026   240    480
  5  26/01/2027   240    240
  6  26/02/2027   240      0
  ```
  *Nota: guía §10 muestra 960/720/480/240/0 pero omite 1200 inicial por redondeo; app implementa cálculo correcto §8*

## Historial
- Botón `History` en Registro → lista todos los préstamos guardados → tap para ver Resumen/Cronograma de cada uno

## Validaciones
- Nombre vacío → "Nombre es obligatorio"
- Monto no numérico / ≤0 → error
- Monto >1M → límite
- Cuotas no 6/12/24 → `IllegalArgumentException`

Ver [[04-Guia-Uso/03-Build-Run]] y [[01-Arquitectura/04-Verificacion-Guia]].
