# Stress Relief Android App

![Android Build](https://github.com/amves/stressrelief/workflows/Android%20Build/badge.svg)
[![Latest Release](https://img.shields.io/github/v/release/amves/stressrelief)](https://github.com/amves/stressrelief/releases/latest)

A stress relief application built with Android Jetpack Compose featuring user authentication, analytics dashboard, AI prediction, subscription management, and health monitoring with HRV calculations.

## 📥 Download

**Latest Release**: [Download APK](https://github.com/amves/stressrelief/releases/latest)

Official releases are published automatically when git tags are created. Download the latest version from the releases page.

## Features

- **Firebase Authentication**: User login and registration with email/password
- **Navigation System**: Bottom navigation between Dashboard, AI Prediction, and Subscription
- **Dashboard Analytics**: Session tracking with Room database showing stress reduction metrics
- **AI Prediction**: Mock stress level prediction (premium feature placeholder)
- **Subscription/Paywall**: Basic premium subscription UI
- **Health Monitoring**: HRV calculation with Health Connect and Wear OS integration
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

### Option 1: Download Pre-built Release (Easiest! 🎉)

**Just want to use the app?** Download the latest release:

1. **Go to Releases**: https://github.com/amves/stressrelief/releases/latest
2. **Download APK**: Click on `stressrelief-X.X.X-release.apk`
3. **Install on phone**: Transfer and install

### Option 2: Build APK with GitHub Actions (No Android Studio Required! ⭐)

**Don't want to install Android Studio?** GitHub Actions builds APKs for you in the cloud!

1. **Push your code to GitHub**
2. **Go to Actions tab** on GitHub
3. **Download APK** from the latest workflow run
4. **Install on your phone**

📖 **Detailed Guide**: See [GITHUB_ACTIONS_APK_BUILD.md](GITHUB_ACTIONS_APK_BUILD.md)

**Quick Link**: [View Latest Builds](https://github.com/amves/stressrelief/actions/workflows/build.yml)

### Option 3: Build Locally with Android Studio

1. **Prerequisites**:
   - Android Studio Arctic Fox or later
   - JDK 17
   - Android SDK 34

2. **Firebase Configuration**:
   - Replace `app/google-services.json` with your own Firebase configuration
   - Enable Email/Password authentication in Firebase Console

3. **Build**:
   ```bash
   ./gradlew assembleDebug
   ```
   APK location: `app/build/outputs/apk/debug/app-debug.apk`

4. **Run**:
   - Open project in Android Studio
   - Run on emulator or device (API 26+)

📖 **Local Build Guide**: See [BUILD_APK_GUIDE.md](BUILD_APK_GUIDE.md)

## Dependencies

- Jetpack Compose BOM 2023.10.01
- Firebase BOM 32.6.0
- Room 2.6.1
- Hilt 2.48
- Navigation Compose 2.7.5
- Health Connect 1.1.0-alpha07
- Wear OS Play Services 18.1.0

## Health & Wellness Features

This app includes comprehensive health monitoring capabilities:

- **HRV Calculator**: Calculate RMSSD, SDNN, and PNN50 metrics
- **Health Connect Integration**: Read heart rate and HRV data
- **Wear OS Support**: Real-time heart rate monitoring from smartwatch
- **Health Monitor UI**: View real-time health metrics and history

📖 **Health Integration Guide**: See [WEAR_OS_HEALTH_CONNECT.md](WEAR_OS_HEALTH_CONNECT.md)

## 🚀 Creating Releases

**For Maintainers**: To publish an official release:

```bash
# Create and push a version tag
git tag v1.0.0
git push origin v1.0.0
```

This automatically:
- Builds release APK
- Generates changelog
- Publishes to GitHub Releases
- **Uploads to Google Play Store** (if configured)

📖 **Full Release Guide**: See [RELEASE_PROCESS.md](RELEASE_PROCESS.md)

### Publishing to Google Play Store

Want to automate uploads to Google Play? The workflow supports it!

📖 **Google Play Setup Guide**: See [GOOGLE_PLAY_PUBLISHING.md](GOOGLE_PLAY_PUBLISHING.md)

Once configured with required secrets, releases automatically upload to Google Play Console (Internal Track).

**Required Secrets**:
- `KEYSTORE_BASE64` - App signing keystore
- `KEYSTORE_PASSWORD`, `KEY_ALIAS`, `KEY_PASSWORD` - Keystore credentials
- `PLAY_STORE_SERVICE_ACCOUNT_JSON` - Google Play API credentials

## Notes

- The app includes a placeholder `google-services.json` for Firebase. Replace it with your actual Firebase configuration.
- AI prediction is currently a mock implementation returning random values
- Subscription functionality is a UI placeholder without actual payment integration
- HRV calculations estimate RR intervals from heart rate; for medical-grade accuracy, use direct RR interval sensors
