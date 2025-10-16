# EpicMedalsApp

> **Una aplicación Android de gamificación con sistema de medallas épicas**

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9.10-blue.svg)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.5.4-green.svg)](https://developer.android.com/jetpack/compose)
[![Clean Architecture](https://img.shields.io/badge/Architecture-Clean-orange.svg)](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
[![Hilt](https://img.shields.io/badge/DI-Hilt-purple.svg)](https://dagger.dev/hilt/)

## 📋 Descripción del Proyecto

**EpicMedalsApp** es una aplicación de gamificación que permite a los usuarios coleccionar medallas
épicas, completar misiones, mantener rachas y visualizar su progreso en un álbum interactivo. La app
utiliza animaciones Lottie espectaculares y un sistema de niveles para crear una experiencia
inmersiva.

### ✨ Características Principale

- 🥇 **Sistema de Medallas**: Colecciona medallas de diferentes categorías y rarezas
- 🎯 **Misiones Dinámicas**: Completa objetivos para desbloquear recompensas
- 🔥 **Sistema de Rachas**: Mantén tu progreso diario activo
- 📚 **Álbum Interactivo**: Visualiza tu colección de medallas
- 🎭 **Animaciones Épicas**: Lottie animations para celebrar logros
- 📊 **Sistema de Niveles**: Progresa y desbloquea nuevas recompensas
- 🎨 **UI Moderna**: Jetpack Compose con Material Design 3

## 🏗️ Arquitectura del Proyecto

### 📁 Estructura de Módulos

```
EpicMedalsApp/
├── 📱 app/                     # Módulo de presentación (UI)
├── 💾 data/                    # Módulo de datos (Repository + DataStore)
└── 🧠 domain/                  # Módulo de dominio (Lógica de negocio)
```

### 🎯 Clean Architecture + MVVM

Este proyecto implementa **Clean Architecture** con **MVVM** usando **Jetpack Compose** y **Kotlin
**.

#### **Domain Layer** (`domain` module) - Lógica de Negocio Pura

```kotlin
📁 domain/
├── 🔧 common/
│   └── UIState.kt              # Estados de UI genéricos
├── ⚙️ config/
│   └── GameConfig.kt           # Configuraciones del juego
├── 📝 model/
│   ├── Medal.kt                # Entidad principal de medalla
│   ├── MedalCategory.kt        # Categorías (STREAKS, MISSIONS, etc.)
│   ├── MedalRarity.kt          # Rarezas (COMMON, RARE, EPIC, LEGENDARY)
│   └── AnimationType.kt        # Tipos de animaciones
├── 📋 repository/
│   └── MedalRepository.kt      # Interface del repositorio
└── 🎯 usecase/
    ├── GetMedalsFlowUseCase.kt
    ├── SaveMedalsUseCase.kt
    ├── UpdateMedalsUseCase.kt
    └── ResetAllMedalsUseCase.kt
```

**Características:**
- ✅ **Kotlin Puro** - Sin dependencias Android
- ✅ **Testeable al 100%** - Lógica aislada
- ✅ **Casos de Uso** - Operaciones de negocio encapsuladas

#### **Data Layer** (`data` module) - Gestión de Datos

```kotlin
📁 data/
├── 💾 datastore/
│   └── MedalDataStore.kt       # Persistencia con DataStore
└── 📦 repository/
    └── MedalRepositoryImpl.kt  # Implementación del repositorio
```

**Características:**
- ✅ **DataStore Preferences** - Almacenamiento local eficiente
- ✅ **JSON Serialization** - Kotlinx Serialization
- ✅ **Repository Pattern** - Abstracción de datos

#### **Presentation Layer** (`app` module) - Interfaz de Usuario

```kotlin
📁 app/
├── 🏛️ core/
│   ├── EpicMedalsApp.kt        # Aplicación principal
│   └── 💉 di/                  # Inyección de dependencias
│       ├── DataModule.kt
│       ├── InterfacesModule.kt
│       └── UseCaseModule.kt
└── 🎨 ui/
    ├── 🎬 animations/          # Animaciones Lottie
    ├── 🧩 composables/         # Componentes reutilizables
    ├── 🧭 navigation/          # Navegación de la app
    ├── 📱 screens/             # Pantallas principales
    │   ├── 📚 album/           # Pantalla de álbum
    │   ├── 🏅 medals/          # Pantalla de medallas
    │   ├── 🎯 missions/        # Pantalla de misiones
    │   ├── 🚀 splash/          # Pantalla de inicio
    │   └── 🔥 streaks/         # Pantalla de rachas
    └── 🎨 theme/               # Tema de la aplicación
```

## 🚀 Tecnologías y Librerías

### **Core Technologies**
- **Kotlin** `1.9.10` - Lenguaje principal
- **Android SDK** `34` - Plataforma target
- **Gradle** `8.1.1` - Sistema de build

### **UI & Animation**
- **Jetpack Compose** `1.5.4` - UI toolkit moderno
- **Material Design 3** - Sistema de diseño
- **Lottie** `6.1.0` - Animaciones vectoriales
- **Navigation Compose** - Navegación declarativa

### **Architecture & DI**
- **Hilt** `2.48` - Inyección de dependencias
- **ViewModel** - Gestión de estado
- **Lifecycle** - Ciclo de vida aware

### **Data & Storage**
- **DataStore Preferences** - Almacenamiento local
- **Kotlinx Serialization** `1.6.0` - Serialización JSON
- **Coroutines** `1.7.3` - Programación asíncrona

### **Development Tools**
- **Version Catalog** - Gestión de dependencias
- **ProGuard** - Ofuscación de código

## 🎮 Funcionalidades Detalladas

### 🏅 Sistema de Medallas

```kotlin
data class Medal(
    val id: String,
    val name: String,
    val description: String,
    val category: MedalCategory,
    val rarity: MedalRarity,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null,
    val iconRes: Int,
    val animationType: AnimationType,
    val experience: Int
)
```

**Categorías Disponibles:**
- 🔥 **STREAKS** - Por mantener rachas consecutivas
- 🎯 **MISSIONS** - Por completar misiones específicas
- 📈 **PROGRESS** - Por alcanzar hitos de progreso
- 🏆 **ACHIEVEMENTS** - Logros especiales

**Sistemas de Rareza:**
- 🤍 **COMMON** - Medallas básicas
- 🟢 **RARE** - Medallas poco comunes
- 🟣 **EPIC** - Medallas épicas
- 🟠 **LEGENDARY** - Medallas legendarias

### 🎬 Sistema de Animaciones

La aplicación incluye múltiples animaciones Lottie:

```kotlin
📁 raw/
├── confetti.json       # Celebración general
├── explosion.json      # Efectos de impacto
├── flamefire.json      # Animaciones de fuego
├── gold_medal.json     # Medalla dorada
├── loading.json        # Pantalla de carga
├── shine.json          # Efectos de brillo
├── sparkle.json        # Partículas brillantes
└── streak.json         # Efectos de racha
```

### 📱 Pantallas Principales

#### 🏅 Medals Screen
- Visualización de todas las medallas
- Filtros por categoría y rareza
- Animaciones de desbloqueo
- Sistema de experiencia

#### 📚 Album Screen
- Galería interactiva de medallas
- Estadísticas de colección
- Progreso visual

#### 🎯 Missions Screen
- Lista de misiones activas
- Progreso de objetivos
- Sistema de recompensas

#### 🔥 Streaks Screen
- Seguimiento de rachas diarias
- Visualización de progreso
- Motivación para mantener consistencia

## 🔄 Flujo de Datos

```
UI (Compose) → ViewModel → Use Case → Repository → DataStore
                                    ↘️ Domain Models ↙️
```

1. **UI** trigger actions en ViewModel
2. **ViewModel** invoca Use Cases
3. **Use Cases** ejecutan lógica de negocio
4. **Repository** maneja persistencia
5. **DataStore** persiste datos localmente

## 💉 Inyección de Dependencias

### Hilt Modules

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Provides
    @Singleton
    fun provideMedalDataStore(@ApplicationContext context: Context): MedalDataStore
    
    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences>
}
```

**Estrategia de Scopes:**
- **`@Singleton`**: Repository, DataStore
- **`@ViewModelScoped`**: ViewModels automático
- **Sin scope**: Use Cases (stateless)

## 🎨 UI/UX Design

### **Material Design 3**
- ✅ **Dynamic Color** - Colores adaptativos
- ✅ **Typography Scale** - Escalas tipográficas consistentes
- ✅ **Component Library** - Componentes reutilizables

### **Compose Best Practices**
- ✅ **Stateless Composables** - UI pura sin efectos secundarios
- ✅ **State Hoisting** - Estado gestionado por ViewModels
- ✅ **Unidirectional Data Flow** - Flujo de datos predecible
- ✅ **Performance Optimized** - Recomposición eficiente

## 🧪 Estado de Testing

```kotlin
📁 test/
├── domain/ ✅          # Tests de lógica de negocio
├── data/ 🚧            # Tests de repositorio (en progreso)
└── ui/ 📋              # Tests de UI (planeado)
```

## 🚀 Getting Started

### Prerrequisitos

- **Android Studio** Hedgehog | 2023.1.1+
- **JDK** 17+
- **Android SDK** 34+
- **Gradle** 8.1.1+

### Instalación

1. **Clonar el repositorio**
```bash
git clone https://github.com/tuusuario/EpicMedalsApp.git
cd EpicMedalsApp
```

2. **Abrir en Android Studio**
```bash
# Abrir Android Studio y seleccionar "Open an existing project"
# Navegar hasta la carpeta del proyecto
```

3. **Sync & Build**
```bash
# Android Studio sincronizará automáticamente
# O ejecutar desde terminal:
./gradlew build
```

4. **Ejecutar la aplicación**
```bash
./gradlew installDebug
```

## 📊 Métricas del Proyecto

- **Líneas de código**: ~3,500+
- **Módulos**: 3 (app, data, domain)
- **Pantallas**: 5 principales
- **Animaciones Lottie**: 9
- **Componentes Reutilizables**: 10+
- **Use Cases**: 4
- **Modelos de Dominio**: 5

## 🔮 Roadmap

### v1.1.0 - Próximas Funcionalidades
- [ ] 🌐 **Sincronización en la nube**
- [ ] 👥 **Sistema social** - Comparar con amigos
- [ ] 🏆 **Leaderboards globales**
- [ ] 🔔 **Notificaciones push**

### v1.2.0 - Mejoras de UX
- [ ] 🎨 **Temas personalizables**
- [ ] 🔍 **Búsqueda avanzada**
- [ ] 📊 **Analytics detallados**
- [ ] 🎵 **Efectos de sonido**

### v2.0.0 - Funcionalidades Avanzadas
- [ ] 🕹️ **Mini-juegos**
- [ ] 🎁 **Sistema de recompensas diarias**
- [ ] 📱 **Widgets de pantalla de inicio**
- [ ] 🌍 **Soporte multi-idioma**

## 🤝 Contribución

¡Las contribuciones son bienvenidas! Por favor:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/amazing-feature`)
3. Commit tus cambios (`git commit -m 'Add amazing feature'`)
4. Push a la rama (`git push origin feature/amazing-feature`)
5. Abre un Pull Request

## 📝 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.

## 👨‍💻 Autor

**Keving Hanz Roque Huich** - *Desarrollador Principal*
- GitHub: [@kevingroquehuich](https://github.com/kevingroquehuich)
- LinkedIn: [Keving Hanz Roque Huich](https://www.linkedin.com/in/keving-hanz-roque-huich-52870a120)

---

<div align="center">

**⭐ Si te gusta este proyecto, ¡dale una estrella! ⭐**

*Construido con ❤️ usando Jetpack Compose y Clean Architecture*

</div>