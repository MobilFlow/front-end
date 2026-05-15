# AutoMatch - Aplicación Móvil Android

## Mobile Application for Automotive Assistance

AutoMatch is a mobile application designed to connect drivers with trusted mechanics quickly, safely, and efficiently through a digital platform based on geolocation, reputation systems, and intelligent recommendations.

The application helps users find nearby mechanics, review ratings and reviews, request automotive services, and improve trust in the automotive service process through a modern mobile experience.

---

# Problem Statement

Many drivers experience difficulties finding reliable mechanics due to the lack of centralized digital platforms, limited transparency in service quality, and informal recommendation methods. AutoMatch addresses this problem by providing a mobile solution that improves accessibility, trust, and efficiency in the automotive ecosystem.

---
[![Android](https://img.shields.io/badge/Android-35-brightgreen?logo=android)](https://developer.android.com/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.21-purple?logo=kotlin)](https://kotlinlang.org/)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-2024.11.00-blue?logo=jetpackcompose)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)
[![Version](https://img.shields.io/badge/Version-1.0-blue.svg)](README.md)

---

## Tabla de Contenidos

- [Características](#características)
- [Requisitos del Sistema](#requisitos-del-sistema)
- [Instalación](#instalación)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Tecnologías Utilizadas](#tecnologías-utilizadas)
- [Configuración](#configuración)
- [Guía de Uso](#guía-de-uso)
- [Arquitectura](#arquitectura)
- [Contribuciones](#contribuciones)

---

## Características

- **Autenticación Segura**: Sistema de login con validación de credenciales
- **Gestión de Perfiles**: Perfiles personalizados para conductores y mecánicos
- **Integración de Google Maps**: Localización de mecánicos cercanos en tiempo real
- **Sistema de Reseñas**: Calificación y comentarios sobre servicios
- **Historial de Servicios**: Seguimiento de trabajos realizados
- **Almacenamiento Local**: Base de datos local con Room para acceso offline
- **Interfaz Moderna**: Diseño limpio con Jetpack Compose
- **Responsive Design**: Compatible con dispositivos de diferentes tamaños
- **Sincronización en Tiempo Real**: Comunicación eficiente con backend

---



### Requisitos de Desarrollo
- **Android Studio**: Versión más reciente (2024.1+)
- **Java/JDK**: Versión 11 o superior
- **Gradle**: 8.13.2
- **Kotlin**: 2.0.21



## Instalación

### 1. Clonar el Repositorio
```bash
git clone https://github.com/tu-usuario/automatch.git
cd automatch
```

### 2. Abrir en Android Studio
- Abre **Android Studio**
- Selecciona **File → Open**
- Navega a la carpeta del proyecto y selecciona la carpeta `front-end`
- Android Studio sincronizará automáticamente el proyecto con Gradle

### 3. Sincronizar Dependencias
```bash
./gradlew build
```

### 4. Compilar y Ejecutar
```bash
# Construir la aplicación
./gradlew assembleDebug

# Instalar en dispositivo/emulador
./gradlew installDebug
```

### 5. Ejecutar Tests
```bash
# Tests unitarios
./gradlew test

# Tests instrumentados
./gradlew connectedAndroidTest
```

---

## Estructura del Proyecto

```
automatch/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/edu/pe/automatch/
│   │   │   │   ├── MainActivity.kt              # Actividad principal
│   │   │   │   └── presentation/                # Capa de presentación (UI)
│   │   │   │       ├── screens/                 # Pantallas principales
│   │   │   │       │   ├── MechanicProfileScreen.kt
│   │   │   │       │   ├── UserProfileScreen.kt
│   │   │   │       │   ├── ReviewScreen.kt
│   │   │   │       │   └── HomeScreen.kt
│   │   │   │       ├── driver/                  # Módulo de conductores
│   │   │   │       │   └── HomeScreen.kt
│   │   │   │       ├── login/                   # Módulo de autenticación
│   │   │   │       ├── components/              # Componentes reutilizables
│   │   │   │       ├── navigation/              # Sistema de navegación
│   │   │   │       └── theme/                   # Tema y estilos
│   │   │   ├── res/
│   │   │   │   ├── drawable/                    # Recursos de diseño
│   │   │   │   ├── values/                      # Strings, colors, dimens
│   │   │   │   └── xml/                         # Configuraciones XML
│   │   │   └── AndroidManifest.xml              # Configuración de la app
│   │   ├── test/                                # Tests unitarios
│   │   └── androidTest/                         # Tests instrumentados
│   ├── build.gradle.kts                         # Configuración de dependencias
│   └── proguard-rules.pro                       # Reglas de ofuscación
├── gradle/
│   ├── libs.versions.toml                       # Versionamiento de librerías
│   └── wrapper/
├── build.gradle.kts                             # Build root
├── settings.gradle.kts                          # Configuración de proyectos
└── README.md                                    # Este archivo
```

---

## Tecnologías Utilizadas

### Framework y Lenguaje
- **Kotlin 2.0.21**: Lenguaje principal de desarrollo
- **Jetpack Compose 2024.11.00**: Framework moderno para UI
- **Android 15 (API 35)**: Sistema operativo objetivo

### Jetpack Libraries
| Librería | Versión | Propósito |
|----------|---------|----------|
| AndroidX Core | 1.15.0 | Funcionalidades base de Android |
| Lifecycle | 2.8.7 | Gestión del ciclo de vida |
| Navigation | 2.8.4 | Sistema de navegación |
| Room | 2.6.1 | Base de datos local |
| Compose | 2024.11.00 | Interfaz de usuario moderna |

### Networking & Data
- **Retrofit 2.11.0**: Cliente HTTP REST
- **Gson Converter**: Serialización/deserialización de JSON
- **Kotlin Serialization 1.7.3**: Serialización de datos

### Multimedia & Imágenes
- **Coil 3.0.4**: Carga eficiente de imágenes
- **OkHttp**: Interceptor de red

### Servicios Google
- **Google Maps API**: Integración de mapas y ubicación


---

## Configuración

### 1. Configurar API Key de Google Maps

#### En `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="TU_API_KEY_AQUI"/>
```

#### Obtener API Key:
1. Ve a [Google Cloud Console](https://console.cloud.google.com/)
2. Crea un nuevo proyecto
3. Habilita la API de Google Maps
4. Crea una credencial de tipo "API Key"
5. Restringe la clave a Android con tu SHA-1 certificate fingerprint

### 2. Configuración de Backend

Edita el archivo de configuración de Retrofit para conectar con tu backend:

```kotlin
// En tu DI container o NetworkModule.kt
private const val BASE_URL = "https://tu-backend.com/api/"
```

### 3. Configurar local.properties

```properties
sdk.dir=/path/to/android/sdk
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
```

---

## Guía de Uso

### Para Usuarios (Conductores)

1. **Registro/Login**
   - Inicia la app
   - Introduce tus credenciales
   - O crea una nueva cuenta

2. **Ver Mecánicos Disponibles**
   - En la pantalla principal verás mecánicos cercanos
   - Filtra por servicios disponibles
   - Consulta calificaciones y reseñas

3. **Solicitar Servicio**
   - Selecciona un mecánico
   - Describe el problema de tu vehículo
   - Elige horario disponible
   - Realiza el pago

4. **Seguimiento**
   - Visualiza el estado en tiempo real
   - Comunícate con el mecánico
   - Recibe notificaciones de actualizaciones

5. **Dejar Reseña**
   - Después del servicio
   - Califica al mecánico
   - Comparte tu experiencia

### Para Mecánicos

1. **Registro Profesional**
   - Crear perfil de mecánico
   - Validar credenciales
   - Cargar certificaciones

2. **Gestionar Disponibilidad**
   - Establece horarios de trabajo
   - Define área de cobertura
   - Configura servicios ofrecidos

3. **Recibir Solicitudes**
   - Notificaciones de nuevos servicios
   - Acepta o rechaza solicitudes
   - Actualiza el estado del trabajo

---

## Arquitectura

### Patrón Arquitectónico: MVVM + Clean Architecture

```
Presentation Layer (UI)
        ↓
ViewModel Layer (State Management)
        ↓
Use Case Layer (Business Logic)
        ↓
Repository Layer (Data Abstraction)
        ↓
Data Layer (APIs, Database, SharedPreferences)
```

### Flujo de Datos

```
UI (Compose) 
  ↓ (Observa)
ViewModel 
  ↓ (Solicita)
Repository 
  ↓ (Obtiene de)
RemoteDataSource (API)
LocalDataSource (Room DB)
```

---



## Equipo de Desarrollo

## Development Team

| Team Member | Student Code |
|---|---|
| Villugas Jeronimo, Liam Anderson | U202211634 |
| Taquiri Calderon, Jhunior Giussepe | U20221C576 |
| Sanchez Gonzales, Gabriel | U202310609 |
| Tuesta Marin, Romina Alejandra | U202211706 |
| Torrejon Navarro, Braulio Rodrigo | U201711828 |

---

## Soporte y Contacto

Para reportar problemas o sugerencias:

- **Email**: soporte@automatch.com
- **Issues**: [GitHub Issues](https://github.com/tu-usuario/automatch/issues)
- **Discussions**: [GitHub Discussions](https://github.com/tu-usuario/automatch/discussions)

---

## Enlaces Útiles

- [Documentación de Android](https://developer.android.com/docs)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Retrofit Documentation](https://square.github.io/retrofit/)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Google Maps API](https://developers.google.com/maps/documentation)

---

**Hecho con amor para conectar conductores con mecánicos profesionales**
