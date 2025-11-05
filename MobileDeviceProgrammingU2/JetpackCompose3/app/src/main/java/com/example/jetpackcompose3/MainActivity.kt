package com.example.jetpackcompose3

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    var showOnboarding by remember { mutableStateOf(true) }

    if (showOnboarding) {
        OnboardingScreen2(onContinueClicked = { showOnboarding = false})
    } else { Items2() }

}

@Composable
fun OnboardingScreen2( onContinueClicked: () -> Unit ){

    Column (

        modifier = Modifier.fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ){

        Text("JetpackCompose 3!!", fontSize = 26.sp)

        Spacer( modifier = Modifier.height(15.dp))

        Button (onClick = onContinueClicked,

            colors = ButtonDefaults.buttonColors()

        ){

            Text("Continuar")

        }

    }

}

@Composable
fun Items2() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Encabezado principal
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ){

            Column (modifier = Modifier.weight(0.7f)){

                Text("Onboarding UI", fontSize = 32.sp, fontWeight = FontWeight.Bold)
                Text("I earn draggable nodes. user can tap them to open more.")

            }

            ItemCard()

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Filas como diseño final
        ItemCirclesRow()
        ItemCirclesRow()
        ItemBoxRow()
        ItemBoxRow()

        Spacer(modifier = Modifier.height(16.dp))

        // Footer con círculo + texto
        Row(verticalAlignment = Alignment.CenterVertically) {
            CircleItem(icon = Icons.Default.Person)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text("Onboarding Steam", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text("El onboarding es muy útil para el usuario.")
            }


        }
    }
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
                    .padding(8.dp)

            ) {

                Text(text = "Onbording Ui",
                    fontSize = 28.sp)
                Text("El estoicismo es una filosofía griega fundada por Zenón de Citio.")

            }

            Column(
                modifier = Modifier.weight(0.3f)
            ) {

                Text("Expand",
                    fontSize = 20.sp)
                Text("This buttom will expand.")
                Button(onClick = {  }) {
                    Text(text = "Expand")
                }

            }

        }

        ItemCircles()
        ItemCircles()
        ItemBoxes()
        ItemBoxes()

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(8.dp)
        ) {

            Box(
                modifier = Modifier.size(75.dp)
                    .border(2.dp,
                        Black,
                        CircleShape)
                    .padding(20.dp)
            )

            Column(
                modifier = Modifier.weight(0.8f)
            ) {

                Text("Onboarding Steam",
                    fontSize = 28.sp)
                Text("El onboarding es una herramienta muy util para lo que la usamos.")

            }

        }


    }
    
}

@Composable
fun ItemCard(){

    var expanded by rememberSaveable { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .width(110.dp)
            .height(if (expanded) 150.dp else 110.dp)
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.ui.graphics.Color.Blue // azul clarito
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Expand",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            if (expanded) {
                Text(
                    text = "More info displayed!",
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }

            Button(
                onClick = { expanded = !expanded },
                shape = RoundedCornerShape(50),
                modifier = Modifier.height(28.dp)
            ) {
                Text(
                    text = if (expanded) "Hide" else "Expand",
                    fontSize = 12.sp
                )
            }
        }
    }

}

@Composable
fun CircleItem(icon: ImageVector) {

    Box(
        modifier = Modifier
            .size(75.dp)
            .background(color = androidx.compose.ui.graphics.Color.Black , shape = CircleShape)
            .border(4.dp, color = androidx.compose.ui.graphics.Color.White ,CircleShape)
            .shadow(8.dp, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(32.dp)
        )
    }

}

@Composable
fun ItemCirclesRow() {

    Row (modifier = Modifier.fillMaxWidth()){

        Box (
            modifier = Modifier
                .padding(vertical = 5.dp)
                .border(2.dp, androidx.compose.ui.graphics.Color.Black, RoundedCornerShape(16.dp))
                .padding(8.dp)
                .weight(0.7f)
        ){

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                repeat(3) {
                    CircleItem(icon = Icons.Default.Star)
                }
            }

        }

        ItemCard()

    }
}

@Composable
fun ItemBoxRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        repeat(3) {
            ItemCard()
        }
    }
}


@Composable
fun ItemCircles() {

    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(8.dp)
    ) {

        Column(
            modifier = Modifier.weight(0.7f)
                .padding(8.dp)

        ) {

            Row (modifier = Modifier.fillMaxWidth()
                .border(1.dp,Black),
                horizontalArrangement = Arrangement.SpaceBetween,) {

                repeat(3){

                    Box(
                        modifier = Modifier.size(75.dp)
                            .border(2.dp,
                                Black,
                                CircleShape)
                            .padding(20.dp)
                    )

                }

            }

        }

        Column(
            modifier = Modifier.weight(0.3f)
        ) {

            Text("Expand",
                fontSize = 20.sp)
            Text("This buttom will expand.")
            Button(onClick = {  }) {
                Text(text = "Expand")
            }

        }

    }

}

@Composable
fun ItemBoxes(){

    Row(modifier = Modifier.fillMaxWidth()
        .padding(8.dp)){

        repeat(3){

            Column(
                modifier = Modifier.weight(0.3f)
            ) {

                Text("Expand",
                    fontSize = 20.sp)
                Text("This buttom will expand.")
                Button(onClick = {  }) {
                    Text(text = "Expand")
                }

            }

        }

    }

}

@Preview(showBackground = true, widthDp = 320)
@Composable
fun GreetingPreview() {
    JetpackCompose3Theme {
        MyApp2(Modifier.fillMaxSize())
    }
}