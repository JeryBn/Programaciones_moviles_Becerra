# Prompts utilizados — ChatGPT

## Herramienta y autoría
Se usó ChatGPT, no Gemini, por solicitud del estudiante. La implementación de main también fue asistida. No se afirma que la fase inicial haya sido elaborada sin IA ni se presenta como importación del ZIP docente.

## Solicitud principal
**Texto real del usuario:** «lee la guia completa y hazlo, tengo el android studio abierto elaboralo ahi ... haz los commits en el repositorio ... tiene que haber un valor agregado laboratorio 8».
**Qué generó la IA:** proyecto reconstruido conforme a las cuatro tablas, DAOs, Repository interface, ViewModels, pantallas Compose, navegación y preferencias.
**Qué se corrigió durante la revisión:** configuración de Gradle/JDK, BOM de pruebas, Espresso compatible con Android del emulador y selector de prueba que confundía título con botón.
**Respeta MVVM:** la UI observa ViewModels; acceso Room solo en implementación del Repository. Sesión también observa preferencias DataStore, según el contrato de la guía.

## Mejora 1 — buscar productos
**Instrucción de trabajo:** agregar búsqueda reactiva combinada con categoría en MenuViewModel.
**Generado:** StateFlow busqueda y combine de productos, categoría y texto; campo de UI enlazado al VM.
**Revisión:** se normalizaron tildes, mayúsculas y espacios; se mantuvo el filtro fuera del Composable.
**MVVM:** sí; no se accede al DAO desde MenuScreen.

## Mejora 2 — repetir pedido
**Instrucción de trabajo:** cargar el carrito desde el historial conservando identidad de producto y validando disponibilidad actual.
**Generado:** botón de historial y evento repetir en MenuViewModel.
**Corrección:** la primera versión consultaba un StateFlow que podía conservar datos antiguos tras dejar de observar el menú. Se cambió a una lectura fresca del Repository antes de repetir; se rechaza el pedido completo si falta un producto o está agotado y se usan precios actuales.
**MVVM:** sí; ViewModel conoce solo la interface CafeRepository.

## Mejora 3 — gráfico por hora
**Instrucción de trabajo:** calcular pedidos del día por hora con Room y mostrarlos en el panel sin datos fijos.
**Generado:** consulta agregada SQL, modelo PedidosPorHora, firma en interface Repository, delegación, StateFlow y gráfico de barras.
**Revisión:** se incluyó límite superior del día y se excluyeron cancelados; etiquetas textuales permiten leer el gráfico.
**MVVM:** sí; DAO → Repository → ViewModel → View.

## Valor agregado — Mi recreo
**Petición real:** «un valor agregado laboratorio 8 que sea unico».
**Diseño aplicado:** selector de 5/10/15/20 minutos y estimación reactiva según pedidos Recibido y Preparando, conectada con DataStore para recepción abierta/cerrada.
**Supuestos:** dos estaciones; cuatro minutos por recibido, dos por preparando y cuatro para la nueva compra. Se trata de un modelo educativo; no mide el rendimiento real ni reserva turnos.
**Revisión:** se añadieron pruebas de cola vacía, cola larga, límite exacto, cafetería cerrada y entradas inválidas. No se promete exclusividad mundial.
**MVVM:** sí; algoritmo puro en model, consulta en Repository, combine en MiRecreoViewModel, UI solamente observa.

## Reflexión propuesta — revisar con palabras propias
La asistencia aceleró la creación y permitió mantener el contrato del Repository.
La prueba inicial confundió dos nodos con el texto Confirmar pedido.
La corrección seleccionó el botón por su acción, no por el texto solamente.
Room y Flow permiten compartir estados sin recargas manuales.
Una estimación necesita supuestos explícitos y no debe confundirse con una garantía.

## Historial
Un commit y push por cada avance; los commits ia: se separaron por mejora. Las pruebas y capturas reflejan resultados reales y VERIFICACION.md conserva su alcance.

