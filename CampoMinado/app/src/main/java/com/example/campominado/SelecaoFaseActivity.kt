package com.example.campominado

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.campominado.ui.theme.CampoMinadoTheme
import kotlin.math.max

class SelecaoFaseActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CampoMinadoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TelaSelecaoFase(
                        modifier = Modifier.padding(innerPadding),
                        onIniciarJogo = { linhas, colunas, porcentagem, bombasFixas ->
                            val intent = Intent(this, MainActivity::class.java).apply {
                                putExtra(EXTRA_LINHAS, linhas)
                                putExtra(EXTRA_COLUNAS, colunas)
                                putExtra(EXTRA_PORCENTAGEM_BOMBAS, porcentagem)
                                putExtra(EXTRA_BOMBAS_FIXAS, bombasFixas)
                            }
                            startActivity(intent)
                        }
                    )
                }
            }
        }
    }

    companion object {
        const val EXTRA_LINHAS = "EXTRA_LINHAS"
        const val EXTRA_COLUNAS = "EXTRA_COLUNAS"
        const val EXTRA_PORCENTAGEM_BOMBAS = "EXTRA_PORCENTAGEM_BOMBAS"
        const val EXTRA_BOMBAS_FIXAS = "EXTRA_BOMBAS_FIXAS"
    }
}

@Composable
fun TelaSelecaoFase(
    modifier: Modifier = Modifier,
    onIniciarJogo: (linhas: Int, colunas: Int, porcentagem: Int, bombasFixas: Int) -> Unit
) {
    var mostrarDialogPersonalizado by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "💣",
            fontSize = 64.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Campo Minado",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "Selecione o nível de dificuldade",
            fontSize = 16.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Card Fácil
        CardDificuldade(
            titulo = "Fácil",
            detalheTamanho = "Tabuleiro 10 x 10",
            detalheBombas = "15% de bombas (15 bombas)",
            corDestaque = Color(0xFF4CAF50),
            onClick = {
                onIniciarJogo(10, 10, 15, -1)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card Médio
        CardDificuldade(
            titulo = "Médio",
            detalheTamanho = "Tabuleiro 16 x 16",
            detalheBombas = "18% de bombas (~46 bombas)",
            corDestaque = Color(0xFFFFA000),
            onClick = {
                onIniciarJogo(16, 16, 18, -1)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card Difícil (30x16: 16 linhas x 30 colunas)
        CardDificuldade(
            titulo = "Difícil",
            detalheTamanho = "Tabuleiro 30 x 16",
            detalheBombas = "21% de bombas (~100 bombas)",
            corDestaque = Color(0xFFE53935),
            onClick = {
                // 16 linhas x 30 colunas para melhor rolagem horizontal
                onIniciarJogo(16, 30, 21, -1)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Card Personalizado
        CardDificuldade(
            titulo = "Personalizado",
            detalheTamanho = "Defina altura e largura",
            detalheBombas = "Escolha a quantidade ou porcentagem",
            corDestaque = Color(0xFF7E57C2),
            onClick = {
                mostrarDialogPersonalizado = true
            }
        )
    }

    if (mostrarDialogPersonalizado) {
        DialogPersonalizado(
            onDismiss = { mostrarDialogPersonalizado = false },
            onConfirmar = { linhas, colunas, porcentagem, bombasFixas ->
                mostrarDialogPersonalizado = false
                onIniciarJogo(linhas, colunas, porcentagem, bombasFixas)
            }
        )
    }
}

@Composable
fun CardDificuldade(
    titulo: String,
    detalheTamanho: String,
    detalheBombas: String,
    corDestaque: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp, 50.dp)
                    .background(corDestaque, shape = RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = corDestaque
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = detalheTamanho,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = detalheBombas,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun DialogPersonalizado(
    onDismiss: () -> Unit,
    onConfirmar: (linhas: Int, colunas: Int, porcentagem: Int, bombasFixas: Int) -> Unit
) {
    var larguraTexto by remember { mutableStateOf("10") }
    var alturaTexto by remember { mutableStateOf("10") }
    var modoPorcentagem by remember { mutableStateOf(true) }
    var bombasTexto by remember { mutableStateOf("15") }

    val largura = larguraTexto.toIntOrNull() ?: 0
    val altura = alturaTexto.toIntOrNull() ?: 0
    val totalCelulas = largura * altura
    val bombasNum = bombasTexto.toIntOrNull() ?: 0

    val bombasCalculadas = if (modoPorcentagem) {
        max(1, (totalCelulas * bombasNum) / 100)
    } else {
        bombasNum
    }

    val valido = largura in 4..50 &&
            altura in 4..50 &&
            bombasCalculadas in 1 until totalCelulas

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Configurar Partida", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = larguraTexto,
                        onValueChange = { larguraTexto = it.filter { c -> c.isDigit() } },
                        label = { Text("Largura (Colunas)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = alturaTexto,
                        onValueChange = { alturaTexto = it.filter { c -> c.isDigit() } },
                        label = { Text("Altura (Linhas)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }

                Text(
                    text = "Total de células: $totalCelulas",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                // Alternar entre Porcentagem ou Quantidade Fixa
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = modoPorcentagem,
                        onClick = {
                            modoPorcentagem = true
                            bombasTexto = "15"
                        },
                        label = { Text("Porcentagem (%)") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = !modoPorcentagem,
                        onClick = {
                            modoPorcentagem = false
                            bombasTexto = "${max(1, (totalCelulas * 15) / 100)}"
                        },
                        label = { Text("Qtd. Fixa") },
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = bombasTexto,
                    onValueChange = { bombasTexto = it.filter { c -> c.isDigit() } },
                    label = {
                        Text(if (modoPorcentagem) "Porcentagem de Bombas (1 a 99%)" else "Quantidade de Bombas")
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                if (totalCelulas > 0) {
                    Text(
                        text = "Total de bombas estimadas: $bombasCalculadas",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (valido) MaterialTheme.colorScheme.primary else Color.Red
                    )
                }

                if (!valido && larguraTexto.isNotEmpty() && alturaTexto.isNotEmpty() && bombasTexto.isNotEmpty()) {
                    Text(
                        text = "Atenção: Largura e Altura devem estar entre 4 e 50, e a quantidade de bombas menor que o total de casas.",
                        color = Color.Red,
                        fontSize = 12.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (valido) {
                        if (modoPorcentagem) {
                            onConfirmar(altura, largura, bombasNum, -1)
                        } else {
                            onConfirmar(altura, largura, 0, bombasNum)
                        }
                    }
                },
                enabled = valido
            ) {
                Text("Iniciar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
