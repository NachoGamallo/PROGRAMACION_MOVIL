package com.example.a2mediumcomposable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a2mediumcomposable.screens.NumbersScreen
import com.example.a2mediumcomposable.screens.OperationsScreen
import com.example.a2mediumcomposable.screens.ResultsScreen
import com.example.a2mediumcomposable.ui.theme.A2MediumComposableTheme
import com.example.a2mediumcomposable.viewmodel.CalculatorViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF005BC0),
                    secondary = Color(0xFF2C7DBC),
                    tertiaryContainer = Color(0xFFE0F7FF),
                    error = Color(0xFFD32F2F),
                    errorContainer = Color(0xFFFFCDD2),
                    surfaceContainerHigh = Color(0xFFF5F5F5)
                ),
                shapes = Shapes(
                    extraLarge = RoundedCornerShape(24.dp),
                    large = RoundedCornerShape(16.dp),
                    medium = RoundedCornerShape(12.dp),
                    small = RoundedCornerShape(8.dp)
                )
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorApp()
                }
            }
        }
    }
}

@Composable
fun CalculatorApp() {
    val navController = rememberNavController()
    // Obtener el ViewModel. Compose se encarga de que sea el mismo para todas las pantallas.
    val viewModel: CalculatorViewModel = viewModel()

    // Definición de las pantallas del NavHost
    NavHost(
        navController = navController,
        startDestination = CalculatorScreen.Start.name
    ) {
        // Pantalla 1: Selección
        composable(route = CalculatorScreen.Start.name) {
            OperationsScreen(navController = navController, viewModel = viewModel)
        }

        // Pantalla 2: Ingreso de Números
        composable(route = CalculatorScreen.Input.name) {
            NumbersScreen(navController = navController, viewModel = viewModel)
        }

        // Pantalla 3: Resultado
        composable(route = CalculatorScreen.Result.name) {
            ResultsScreen(navController = navController, viewModel = viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    A2MediumComposableTheme {

    }
}

enum class CalculatorScreen {

    Start,
    Input,
    Result

}