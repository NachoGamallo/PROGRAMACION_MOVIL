package com.example.jetpackcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose.ui.theme.JetpackComposeTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackComposeTheme {
                Surface(

                ){
                    Greeting("Android")
                }

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Surface(color = MaterialTheme.colorScheme.primary){}
    Text(
        text = "Hello $name!",
        color = Color.Red,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(50.dp)
            .clip(RoundedCornerShape(5.dp))
            .background(Color.Cyan)
            .fillMaxWidth()
            .border(2.dp, Color.Blue)
    )

    ThirdExercice()

}

@Composable
fun ThirdExercice (){

    var name by remember { mutableStateOf("") } //Variables que irán cambiando el contenido según lo que se ingrese por el usuario.
    var text by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.padding(100.dp)//Generamos una columna con padding 100, para estar separado al texto inicial.
            .fillMaxWidth()//Ocupa todo el width.
    ){
        Spacer (modifier = Modifier.height(200.dp))//Esto son espacios , para que no este todo el contenido junto.

        TextField(
            value = name,
            onValueChange = {name = it},
            label = {Text("Tu nombre: ")}
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { text = "Hola que tal $name?" }) {
            Text("Generar saludo")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = text,
            modifier = Modifier
                .background(Color.Cyan)//Color del fondo en cyan
                .fillMaxWidth()//Ocupa todo el espacio
                .border(2.dp,Color.Gray)//Con un border de 2dp de diametro y de color gris
                .padding(10.dp)//10 de padding , para que no este el texto junto. 
        )
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetpackComposeTheme {
        Greeting("Android")
    }
}
