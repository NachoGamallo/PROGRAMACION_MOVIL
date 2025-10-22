package com.example.multiplicationtables

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

class UnitConversor : ComponentActivity() {

    private lateinit var inputValue: EditText
    private lateinit var btnConvert : Button
    private lateinit var resultText : TextView
    private lateinit var unitSpinner: Spinner

    private lateinit var btnBack : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.unit_converter)

        inputValue = findViewById(R.id.inputValue)
        btnConvert = findViewById(R.id.btnConvert)
        resultText = findViewById(R.id.resultText)
        unitSpinner = findViewById(R.id.unitSpinner)
        btnBack = findViewById(R.id.btnBack)

        val conversions = arrayOf(
            "Meters (m) to Kilometers (Km)",
            "Kilometers (Km) to meters (m)",
            "Grames (g) to Kilograms (Kg)",
            "Kilogrames (Kg) to grames (g)"
        )

        val adapter = ArrayAdapter(this,android.R.layout.simple_spinner_item,conversions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        unitSpinner.adapter = adapter

        btnConvert.setOnClickListener {

            val input = inputValue.text.toString().toDoubleOrNull()
            if (input == null){

                Toast.makeText(this,"Enter a valid number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener

            }

            val result = when (unitSpinner.selectedItem.toString()){

                "Meters (m) to Kilometers (Km)" -> input / 1000
                "Kilometers (Km) to meters (m)" -> input * 1000
                "Grames (g) to Kilograms (Kg)" -> input / 1000
                "Kilogrames (Kg) to grames (g)" -> input * 1000
                else -> 0.0

            }

            resultText.text = "Result: $result"

        }

        btnBack.setOnClickListener { finish() }

    }

}