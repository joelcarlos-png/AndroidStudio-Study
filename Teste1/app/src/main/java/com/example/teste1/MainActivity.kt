package com.example.teste1

import android.os.Build
import android.os.Bundle
import android.util.Log.i
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.teste1.ui.theme.Teste1Theme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Teste1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize().padding(innerPadding)
                    ) { }
                    /*Greeting(
                        name = "",
                        modifier = Modifier.padding(16.dp)
                    )*/
                    MainButton(onCountChanged = {valor -> println("$valor")})
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "$name",
        modifier = modifier
    )
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainButton(onCountChanged: (Int) -> Unit){
    //var count by remember { mutableStateOf(0) }
    val context = LocalContext.current

    val horas = remember {mutableStateListOf<String?>().apply { repeat(10){add(null)}}}
    var indice by remember {mutableStateOf(0)}
    var resets by remember {mutableStateOf(0)}

    Box(
        modifier = Modifier.fillMaxSize()
    ){
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(top = 150.dp, start = 70.dp).size(230.dp, 45.dp).border(1.dp, Color.Gray, CircleShape)
        ) {
            Text(
                "Horas perdidas: $resets",
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
            )
        }

        Button(
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 60.dp).size(width = 280.dp, height = 80.dp).border(1.dp, Color.White, CircleShape),
            onClick = {
                //count++
                //onCountChanged(count)
                val horaAgora = LocalDateTime.now()
                val formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")
                val dataAgora = horaAgora.format(formatador)

                if(indice > 9){
                    for(i in 0..8){
                        horas[i] = horas[i + 1]
                    }
                    indice = 9
                    resets++
                    if(resets == 100){
                        Toast.makeText(context,"Voce ja perdeu 100 horas",Toast.LENGTH_SHORT).show()
                    }
                }
                horas[indice] = dataAgora
                //Toast.makeText(context,"Hora agora guardada no indice: " + indice,Toast.LENGTH_SHORT).show()
                indice++

            },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.LightGray,
                contentColor = Color.Black
            )
        ) {
            Text(
                text = "Guardar Hora",
                style = androidx.compose.material3.MaterialTheme.typography.headlineSmall
            )
        }

        Column(
            modifier = Modifier.align(Alignment.Center).padding(top = 120.dp),
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)){
                caixinha(0, horas[0])
                caixinha(1, horas[1])
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)){
                caixinha(2, horas[2])
                caixinha(3, horas[3])
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)){
                caixinha(4, horas[4])
                caixinha(5, horas[5])
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)){
                caixinha(6, horas[6])
                caixinha(7, horas[7])
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)){
                caixinha(8, horas[8])
                caixinha(9, horas[9])
            }
            }
        }
    }

@Composable
fun caixinha(i: Int, hora: String?){
    Box(
        Modifier.size(165.dp, 96.dp).border(1.dp, Color.DarkGray),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            if(hora != null) "$i: $hora" else "$i: ",
            Modifier.padding(12.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Teste1Theme {
        Greeting("Android")
    }
}