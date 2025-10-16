# EpicMedalsApp

# 🏗️ Architecture Documentation - EpicMedalsApp

## 📋 Overview

Este proyecto implementa **Clean Architecture** con **MVVM** usando **Jetpack Compose** y **Kotlin
**.

## 🎯 Clean Architecture Layers

### 1. **Domain Layer** (`domain` module)

- **Pure Kotlin/Java** - Sin dependencias Android
- **Entities**: `Medal`, `MedalCategory`, `MedalRarity`, `UIState`
- **Use Cases**: Lógica de negocio encapsulada
    - `GetMedalsFlowUseCase`
    - `SaveMedalsUseCase`
    - `UpdateMedalsUseCase`
    - `ResetAllMedalsUseCase`
- **Repository Interfaces**: `MedalRepository`
- **Configuration**: `GameConfig`

### 2. **Data Layer** (`data` module)

- **Repository Implementations**: `MedalRepositoryImpl`
- **Data Sources**: `MedalDataStore` (DataStore Preferences)
- **Data Models**: Mapeo entre domain entities y data sources
- **Persistence**: JSON serialization con Kotlinx Serialization

### 3. **Presentation Layer** (`app` module)

- **MVVM Pattern**: ViewModels + Compose UI
- **UI Components**: Screens, Composables, Navigation
- **Dependency Injection**: Hilt modules
- **State Management**: StateFlow + Compose State

## 🔄 MVVM Implementation

### ViewModel (`MedalsViewModel`)

```kotlin
@HiltViewModel
class MedalsViewModel @Inject constructor(
    private val getMedalsUseCase: GetMedalsFlowUseCase,
    private val saveMedalsUseCase: SaveMedalsUseCase,
    // ... otros use cases
) : ViewModel()
```

**Responsabilidades:**

- ✅ Manejo de estado UI con `StateFlow`
- ✅ Orquestación de Use Cases
- ✅ Manejo del ciclo de vida
- ✅ Estados de loading/error/success

### View (Compose UI)

```kotlin
@Composable
fun MedalsScreen(medalsViewModel: MedalsViewModel) {
    val medalsState by medalsViewModel.medalsState.collectAsState()
    // UI reactiva sin lógica de negocio
}
```

## 🎭 State Management

### UIState Pattern

```kotlin
sealed class UIState<out T> {
    object Loading : UIState<Nothing>()
    data class Success<T>(val data: T) : UIState<T>()
    data class Error(val message: String, val exception: Throwable? = null) : UIState<Nothing>()
}
```

## 💉 Dependency Injection (Hilt)

### Modules Structure

- **`DataModule`**: Provee DataStore y Repository implementations
- **`InterfacesModule`**: Bindea interfaces con implementaciones
- **`UseCaseModule`**: Provee Use Cases

### Scope Strategy

- **`@Singleton`**: Repository, DataStore
- **`@ViewModelScoped`**: ViewModels (automático con `@HiltViewModel`)
- **Sin scope**: Use Cases (stateless)

## 📊 Data Flow

```
UI (Compose) → ViewModel → Use Case → Repository → DataStore
                                    ↘️ Domain Models ↙️
```

1. **UI** trigger actions en ViewModel
2. **ViewModel** invoca Use Cases
3. **Use Cases** ejecutan lógica de negocio
4. **Repository** maneja persistencia
5. **DataStore** persiste datos localmente

## 🔧 Benefits Achieved

### ✅ Testability

- Use Cases con lógica pura
- Repository interfaces mockeable
- ViewModels testeable con fake repositories

### ✅ Separation of Concerns

- UI solo maneja presentación
- ViewModels orquestan sin lógica de negocio
- Use Cases contienen lógica de dominio
- Repository abstrae persistencia

### ✅ Scalability

- Módulos independientes
- Nuevas features = nuevos Use Cases
- UI components reutilizables

### ✅ Maintainability

- Cambios aislados por capa
- Interfaces estables
- Código predecible

## 🚀 Performance Optimizations

### State Management

- `StateFlow` con `distinctUntilChanged()`
- `stateIn()` para hot streams
- Lazy evaluation en repositories

### Memory Management

- `viewModelScope` para coroutines
- Cleanup en `onCleared()`
- Efficient Compose recomposition

## 📱 UI Architecture

### Compose Best Practices

- **Stateless Composables**: UI pura sin side effects
- **State Hoisting**: Estado manejado por ViewModels
- **Single Source of Truth**: StateFlow como única fuente
- **Unidirectional Data Flow**: Actions up, State down

## 🔮 Future Improvements

1. **Error Handling**: Global error handling with sealed classes
2. **Offline Support**: Room database + sync strategies
3. **Testing**: Increase test coverage to 80%+
4. **Performance**: Lazy loading + pagination
5. **Architecture**: Feature modules for larger scale