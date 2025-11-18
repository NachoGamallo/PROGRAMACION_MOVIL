package com.example.a2mediumcomposable.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a2mediumcomposable.CalculatorScreen
import com.example.a2mediumcomposable.data.DataSource
import com.example.a2mediumcomposable.viewmodel.CalculatorViewModel

@Composable
fun NumbersScreen (

    navController: NavController,
    viewModel: CalculatorViewModel

){
    // *** Usando DataSource ***
    val numbers = DataSource.listOfNumbers

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Título con la operación seleccionada
        Text(
            text = "Operación: ${viewModel.selectedOperation ?: "?"}",
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 20.dp, bottom = 16.dp),
            color = MaterialTheme.colorScheme.secondary
        )

        // Teclado Numérico (Grid de 3x4)
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            numbers.chunked(3).forEach { rowNumbers ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowNumbers.forEach { number ->
                        Button(
                            onClick = { viewModel.addNumber(number) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .aspectRatio(1f),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(text = number.toString(), fontSize = 24.sp)
                        }
                    }
                    // Espacio para la columna restante en la última fila si es necesario
                    if (rowNumbers.size < 3) {
                        repeat(3 - rowNumbers.size) {
                            Spacer(modifier = Modifier.weight(1f).padding(4.dp))
                        }
                    }
                }
            }

            // Botón de Borrar (DELETE)
            Button(
                onClick = { viewModel.deleteLastNumber() },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    contentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Spacer(Modifier.width(8.dp))
                Text("BORRAR", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Área de visualización de la lista de números
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "Números Ingresados:",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Muestra la lista como una cadena legible
                val numbersDisplay = viewModel.numberList.joinToString(separator = ", ")
                Text(
                    text = if (numbersDisplay.isEmpty()) "(Vacío)" else numbersDisplay,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Botón para enviar y calcular (Navegar a Pantalla 3)
        Button(
            onClick = {
                viewModel.calculateResult()
                navController.navigate(CalculatorScreen.Result.name)
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 16.dp)
                .height(56.dp),
            enabled = viewModel.numberList.isNotEmpty(),
            shape = MaterialTheme.shapes.large
        ) {
            Text("CALCULAR Y ENVIAR", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}