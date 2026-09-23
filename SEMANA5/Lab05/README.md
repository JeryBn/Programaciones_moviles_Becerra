# Lab05 - Aplicación Móvil Tecsup (Jetpack Compose UI/UX Redesign)

Esta aplicación Android desarrollada con **Kotlin** y **Jetpack Compose** ha sido completamente rediseñada bajo las pautas de **Material Design 3**. Proporciona una interfaz moderna, limpia, minimalista e intuitiva manteniendo al 100% la lógica de negocio, arquitectura y flujo de navegación original.

---

## 📱 Pantallas de la Aplicación

### 1. Pantalla de Inicio (`HomeScreen`)
* **Header / TopAppBar:** Muestra la identidad académica de Tecsup con un ícono institucional y un banner decorativo.
* **Componentes:**
  * Insignia decorativa con ícono de tecnología (`Icons.Rounded.Code`).
  * Título principal y subtítulo de bienvenida.
  * Tarjetas interactivas modernas (`PrimaryActionCard`) con íconos personalizados para acceder a la **Lista de elementos** y al **Perfil de usuario**.
  * Botón de acción rápido.

### 2. Pantalla de Lista (`ListScreen`)
* **TopAppBar:** Barra superior moderna con botón de regreso y contador dinámico de elementos.
* **Componentes:**
  * `LazyColumn` optimizada con separación vertical adecuada (`12.dp`).
  * Cada elemento se renderiza dentro de una tarjeta elevada (`ElementCard`) con bordes redondeados (`18.dp`).
  * Muestra el título del elemento, subtítulo descriptivo, un ícono distintivo y un *chevron* indicador de navegación hacia el detalle.
  * Mantiene el paso exacto del parámetro de identificación al hacer clic.

### 3. Pantalla de Detalle (`DetailScreen`)
* **TopAppBar:** Permite retornar a la lista de elementos mediante la flecha de navegación.
* **Componentes:**
  * Tarjeta de encabezado estilo *Hero* con un distintivo que destaca el identificador o texto recibido (`"Elemento #$itemId"`).
  * Tarjeta contenedora de información que organiza los datos en secciones (`InfoRow`):
    * **ID / Texto recibido:** Parámetro capturado de la navegación.
    * **Estado:** Indicador dinámico visual ("Disponible").
    * **Categoría:** Etiqueta del componente ("Componente Tecsup").
  * Botón inferior delineado para regresar a la lista fácilmente.

### 4. Pantalla Mi Perfil (`ProfileScreen`)
* **Fondo:** Degradado suave y elegante en tonos violetas (`#4A2580` $\rightarrow$ `#6C4AB6` $\rightarrow$ `#8B5CF6`).
* **Componentes:**
  * Avatar circular con borde translúcido e ícono de usuario.
  * Título de perfil e insignia de rol ("Estudiante Tecsup").
  * Tarjeta de información personal con datos académicos:
    * **Nombre:** Estudiante
    * **Institución:** Tecsup
    * **Estado Académico:** Activo / En Cursado
  * Botón flotante blanco con ícono de inicio que redirige a la pantalla principal desapilando el historial (`popUpTo`).

---

## 🎨 Sistema de Diseño (Material Design 3)

La paleta de colores oficial configurada en `ui/theme/Color.kt` y `ui/theme/Theme.kt` es:

| Rol de Color | Valor Hexadecimal | Uso en la Aplicación |
| :--- | :--- | :--- |
| **Primary** | `#6C4AB6` | Botones principales, íconos de énfasis y títulos de tarjetas. |
| **Primary Container** | `#EEE7FF` | Fondos de íconos badges e insignias destacadas. |
| **Secondary** | `#8B5CF6` | Elementos secundarios y degradados de perfil. |
| **Secondary Container**| `#F3E8FF` | Fondos de contenedores secundarios. |
| **Background** | `#F8F7FC` | Fondo claro y limpio de la aplicación. |
| **Surface** | `#FFFFFF` | Tarjetas elevadas y contenedores de información. |
| **On Surface / Text** | `#1D1B20` | Texto principal de alta legibilidad. |
| **On Surface Variant** | `#66616D` | Subtítulos y etiquetas secundarias. |

---

## 🛠️ Componentes Reutilizables (`components/AppComponents.kt`)

* **`PrimaryActionCard`:** Tarjeta interactiva con elevado suave, ícono contenedor y flecha indicadora de navegación.
* **`ElementCard`:** Componente de ítem para listas dentro de `LazyColumn`.
* **`InfoRow`:** Fila estructurada para mostrar pares clave-valor acompañados de un ícono identificador.

---

## 🚀 Requisitos e Instalación

### Requisitos Previos
* **Android Studio:** Jellyfish / Koala / Ladybug o posterior.
* **JDK:** Usar el JDK incluido en Android Studio, compatible con AGP 9.3.3 y Gradle 9.5.0.
* **Android SDK:** Compile SDK 37 (o Target SDK 34/37).
* **Min SDK:** 24 (Android 7.0 Nougat o superior).

---

## 💻 Pasos para Ejecutar la Aplicación

1. **Clonar o abrir el proyecto en Android Studio:**
   * Inicia Android Studio y selecciona `File > Open...`.
   * Selecciona la carpeta del proyecto `SEMANA5/Lab05`.

2. **Sincronizar Gradle:**
   * Espera a que Gradle descargue las dependencias necesarias (`androidx.navigation:navigation-compose`, `androidx.compose.material3`, `material-icons-extended`).

3. **Ejecutar en Emulador o Dispositivo Físico:**
   * Asegúrate de tener configurado un dispositivo virtual (AVD) o conectar un celular por depuración USB.
   * Haz clic en el botón verde **Run 'app'** (o presiona `Shift + F10`).

---

## 📂 Estructura del Proyecto

```
app/src/main/java/com/example/lab05/
├── MainActivity.kt          # Actividad principal configurada con Lab05Theme
├── AppNavigation.kt         # Grafo de navegación (NavHost)
├── Screen.kt                # Rutas selladas (Screen.Home, Screen.List, Screen.Profile, Screen.DetailScreen)
├── HomeScreen.kt            # Pantalla principal con accesos rápidos
├── ListScreen.kt            # Pantalla de catálogo en LazyColumn
├── DetailScreen.kt          # Pantalla de detalle con ID/texto dinámico
├── ProfileScreen.kt         # Pantalla de perfil de estudiante con degradado
├── components/
│   └── AppComponents.kt     # Componentes reutilizables M3
└── ui/theme/
    ├── Color.kt             # Paleta de colores M3 Tecsup
    ├── Theme.kt             # Configuración del tema claro y oscuro
    └── Type.kt              # Estilos de tipografía
```

