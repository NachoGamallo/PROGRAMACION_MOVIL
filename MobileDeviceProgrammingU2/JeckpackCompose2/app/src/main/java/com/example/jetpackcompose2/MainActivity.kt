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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcompose2.ui.theme.JetpackCompose2Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            JetpackCompose2Theme {

                MyApp4(modifier = Modifier)

            }

        }

    }

}

//Actividad 1
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
}

//Activity 4 , Explication.

//Step 1 : Create the Basic Screen.
// 1. Open the MainActivity.kt
// 2. Go to onCreate , call a new composable function named MyApp4.
// 3. In MyApp4 , start using a Scaffold to define the main layout of the screen.
// 4. add a Column to organize the content.

//Step 2: Add Rows
// 1. Create a new composable function called RowItem (String).
// 2. Inside this function , use a row composable with
// horizontalArrangement = Arrangement.SpaceBetween to separate the text and the buttom.
// 3. Apply modifiers to the give the row background and space.
// 4. Call to RowItem several times in your column of MyApp4.

//Step 3: Add a textfield and the navegator
// 1. Below the list, add an OutlinedTextFild, to allow to enter text (TO THE FUTURE).
// 2. In the Scaffold , define a bottomBar parameter.
// 3. Inside it , create a new function called BottomBar.
// 4. Use a Row with Arrangement.SpaceEvenly to place 3 boxes, and give it a format.

//Step 4: Adjunst the Preview and Layout
// 1. In your @preview , set widthDp = 320 and heightDp = 550.
// 2. Add verticalArrangement = Arrangment.Top in the column so the items apper higher up.
// 3. Use Arrangement.spacedby() and padding() to maintain the spacing.

@Composable
fun MyApp4 (modifier: Modifier = Modifier){

    var text by remember { mutableStateOf("") }

    Scaffold (
        bottomBar = { BottomBar() }
    ){
        paddingValues ->
        Column (
            modifier = Modifier.fillMaxSize()
                .padding(paddingValues)
                .padding(30.dp),
            verticalArrangement = Arrangement.spacedBy(40.dp)

        ){

            RowItem("Item 1")
            RowItem("Item 2")
            RowItem("Item 3")
            RowItem("Item 4")

            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                label = {Text("Ingresa Texto")},
                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(12.dp)
            )

        }
    }

}

@Composable
fun BottomBar(){

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Blue)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.Top
    ) {
        repeat(3) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, Black, CircleShape)
                    .background(Color.White, CircleShape)
            )

        }

    }

}
@Composable
fun RowItem( name : String){

    Row (
        modifier = Modifier.fillMaxWidth()
            .background(Color.Cyan, RoundedCornerShape(12.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){

        Text(modifier = Modifier.padding(10.dp), text = name)
        Box(
            modifier = Modifier.size(25.dp)
                .border(2.dp,
                Black,
                CircleShape)
                .padding(20.dp)
        )

    }

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

@Preview(showBackground = true, widthDp = 320, heightDp = 600)//Pongo altura 600 porque sino queda demasiado separado.
@Composable
fun GreetingPreview() {
    JetpackCompose2Theme {
        MyApp4()
    }
}