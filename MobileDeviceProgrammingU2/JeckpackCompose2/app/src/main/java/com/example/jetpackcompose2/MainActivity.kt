package com.example.jetpackcompose2

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose2.ui.theme.JetpackCompose2Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            JetpackCompose2Theme {

                MyApp(modifier = Modifier)

            }

        }

    }

}

@Composable
fun GreetingCard( modifier: Modifier , name: String ){

    Surface (

        modifier = modifier

    ){
        Greeting(name, modifier = modifier)
    }

}

//Actividad 3
@Composable
fun MyApp(modifier: Modifier = Modifier,
          names: List<String> = listOf("World","Compose")
){

    var rowsFormat : Modifier = modifier.fillMaxWidth().padding(8.dp)

    Column () {

        Row ( modifier = rowsFormat ){

            Text( "Test",
                modifier = modifier.weight(1f))

            Button( onClick = {  } ) {
                Text("Show less")
            }

        }

        Row ( modifier = rowsFormat ){

            Text( "Test2",
                modifier = modifier.weight(1f))

            OutlinedButton( onClick = {  } ) {
                Text("Show less")
            }

        }

        Row ( modifier = rowsFormat ){

            Text( "Test3",
                modifier = modifier.weight(1f))

            TextButton( onClick = {  } ) {
                Text("Show less")
            }

        }
    }



//    Column(modifier = modifier.fillMaxSize()
//        .background(Color.Cyan)
//        .padding(10.dp)) {
//
//
//    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    Surface(color = MaterialTheme.colorScheme.primary){

        Text(
            text = "Hello $name",
            modifier = modifier.padding(24.dp)
        )

    }

}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    JetpackCompose2Theme {
        MyApp()
    }
}