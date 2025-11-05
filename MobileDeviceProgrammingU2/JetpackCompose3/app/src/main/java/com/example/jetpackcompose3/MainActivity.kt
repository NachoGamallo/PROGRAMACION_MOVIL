package com.example.jetpackcompose3

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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

                MyApp2(modifier = Modifier)

        }
    }
}}

@Composable
fun MyApp(modifier: Modifier = Modifier){

    var shouldShowOnboarding by remember { mutableStateOf(true) }

    if (shouldShowOnboarding) OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false }) else Greetings()

}

//EX1
@Composable
fun OnboardingScreen(onContinueClicked : () -> Unit){

    Surface( modifier = Modifier.fillMaxSize()) {

        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){

            Text("Welcome to the Basics Codelab!")
            Spacer(modifier = Modifier.height(24.dp))
            Button(onClick = onContinueClicked) {

                Text("Continue")

            }

        }

    }

}


//Ex1
@Composable
private fun Greetings(names : List<String> = listOf("World","Compose")){

    Column (modifier = Modifier.padding(vertical = 4.dp)){

        for (name in names){

            Greeting(name)

        }

    }

}

//Ex1
@SuppressLint("UnrememberedMutableState")
@Composable
fun Greeting(name: String) {

    var expanded by remember { mutableStateOf(false) }

    Surface (
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {

        Row ( modifier = Modifier.padding(24.dp)) {

            Column ( modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)) {

                Text("Hello, $name!")

                if (expanded) Text("This is some extra details")

            }

            ElevatedButton( onClick = { expanded = !expanded }) {

                Text( if (expanded) "Show less" else "Show more")

            }

        }

    }

}













//Ex2
@Composable
fun MyApp2(modifier: Modifier){

    Items()

}

@Composable
fun Items (){

    Column ( modifier = Modifier.fillMaxSize()){

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
        ) {

            Column(
                modifier = Modifier.weight(0.7f)

            ) {

                Text(text = "Onbording Ui")
                Text("El estoicismo es una filosofía griega fundada por Zenón de Citio en el siglo III a.C. que enseña a vivir una vida de virtud y serenidad, enfocándose en lo que se puede controlar y aceptando lo que no")

            }

            Column(
                modifier = Modifier.weight(0.3f)
            ) {

                Text("Expand")
                Text("This buttom will expand for get a little bit more of info.")
                Button(onClick = {  },
                    modifier = Modifier.background(Color.CYAN)) {
                    Text(text = "Expand")
                }

            }

        }

    }
    
}

@Composable
fun ItemBox() {


        Column(
            //modifier = Modifier.weight(0.25f)
        ) {

            Text("Expand")
            Text("This buttom will expand for get a little bit more of info.")
            Button(onClick = { }) {
                Text("Expand")
            }

        }


}

@Composable
fun OnboardingScreen2(){



}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    JetpackCompose3Theme {
        MyApp2(Modifier.fillMaxSize())
    }
}