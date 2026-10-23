package com.example.jogodavelha

import android.R.attr.text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Matrix
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jogodavelha.ui.theme.JogodavelhaTheme

class TelaPrincipal : ComponentActivity() {
    var matriz by mutableStateOf(List(3) { MutableList(3) { -1 } })

    fun jogada()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val jogador1 = intent.getStringExtra("JOGADOR_1") ?: "Jogador 1"
        val jogador2 = intent.getStringExtra("JOGADOR_2") ?: "Jogador 2"
        val qmComeca = intent.getIntExtra("QM_COMECA", 1)

        enableEdgeToEdge()
        setContent {
            JogodavelhaTheme {
                telaPrincipal(
                    jogador1 = jogador1,
                    jogador2 = jogador2,
                    qmComeca = qmComeca,
                    matriz = matriz
                )
            }
        }
    }
}

@Composable
fun telaPrincipal(
    jogador1 : String,
    jogador2 : String,
    qmComeca : Int,
    matriz : ArrayList<ArrayList<Int>>,
    onQuadradoClicado: (linha: Int, coluna: Int) -> Unit
) {
    var vezJog1 : Boolean
    if(qmComeca == 1) vezJog1 = true else vezJog1 = false
    var text : String
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement  = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for(i in 0..2){
            Row() {
                for(j in 0 .. 2){
                    Box(modifier = Modifier.padding(1.dp).border(1.dp, Color.DarkGray).size(100.dp)
                        .clickable{
                            onQuadradoClicado(i, j)
                        }
                        ){
                        if(matriz[i][j] == -1){
                            text = ""
                        }else if(matriz[i][j] == 1){
                            text = "X"
                        }else{
                            text = "O"
                        }
                        Text(text)
                    }
                }
            }
        }
    }
}