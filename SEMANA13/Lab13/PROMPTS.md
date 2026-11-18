# Registro de asistencia · CampusGPS

Asistente utilizado en esta entrega: Codex. La solicitud del usuario fue implementar la guía, renovar toda la interfaz y publicar el proyecto en `SEMANA13`.

| Encargo trabajado | Propuesta asistida | Implementación | Motivo técnico |
| --- | --- | --- | --- |
| «Mantener las funcionalidades y requerimientos de la guía con una interfaz renovada». | Mantener Compose y MVVM, encapsular Fused Location y ordenar las distancias en el ViewModel. | Paleta azul, panel de mapa redondeado, tarjetas con selección y errores con acciones. | La UI renderiza estados y la obtención de ubicación queda detrás de una interfaz. |
| Adaptar la mejora de filtros por categoría. | Derivar la lista visible combinando filtro y catálogo con distancias. | La misma lista alimenta mapa, tarjetas y candidato más cercano. | Los tres elementos concuerdan al cambiar el filtro. |
| Adaptar la mejora de radio. | Derivar los identificadores dentro del umbral y dibujar el círculo desde los valores de estado. | Selector de 100, 200 o 500 m y etiqueta en las tarjetas correspondientes. | La lógica de cercanía queda en el ViewModel. |
| Adaptar la mejora de ubicación nativa. | Condicionar la capa y su botón al permiso vigente. | Comprobación inicial, al regresar de ajustes y tras el diálogo de permisos. | La app solicita el consentimiento antes de activar la capa. |
