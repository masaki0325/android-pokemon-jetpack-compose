# Pokemon App

A simple Pokemon app built with Jetpack Compose, Hilt, Room, and Retrofit. The app fetches data from a Pokemon API and allows users to browse Pokemon, view details, favorite them, and see a list of favorited Pokemon.

## Features

- Fetch and display a list of Pokemon
- View detailed information about each Pokemon (name, height, weight, and sprite)
- Favorite/unfavorite Pokemon and view a list of favorites
- Pagination to load additional Pokemon
- Dark mode and light mode support

## Screenshots

<!-- Add screenshots of your app here -->

## Tech Stack

This project uses the following technologies:

- **Kotlin 2.0.20**: Programming language
- **Jetpack Compose**: UI Toolkit
- **Hilt 2.52**: Dependency injection
- **Room 2.6.1**: Local database
- **Retrofit 2.9.0**: Networking library for API communication
- **Coil 2.3.0**: Image loading library
- **OkHttp 4.12.0**: HTTP client
- **Navigation Compose 2.8.3**: Jetpack Compose Navigation
- **Material3 1.3.0**: Material Design components

/app
    └── src/main/
        ├── java/
        │   └── com/example/myapp/
        │       ├── data/                # Data layer
        │       │   ├── model/           # Data models (e.g., Pokemon.kt)
        │       │   ├── local/           # Room database and DAOs
        │       │   └── remote/          # Retrofit API service
        │       ├── domain/              # Business logic layer
        │       │   └── model/           # Domain models
        │       ├── ui/                  # UI layer
        │       │   ├── components/      # Reusable UI components
        │       │   ├── screens/         # Screen UI (PokemonListScreen, SettingsScreen, etc.)
        │       │   ├── state/           # UI state management (e.g., PokemonListUiState)
        │       │   └── navigation/      # Navigation setup (PokemonNavGraph.kt)
        │       ├── viewmodel/           # ViewModel layer
        │       └── di/                  # Dependency injection (Hilt modules)
        └── res/
            ├── layout/                  # XML layout files (if any)
            ├── drawable/                # Image assets
            └── values/                  # Colors, strings, themes


## Requirements

- Android Studio Flamingo or newer
- Kotlin 2.0.x
- Gradle 8.1.1
- Android SDK 33 or higher

## Getting Started

To get a local copy up and running, follow these steps:

### Prerequisites

1. Install Android Studio Flamingo or newer.
2. Clone this repository:

```bash
git clone https://github.com/your-username/pokemon-app.git
