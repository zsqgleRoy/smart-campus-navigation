# 数智成贤 (Smart Campus Navigation)

A comprehensive campus website navigation solution, providing both web and native Android applications for quick access to campus services.

## Project Overview

This repository contains two implementations of the campus navigation system:

- **cxxy/** - Web application built with Vue 3 + TypeScript + Vant UI
- **szcx/** - Native Android application built with Kotlin + Jetpack Compose

Both applications provide the same core functionality: helping students and faculty quickly access commonly used campus websites.

## Features

### Core Features

- 🔍 **Quick Search** - Search websites by name or description
- ⭐ **Favorites** - Bookmark frequently used websites
- 📊 **Visit History** - Track recently visited websites
- 🎨 **Category Browse** - Browse websites by category
- 🌓 **Theme Switching** - Support for light/dark/eye-care themes
- 📱 **Responsive Design** - Optimized for mobile devices

### Web Application (cxxy)

- PWA support for offline access
- Add to home screen functionality
- Responsive layout for various screen sizes

### Android Application (szcx)

- Native Android experience
- Offline data support with Room Database
- Icon preloading and caching
- WebView integration for website browsing

## Technology Stack

### Web Application (cxxy)

- **Framework**: Vue 3
- **Language**: TypeScript
- **Build Tool**: Vite
- **UI Library**: Vant UI
- **State Management**: Pinia
- **Routing**: Vue Router
- **PWA**: Progressive Web App support

### Android Application (szcx)

- **Language**: Kotlin
- **Min SDK**: API 21 (Android 5.0)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: MVVM (Model-View-ViewModel)
- **UI Framework**: Jetpack Compose
- **Database**: Room Database
- **Image Loading**: Coil
- **Data Storage**: DataStore
- **Navigation**: Navigation Compose

## 📁 Project Structure

```
.
├── cxxy/                    # Web application (Vue 3)
│   ├── src/
│   │   ├── components/      # Reusable components
│   │   ├── views/           # Page components
│   │   ├── stores/          # State management
│   │   ├── router/          # Route configuration
│   │   ├── utils/           # Utility functions
│   │   ├── data/            # Data files
│   │   └── styles/           # Global styles
│   ├── package.json
│   └── vite.config.ts
│
└── szcx/                    # Android application (Kotlin)
    ├── app/
    │   ├── src/main/
    │   │   ├── java/com/szcx/
    │   │   │   ├── data/     # Data layer (Database, Models, Repository)
    │   │   │   ├── ui/       # UI layer (Screens, Components, Theme)
    │   │   │   └── util/     # Utility classes
    │   │   └── res/           # Resources (Layouts, Drawables, Values)
    │   └── build.gradle.kts
    ├── build.gradle.kts
    └── settings.gradle.kts
```

## Getting Started

### Prerequisites

#### For Web Application (cxxy)

- Node.js 16+ and npm/yarn/pnpm

#### For Android Application (szcx)

- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK (API 21+)

### Web Application Setup

1. Navigate to the web application directory:
   
   ```bash
   cd cxxy
   ```

2. Install dependencies:
   
   ```bash
   npm install # recommandation
   # or
   yarn install
   # or
   pnpm install
   ```

3. Start the development server:
   
   ```bash
   npm run dev  # recommandation
   # or
   yarn dev
   # or
   pnpm dev
   ```

4. Build for production:
   
   ```bash
   npm run build
   ```

### Android Application Setup

1. Open the project in Android Studio:
   
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `szcx` directory

2. Sync Gradle dependencies:
   
   - Android Studio will automatically sync Gradle
   - Wait for the sync to complete

3. Run the application:
   
   - Connect an Android device or start an emulator (API 21+)
   - Click the "Run" button or press `Shift+F10`

## Development

### Web Application Development

- **Development server**: `npm run dev` (runs on http://localhost:5173)
- **Type checking**: `npm run type-check`
- **Build**: `npm run build`
- **Preview**: `npm run preview`

### Android Application Development

- Open the project in Android Studio
- Sync Gradle dependencies
- Run on device/emulator
- The app will automatically initialize database data on first run

## Key Features Implementation

### Theme Support

Both applications support three theme modes:

- **Light Mode** - Default light theme
- **Dark Mode** - Dark theme for low-light environments
- **Eye-care Mode** - Warm color scheme to reduce eye strain

### Data Management

- **Web App**: Uses localStorage for favorites and history
- **Android App**: Uses Room Database for persistent storage and DataStore for preferences

### Website Icons

- **Web App**: Loads icons from website favicons
- **Android App**: Preloads and caches all website icons on app startup

## Notes

- The Android application requires network permission to access websites
- First run of the Android app will automatically initialize database data
- Web application supports PWA features for offline access
- Both applications share the same website data structure

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

MIT License

## 👤 Author

- GitHub: [zsqgleRoy](https://github.com/zsqgleRoy)

## 🔗 Links

- [Author's GitHub Homepage](https://github.com/zsqgleRoy/)

---

For Chinese documentation, please see [中文说明文档](README_CN.md).
