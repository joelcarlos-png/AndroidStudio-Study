# AndroidStudio-Study

> 🌐 **Language / Idioma:** English | [Versão em Português](README-pt-BR.md)

Welcome to the **AndroidStudio-Study** repository! This repository serves as a personal workspace and collection of study projects, practical experiments, and applications developed using **Android Studio**, **Kotlin**, and **Jetpack Compose**.

---

## 📂 Projects in this Repository

### 1. 💣 [CampoMinado](./CampoMinado) (Minesweeper)
A fully functional implementation of the classic **Minesweeper** game built with modern Android declarative UI.

* **Key Features:**
  * **Dynamic Board Generation:** 10x10 grid with automated mine allocation (15% mine density).
  * **Recursive Expansion:** Automatically uncovers neighboring empty cells when a zero-value cell is tapped.
  * **Game State Management:** Tracks wins and losses reactively with instant UI feedback.
  * **Material 3 UI:** Built entirely with Jetpack Compose, featuring color-coded indicator numbers and custom cell states.

### 2. 🧪 [Teste1](./Teste1) (Compose Playground)
An exploratory sandbox project focused on testing Jetpack Compose state management and UI components.

* **Key Features:**
  * **State & Collections:** Practice with `remember`, `mutableStateOf`, and `mutableStateListOf`.
  * **Event & Timestamp Logging:** Captures and displays timestamp records using `LocalDateTime`.
  * **Layout Experiments:** Exploring `Scaffold`, `Box`, `Column`, `Row`, and custom borders/shapes.

---

## 🛠️ Tech Stack & Tools

* **Language:** [Kotlin](https://kotlinlang.org/)
* **UI Framework:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Build System:** Gradle (Kotlin DSL - `build.gradle.kts`) & Version Catalogs (`libs.versions.toml`)
* **IDE:** [Android Studio](https://developer.android.com/studio)

---

## 🚀 Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/joelcarlos-png/AndroidStudio-Study.git
   ```

2. **Open a project in Android Studio:**
   * Launch Android Studio.
   * Select **Open** and choose either the [`CampoMinado`](./CampoMinado) or [`Teste1`](./Teste1) directory.

3. **Build & Run:**
   * Wait for Gradle sync to complete.
   * Run the project on an Android Emulator or a connected physical device (API level 26+ recommended).

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.