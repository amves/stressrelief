# Stress Relief Android App

A stress relief application built with Android Jetpack Compose featuring user authentication, analytics dashboard, AI prediction, and subscription management.

## Features

- **Firebase Authentication**: User login and registration with email/password
- **Navigation System**: Bottom navigation between Dashboard, AI Prediction, and Subscription
- **Dashboard Analytics**: Session tracking with Room database showing stress reduction metrics
- **AI Prediction**: Mock stress level prediction (premium feature placeholder)
- **Subscription/Paywall**: Basic premium subscription UI
- **Modern UI**: Built with Jetpack Compose and Material Design 3

## Architecture

The app follows a clean architecture pattern with the following layers:

### Data Layer
- **SessionEntity**: Room entity for storing session data with before/after stress metrics
- **SessionDao**: Data access object for session operations
- **AppDatabase**: Room database configuration
- **AuthRepository**: Interface for authentication operations
- **AuthRepositoryImpl**: Firebase Authentication implementation

### UI Layer
- **MainActivity**: Entry point setting up Compose UI
- **RootScreen**: Authentication routing based on auth state
- **LoginScreen**: Login and registration form
- **HomeScreen**: Navigation hub with bottom navigation
- **DashboardScreen**: Analytics display with placeholders
- **AiDashboardScreen**: AI stress prediction mock
- **SubscriptionScreen**: Premium paywall UI

### ViewModels
- **RootViewModel**: Manages authentication state flow
- **LoginViewModel**: Handles login/register operations
- **HomeViewModel**: Manages logout

### Dependency Injection
- Hilt/Dagger for dependency injection
- DatabaseModule: Provides Room database dependencies
- FirebaseModule: Provides Firebase Auth dependencies

## Tech Stack

- **Language**: Kotlin
- **UI**: Jetpack Compose + Material Design 3
- **Database**: Room
- **Authentication**: Firebase Auth
- **DI**: Hilt/Dagger
- **Architecture**: MVVM with StateFlow
- **Build System**: Gradle with Kotlin DSL

## Project Structure

```
app/src/main/java/com/amves/stressrelief/
├── MainActivity.kt                          # Main activity
├── StressReliefApplication.kt              # Application class with Hilt
├── data/
│   ├── local/
│   │   ├── AppDatabase.kt                  # Room database
│   │   ├── SessionDao.kt                   # Session data access
│   │   └── SessionEntity.kt                # Session entity
│   └── repository/
│       ├── AuthRepository.kt               # Auth interface
│       └── AuthRepositoryImpl.kt           # Auth implementation
├── di/
│   ├── DatabaseModule.kt                   # Database DI module
│   └── FirebaseModule.kt                   # Firebase DI module
├── ui/
│   ├── screens/
│   │   ├── AiDashboardScreen.kt           # AI prediction screen
│   │   ├── DashboardScreen.kt             # Analytics dashboard
│   │   ├── HomeScreen.kt                  # Home navigation
│   │   ├── LoadingScreen.kt               # Loading state
│   │   ├── LoginScreen.kt                 # Login/register
│   │   ├── RootScreen.kt                  # Root routing
│   │   └── SubscriptionScreen.kt          # Subscription/paywall
│   ├── theme/
│   │   ├── Color.kt                       # Color definitions
│   │   ├── Theme.kt                       # Theme setup
│   │   └── Type.kt                        # Typography
│   └── viewmodels/
│       ├── HomeViewModel.kt               # Home logic
│       ├── LoginViewModel.kt              # Login logic
│       └── RootViewModel.kt               # Root logic
```

## Setup

1. **Prerequisites**:
   - Android Studio Arctic Fox or later
   - JDK 17
   - Android SDK 34

2. **Firebase Configuration**:
   - Replace `app/google-services.json` with your own Firebase configuration
   - Enable Email/Password authentication in Firebase Console

3. **Build**:
   ```bash
   ./gradlew build
   ```

4. **Run**:
   - Open project in Android Studio
   - Run on emulator or device (API 26+)

## Dependencies

- Jetpack Compose BOM 2023.10.01
- Firebase BOM 32.6.0
- Room 2.6.1
- Hilt 2.48
- Navigation Compose 2.7.5

## Notes

- The app includes a placeholder `google-services.json` for Firebase. Replace it with your actual Firebase configuration.
- AI prediction is currently a mock implementation returning random values
- Subscription functionality is a UI placeholder without actual payment integration
