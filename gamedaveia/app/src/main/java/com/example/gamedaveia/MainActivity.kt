package com.example.gamedaveia

import android.content.Intent
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.fastCbrt
import com.example.gamedaveia.ui.theme.GamedaveiaTheme

class MainActivity : ComponentActivity() {
    var jogodedois by mutableStateOf(true)
    var clicou by mutableStateOf(false)
    var texto by mutableStateOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GamedaveiaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement  = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Box(modifier = Modifier.padding(bottom = 50.dp)){
                            Text("JOGO DA VELHA",
                                color = Color(0xFFFFFFFF),
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold
                            ) }

                        Box(modifier = Modifier.padding(bottom = 30.dp)){
                            Text("Escolha um modo de jogo",
                                color = Color(0xFF358333),
                                fontSize = 25.sp,
                                fontWeight = FontWeight.Bold
                            ) }

                        Row() {
                            Box(modifier = Modifier
                                .clickable(){
                                    jogodedois = true
                                    clicou = true
                                }
                                .padding(20.dp)
                                .border(1.dp, Color.DarkGray)
                                .size(width = 130.dp, height = 70.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Text("Jogo de Dois")
                            }

                            Box(modifier = Modifier
                                .clickable(){
                                    jogodedois = false
                                    clicou = true
                                }
                                .padding(20.dp)
                                .border(1.dp, Color.DarkGray)
                                .size(width = 130.dp, height = 70.dp),
                                contentAlignment = Alignment.Center
                            ){
                                Text("Jogo contra Bot")
                            }

                        }
                        Button(modifier = Modifier.padding(top = 20.dp),
                            onClick = {
                                if(clicou == true){
                                    if(jogodedois){
                                        val intent = Intent(this@MainActivity, telasembot::class.java)
                                        startActivity(intent)
                                    }else{
                                        val intent = Intent(this@MainActivity, telacombot::class.java)
                                        startActivity(intent)
                                    }
                                }else{
                                    texto = "Selecione uma opcao"
                                }
                        }) {
                            Text("Começar")
                        }

                        Box(modifier = Modifier.padding(top = 35.dp)){
                            Text(texto, color = Color.Red, fontSize = 25.sp)
                        }

                    }
                    }
            }
        }
    }
}