package com.example.a2mediumcomposable.data

data class OrderUiState (

    //Operacion que va a realizar el usuario.
    val operation: String = "",

    //Lista de las operaciones que el usuario podrá realizar.
    val operationsOptions : List<String> = listOf(),

    //Lista de números disponibles para ingresar, del 0 al 9.
    val listOfNumbers : List<Int>,

    //Lista de los números que el usuario va a pasar a la pantalla final.
    val userNumbersList : List<Int> = emptyList(),

    //resultado de la operación.
    val result : Double = 0.0


)