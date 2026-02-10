package com.example.funappmanga.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.funappmanga.ui.Screens.Home.HomeScreen
import com.example.funappmanga.ui.Screens.MangaDetails.DetailsScreen

@Composable
fun AppNavigation(isSmallMode: Boolean) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home" //Ruta inicial.
    ){

        //Main screen.
        composable("home"){

            HomeScreen (
                onMangaClick = { id ->
                    navController.navigate("detail/$id")
                }
            )
        }

        //Pantalla detalle.
        composable("detail/{mangaId}",
            arguments = listOf(navArgument("mangaId") { type = NavType.IntType })
        ){ backStackEntry ->
            val mangaId = backStackEntry.arguments?.getInt("mangaId") ?: 0
            DetailsScreen(
                mangaId = mangaId,
                isSmallMode = isSmallMode,
                onBackClick = {
                    navController.popBackStack() //Accion de ir para atras
                }
            )
        }
    }
}