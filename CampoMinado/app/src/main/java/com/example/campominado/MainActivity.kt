package com.example.campominado

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campominado.ui.theme.CampoMinadoTheme
import kotlin.math.max
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private var tamMatrix: Int = 10
    private lateinit var campo : Array<IntArray>
    private var campoRevelado by mutableStateOf(Array(tamMatrix) { BooleanArray(tamMatrix) })
    private var perdeu by mutableStateOf(false)
    private var ganhou by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        initCampo()

        setContent {
            CampoMinadoTheme {
                var refreshTick by remember { mutableIntStateOf(0) }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        TelaCampoMinado(
                            tamMatrix = tamMatrix,
                            campo = campo,
                            campoRevelado = campoRevelado,
                            perdeu = perdeu,
                            ganhou = ganhou,
                            tick = refreshTick,
                            onCliqueCelula = { linha, coluna ->
                                revelarCelula(linha, coluna)
                                refreshTick++
                            },
                            onReiniciar = {
                                initCampo()
                                refreshTick++
                            }
                        )
                    }
                }
            }
        }
    }


    private fun expandirZeros(i: Int, j: Int) {
        for (v1 in -1..1) {
            for (v2 in -1..1) {
                val viz1 = i + v1
                val viz2 = j + v2
                if (viz1 in 0..<tamMatrix && viz2 in 0..<tamMatrix) {
                    if (!campoRevelado[viz1][viz2]) {
                        revelarCelula(viz1, viz2)
                    }
                }
            }
        }
    }
    fun verifGanhou() {
        val totalCelulas: Int = tamMatrix * tamMatrix
        val qntBomba = max(1, (totalCelulas * 15) / 100)
        var qntRevelado = 0
        for (i in 0..<tamMatrix) {
            for (j in 0..<tamMatrix) {
                if (campoRevelado[i][j] == true) qntRevelado++
            }
        }
        if (qntRevelado == (totalCelulas - qntBomba)) ganhou = true
    }

    private fun revelarCelula(linha: Int, coluna: Int) {
        if (perdeu || ganhou) return
        if (linha !in 0..<tamMatrix || coluna !in 0..<tamMatrix) return
        if (campoRevelado[linha][coluna]) return
        val novaMatriz = campoRevelado.map { it.clone() }.toTypedArray()
        fun abrir(l: Int, c: Int) {
            if (l !in 0..<tamMatrix || c !in 0..<tamMatrix) return
            if (novaMatriz[l][c]) return
            novaMatriz[l][c] = true
            val v = campo[l][c]
            if (v == -1) {
                perdeu = true
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
            for (j in 0..< tamMatrix) {
                if (campo[i][j] != -1) {
                    for (v1 in -1..1) {
                        for (v2 in -1..1) {
                            val viz1 = i - v1
                            val viz2 = j - v2
                            if (viz1 in 0..<tamMatrix && viz2 in 0..<tamMatrix) {
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
        this.perdeu = false
        this.ganhou = false
        this.campo = Array(tamMatrix) {IntArray(tamMatrix)}
        this.campoRevelado = Array(tamMatrix) {BooleanArray(tamMatrix)}
        val totalCelulas = tamMatrix * tamMatrix
        val totalBombas = Math.max(1, (totalCelulas * 15) / 100)

        var bombasColocadas = 0
        while (bombasColocadas < totalBombas){
            val linha = Random.nextInt(tamMatrix);
            val coluna = Random.nextInt(tamMatrix);

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
    onClique: () -> Unit
) {
    val corFundo = when {
        !revelado -> Color(0xFF81C784)      // Verde (Grama / Escondido)
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
                    color = getCorNumero(valor),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun TelaCampoMinado(
    tamMatrix: Int,
    campo: Array<IntArray>,
    campoRevelado: Array<BooleanArray>,
    perdeu: Boolean,
    ganhou: Boolean,
    tick: Int,
    onCliqueCelula: (Int, Int) -> Unit,
    onReiniciar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when {
            perdeu -> Text("💥 VOCÊ PERDEU!", color = Color.Red, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            ganhou -> Text("🏆 PARABÉNS, VOCÊ GANHOU!", color = Color(0xFF388E3C), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            else -> Text("💣 Campo Minado", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        for (i in 0 until tamMatrix) {
            Row {
                for (j in 0 until tamMatrix) {
                    CelulaView(
                        valor = campo[i][j],
                        revelado = campoRevelado[i][j],
                        jogoFinalizado = perdeu || ganhou,
                        onClique = { onCliqueCelula(i, j) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onReiniciar) {
            Text("Novo Jogo")
        }
    }
}