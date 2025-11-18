package com.example.a2mediumcomposable.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.a2mediumcomposable.CalculatorScreen
import com.example.a2mediumcomposable.viewmodel.CalculatorViewModel

@Composable
fun ResultsScreen (

    navController: NavController,
    viewModel: CalculatorViewModel

){
    val operation = viewModel.selectedOperation
    val numbers = viewModel.numberList.joinToString(separator = ", ")
    val result = viewModel.result

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween // Distribuir el contenido
    ) {
        // Contenido del Resultado
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(top = 40.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                Text("RESULTADO FINAL", fontSize = 28.sp, fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary)
                Divider(modifier = Modifier.padding(vertical = 8.dp))

                // Línea de Operación
                ResultLine(label = "Operación Seleccionada:", value = operation ?: "N/A", color = MaterialTheme.colorScheme.secondary)

                // Línea de Números
                ResultLine(label = "Números Ingresados:", value = if (numbers.isEmpty()) "Ninguno" else numbers, color = MaterialTheme.colorScheme.onPrimaryContainer)

                // Línea de Resultado
                Spacer(modifier = Modifier.height(16.dp))
                ResultLine(label = "Resultado:", value = result ?: "Calculando...", isResult = true)
            }
        }

        // Botón de Reset
        Button(
            onClick = {
                viewModel.resetState()
                // Volver a la pantalla inicial (SelectionScreen)
                navController.popBackStack(
                    route = CalculatorScreen.Start.name,
                    inclusive = false
                )
            },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(bottom = 40.dp)
                .height(60.dp),
            shape = MaterialTheme.shapes.large,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
        ) {
            Text("RESET E INICIO", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

    }

}

@Composable
fun ResultLine(label: String, value: String, color: Color = MaterialTheme.colorScheme.onSurface, isResult: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
        Text(
            text = value,
            fontSize = if (isResult) 36.sp else 18.sp,
            fontWeight = if (isResult) FontWeight.ExtraBold else FontWeight.Normal,
            color = if (isResult) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
        )
    }
}
