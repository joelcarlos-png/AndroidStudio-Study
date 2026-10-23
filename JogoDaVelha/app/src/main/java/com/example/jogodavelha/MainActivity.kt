package com.example.jogodavelha

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jogodavelha.ui.theme.JogodavelhaTheme

class MainActivity : ComponentActivity() {
    private var nomeJogador1 by mutableStateOf("")
    private var nomeJogador2 by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement  = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
                ) {
                var inputJog1 by remember { mutableStateOf("") }
                var inputJog2 by remember { mutableStateOf("") }
                var TextoIniciado by remember { mutableStateOf("") }

                Box(modifier = Modifier
                    .size(width = 350.dp, height = 100.dp),
                    contentAlignment = Alignment.Center){
                    Text("Digite o nome dos jogadores", fontSize = 25.sp)
                }

                TextField(
                    value = inputJog1,
                    onValueChange = { inputJog1 = it },
                    label = { Text("Jogador 1") }
                )

                TextField(
                    value = inputJog2,
                    onValueChange = { inputJog2 = it },
                    label = { Text("Jogador 2") }
                )


                Button(
                    modifier = Modifier.padding(top = 10.dp).size(width = 150.dp, height = 50.dp),
                    onClick = {
                        if(inputJog1.isNotEmpty() && inputJog2.isNotEmpty()){
                            val qmComeca = (1..2).random()

                            nomeJogador1 = inputJog1
                            nomeJogador2 = inputJog2
                            val intent = Intent(this@MainActivity, TelaPrincipal::class.java).apply{
                                putExtra("JOGADOR_1", inputJog1)
                                putExtra("JOGADOR_2", inputJog2)
                                putExtra("QM_COMECA", qmComeca)
                            }
                            startActivity(intent)
                        }else{
                            TextoIniciado = "Algum Nome está VAZIO!!"
                        }
                    }
                ) {
                    Text("Iniciar Jogo")
                }

                Box(
                    modifier = Modifier
                        .padding(top = 5.dp)
                        .size(width = 250.dp, height = 100.dp),
                    contentAlignment = Alignment.Center
                ){
                    if(inputJog1.isEmpty() || inputJog2.isEmpty()){
                        Text(TextoIniciado, fontSize = 15.sp, color = Color.Red)
                    }else{
                        Text(TextoIniciado, fontSize = 25.sp, color = Color.Green)
                    }
                }

            }
        }


    }
}

