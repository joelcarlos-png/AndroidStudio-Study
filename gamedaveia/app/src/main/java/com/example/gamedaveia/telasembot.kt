package com.example.gamedaveia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gamedaveia.ui.theme.GamedaveiaTheme

class telasembot : ComponentActivity() {
    var matriz by mutableStateOf(List(3) { MutableList(3) { -1 } })

    val matrizVerif = listOf(
        listOf(0,  0,  0,   0,  1,  2), // Linha 0: (0,0), (0,1), (0,2)
        listOf(1,  1,  1,   0,  1,  2), // Linha 1: (1,0), (1,1), (1,2)
        listOf(2,  2,  2,   0,  1,  2), // Linha 2: (2,0), (2,1), (2,2)
        listOf(0,  1,  2,   0,  0,  0), // Coluna 0: (0,0), (1,0), (2,0)
        listOf(0,  1,  2,   1,  1,  1), // Coluna 1: (0,1), (1,1), (2,1)
        listOf(0,  1,  2,   2,  2,  2), // Coluna 2: (0,2), (1,2), (2,2)
        listOf(0,  1,  2,   0,  1,  2), // Diagonal Principal:  (0,0), (1,1), (2,2)
        listOf(0,  1,  2,   2,  1,  0)  // Diagonal Secundária: (0,2), (1,1), (2,0)
    )

    var vezJog1 by mutableStateOf(true)

    var jogadas by mutableStateOf(0)

    fun jogada(i : Int, j : Int){
        if(matriz[i][j] == -1){
            val novaMatriz = matriz.map { it.toMutableList() }
            if(vezJog1) novaMatriz[i][j] = 1 else novaMatriz[i][j] = 2
            matriz = novaMatriz
            vezJog1 = !vezJog1
            jogadas++
        }
    }

    fun verifGanhou() : Int{
        for(i in matrizVerif){
            val p1 = matriz[i[0]][i[3]]
            val p2 = matriz[i[1]][i[4]]
            val p3 = matriz[i[2]][i[5]]

            if((p1 != -1 && p2 != -1 && p3 != -1) && (p1 == p2 && p1 == p3)){
                return p1
            }
        }
        return -1
    }

    var textganhou by mutableStateOf("")

    var jogador1 by mutableStateOf(-1)
    var jogador2 by mutableStateOf(-1)
    var ganhou by mutableStateOf(-1)
    var escolheu by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamedaveiaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                        verticalArrangement  = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if(!escolheu){

                            Box(modifier = Modifier.padding(bottom = 30.dp)){
                                Text("Jogador 1 escolhe\n\n(Jogador 2 ficara com oq sobrar)",
                                    color = Color(0xFF358333),
                                    fontSize = 25.sp,
                                    fontWeight = FontWeight.Bold
                                ) }

                        Row() {
                            Box(modifier = Modifier
                                .clickable(){
                                    jogador1 = 1
                                    jogador2 = 2
                                    escolheu = true
                                }
                                .padding(20.dp)
                                .border(1.dp, Color.DarkGray)
                                .size(width = 130.dp, height = 70.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Text(text = "✕", color = Color(0xFFE53935))
                            }

                            Box(modifier = Modifier
                                .clickable(){
                                    jogador1 = 2
                                    jogador2 = 1
                                    escolheu = true
                                }
                                .padding(20.dp)
                                .border(1.dp, Color.DarkGray)
                                .size(width = 130.dp, height = 70.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Text(text = "○" ,color = Color(0xFF1E88E5))
                            }

                        }
                        }
                        if(escolheu){
                            telaPrincipal(
                                jogador1 = jogador1,
                                jogador2 = jogador2,
                                matriz = matriz,
                                vezJog1 = vezJog1,
                                textganhou = textganhou,
                                ganhou = ganhou,
                                onQuadradoClicado = {linha, coluna ->
                                    jogada(linha,coluna)
                                    if(jogadas >= 5){
                                        ganhou = verifGanhou()
                                        if(ganhou == 1){
                                            textganhou = "Jogador $jogador1 GANHOU"
                                        }else if(ganhou == 2){
                                            textganhou = "Jogador $jogador2 GANHOU"
                                        }else if(jogadas >= 9){
                                            textganhou = "Deu Velha"
                                        }else{
                                            textganhou = ""
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun telaPrincipal(
    jogador1 : Int,
    jogador2 : Int,
    matriz : List<List<Int>>,
    vezJog1 : Boolean,
    textganhou : String,
    ganhou : Int,
    onQuadradoClicado: (linha: Int, coluna: Int) -> Unit
) {
    var text : String
    var cor : Color

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement  = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier
            .size(width = 260.dp, height = 80.dp)
            .padding(bottom = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("# Jogo Da Velha #", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF046A10))
        }

        Row() {
            Box(
                modifier = Modifier
                    .padding(bottom = 20.dp)
                    .padding(2.dp)
                    .border(1.dp, Color.DarkGray)
                    .size(width = 180.dp, height = 60.dp),
                contentAlignment = Alignment.Center
            ){
                if(jogador1 == 1){
                    Text("Jogador 1: ✕ \nJogador 2: ○",fontSize = 18.sp)
                }else{
                    Text("Jogador 1: ○ \nJogador 2: ✕ ",fontSize = 18.sp)
                }


            }
        }

        Box( modifier = Modifier
            .padding(5.dp)
            .size(width = 180.dp, height = 60.dp),
            contentAlignment = Alignment.Center)
        {
            if(vezJog1) {
                text = "Vez de ✕"
                cor = Color(0xFFE53935)
            } else {
                text = "Vez de ○"
                cor = Color(0xFF1E88E5)
            }

            Text(text, fontSize = 24.sp,fontWeight = FontWeight.Bold, color = cor)
        }



        for(i in 0..2){
            Row() {
                for(j in 0 .. 2){
                    Box(modifier = Modifier.padding(1.dp).border(1.dp, Color.DarkGray).size(100.dp)
                        .clickable(ganhou == -1){
                            onQuadradoClicado(i, j)
                        },
                        contentAlignment = Alignment.Center
                    ){
                        if(matriz[i][j] == 1){
                            text = "✕"
                            cor = Color(0xFFE53935)
                        }else if(matriz[i][j] == 2){
                            text = "○"
                            cor = Color(0xFF1E88E5)
                        }else{
                            text = ""
                            cor = Color.Transparent
                        }
                        Text(text, color = cor, fontSize = 40.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Box(modifier = Modifier
            .size(width = 300.dp, height = 80.dp)
            .padding(bottom = 15.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = textganhou, fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color(0xFF046A10))
        }
    }
}