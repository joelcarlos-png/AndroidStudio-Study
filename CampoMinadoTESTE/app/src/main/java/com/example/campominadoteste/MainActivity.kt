package com.example.campominadoteste

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campominadoteste.ui.theme.CampoMinadoTESTETheme
import kotlin.math.max
import kotlin.random.Random
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds
import java.io.File
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalTime

class MainActivity : ComponentActivity() {

    data class resultado(
        val hora: String,
        val nome: String,
        val tempo: String,
        val tempoSeg : Int,
        val cliques: Int,
        val qntBombas: Int,
        val tamMatrix: Int,
        val tamMatrix2: Int
    )
    private var tamMatrix2 by mutableStateOf(10)
    private var tamMatrix by mutableStateOf(10)
    private var campo by mutableStateOf(Array(tamMatrix) {IntArray(tamMatrix2)})
    private var campoRevelado by mutableStateOf(Array(tamMatrix) { BooleanArray(tamMatrix2) })
    private var perdeu by mutableStateOf(false)
    private var ganhou by mutableStateOf(false)
    private var qntPartidas by mutableStateOf(value = 0)
    private var cliques by mutableStateOf(0)
    private var tempoSegundos by mutableStateOf(0)
    private var iniciou by mutableStateOf(false)
    private val ranking = mutableStateListOf<resultado>()
    private var mostrarDialogNome by mutableStateOf(false)
    private var nomeJogador by mutableStateOf("")
    private var horaAtual by mutableStateOf("")
    private var qntBombas by mutableStateOf(15)

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        carregarParametrosIntent(intent)
        initCampo()
    }

    private fun carregarParametrosIntent(intent: Intent?) {
        if (intent == null) return
        val largura = intent.getIntExtra("LARGURA", intent.getIntExtra("EXTRA_LARGURA", -1))
        val altura = intent.getIntExtra("ALTURA", intent.getIntExtra("EXTRA_ALTURA", -1))
        val bombas = intent.getIntExtra("BOMBAS", intent.getIntExtra("EXTRA_BOMBAS", -1))

        if (largura > 0) tamMatrix2 = largura
        if (altura > 0) tamMatrix = altura
        if (bombas > 0) qntBombas = bombas
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        carregarParametrosIntent(intent)
        carregarJson()
        initCampo()

        setContent {
            LaunchedEffect(Unit) {
                val format = DateTimeFormatter.ofPattern("HH:mm:ss")
                while (true){
                    delay(1000)
                    horaAtual = LocalTime.now().format(format)
                }
            }
            LaunchedEffect(qntPartidas, iniciou, ganhou, perdeu) {
                while (iniciou && !ganhou && !perdeu){
                    delay(100.milliseconds)
                    tempoSegundos++
                }
            }

            CampoMinadoTESTETheme {
                if (mostrarDialogNome){
                    AlertDialog(
                        onDismissRequest = {},
                        title = {
                            Text("Voce Ganhou")
                        },
                        text = {
                            OutlinedTextField(
                                value = nomeJogador,
                                onValueChange = {novoTexto -> nomeJogador = novoTexto},
                                label = {
                                    Text("Seu Nome")
                                },
                                singleLine = true
                            )
                        },
                        confirmButton = {
                            TextButton(
                                enabled = nomeJogador.isNotBlank(),
                                onClick = {
                                    registrarResultado()
                                }
                            ) {
                                Text("Salvar")
                            }
                        }
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        TelaCampoMinado(
                            tamMatrix = tamMatrix,
                            tamMatrix2 = tamMatrix2,
                            campo = campo,
                            campoRevelado = campoRevelado,
                            perdeu = perdeu,
                            ganhou = ganhou,
                            qntPartidas = qntPartidas,
                            cliques = cliques,
                            tempoSegundos = tempoSegundos,
                            ranking = ranking,
                            horaAtual = horaAtual,
                            qntBombas = qntBombas,
                            onAlterarBombas = {
                                    novaQuantidade -> qntBombas = novaQuantidade
                                initCampo()
                            },
                            onCliqueCelula = { linha, coluna ->
                                revelarCelula(linha, coluna)
                            },
                            onReiniciar = {
                                initCampo()
                            }
                        )
                    }
                }
            }
        }
    }

    private fun carregarJson(){
        val arquivo = File(filesDir, "ranking.json")
        if(!arquivo.exists())return

        val listaJson = JSONArray(arquivo.readText())
        val listaCarregada = mutableListOf<resultado>()

        for(i in 0 until listaJson.length()){
            val objeto = listaJson.getJSONObject(i)

            listaCarregada.add(
                resultado(
                    hora = objeto.getString("hora"),
                    nome = objeto.getString("nome"),
                    tempo = objeto.getString("tempo"),
                    tempoSeg = objeto.getInt("tempoSeg"),
                    cliques = objeto.getInt("cliques"),
                    qntBombas = objeto.getInt("qntBombas"),
                    tamMatrix = objeto.getInt("tamMatrix"),
                    tamMatrix2 = objeto.getInt("tamMatrix2")
                )
            )
        }

        ranking.clear()
        ranking.addAll(
            listaCarregada.sortedBy { it.tempoSeg }.take(10)
        )
    }

    private fun salvarJson(){
        val listaJson = JSONArray()

        for(item in ranking){
            val objeto = JSONObject().apply {
                put("hora", item.hora)
                put("nome", item.nome)
                put("tempoSeg", item.tempoSeg)
                put("tempo", item.tempo)
                put("cliques", item.cliques)
                put("qntBombas", item.qntBombas)
                put("tamMatrix", item.tamMatrix)
                put("tamMatrix2", item.tamMatrix2)
            }

            listaJson.put(objeto)
        }

        val arquivo = File(filesDir, "ranking.json")
        arquivo.writeText(listaJson.toString(2))
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun registrarResultado(){
        val tempo = tempoSegundos / 10
        val minutos = tempo / 60
        val segundos = tempo % 60
        val milesegundos = tempoSegundos % 10
        var tempof = "%02d:%02d:%01d".format(minutos,segundos,milesegundos)

        val format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        val hora = LocalDateTime.now().format(format)

        ranking.add(
            resultado(
                hora = hora,
                nome = nomeJogador.trim(),
                tempo = tempof,
                tempoSeg = tempoSegundos,
                cliques = cliques,
                qntBombas = qntBombas,
                tamMatrix = tamMatrix,
                tamMatrix2 = tamMatrix2
            )
        )

        ranking.sortBy { it.tempoSeg }
        while (ranking.size > 10) {
            ranking.removeAt(ranking.lastIndex)
        }

        salvarJson()

        mostrarDialogNome = false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun verifGanhou() {
        val totalCelulas: Int = tamMatrix * tamMatrix2
        val qntBomba = max(1, (totalCelulas * qntBombas) / 100)
        var qntRevelado = 0
        for (i in 0..<tamMatrix) {
            for (j in 0..<tamMatrix2) {
                if (campoRevelado[i][j] == true) qntRevelado++
            }
        }
        if (qntRevelado == (totalCelulas - qntBomba)) {
            ganhou = true
            qntPartidas++

            nomeJogador = ""
            mostrarDialogNome = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun revelarCelula(linha: Int, coluna: Int) {
        cliques++
        iniciou = true
        if (perdeu || ganhou) return
        if (linha !in 0..<tamMatrix || coluna !in 0..<tamMatrix2) return
        if (campoRevelado[linha][coluna]) return
        val novaMatriz = campoRevelado.map { it.clone() }.toTypedArray()

        fun abrir(l: Int, c: Int) {
            if (l !in 0..<tamMatrix || c !in 0..<tamMatrix2) return
            if (novaMatriz[l][c]) return
            novaMatriz[l][c] = true
            val v = campo[l][c]
            if (v == -1) {
                perdeu = true
                qntPartidas++
            } else if (v == 0) {
                for (v1 in -1..1) {
                    for (v2 in -1..1) {
                        abrir(l + v1, c + v2)
                    }
                }
            }
        }

        abrir(linha, coluna)
        this.campoRevelado = novaMatriz
        verifGanhou()
    }

    private fun colocarNums(){
        for (i in 0..< tamMatrix) {
            for (j in 0..< tamMatrix2) {
                if (campo[i][j] != -1) {
                    for (v1 in -1..1) {
                        for (v2 in -1..1) {
                            val viz1 = i - v1
                            val viz2 = j - v2
                            if (viz1 in 0..<tamMatrix && viz2 in 0..<tamMatrix2) {
                                if (campo[viz1][viz2] == -1) {
                                    campo[i][j]++
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initCampo() {
        cliques = 0
        tempoSegundos = 0
        iniciou = false
        this.perdeu = false
        this.ganhou = false
        this.campo = Array(tamMatrix) {IntArray(tamMatrix2)}
        this.campoRevelado = Array(tamMatrix) {BooleanArray(tamMatrix2)}
        val totalCelulas = tamMatrix * tamMatrix2
        val totalBombas = Math.max(1, (totalCelulas * qntBombas) / 100)

        var bombasColocadas = 0
        while (bombasColocadas < totalBombas){
            val linha = Random.nextInt(tamMatrix);
            val coluna = Random.nextInt(tamMatrix2);

            if(campo[linha][coluna] == 0){
                campo[linha][coluna] = -1
                bombasColocadas++
            }
        }

        colocarNums()
    }
}

fun getCorNumero(num: Int): Color = when (num) {
    1 -> Color(0xFF1976D2) // Azul
    2 -> Color(0xFF388E3C) // Verde
    3 -> Color(0xFFD32F2F) // Vermelho
    4 -> Color(0xFF7B1FA2) // Roxo
    else -> Color.Black
}

@Composable
fun CelulaView(
    valor: Int,
    revelado: Boolean,
    jogoFinalizado: Boolean,
    modifier: Modifier = Modifier,
    tamanhoFonte: androidx.compose.ui.unit.TextUnit = 16.sp,
    onClique: () -> Unit
) {
    val corFundo = when {
        !revelado -> Color(0xFF81C784)      // Verde (Grama / Escondido)
        valor == -1 -> Color(0xFFE57373)    // Vermelho (Bomba)
        else -> Color(0xFFE0E0E0)           // Cinza Claro (Aberto)
    }

    Box(
        modifier = modifier
            .background(corFundo)
            .border(0.5.dp, Color.DarkGray)
            .clickable(enabled = !revelado && !jogoFinalizado) {
                onClique()
            },
        contentAlignment = Alignment.Center
    ) {
        if (revelado) {
            if (valor == -1) {
                Text("💣", fontSize = tamanhoFonte)
            } else if (valor > 0) {
                Text(
                    text = "$valor",
                    color = getCorNumero(valor),
                    fontWeight = FontWeight.Bold,
                    fontSize = tamanhoFonte
                )
            }
        }
    }
}

@Composable
fun TelaCampoMinado(
    tamMatrix: Int,
    tamMatrix2: Int,
    campo: Array<IntArray>,
    campoRevelado: Array<BooleanArray>,
    perdeu: Boolean,
    ganhou: Boolean,
    qntPartidas: Int,
    cliques : Int,
    ranking : List<MainActivity.resultado>,
    tempoSegundos : Int,
    horaAtual : String,
    onCliqueCelula: (Int, Int) -> Unit,
    onReiniciar: () -> Unit,
    qntBombas : Int,
    onAlterarBombas : (Int) -> Unit
) {
    var mostrarRanking by remember { mutableStateOf(false) }
    var mostrarQntBombas by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(

        ) {
            Box(

            ){
                Button(
                    modifier = Modifier.padding(top = 5.dp, end = 60.dp),
                    onClick = {mostrarRanking = true}
                ) {
                    Text("Ranking")
                }

            }
            Box(){
                val tempo = tempoSegundos / 10
                val minutos = tempo / 60
                val segundos = tempo % 60
                val milesegundos = tempoSegundos % 10
                Text("Tentativas: $qntPartidas\n Cliques: $cliques\n %02d:%02d:%01d".format(minutos,segundos,milesegundos), fontSize = 18.sp)
            }

        }

        if (mostrarRanking){
            AlertDialog(
                onDismissRequest = {
                    mostrarRanking = false
                },
                title = {
                    Text("Rankings")
                },
                text = {
                    Column {
                        for (i in 0..9){
                            val item = ranking.getOrNull(i)

                            if(item != null){
                                Text(
                                    "${i + 1}° - ${item.nome} = Hora: ${item.hora} \nTempo: ${item.tempo} Cliques: ${item.cliques} %Bombas: ${item.qntBombas} \nTamanho: ${item.tamMatrix2}X${item.tamMatrix}",
                                    fontSize = 12.sp
                                )
                            }else{
                                Text("${i + 1}° - ====== ",
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = {mostrarRanking = false}) {
                        Text("Fechar")
                    }
                }
            )
        }

        if (mostrarQntBombas){
            var textoBombas by remember { mutableStateOf(qntBombas.toString()) }
            val quantidade = textoBombas.toIntOrNull()
            AlertDialog(
                onDismissRequest = {mostrarQntBombas = false},
                title = {
                    Text("Quantidade de Bombas")
                },
                text = {
                    OutlinedTextField(
                        value = textoBombas,
                        onValueChange = {textoBombas = it},
                        label = {
                            Text("Quantidade de Bombas")
                        },
                        singleLine = true
                    )
                },
                confirmButton = {
                    TextButton(
                        enabled = quantidade != null && quantidade in 1..99,
                        onClick = {
                            quantidade?.let { onAlterarBombas(it) }
                            mostrarQntBombas = false
                        }
                    ) {
                        Text("Salvar")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        val tamanhoFonte = when {
            tamMatrix2 > 20 -> 9.sp
            tamMatrix2 > 12 -> 13.sp
            else -> 16.sp
        }

        Box(
            modifier = Modifier
                .fillMaxWidth(1f)
                .aspectRatio(tamMatrix2.toFloat() / tamMatrix)
        ){
            Column(Modifier.fillMaxSize()) {
                repeat(tamMatrix){i ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        repeat(tamMatrix2){j ->
                            CelulaView(
                                valor = campo[i][j],
                                revelado = campoRevelado[i][j],
                                jogoFinalizado = perdeu || ganhou,
                                tamanhoFonte = tamanhoFonte,
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                onClique = {onCliqueCelula(i, j)}
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onReiniciar) {
            Text("Novo Jogo")
        }
    }
}
