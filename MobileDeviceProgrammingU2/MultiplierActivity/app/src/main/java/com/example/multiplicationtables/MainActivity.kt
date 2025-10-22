package com.example.multiplicationtables

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.multiplicationtables.ui.theme.MultiplicationTablesTheme

class MainActivity : ComponentActivity() {

    //Variables para las etiquetas XML.
    private lateinit var inputNumber: EditText
    private lateinit var btnCalculate : Button
    private lateinit var resultText : TextView

    private lateinit var autoSafe : SharedPreferences //Cuando se minimiza
    // , no se borra el contenido

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) //Con esto conectamos con el formato del XML.

        inputNumber = findViewById(R.id.inputNumber)
        btnCalculate = findViewById(R.id.btnCalculate)
        resultText = findViewById(R.id.resultText)//Ingresamos las variables del XML

        val btnConverter : Button = findViewById(R.id.btnConverter)
        val btnGame : Button = findViewById(R.id.btnGame)

        btnConverter.setOnClickListener {
            startActivity(Intent(this, UnitConversor::class.java))//Ir a actividad de conversion
        }

        btnGame.setOnClickListener {
            startActivity(Intent(this, Game::class.java))//Ir a actividad de juego
        }
        // en variables de la clase.

        autoSafe = getSharedPreferences("MyPrefs",MODE_PRIVATE);//Como una base
        // de datos.

        val lastTable = autoSafe.getString("lastTable","")

        if (!lastTable.isNullOrEmpty()){
            resultText.text = lastTable
        }

        btnCalculate.setOnClickListener { calculateTable() }//Accion que se realiza cuando
    // se toca el boton.

    }

    private fun calculateTable(){

        val input = inputNumber.text.toString().trim()

        if (input.isEmpty()){
            //Cuando se da al boton sin contenido, en vez de hacer crash. Se imprime un mensaje.
            Toast.makeText(this,"Please enter a number", Toast.LENGTH_SHORT).show()
            return

        }

        //Numero ingresado.
        val number = input.toInt()
        val table = StringBuilder()

        for(i in 1..10){
            //Se va agregando al StringBuilder , el numero multiplicado por el multiplo.
            table.append("$number x $i = ${number * i}\n")

        }

        resultText.text = table.toString()//La etiqueta se convierte en el StringBuilder.

        val edit = autoSafe.edit()//Por si acaso se minimiza , hay un guardado
        edit.putString("lastTable: ",table.toString())
        edit.apply()

    }
}