package com.example.campominadoteste

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campominadoteste.ui.theme.CampoMinadoTESTETheme

class SelecaoDificuldade : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CampoMinadoTESTETheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        TelaSelecaoDificuldade(
                            onDificuldadeSelecionada = { largura, altura, porcentagemBombas ->
                                val intent = Intent(this@SelecaoDificuldade, MainActivity::class.java).apply {
                                    putExtra("LARGURA", largura)
                                    putExtra("ALTURA", altura)
                                    putExtra("BOMBAS", porcentagemBombas)
                                    putExtra("EXTRA_LARGURA", largura)
                                    putExtra("EXTRA_ALTURA", altura)
                                    putExtra("EXTRA_BOMBAS", porcentagemBombas)
                                }
                                startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TelaSelecaoDificuldade(
    onDificuldadeSelecionada: (largura: Int, altura: Int, porcentagemBombas: Int) -> Unit
) {
    var mostrarDialogPersonalizado by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "CAMPO MINADO\n\nDificuldade",
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .padding(vertical = 8.dp),
            onClick = {
                onDificuldadeSelecionada(10, 10, 15)
            }
        ) {
            Text("Fácil", fontSize = 18.sp)
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .padding(vertical = 8.dp),
            onClick = {
                onDificuldadeSelecionada(16, 16, 18)
            }
        ) {
            Text("Médio", fontSize = 18.sp)
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .padding(vertical = 8.dp),
            onClick = {
                onDificuldadeSelecionada(16, 30, 21)
            }
        ) {
            Text("Difícil", fontSize = 18.sp)
        }

        Button(
            modifier = Modifier
                .fillMaxWidth(0.65f)
                .padding(vertical = 8.dp),
            onClick = {
                mostrarDialogPersonalizado = true
            }
        ) {
            Text("Personalizado", fontSize = 18.sp)
        }
    }

    if (mostrarDialogPersonalizado) {
        var textoLargura by remember { mutableStateOf("10") }
        var textoAltura by remember { mutableStateOf("10") }
        var textoBombas by remember { mutableStateOf("15") }

        val largura = textoLargura.toIntOrNull()
        val altura = textoAltura.toIntOrNull()
        val bombas = textoBombas.toIntOrNull()

        val ehValido = largura != null && largura in 5..50 &&
                altura != null && altura in 5..50 &&
                bombas != null && bombas in 1..90

        AlertDialog(
            onDismissRequest = {
                mostrarDialogPersonalizado = false
            },
            title = {
                Text("Personalizado")
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = textoLargura,
                        onValueChange = { textoLargura = it },
                        label = { Text("Largura") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = textoAltura,
                        onValueChange = { textoAltura = it },
                        label = { Text("Altura") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = textoBombas,
                        onValueChange = { textoBombas = it },
                        label = { Text("Bombas (%)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = ehValido,
                    onClick = {
                        if (largura != null && altura != null && bombas != null) {
                            mostrarDialogPersonalizado = false
                            onDificuldadeSelecionada(largura, altura, bombas)
                        }
                    }
                ) {
                    Text("Salvar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        mostrarDialogPersonalizado = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
