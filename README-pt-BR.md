# AndroidStudio-Study

> 🌐 **Idioma / Language:** Português | [English Version](README.md)

Bem-vindo ao repositório **AndroidStudio-Study**! Este repositório serve como um espaço pessoal e coleção de projetos de estudo, experimentos práticos e aplicativos desenvolvidos utilizando **Android Studio**, **Kotlin** e **Jetpack Compose**.

---

## 📂 Projetos no Repositório

### 1. 💣 [CampoMinado](./CampoMinado) (Minesweeper)
Uma implementação completa do clássico jogo de **Campo Minado** construída com a moderna interface declarativa do Android (Jetpack Compose).

* **Principais Funcionalidades:**
  * **Geração Dinâmica de Tabuleiro:** Matriz 10x10 com posicionamento aleatório de minas (15% de densidade de bombas).
  * **Expansão Recursiva:** Revela automaticamente todas as células vizinhas vazias ao clicar em uma célula com valor zero.
  * **Gerenciamento de Estado de Jogo:** Controle de vitória e derrota com feedback visual instantâneo.
  * **Interface Material 3:** Desenvolvida 100% em Jetpack Compose, com números coloridos indicadores de bombas e estados visuais personalizados para cada célula.

### 2. 🧪 [Teste1](./Teste1) (Playground Compose)
Um projeto sandbox experimental focado no estudo e teste de gerenciamento de estado e componentes de UI no Jetpack Compose.

* **Principais Funcionalidades:**
  * **Estados e Listas Reativas:** Prática com `remember`, `mutableStateOf` e `mutableStateListOf`.
  * **Registro de Eventos e Horários:** Captura e exibição de registros de timestamp utilizando `LocalDateTime`.
  * **Experimentação de Layout:** Uso de `Scaffold`, `Box`, `Column`, `Row` e personalização de bordas e formas.

---

## 🛠️ Tecnologias & Ferramentas

* **Linguagem:** [Kotlin](https://kotlinlang.org/)
* **Framework de UI:** [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
* **Sistema de Build:** Gradle (Kotlin DSL - `build.gradle.kts`) e Catálogo de Versões (`libs.versions.toml`)
* **IDE:** [Android Studio](https://developer.android.com/studio)

---

## 🚀 Como Executar os Projetos

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/joelcarlos-png/AndroidStudio-Study.git
   ```

2. **Abra o projeto no Android Studio:**
   * Abra o Android Studio.
   * Clique em **Open** e selecione a pasta do projeto desejado ([`CampoMinado`](./CampoMinado) ou [`Teste1`](./Teste1)).

3. **Compile e Execute:**
   * Aguarde o término da sincronização do Gradle.
   * Execute o aplicativo em um Emulador Android ou em um dispositivo físico conectado (recomendado Android 8.0 / API 26+).

---

## 📄 Licença

Este projeto está sob a licença MIT - consulte o arquivo [LICENSE](LICENSE) para obter mais detalhes.
