# Tarea 03 Registro de Notas

Aplicación Android hecha con Jetpack Compose para registrar cuatro notas del ciclo y calcular su promedio ponderado.

**Estudiante:** Jery Becerra Ninaquispe  
**Curso:** Programación en Móviles  
**Docente:** Juan León Suiyon

## Funcionalidades

- Cuatro controles `Slider` con notas enteras de 0 a 20 y badge actualizado en vivo.
- Pesos fijos: Fundamentos 20%, POO 25%, Móviles 30% y Base de Datos 25%.
- `Switch` para redondear el promedio final y `Checkbox` de confirmación que habilita el cálculo.
- Tarjeta de resultado con promedio ponderado, promedio final, observación por rango y aporte de cada curso.
- Badge con semáforo, mensaje de confirmación y botón **LIMPIAR**.

## Ejecutar

1. Abre esta carpeta (`Tarea03RegistroNotas`) con Android Studio.
2. Espera la sincronización de Gradle y ejecuta la aplicación en un emulador o dispositivo Android.

## Casos de prueba

| Notas (F, POO, M, BD) | Redondear | Ponderado | Final | Resultado |
| --- | --- | ---: | ---: | --- |
| 15, 13, 16, 14 | Sí | 14.55 | 15 | APROBADO |
| 12, 10, 11, 9 | No | 10.45 | 10.45 | EN RECUPERACIÓN |
| 18, 17, 19, 18 | Sí | 18.05 | 18 | EXCELENTE |
| 8, 9, 7, 10 | No | 8.45 | 8.45 | DESAPROBADO |
