package com.example.jetpackcomposemedium2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jetpackcomposemedium2.ui.theme.JetPackComposeMedium2Theme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetPackComposeMedium2Theme {

                val windowSizeClass = calculateWindowSizeClass(activity = this)

                MainActivity3(windowSizeClass)
                //EmailApp(windowSizeClass)
                //AdaptativeLayout(windowSizeClass)
            }
        }
    }
}

@Composable
fun AdaptativeLayout(windowSizeClass : WindowSizeClass){
    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded){

        Row(Modifier.fillMaxSize()) {
            Text("test" , modifier = Modifier.padding(20.dp))
            Divider(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(1.dp)
            )
            MainContent()
        }

    }else {

        Column (
            modifier = Modifier.fillMaxSize()
        ) {
            MainContent()
        }

    }
}

//INICIO ACTIVIDAD 1.

@Composable
fun MainContent() {
    AnimatedOpacityExample()
}

@Composable
fun EmailApp(widthSizeClass: WindowSizeClass){

    var selectedEmail by remember {mutableStateOf<Email?>(null)}

    mockEmailApp(
        selectedEmail = selectedEmail,
        onEmailSelected = { selectedEmail = it },
        widthSizeClass = widthSizeClass,
        onBack = { selectedEmail = null }
    )

}

//Funcion principal , la cual va a hacer las diferentes pantallas según el tamaño de la pantalla (Orientación).
@Composable
fun mockEmailApp(
    selectedEmail: Email?,
    onEmailSelected: (Email) -> Unit,
    widthSizeClass: WindowSizeClass,
    onBack: () -> Unit
){

    if (widthSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded){

        Row (
            Modifier.fillMaxSize()
        ){

            Box(Modifier.weight(1f)){

                showTitlesOfEmail(onEmailSelected)

            }

            VerticalDivider(Modifier.fillMaxHeight().width(1.dp))

            Box(Modifier.weight(2f)){

                if (selectedEmail != null){

                    showEmailContent(selectedEmail, false, onBack)

                }else {

                    Text(
                        "No hay correo seleccionado...",
                        modifier = Modifier.padding(15.dp),
                        fontSize = 15.sp)
                }

            }

        }

    }else {

        Column (Modifier.fillMaxSize()
            .padding(35.dp)){

            if (selectedEmail == null){

                showTitlesOfEmail(onEmailSelected)

            }else {

                showEmailContent(selectedEmail, true, onBack)

            }

        }

    }


}

data class Email(
    val id : Int,
    val title : String,
    val content : String
)

val listOfEmails : List<Email> = listOf(
    Email(1,"Solicitud de Nomina","Buenos días , queria realizar una solicitud para ver mi nómina de Diciembre 2025, gracias."),
    Email(2,"Oferta de trabajo","Buenos días , venimos a ofrecerle un puesto de desarrollador en las oficinas de Microsoft."),
    Email(3,"Pedido JustEat","Aquí tienes tu recivo del ultimo pedido de JustEat."),
    Email(4,"¿Sabes cuando vale tu coche?","Buenos días, Soy Antonoio lovato, si quieres saber cuanto vale tu coche responde a este correo."),
    Email(5,"Entrega de actividad 2","Buenos días chicos , debéis entregar la actividad para el proximo martes. Un saludo. "))



//Mostrar los correos, cuando seleccionas uno se guarda que email estas clicando.
@Composable
fun showTitlesOfEmail(onEmailSelected : (Email) -> Unit){

    Column (modifier = Modifier.fillMaxSize()){
        listOfEmails.forEach {

            mail -> Column (

            Modifier.fillMaxWidth()
                .clickable { onEmailSelected(mail) }
                .padding(15.dp)

            ){
                Text( text = mail.title, fontSize = 18.sp)
            }
        }
    }

}

//Mostrar el contenido del email enviado.
@Composable
fun showEmailContent(
    email : Email,
    status : Boolean,
    onBack : () -> Unit
){

    Column (modifier = Modifier.fillMaxSize().padding(15.dp)){

        Text(text = email.title , fontSize = 22.sp)
        Spacer(Modifier.height(10.dp))
        Text(text = email.content , fontSize = 15.sp)

        if (status){

            Button(onClick = {onBack()},
                modifier = Modifier.padding(10.dp)) {

                Text("Ir a menú de los Emails.")

            }

        }

    }

}

//FIN ACTIVIDAD 1.



//Animación de aparición y desaparición de cubo.
//2
@Composable
fun AnimatedOpacityExample(){

    var visible by remember { mutableStateOf(true) }
    var isSelected by remember { mutableStateOf(true) }
    val alpha : Float by animateFloatAsState(if (visible) 1f else 0f , label = "")
    val color by animateColorAsState(targetValue = if (isSelected) Color.Green else Color.Gray, label = "")


    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(50.dp)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .alpha(alpha)
                    .background(Color.Blue)
                    .padding(10.dp)
            )

            Button(onClick = {visible = !visible},
                modifier = Modifier.padding(10.dp)) {
                Text("Aparecer/Desaparecer")
            }
        }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){

            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(Color.Blue)
                    .padding(10.dp)
                    .background(color)
            )

            Button(onClick = {isSelected = !isSelected},
                modifier = Modifier.padding(10.dp)) {
                Text("Cambiar color")
            }
        }

        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "Favorite"
        )

        Box(modifier = Modifier.background(color))
        Button(
            onClick = {/*Do something*/},
            modifier = Modifier.size(48.dp)
        ) {
            Icon(Icons.Default.ThumbUp, contentDescription = "Like")
        }

        Text(
            text = "High Contrast Text",
            color = Color.Black,
            modifier = Modifier.background(Color.Yellow)
        )
    }
}

//1
@Composable
fun app(){
    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ){
        Text(text = "test1")
        Text(text = "test2")
        Text(text = "test3")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier.padding(30.dp)
    )
    Counter()
}


//Boton que realiza un conteo de +1 al valor anterior.
//1
@Composable
fun Counter(){

    var count by remember { mutableStateOf(0) }
    Button( onClick = { count++ },
        modifier = Modifier
            .fillMaxWidth()
            .padding(100.dp)) {
        Text("Clicked $count times")
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    JetPackComposeMedium2Theme {
        Greeting("Android")
    }
}