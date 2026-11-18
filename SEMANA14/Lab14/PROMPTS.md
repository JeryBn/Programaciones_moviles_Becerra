# Registro de asistencia · CentroTareas

Asistente utilizado en esta entrega: Codex. La solicitud del usuario fue implementar la guía, renovar toda la interfaz y publicar el proyecto en `SEMANA14`.

| Encargo trabajado | Propuesta asistida | Implementación | Motivo técnico |
| --- | --- | --- | --- |
| «Mantener las funcionalidades y requerimientos de la guía con una interfaz renovada». | Construir tres paneles de Compose con estados expuestos por el ViewModel. | Interfaz oscura, control Tócame fijo y tarjetas para tareas, primos y cargas. | El contador permite observar la respuesta durante cualquier operación. |
| Adaptar la mejora de progreso desde el repositorio. | Publicar los pasos como un Flow que corre en Default. | El ViewModel lo consume en un Job y conserva el último avance al cancelar. | La ejecución queda en el repositorio y el estado de pantalla en el ViewModel. |
| Adaptar la mejora de primos con progreso. | Enviar actualizaciones acotadas por porcentaje mientras se comprueba cancelación. | La barra usa StateFlow y el cálculo se mantiene en Default. | Se limita la frecuencia de actualización y se evita saturar la interfaz. |
| Adaptar la mejora de comparación de tiempos. | Guardar mediciones por modo durante la sesión. | Una tabla muestra hasta cinco resultados por columna con la carga más reciente primero. | Permite comparar tiempos bajo las mismas condiciones del dispositivo. |
