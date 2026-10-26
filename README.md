# AndroidStudio-Study

> 🌐 **Language / Idioma:** English | [Versão em Português (Guia Completo)](README-pt-BR.md)

Welcome to the **AndroidStudio-Study** repository! This repository serves as a personal workspace, study portfolio, and practical lab for modern Android development with **Android Studio**, **Kotlin**, and **Jetpack Compose** (Material 3).

Here you will find fully functional games, interactive UI experiments, and consolidated educational documentation on Android architecture, lifecycle management, coroutines, and declarative state patterns.

---

## 📑 Table of Contents

- [📂 Projects in this Repository](#-projects-in-this-repository)
  - [1. 💣 CampoMinado (Minesweeper)](#1--campominado-minesweeper)
  - [2. ❌⭕ JogoDaVelha (Tic-Tac-Toe)](#2-️-jogodavelha-tic-tac-toe)
  - [3. 🧪 Teste1 (Compose Playground & Timestamp Logger)](#3--teste1-compose-playground--timestamp-logger)
  - [4. 🔬 CampoMinadoTESTE (Experimental Sandbox)](#4--campominadoteste-experimental-sandbox)
- [📚 Mobile Computing Study Guide](#-mobile-computing-study-guide)
- [🛠️ Tech Stack & Tools](#️-tech-stack--tools)
- [🚀 Getting Started](#-getting-started)
- [📄 License](#-license)

---

## 📂 Projects in this Repository

### 1. 💣 [CampoMinado](./CampoMinado) (Minesweeper)

A feature-complete, modern implementation of the classic **Minesweeper** puzzle game crafted with declarative Jetpack Compose UI.

* **Key Features:**
  * **Multi-Tier Difficulty & Level Selection (`SelecaoFaseActivity`):**
    * **Easy:** 10 × 10 grid with 15% mine density (15 mines).
    * **Medium:** 16 × 16 grid with 18% mine density (~46 mines).
    * **Hard:** 16 × 30 grid with 21% mine density (~100 mines).
    * **Custom:** Interactive dialog enabling custom row/column dimensions and user-defined mine density (either as a percentage or fixed bomb count).
  * **Dynamic Engine & Recursive Expansion (`MainActivity`):**
    * Automated randomized mine distribution.
    * Recursive flood-fill expansion: tapping an empty cell (`0`) automatically cascades and reveals all adjacent safe tiles.
  * **Real-Time Asynchronous Timers:**
    * Live digital clock header running continuously via `LaunchedEffect(Unit)`.
    * Match stopwatch updating at 100ms intervals using Kotlin Coroutines (`delay`).
  * **Two-Way Scrollable Board:**
    * Employs both `horizontalScroll` and `verticalScroll` modifiers to fluidly navigate large or custom boards without clipping.
  * **Persistent Top 10 Leaderboard:**
    * Records victory statistics (player name, completion time, elapsed seconds, total clicks, and bomb count).
    * Persisted locally to the application's private sandboxed directory (`filesDir/ranking.json`) using `org.json`.
  * **Reactive Material 3 UI:**
    * Color-coded proximity numbers, custom cell states, victory/game-over dialogs, and smooth state recomposition.

---

### 2. ❌⭕ [JogoDaVelha](./JogoDaVelha) (Tic-Tac-Toe)

A classic 2-player local pass-and-play **Tic-Tac-Toe** game demonstrating multi-activity screen transitions, intent communication, and reactive matrix state management.

* **Key Features:**
  * **Player Registration & Validation (`MainActivity`):**
    * Input fields for Player 1 and Player 2 names with input validation and inline feedback.
    * Randomized coin-toss logic (`(1..2).random()`) determining who makes the opening move.
    * Inter-activity navigation passing game configuration via explicit `Intent` extras (`putExtra`).
  * **Interactive Game Board (`TelaPrincipal`):**
    * 3 × 3 responsive touch grid with custom visual tokens ('✕' in red and '○' in blue).
    * Active turn indicator dynamically showing which player needs to play.
    * Move counter tracking progress towards a potential draw.
  * **Win Detection Algorithm:**
    * Validates all 8 winning combinations across rows, columns, and main/anti-diagonals.
    * Declares winners reactively or signals a tie ("Deu Velha") upon reaching 9 moves.
  * **Defensive Matrix State Updates:**
    * Utilizes deep copying on 2D list structures to trigger clean Jetpack Compose recomposition cycles.

---

### 3. 🧪 [Teste1](./Teste1) (Compose Playground & Timestamp Logger)

An exploratory sandbox project focused on testing Jetpack Compose state management primitives, layout constraints, and system context interactions.

* **Key Features:**
  * **Reactive State Tracking:**
    * Hands-on experimentation with `remember`, `mutableStateOf`, and `mutableStateListOf`.
  * **System Timestamp Logging:**
    * Captures current date and time using `java.time.LocalDateTime` and `DateTimeFormatter`.
  * **FIFO Circular Display Buffer:**
    * Stores and renders up to 10 recent timestamp records arranged in a structured dual-column layout.
    * Shifts items when the buffer reaches capacity and tracks cumulative buffer wrap-arounds ("Horas perdidas").
  * **Contextual Feedback (`Toast`):**
    * Interacts with Android's system `Context` via `LocalContext.current` to trigger toast notifications upon reaching specific click thresholds (e.g. 100 resets).

---

### 4. 🔬 [CampoMinadoTESTE](./CampoMinadoTESTE) (Experimental Sandbox)

An internal development prototype used during the exploration and benchmarking of:
* Dynamic grid dimensioning with customizable board widths and heights.
* Parameter passing between Activities via explicit Intents.
* Prototyping custom difficulty selection dialogs prior to final integration into the main `CampoMinado` application.

---

## 📚 Mobile Computing Study Guide

This repository also contains a comprehensive theoretical and practical guide in Portuguese: [README-pt-BR.md](README-pt-BR.md) (and [Resumo_Computacao_Movel.md](Resumo_Computacao_Movel.md)).

Key topics covered include:
* **Android Architecture & Project Structure:** `compileSdk`, `minSdk`, `targetSdk`, Gradle Kotlin DSL (`build.gradle.kts`), and Version Catalogs.
* **The Android Manifest (`AndroidManifest.xml`):** Activity registration, `android:exported`, launcher filters, and soft input modes.
* **Activities & Lifecycle:** `ComponentActivity`, `onCreate`, `enableEdgeToEdge`, `setContent`, and `finish()`.
* **Inter-Screen Navigation (Intents):** Explicit vs. Implicit intents, sending data with `putExtra`, and recovering parameters safely.
* **Declarative UI with Jetpack Compose:** Declarative paradigm, the `@Composable` annotation, `.dp` vs `.sp` sizing rules.
* **Layouts & Modifiers:** `Column`, `Row`, `Box`, `Scaffold`, and chaining `Modifier` properties (`padding`, `clickable`, `scrollable`, `border`).
* **State Management:** `mutableStateOf`, property delegation (`by`), `remember`, `mutableStateListOf`, and defensive cloning for 2D arrays/matrices.
* **Asynchronous Programming & Coroutines:** Native threads vs Coroutines, ANR (Application Not Responding) prevention, and thread dispatchers (`Main`, `IO`, `Default`).
* **Side-Effects (`LaunchedEffect`):** Lifecycle-aware coroutine launches, continuous loops, and reactive key triggers.
* **Local Persistence:** Sandbox file storage with `filesDir` and JSON serialization (`org.json.JSONObject` & `JSONArray`).
* **Context & In-App Feedback:** Accessing `LocalContext.current` and displaying `Toast` notifications.

---

## 🛠️ Tech Stack & Tools

* **Programming Language:** [Kotlin](https://kotlinlang.org/) (100%)
* **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Concurrency:** [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) (`LaunchedEffect`, `delay`, `Dispatchers`)
* **Data Persistence:** Internal Storage (`filesDir`), [JSON](https://www.json.org/) (`org.json`)
* **Build System:** Gradle (Kotlin DSL - `build.gradle.kts`) with Version Catalog (`libs.versions.toml`)
* **Compatibility:** Minimum SDK: API 24 (Android 7.0) | Compile / Target SDK: API 37
* **IDE:** [Android Studio](https://developer.android.com/studio)

---

## 🚀 Getting Started

### Prerequisites

* [Android Studio](https://developer.android.com/studio) (Ladybug / Iguana or newer recommended)
* Android SDK (API level 24 minimum, API 26+ recommended for `java.time` APIs)
* Java Development Kit (JDK 11 or higher)
* Android Emulator or physical device with USB Debugging enabled

### Installation & Execution

1. **Clone the repository:**
   ```bash
   git clone https://github.com/joelcarlos-png/AndroidStudio-Study.git
   cd AndroidStudio-Study
   ```

2. **Open a project in Android Studio:**
   * Launch Android Studio.
   * Click on **Open**.
   * Browse into the cloned repository folder and select the project you wish to run:
     * [`CampoMinado`](./CampoMinado)
     * [`JogoDaVelha`](./JogoDaVelha)
     * [`Teste1`](./Teste1)
     * [`CampoMinadoTESTE`](./CampoMinadoTESTE)

3. **Build & Run:**
   * Allow Gradle to sync dependencies.
   * Select your target emulator or connected physical Android device.
   * Press **Run** (`Shift + F10` or the green Play button ▶️).

---

## 📄 License

This repository is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
