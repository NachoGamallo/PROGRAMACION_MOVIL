package com.example.jetpackcomposemedium2

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Habit(

    val name: String,
    val description : String,
    val icon : ImageVector,
    val current : Int,
    val goal : Int,
    val completed : Boolean = false
)

val listOfHabits : List<Habit> = listOf(

    Habit("Ejercicio", "Mover el cuerpo para realizar actividad física", Icons.Default.Person, 4, 10),
    Habit( "Leer", "Leer un libro durante 10 minutos diarios", Icons.Default.DateRange, 7, 15),
    Habit( "Comer Bien", "Comer comida saludable , y no pecar con alimentos ultra procesados.", Icons.Default.Favorite, 2, 7),
    Habit("Meditar", "Meditar 5 minutos", Icons.Default.Check, 2, 7)

)

@Composable
fun MainActivity3(widthSizeClass: WindowSizeClass){

    val habits = remember { mutableStateListOf<Habit>().apply { addAll(listOfHabits) } }

    Column(Modifier.fillMaxSize().padding(60.dp)) {

        // Botón de reinicio general
        Button(
            onClick = {
                habits.clear()
                habits.addAll(listOfHabits.map {
                    it.copy(current = 0, completed = false)})
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF9800),
                contentColor = Color.Black
            )
        ) {
            Text("Reiniciar hábitos")
        }

        Spacer(Modifier.height(16.dp))

        mockHabitApp(
            widthSizeClass = widthSizeClass,
            habits = habits,
            onAddProgress = { habit ->
                val index = habits.indexOf(habit)
                val newCurrent = habit.current + 1
                val completed = newCurrent >= habit.goal
                habits[index] = habit.copy(current = newCurrent, completed = completed)
            }
        )
    }

}


@Composable
fun mockHabitApp (

    widthSizeClass: WindowSizeClass,
    habits: List<Habit>,
    onAddProgress: (Habit) -> Unit,
){

    val pendingHabits = habits.filter { !it.completed }
    val completedHabits = habits.filter { it.completed }

    if (widthSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {

        // TABLET: multi-panel.
        Row(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Columna izquierda (pendientes)
            Column(
                Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text("Pendientes", fontSize = 22.sp)
                Spacer(Modifier.height(8.dp))
                LazyColumn {
                    items(pendingHabits.size) { index ->
                        val habit = pendingHabits[index]
                        HabitRow(habit, onAddProgress)
                    }
                }
            }

            // Columna derecha (completados)
            Column(
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text("Completados", fontSize = 22.sp)
                Spacer(Modifier.height(8.dp))
                LazyColumn {
                    items(completedHabits.size) { index ->
                        val habit = completedHabits[index]
                        HabitCompletedRow(habit)
                    }
                }
            }
        }

    } else {
        // MÓVIL: single column con botón +1
        LazyColumn {
            items(habits.size) { index ->
                val habit = habits[index]
                HabitRow(habit, onAddProgress)
            }
        }
    }
}

@Composable
fun HabitCompletedRow(habit: Habit) {
    var expanded = remember { mutableStateOf(false) }

    Column(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .semantics { contentDescription = "Hábito completado: ${habit.name}" }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .semantics { role = Role.Button }
                .then(Modifier)
                .padding(4.dp)
        ) {
            // Icono en verde, al clicar saldra o se escondera el contenido.
            Icon(
                habit.icon,
                contentDescription = "Hábito completado",
                tint = Color(0xFF4CAF50),
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp)
                    .clickable { expanded.value = !expanded.value }
            )

            Spacer(Modifier.width(12.dp))

            Text(
                habit.name,
                fontSize = 18.sp,
                color = Color(0xFF4CAF50)
            )
        }

        // Desplegable animado
        AnimatedVisibility(visible = expanded.value) {
            Column(Modifier.padding(start = 16.dp, top = 4.dp)) {
                Text(habit.description, fontSize = 14.sp, color = Color.Gray)
                Text("Meta: ${habit.goal}", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun HabitRow(habit: Habit, onAddProgress: (Habit) -> Unit) {

    val progress = habit.current.toFloat() / habit.goal
    val animatedProgress by animateFloatAsState(targetValue = progress, animationSpec = tween(600))
    val strokeColor by animateColorAsState(
        targetValue = if (habit.completed) Color(0xFF4CAF50) else Color(0xFFFF9800),
        animationSpec = tween(700)
    )

    Row(
        Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .semantics {
                contentDescription = "Hábito: ${habit.name}"
                stateDescription = "Progreso: ${habit.current} de ${habit.goal}"
                role = Role.Button
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Progress Ring
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(60.dp)) {
            Canvas(Modifier.size(60.dp)) {
                drawArc(
                    color = strokeColor,
                    startAngle = -90f,
                    sweepAngle = animatedProgress * 360f,
                    useCenter = false,
                )
            }
            Icon(habit.icon, contentDescription = null, tint = Color.Black, modifier = Modifier.size(30.dp))
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(habit.name, fontSize = 20.sp)
            Text("${habit.current}/${habit.goal}", fontSize = 14.sp)
            Text(habit.description, fontSize = 12.sp, color = Color.Gray)
        }

        Spacer(Modifier.width(12.dp))

        // Botón +1
        Button(
            onClick = { onAddProgress(habit) },
            enabled = !habit.completed,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFFF9800),
                contentColor = Color.Black
            )
        ) {
            Text("+1", color = Color.Black)
        }
    }
}


