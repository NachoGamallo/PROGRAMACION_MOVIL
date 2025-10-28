package com.example.multiplicationtables

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import kotlin.random.Random

class Game : ComponentActivity(){

    private lateinit var inputGuess : EditText
    private lateinit var btnCheck : Button
    private lateinit var btnReset : Button
    private lateinit var resultText : TextView
    private lateinit var btnBack : Button

    private var number = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.game)

        inputGuess = findViewById<EditText>(R.id.inputGuess)
        btnCheck = findViewById<Button>(R.id.btnCheck)
        btnReset = findViewById<Button>(R.id.btnReset)
        resultText = findViewById<TextView>(R.id.resultText)
        btnBack = findViewById<Button>(R.id.btnBack)

        startGame()

    }

    fun startGame(){

        number = Random.nextInt(1,101)
        resultText.text = "Try to guess the number: "
        inputGuess.text.clear()

        btnCheck.setOnClickListener {

            val guess = inputGuess.text.toString().toIntOrNull()
            if (guess == null){

                Toast.makeText(this,"Enter a valid number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            when {

                guess < number -> resultText.text = "To low."
                guess > number -> resultText.text = "To high."
                else -> resultText.text = "Correct, The number was $number!!"

            }

        }

        btnReset.setOnClickListener { startGame() }
        btnBack.setOnClickListener { finish() }

    }

}