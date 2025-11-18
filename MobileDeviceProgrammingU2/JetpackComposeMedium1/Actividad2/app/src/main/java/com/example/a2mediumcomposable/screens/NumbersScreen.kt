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
    val numbers = DataSource.listOfNumbers

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // *** Pantalla de número temporal ***
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Text(
                text = if (viewModel.currentInput.isEmpty()) "—"
                else viewModel.currentInput,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(20.dp)
            )
        }

        // *** TECLADO ***
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            numbers.chunked(3).forEach { rowNumbers ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    rowNumbers.forEach { number ->
                        Button(
                            onClick = { viewModel.addDigit(number) },
                            modifier = Modifier
                                .weight(1f)
                                .padding(4.dp)
                                .aspectRatio(1f),
                        ) {
                            Text(text = number.toString(), fontSize = 24.sp)
                        }
                    }
                    if (rowNumbers.size < 3) {
                        repeat(3 - rowNumbers.size) {
                            Spacer(Modifier.weight(1f).padding(4.dp))
                        }
                    }
                }
            }

            // *** NUEVO BOTÓN GENERAR ***
            Button(
                onClick = { viewModel.addNumber() },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(top = 8.dp),
                enabled = viewModel.currentInput.isNotEmpty()
            ) {
                Text("GENERAR", fontSize = 18.sp)
            }

            // *** BOTÓN BORRAR ***
            Button(
                onClick = { viewModel.deleteDigit() },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(vertical = 8.dp)
            ) {
                Text("BORRAR", fontSize = 18.sp)
            }
        }

        Spacer(Modifier.height(20.dp))

        // *** LISTA DE NÚMEROS INGRESADOS ***
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
        ) {
            Row(modifier = Modifier.padding(12.dp)) {
                Text("Números Ingresados:", fontWeight = FontWeight.SemiBold)

                val numbersDisplay = viewModel.numberList.joinToString(", ")
                Text(text = if (numbersDisplay.isEmpty()) "(Vacío)" else numbersDisplay)
            }
        }

        // *** BOTÓN FINAL CALCULAR ***
        Button(
            onClick = {
                viewModel.calculateResult()
                navController.navigate(CalculatorScreen.Result.name)
            },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(vertical = 16.dp),
            enabled = viewModel.numberList.isNotEmpty()
        ) {
            Text("CALCULAR Y ENVIAR", fontSize = 18.sp)
        }
    }
}

