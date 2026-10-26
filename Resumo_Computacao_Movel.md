# 📱 Guia e Resumo Completo de Computação Móvel (Android & Jetpack Compose)

> **Material de Estudo Consolidado**  
> Baseado nos projetos desenvolvidos (`CampoMinado`, `JogoDaVelha`, `Teste1`), no material teórico de apoio e na documentação oficial do [Android Developers](https://developer.android.com/?hl=pt-br).

---

## 📑 Sumário

1. [Visão Geral da Plataforma Android & Estrutura de Projeto](#1-visão-geral-da-plataforma-android--estrutura-de-projeto)
2. [O Manifesto da Aplicação (`AndroidManifest.xml`)](#2-o-manifesto-da-aplicação-androidmanifestxml)
3. [Activities & Ciclo de Vida (Activity Lifecycle)](#3-activities--ciclo-de-vida-activity-lifecycle)
4. [Navegação e Comunicação entre Telas com Intents](#4-navegação-e-comunicação-entre-telas-com-intents)
5. [Interface Declarativa com Jetpack Compose](#5-interface-declarativa-com-jetpack-compose)
6. [Componentes de Layout e Modificadores (`Modifier`)](#6-componentes-de-layout-e-modificadores-modifier)
7. [Componentes Visuais Material 3 (Material Design)](#7-componentes-visuais-material-3-material-design)
8. [Gerenciamento de Estado (State Management) no Compose](#8-gerenciamento-de-estado-state-management-no-compose)
9. [Programação Assíncrona: Threads, Concorrência e Coroutines](#9-programação-assíncrona-threads-concorrência-e-coroutines)
10. [Efeitos Colaterais com `LaunchedEffect`](#10-efeitos-colaterais-com-launchedeffect)
11. [Persistência de Dados Local com Arquivos JSON](#11-persistência-de-dados-local-com-arquivos-json)
12. [Mensagens de Feedback Rápido (`Toast`) e Contexto](#12-mensagens-de-feedback-rápido-toast-e-contexto)
13. [Tabela Resumo: O Que É, O Que Precisa Ter e Onde Foi Usado](#13-tabela-resumo-o-que-é-o-que-precisa-ter-e-onde-foi-usado)
14. [Links e Referências Oficiais](#14-links-e-referências-oficiais)

---

## 1. Visão Geral da Plataforma Android & Estrutura de Projeto

### O que é
O ecossistema Android moderno utiliza a linguagem **Kotlin** e o kit de ferramentas declarativo **Jetpack Compose** para criação de interfaces nativas. A compilação e o empacotamento são orquestrados pelo **Gradle** com Kotlin DSL (`build.gradle.kts`).

### O que precisa ter / Regras no `build.gradle.kts`
* **`compileSdk`**: Versão da API do Android contra a qual o código é compilado.
* **`minSdk`**: Versão mínima do Android requerida para o app rodar (ex: API 24 = Android 7.0). Aparelhos com versão inferior não podem instalar o aplicativo.
* **`targetSdk`**: Versão da API para a qual o aplicativo foi projetado e testado.
* **`buildFeatures { compose = true }`**: Habilita o compilador e as bibliotecas do Jetpack Compose.
* **Dependências essenciais**:
  * `androidx.activity:activity-compose`: Integração de Activities com o Compose.
  * `androidx.compose.material3:material3`: Componentes baseados na especificação Material You / Material 3.
  * `org.jetbrains.kotlinx:kotlinx-coroutines-core` e `kotlinx-coroutines-android`: Suporte nativo a Coroutines.

---

## 2. O Manifesto da Aplicação (`AndroidManifest.xml`)

### O que é
O arquivo `AndroidManifest.xml` é o arquivo de configuração raiz obrigatório de toda aplicação Android. Ele declara a identidade do aplicativo, os componentes essenciais (Activities, Services, Broadcast Receivers), permissões e temas.

### O que precisa ter / Regras
1. **Declaração de Activities**: Toda Activity criada no código precisa obrigatoriamente estar declarada dentro da tag `<application>`. Se uma Activity não for registrada no manifesto, chamá-la gerará uma exceção em tempo de execução (`ActivityNotFoundException`).
2. **Atributo `android:exported`**:
   * `android:exported="true"`: Indica que o componente pode ser iniciado por elementos externos ao aplicativo (obrigatório para a Activity principal que o Launcher do sistema inicia).
   * `android:exported="false"`: Impede que aplicativos externos iniciem essa Activity, garantindo a segurança de telas internas do seu aplicativo.
3. **`<intent-filter>` da tela inicial**: Define qual Activity será a porta de entrada exibida no menu de aplicativos do dispositivo. Deve conter:
   * `<action android:name="android.intent.action.MAIN" />`
   * `<category android:name="android.intent.category.LAUNCHER" />`
4. **`android:windowSoftInputMode="adjustResize"`**: Configura o comportamento da tela ao abrir o teclado virtual, redimensionando o layout para que os campos não fiquem escondidos.

### Sintaxe e Exemplo Real (Extraído do projeto `CampoMinado`):
```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android">

    <application
        android:allowBackup="true"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.CampoMinado">

        <!-- Activity Inicial / Launcher -->
        <activity
            android:name=".SelecaoFaseActivity"
            android:exported="true"
            android:label="@string/app_name"
            android:theme="@style/Theme.CampoMinado"
            android:windowSoftInputMode="adjustResize">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Activity do Jogo (interna) -->
        <activity
            android:name=".MainActivity"
            android:exported="false"
            android:label="@string/app_name"
            android:theme="@style/Theme.CampoMinado"
            android:windowSoftInputMode="adjustResize" />

    </application>
</manifest>
```

---

## 3. Activities & Ciclo de Vida (Activity Lifecycle)

### O que é uma Activity?
Uma **Activity** representa uma única tela focada com a qual o usuário interage, funcionando de forma muito parecida com uma janela ou página. A maioria dos aplicativos Android possui várias telas, o que significa que contêm múltiplas Activities.

### Herança e Regras
* No desenvolvimento moderno com Compose, herda-se de `ComponentActivity` (do pacote `androidx.activity`).
* **`super.onCreate(savedInstanceState)`**: Chamada obrigatória na primeira linha do método `onCreate` para que o framework do Android inicialize o estado interno da janela.
* **`enableEdgeToEdge()`**: Ativa o modo de tela cheia estendida, permitindo que a interface desenhe sob as barras de status e navegação do sistema operacional.
* **`setContent { ... }`**: Ponto de entrada do Jetpack Compose que define o conteúdo visual da Activity.
* **`finish()`**: Finaliza e destrói a Activity atual, retirando-a da pilha de tarefas (back stack) e retornando à tela anterior.
* **`onNewIntent(intent: Intent)`**: Método chamado quando a Activity já está em execução e recebe uma nova Intent (usado em modos de inicialização específicos).
* **`@RequiresApi(Build.VERSION_CODES.O)`**: Anotação necessária quando são utilizadas APIs do Java 8+ de data/hora (`LocalTime`, `LocalDateTime`, `DateTimeFormatter`) que exigem a API 26 (Android Oreo) ou superior.

### Sintaxe e Exemplo de Ciclo de Vida:
```kotlin
package com.example.campominado

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi

class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Habilita o layout sob as barras do sistema

        // Código de inicialização e leitura de parâmetros...

        setContent {
            // Composição visual Jetpack Compose
        }
    }

    private fun voltarParaTelaAnterior() {
        finish() // Encerra a tela atual
    }
}
```

---

## 4. Navegação e Comunicação entre Telas com Intents

### O que é um Intent?
O **Intent** (Intenção) é um objeto de mensagem assíncrono usado para solicitar uma ação de outro componente do aplicativo ou de outro app no sistema.

### Tipos de Intents
1. **Explicit Intents (Intenções Explícitas)**:
   * **Conceito**: Especificam explicitamente o componente de destino pelo nome da sua classe (`NomeDaActivity::class.java`).
   * **Uso**: Transitar entre telas do seu próprio aplicativo (ex: da tela de configurações para a tela do jogo).
2. **Implicit Intents (Intenções Implícitas)**:
   * **Conceito**: Não especificam a classe exata; apenas declaram a ação a ser realizada (ex: `ACTION_VIEW`, `ACTION_SEND`).
   * **Uso**: Delegar tarefas ao sistema ou a outros apps (ex: abrir uma URL no navegador web, compartilhar um texto por WhatsApp ou abrir a câmera).

### Passando Dados entre Telas (`putExtra`)
Para enviar dados da tela A para a tela B:
* Utiliza-se o método `intent.putExtra("CHAVE", valor)`.
* É recomendável organizar as chaves de forma padronizada usando um `companion object` com constantes (`const val EXTRA_... = "..."`) ou chaves strings bem estruturadas.
* O bloco de escopo `.apply { ... }` do Kotlin é a forma idiomática de preencher múltiplos extras antes de despachar a intenção.

### Recuperando Dados na Activity de Destino
* **Para textos**: `intent.getStringExtra("CHAVE") ?: "VALOR_PADRAO"` (retorna `null` se a chave não existir; o operador Elvis `?:` garante um valor seguro).
* **Para inteiros**: `intent.getIntExtra("CHAVE", valorPadrao)` (exige um valor padrão obrigatório caso a chave não seja encontrada).

### Sintaxe e Exemplos Práticos

#### 1. Envio de dados na tela de origem (Exemplo do `JogoDaVelha` / `CampoMinado`):
```kotlin
// Na Activity atual (ex: SelecaoFaseActivity ou MainActivity do JogoDaVelha)
val intent = Intent(this@MainActivity, TelaPrincipal::class.java).apply {
    putExtra("JOGADOR_1", inputJog1)
    putExtra("JOGADOR_2", inputJog2)
    putExtra("QM_COMECA", qmComeca)
}
startActivity(intent)
```

#### 2. Recuperação de dados na tela de destino (Exemplo da `TelaPrincipal`):
```kotlin
// Dentro do onCreate() da Activity de destino:
val jogador1 = intent.getStringExtra("JOGADOR_1") ?: "Jogador 1"
val jogador2 = intent.getStringExtra("JOGADOR_2") ?: "Jogador 2"
val qmComeca = intent.getIntExtra("QM_COMECA", 1)
```

---

## 5. Interface Declarativa com Jetpack Compose

### O Paradigma Declarativo
No modelo tradicional do Android (XML), a UI era imperativa (encontrava-se a view com `findViewById` e alterava-se suas propriedades manualmente).  
No **Jetpack Compose**, a interface é **declarativa**: você descreve como a UI deve se parecer para um determinado estado. Quando o estado muda, o Compose executa novamente as funções de layout necessárias — processo chamado de **Recomposição (Recomposition)**.

### A Anotação `@Composable`
* Qualquer função que produza elementos de tela deve ser anotada com `@Composable`.
* **Regra fundamental**: Funções composable só podem ser chamadas de dentro de outras funções composable ou do bloco `setContent { }` da Activity.
* Devem ser preferencialmente puras e idempotentes (não produzir efeitos colaterais descontrolados diretamente no corpo da função).

### Unidades de Medida Oficiais
* **`.dp` (Density-independent Pixels)**: Utilizado para tamanhos, larguras, alturas, bordas e espaçamentos. Garante que os elementos tenham o mesmo tamanho físico proporcional em telas com diferentes densidades de pixels.
* **`.sp` (Scale-independent Pixels)**: Exclusivo para texto e fontes. Leva em consideração o tamanho da tela e as preferências de acessibilidade de escala de fonte configuradas pelo usuário nas configurações do sistema operacional.

---

## 6. Componentes de Layout e Modificadores (`Modifier`)

### Componentes Fundamentais de Disposição
* **`Column`**: Organiza os elementos filhos em uma sequência vertical de cima para baixo.
  * Parâmetros comuns: `verticalArrangement = Arrangement.Center`, `horizontalAlignment = Alignment.CenterHorizontally`.
* **`Row`**: Organiza os elementos filhos lado a lado na horizontal.
  * Parâmetros comuns: `horizontalArrangement = Arrangement.spacedBy(8.dp)`, `verticalAlignment = Alignment.CenterVertically`.
* **`Box`**: Sobrepõe os elementos em camadas no eixo Z (um em cima do outro) ou atua como um container flexível com alinhamento interno configurado por `contentAlignment = Alignment.Center`.
* **`Spacer`**: Espaçador transparente com tamanho fixo (`Modifier.height(...)` ou `Modifier.width(...)`).

### Modificadores (`Modifier`)
O `Modifier` é o mecanismo central do Compose para decorar, posicionar, dimensionar e adicionar comportamentos a qualquer componente.

#### Principais Modifiers utilizados e suas regras:
1. **`fillMaxSize()`**: O componente expande para preencher 100% do espaço disponível em largura e altura.
2. **`fillMaxWidth(fração)`**: O componente ocupa a largura total ou uma fração proporcional (ex: `0.65f` para 65%).
3. **`size(width, height)` ou `size(dp)`**: Define dimensões estritas para o componente.
4. **`padding(...)`**: Adiciona margem/espaçamento interno. A ordem do `padding` altera o resultado visual (por exemplo: antes ou depois do `background`).
5. **`background(color, shape)`**: Define a cor e o formato de fundo.
6. **`border(width, color, shape)`**: Desenha uma borda externa ao redor do componente.
7. **`clip(shape)`**: Recorta o conteúdo e os toques do componente no formato desejado (`RoundedCornerShape` ou `CircleShape`).
8. **`clickable(enabled = ...) { ... }`**: Torna o componente clicável, registrando toques do usuário.
9. **`verticalScroll(rememberScrollState())` e `horizontalScroll(rememberScrollState())`**: Habilita rolagem bidirecional quando o conteúdo do tabuleiro ou formulário ultrapassa a área visível do display.
10. **`weight(fator, fill = ...)`**: Distribui proporcionalmente o espaço restante disponível dentro de uma `Row` ou `Column`.

### Sintaxe e Exemplo de Célula de Grade Interativa (`CelulaView` do `CampoMinado`):
```kotlin
@Composable
fun CelulaView(
    valor: Int,
    revelado: Boolean,
    jogoFinalizado: Boolean,
    onClique: () -> Unit
) {
    val corFundo = when {
        !revelado -> Color(0xFF81C784)      // Verde (Grama / Fechado)
        valor == -1 -> Color(0xFFE57373)    // Vermelho (Bomba)
        else -> Color(0xFFE0E0E0)           // Cinza Claro (Aberto)
    }

    Box(
        modifier = Modifier
            .size(34.dp)
            .background(corFundo)
            .border(0.5.dp, Color.DarkGray)
            .clickable(enabled = !revelado && !jogoFinalizado) {
                onClique()
            },
        contentAlignment = Alignment.Center
    ) {
        if (revelado) {
            if (valor == -1) {
                Text("💣", fontSize = 16.sp)
            } else if (valor > 0) {
                Text(
                    text = "$valor",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
```

---

## 7. Componentes Visuais Material 3 (Material Design)

### 1. `Scaffold`
Estrutura de layout padrão que oferece suporte automático para barras de sistema (Edge-to-Edge).
```kotlin
Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
    // O innerPadding DEVE ser aplicado no layout raiz para evitar sobreposição com as barras do sistema
    Box(modifier = Modifier.padding(innerPadding)) {
        // Conteúdo da tela
    }
}
```

### 2. `Text`
Renderiza tipografia na tela com suporte a tamanhos (`fontSize`), pesos (`FontWeight.Bold`) e cores (`Color`).

### 3. `Button` e `TextButton`
* **`Button`**: Botão primário com preenchimento sólido de cor. O parâmetro `enabled = false` desativa cliques e aplica opacidade visual automaticamente.
* **`TextButton`**: Botão limpo sem bordas ou preenchimento de fundo, ideal para ações secundárias ou botões de confirmação/fechamento em diálogos.

### 4. `OutlinedTextField`
Campo de texto padronizado com borda externa para entrada de dados pelo usuário.
* **`value`**: O texto atual do campo.
* **`onValueChange = { novoTexto -> ... }`**: Callback disparado a cada caractere digitado.
* **`singleLine = true`**: Impede quebras de linha.
* **`keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)`**: Exibe diretamente o teclado numérico do sistema.

### 5. `AlertDialog`
Janela modal flutuante que interrompe a navegação para solicitar uma confirmação ou exibir informações cruciais (como Rankings ou Fim de Jogo).
```kotlin
AlertDialog(
    onDismissRequest = { /* Fecha o diálogo ao tocar fora ou no botão voltar */ },
    title = { Text("Configurar Partida") },
    text = {
        // Conteúdo com campos de texto ou listas
    },
    confirmButton = {
        TextButton(onClick = { /* ação */ }) {
            Text("Confirmar")
        }
    },
    dismissButton = {
        TextButton(onClick = { /* fechar */ }) {
            Text("Cancelar")
        }
    }
)
```

### 6. `Card`
Contêiner com elevação e cantos arredondados (`RoundedCornerShape`), perfeito para agrupar informações relacionadas ou itens de seleção.

### 7. `FilterChip`
Componente do Material 3 para alternância de filtros e opções (ex: escolher entre modo "Porcentagem" ou "Quantidade Fixa" de bombas).

---

## 8. Gerenciamento de Estado (State Management) no Compose

### O que é Estado?
Estado em um aplicativo é qualquer valor que pode mudar ao longo do tempo (como o texto de um input, a pontuação do jogo ou o tabuleiro).  
No Jetpack Compose, **a interface é uma função direta do estado**.

### 1. `mutableStateOf(valorInicial)`
Cria um objeto de estado observável. Quando o valor contido nele é alterado, o Compose dispara a recomposição dos componentes que leem essa variável.

### 2. Sintaxe com Delegação de Propriedade (`by`)
Em vez de acessar `variavel.value`, utiliza-se a palavra-chave `by` do Kotlin:
```kotlin
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

var perdeu by mutableStateOf(false)
// Ao ler: perdeu
// Ao atribuir: perdeu = true (dispara recomposição automaticamente)
```

### 3. O que faz o `remember { ... }`?
Se você declarar `val estado = mutableStateOf("")` dentro de uma função Composable sem `remember`, a cada nova recomposição a variável será recriada e voltará ao valor inicial.  
O `remember { ... }` instrui o Compose a **armazenar o valor na memória da árvore de composição**, preservando-o entre sucessivas recomposições da tela.
```kotlin
var inputJogador by remember { mutableStateOf("") }
```

### 4. Coleções Reativas com `mutableStateListOf<T>()`
Uma lista comum (`ArrayList` ou `mutableListOf()`) não notifica o Compose quando elementos são inseridos ou removidos.  
Para exibir listas dinâmicas reativas (como a lista de recordes do ranking ou histórico de horários), deve-se usar `mutableStateListOf<T>()`:
```kotlin
val ranking = remember { mutableStateListOf<Resultado>() }
// Operações como ranking.add(...) ou ranking.clear() atualizam a UI instantaneamente
```

### 5. Regra Crítica: Mutação de Estruturas Bidimensionais (Matrizes / Arrays)
O Compose rastreia a referência do objeto. Se você modificar apenas uma posição interna de um array existente (`matriz[i][j] = 1`), o Compose **não** detectará a alteração, pois a referência do array raiz continua a mesma.  
**Solução aplicada nos projetos (Clonagem defensiva)**:
```kotlin
// Clona as linhas para gerar uma nova referência de matriz
val novaMatriz = campoRevelado.map { it.clone() }.toTypedArray()
novaMatriz[linha][coluna] = true
this.campoRevelado = novaMatriz // Atribuir nova referência dispara a recomposição
```

---

## 9. Programação Assíncrona: Threads, Concorrência e Coroutines

### Threads Nativas vs Coroutines

| Característica | Threads Nativas do Sistema Operacional | Coroutines (Corotinas do Kotlin) |
| :--- | :--- | :--- |
| **Gerenciamento** | Gerenciadas pelo kernel do Sistema Operacional. | Gerenciadas no nível de software pela biblioteca Kotlin Coroutines. |
| **Custo de Criação** | Muito custosas em memória e consumo de CPU. | Extremamente leves ("threads leves"); milhares podem rodar simultaneamente. |
| **Comportamento em Pausa** | Bloqueiam a thread física (`Thread.sleep()`), travando o fluxo. | **Suspendem** a execução (`delay()`), liberando a thread física para outras tarefas. |

### O Problema do ANR (Application Not Responding)
A tela do Android roda em uma thread única chamada **Main Thread** (ou UI Thread). Se você executar uma tarefa pesada ou bloqueante nessa thread (como cálculos longos, laços infinitos ou operações de disco/rede):
1. A interface para de responder aos toques do usuário.
2. A taxa de quadros cai para 0 FPS.
3. O sistema operacional exibe o diálogo de erro fatal **ANR (O aplicativo não está respondendo)**, forçando o fechamento do app.

### Os Dispatchers (Quem gerencia as threads?)
Os *Dispatchers* determinam em qual pool de threads a coroutine executará suas instruções:
1. **`Dispatchers.Main`**: Executa na Main Thread. É de uso **obrigatório** para qualquer manipulação ou leitura de estado que atualize a interface do usuário (UI).
2. **`Dispatchers.IO`**: Otimizado para operações de Entrada e Saída (I/O) fora da Main Thread, como leitura/gravação em arquivos (`File`), bancos de dados locais (`Room`) e requisições de rede.
3. **`Dispatchers.Default`**: Otimizado para computação intensiva de CPU, como processamento de gráficos, ordenação de listas gigantes e cálculos matemáticos pesados.

### Alternando Contextos com `withContext`
A função de suspensão `withContext` permite alternar temporariamente para um dispatcher secundário e retornar os dados de volta para a thread principal de forma transparente e segura:
```kotlin
suspend fun carregarDadosPesados() {
    val dados = withContext(Dispatchers.IO) {
        // Executa em segundo plano sem congelar a interface
        lerArquivoDisco()
    }
    // Retorna automaticamente para o contexto anterior (ex: Main Thread)
}
```

---

## 10. Efeitos Colaterais com `LaunchedEffect`

### O que é um Side-Effect (Efeito Colateral)?
No Jetpack Compose, um efeito colateral é qualquer alteração de estado ou execução de tarefa que ocorra fora do controle direto da composição de visualização (ex: iniciar um temporizador, carregar arquivos, fazer animações temporizadas).

### O que é o `LaunchedEffect`?
O `LaunchedEffect` é um composable de efeito colateral projetado para **lançar uma coroutine vinculada com segurança ao ciclo de vida de um Composable**.

### Regras de Ciclo de Vida do `LaunchedEffect`
1. **Entrada na composição**: Quando o composable é desenhado pela primeira vez na tela, a coroutine dentro do `LaunchedEffect` é iniciada.
2. **Saída da composição**: Se o composable sair da tela (ex: o usuário navegar para outra tela ou fechar o app), a coroutine é **cancelada imediatamente e de forma automática**. Esse mecanismo (concorrência estruturada) impede vazamentos de memória (*memory leaks*) e previne consumo inútil de bateria em segundo plano.

### As Chaves de Controle (`keys`)
O `LaunchedEffect` recebe parâmetros chamados de "chaves" (`key1`, `key2`, etc.):
* **`LaunchedEffect(Unit)`**: A constante `Unit` nunca muda. Portanto, a coroutine é disparada **apenas uma vez** quando a tela é montada e continuará viva até a tela ser destruída.
* **`LaunchedEffect(key1, key2)`**: Se o valor de qualquer uma das chaves mudar entre recomposições, o Compose **cancela a coroutine anterior e inicia uma nova imediatamente**.

### Exemplos Reais do Projeto `CampoMinado`

#### 1. Relógio Contínuo em Tempo Real (`key = Unit`):
```kotlin
// Executa continuamente em segundo plano enquanto a tela estiver ativa
LaunchedEffect(Unit) {
    val format = DateTimeFormatter.ofPattern("HH:mm:ss")
    while (true) {
        delay(1000) // Aguarda 1 segundo sem travar a interface
        horaAtual = LocalTime.now().format(format) // Atualiza o estado da UI
    }
}
```

#### 2. Cronômetro Reativo de Partida (`keys` dinâmicas):
```kotlin
// Monitora os estados da partida. Se o jogador iniciar ou terminar, as chaves mudam
LaunchedEffect(qntPartidas, iniciou, ganhou, perdeu) {
    while (iniciou && !ganhou && !perdeu) {
        delay(100.milliseconds) // Incrementa a cada 100 milissegundos
        tempoSegundos++
    }
}
```

---

## 11. Persistência de Dados Local com Arquivos JSON

### O Diretório `filesDir`
O Android isola cada aplicativo em um ambiente de segurança restrito (sandbox).  
A propriedade `filesDir` (disponível na `Activity` via contexto) aponta para o diretório interno e privado do aplicativo no armazenamento do aparelho:
```
/data/data/<nome_do_pacote>/files
```
Nenhum outro aplicativo instalado no celular pode ler ou modificar arquivos gravados nessa pasta.

### Serialização e Deserialização com `org.json`
Nos projetos desenvolvidos, o histórico de partidas e o ranking de pontuações são gravados usando a biblioteca nativa `org.json`:
* **`JSONObject`**: Representa uma estrutura de chave/valor `{ "nome": "Jogador", "tempo": 45 }`.
* **`JSONArray`**: Representa uma lista de objetos `[ { ... }, { ... } ]`.

### Sintaxe e Exemplo de Leitura e Escrita Local (`CampoMinado`):
```kotlin
// 1. Escrita no disco em formato JSON
private fun salvarJson() {
    val listaJson = JSONArray()

    for (item in ranking) {
        val objeto = JSONObject().apply {
            put("hora", item.hora)
            put("nome", item.nome)
            put("tempoSeg", item.tempoSeg)
            put("tempo", item.tempo)
            put("cliques", item.cliques)
            put("qntBombas", item.qntBombas)
        }
        listaJson.put(objeto)
    }

    val arquivo = File(filesDir, "ranking.json")
    arquivo.writeText(listaJson.toString(2)) // O parâmetro 2 aplica indentação legível
}

// 2. Leitura e carregamento a partir do disco
private fun carregarJson() {
    val arquivo = File(filesDir, "ranking.json")
    if (!arquivo.exists()) return // Proteção caso o arquivo ainda não exista

    val listaJson = JSONArray(arquivo.readText())
    val listaCarregada = mutableListOf<Resultado>()

    for (i in 0 until listaJson.length()) {
        val objeto = listaJson.getJSONObject(i)
        listaCarregada.add(
            Resultado(
                hora = objeto.getString("hora"),
                nome = objeto.getString("nome"),
                tempo = objeto.getString("tempo"),
                tempoSeg = objeto.getInt("tempoSeg"),
                cliques = objeto.getInt("cliques"),
                qntBombas = objeto.getInt("qntBombas")
            )
        )
    }

    ranking.clear()
    ranking.addAll(listaCarregada.sortedBy { it.tempoSeg }.take(10))
}
```

---

## 12. Mensagens de Feedback Rápido (`Toast`) e Contexto

### O que é o `Context`?
O `Context` é uma classe abstrata fornecida pelo sistema Android que atua como uma interface para o ambiente global da aplicação, permitindo acesso a recursos do sistema (temas, strings, inicialização de componentes e exibição de alertas).

### Obtendo o Contexto no Compose
Dentro de uma função `@Composable`, o contexto atual é obtido através de:
```kotlin
val context = LocalContext.current
```

### O que é um `Toast`?
Um `Toast` fornece uma mensagem rápida de feedback em uma pequena caixa flutuante na parte inferior da tela, que desaparece automaticamente após alguns segundos.

### Regras do `Toast`
* É obrigatório chamar o método `.show()` ao final para que a notificação apareça.
* A duração pode ser `Toast.LENGTH_SHORT` (~2 segundos) ou `Toast.LENGTH_LONG` (~3.5 segundos).

### Sintaxe e Exemplo Prático (Extraído do projeto `Teste1`):
```kotlin
val context = LocalContext.current

Button(onClick = {
    if (resets == 100) {
        Toast.makeText(context, "Você já perdeu 100 horas", Toast.LENGTH_SHORT).show()
    }
}) {
    Text("Ação")
}
```

---

## 13. Tabela Resumo: O Que É, O Que Precisa Ter e Onde Foi Usado

| Tecnologia / Recurso | O Que É | O Que Precisa Ter / Regra | Onde Foi Usado no Projeto |
| :--- | :--- | :--- | :--- |
| **`AndroidManifest.xml`** | Arquivo de configuração raiz do app Android. | Todas as Activities registradas; `<intent-filter>` na inicial; `exported="true/false"`. | Configuração de todos os projetos (`CampoMinado`, `JogoDaVelha`). |
| **`ComponentActivity`** | Classe base moderna para telas do Android com suporte ao Compose. | Herdar da classe; sobrescrever `onCreate()`; chamar `super.onCreate()` e `setContent {}`. | `MainActivity.kt`, `SelecaoFaseActivity.kt`, `TelaPrincipal.kt`. |
| **`Intent` (Explícita)** | Mensagem assíncrona para transição de telas e envio de dados. | Passar contexto atual e classe destino (`Destino::class.java`); usar `startActivity(intent)`. | `SelecaoFaseActivity` -> `MainActivity`; `MainActivity` -> `TelaPrincipal`. |
| **`putExtra` / `getIntExtra`** | Mecanismo de passagem e leitura de parâmetros entre telas. | Usar chaves strings; destino deve tratar fallback com operador Elvis `?:` ou valor padrão. | Envio de configurações do tabuleiro (`EXTRA_LINHAS`, `JOGADOR_1`, etc.). |
| **`@Composable`** | Anotação que define uma função de renderização declarativa na UI. | Só pode ser chamada por outra função `@Composable`; não deve ter código bloqueante. | Todas as telas e componentes visuais (`CelulaView`, `TelaPrincipal`, etc.). |
| **`remember`** | Função que armazena estados através do ciclo de recomposições. | Usado dentro de Composables; sem ele o valor reinicia a cada redesenho de tela. | `var inputJog1 by remember { mutableStateOf("") }`. |
| **`mutableStateOf`** | Tipo observável que avisa o Compose quando seu valor muda. | Utilizar preferencialmente com a sintaxe `by mutableStateOf` para acesso direto. | Controle de vitória, derrota, tempos, contagem de cliques e matrizes. |
| **`mutableStateListOf`** | Lista observável que dispara recomposição em inserções/remoções. | Ideal para listas dinâmicas; itens adicionados via `.add()` atualizam a tela na hora. | Lista observável de `ranking` no `CampoMinado` e `horas` no `Teste1`. |
| **`LaunchedEffect`** | Composable de efeito colateral para execução segura de Coroutines. | Chaves de controle (`Unit` para execução única; variáveis para reinicialização em mudanças). | Loop infinito do relógio (`Unit`) e cronômetro de partida com `delay()`. |
| **`Coroutines` & `Dispatchers`**| Concorrência estruturada e gerenciamento de threads leves. | `Dispatchers.Main` para UI, `Dispatchers.IO` para arquivos/rede; `delay()` não bloqueante. | Temporização e execução assíncrona nos projetos e slides. |
| **`File` & `filesDir`** | Armazenamento persistente de arquivos locais em sandbox privada. | Usar `filesDir` para segurança; verificar `arquivo.exists()` antes da leitura. | Salvamento e recuperação do arquivo `ranking.json` no `CampoMinado`. |
| **`AlertDialog`** | Diálogo modal flutuante para avisos e formulários rápidos. | `onDismissRequest` para fechamento; botões `confirmButton` e `dismissButton`. | Entrada do nome do vencedor, configuração de bombas personalizadas e ranking. |
| **`Toast`** | Mensagem rápida flutuante do sistema. | Requer `Context` (`LocalContext.current`) e chamada explícita de `.show()`. | Avisos de limites de cliques / horas no projeto `Teste1`. |

---

## 14. Links e Referências Oficiais

Para aprofundar seus estudos, consulte os guias oficiais traduzidos no **Android Developers**:

* 📘 **Activities e Ciclo de Vida**:  
  [https://developer.android.com/guide/components/activities/intro-activities?hl=pt-br](https://developer.android.com/guide/components/activities/intro-activities?hl=pt-br)
* 🧭 **Intents e Filtros de Intent**:  
  [https://developer.android.com/guide/components/intents-filters?hl=pt-br](https://developer.android.com/guide/components/intents-filters?hl=pt-br)
* 🎨 **Introdução ao Jetpack Compose**:  
  [https://developer.android.com/jetpack/compose/documentation?hl=pt-br](https://developer.android.com/jetpack/compose/documentation?hl=pt-br)
* ⚡ **Gerenciamento de Estado no Jetpack Compose**:  
  [https://developer.android.com/jetpack/compose/state?hl=pt-br](https://developer.android.com/jetpack/compose/state?hl=pt-br)
* ⏱️ **Efeitos Colaterais no Jetpack Compose (`LaunchedEffect`)**:  
  [https://developer.android.com/jetpack/compose/side-effects?hl=pt-br](https://developer.android.com/jetpack/compose/side-effects?hl=pt-br)
* 🧵 **Coroutines do Kotlin no Android**:  
  [https://developer.android.com/kotlin/coroutines?hl=pt-br](https://developer.android.com/kotlin/coroutines?hl=pt-br)
* 💾 **Armazenamento de Arquivos Internos (`filesDir`)**:  
  [https://developer.android.com/training/data-storage/app-specific?hl=pt-br](https://developer.android.com/training/data-storage/app-specific?hl=pt-br)
* 📋 **Visão Geral do Manifesto do Aplicativo**:  
  [https://developer.android.com/guide/topics/manifest/manifest-intro?hl=pt-br](https://developer.android.com/guide/topics/manifest/manifest-intro?hl=pt-br)
