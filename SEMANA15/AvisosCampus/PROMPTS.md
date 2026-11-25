# Asistencia de IA

La solicitud principal fue completar AvisosCampus y documentar sus commits. La asistencia provino de ChatGPT. No se ejecutó una sesión con Gemini.

| Decisión técnica | Propuesta | Adaptación | Razón |
| --- | --- | --- | --- |
| Separar recordatorios | Crear el canal Urgentes con importancia alta. | El origen indica urgencia y el notificador selecciona el canal. | El modelo sigue sin depender de Android. |
| Mostrar texto completo | Usar BigTextStyle. | El texto del recordatorio también queda en el historial. | El aviso conserva el mismo contenido al expandirse. |
| Detectar alimentación | Registrar un receptor dinámico. | El switch controla el registro y onDispose lo da de baja. | El ViewModel recibe un booleano y el receptor sigue el ciclo de la pantalla. |

Para comprobar las mejoras, expande un recordatorio, verifica su canal en Ajustes y conecta y desconecta la alimentación con la escucha activada y desactivada.
