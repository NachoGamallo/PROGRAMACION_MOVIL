package com.example.a2mediumcomposable.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CalculatorViewModel : ViewModel() {
    // Estado para la operación seleccionada (+, -, *, /)
    // Se usa 'by' porque es un MutableState<String?> (necesita getValue/setValue)
    var selectedOperation by mutableStateOf<String?>(null)
        private set

    var currentInput by mutableStateOf("")


    // Lista de números ingresados por el usuario
    // Esta es una lista mutable, NO necesita .value, se usa como una lista normal.
    val numberList = mutableStateListOf<Int>()

    // Resultado de la operación
    var result by mutableStateOf<String?>(null)
        private set

    // Función para establecer la operación y limpiar el estado
    fun setOperation(operation: String) {
        // 'clear()' es un método estándar de lista, funciona aquí.
        numberList.clear()
        selectedOperation = operation
        result = null
    }

    fun addDigit(digit : Int){

        currentInput += digit.toString()

    }

    fun deleteDigit(){
        if (currentInput.isNotEmpty()){
            currentInput = currentInput.dropLast(1)
        }
    }

    // Función para añadir un número a la lista
    fun addNumber() {

        if (currentInput.isNotEmpty()){

            numberList.add(currentInput.toInt())
            currentInput = ""

        }

    }

    // Función para eliminar el último número
    fun deleteLastNumber() { if (numberList.isNotEmpty()) numberList.removeAt(numberList.lastIndex) }

    // Función para realizar el cálculo
    fun calculateResult() {
        if (numberList.isEmpty() || selectedOperation == null) {
            result = "Error: Faltan números u operación."
            return
        }

        if (numberList.size < 2 && (selectedOperation == "-" || selectedOperation == "/" || selectedOperation == "*")) {
            result = "Error: Necesitas al menos 2 números."
            return
        }

        var calculatedResult = 0.0

        try {
            calculatedResult = when (selectedOperation) {

                "+" -> numberList.sum().toDouble()
                "-" -> { numberList.first().toDouble() - numberList.drop(1).sum().toDouble() }
                "*" -> { numberList.fold(1.0) { acc, num -> acc * num } }
                "/" -> {

                    val divisorProduct = numberList.drop(1).fold(1.0) { acc, num ->
                        if (num == 0) throw ArithmeticException("División por cero")
                        acc * num
                    }
                    if (divisorProduct == 0.0) throw ArithmeticException("División por cero")
                    numberList.first().toDouble() / divisorProduct
                }

                else -> throw IllegalArgumentException("Operación no válida")
            }

            // Formatear el resultado: sin decimales si es un entero, sino con cuatro decimales.
            result = if (calculatedResult == calculatedResult.toLong().toDouble()) {
                calculatedResult.toLong().toString()
            } else {
                String.format("%.4f", calculatedResult)
            }
        } catch (e: Exception) {
            result = "Error en el cálculo: ${e.message}"
        }
    }

    // Función para resetear el estado y volver a la pantalla inicial
    fun resetState() {
        selectedOperation = null
        numberList.clear()
        result = null
    }
}