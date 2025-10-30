package com.example.jetpackcompose3

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackcompose3.ui.theme.JetpackCompose3Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetpackCompose3Theme {

                MyApp()

        }
    }
}}

@Composable
fun MyApp(modifier: Modifier = Modifier){

    var showOnboarding by remember { mutableStateOf(true) }

    Surface (modifier){

        if (showOnboarding) OnboardingScreen(onContinueClicked = { showOnboarding = false }) else Greetings()

    }

}

@Composable
private fun Greetings(
    modifier: Modifier = Modifier,
    names : List<String> = listOf("World","Compose")
){

    Column (modifier = modifier.padding(vertical = 4.dp)){

        for (name in names){

            Greeting(name = name)

        }

    }

}

//1
@SuppressLint("UnrememberedMutableState")
@Composable
fun Greeting(name: String) {

    var expanded by remember { mutableStateOf(false) }
    val extraPadding = if (expanded) 48.dp else 0.dp

    Surface (
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding( vertical = 4.dp , horizontal = 8.dp)
    ) {

        Row ( modifier = Modifier.padding(24.dp)) {

            Column ( modifier = Modifier.weight(1f)) {

                Text("Hello")
                Text(text = name)

            }
            ElevatedButton( onClick = { expanded = !expanded } ) {

                Text( if (expanded) "Show less" else "Show more")

            }

        }

    }

}

//2
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    onContinueClicked : () -> Unit
){

    var showOnboarding by remember { mutableStateOf(true) }

    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text("Welcome to the Basics Codelab!")
        Button(
            modifier = Modifier.padding( vertical = 24.dp),
            onClick = onContinueClicked
        ) {

            Text("Continue")

        }

    }

}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    JetpackCompose3Theme {
        MyApp(Modifier.fillMaxSize())
    }
}