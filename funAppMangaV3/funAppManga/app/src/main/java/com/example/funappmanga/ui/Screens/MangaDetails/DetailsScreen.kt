package com.example.funappmanga.ui.Screens.MangaDetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.funappmanga.ui.Screens.Components.DetailContent
import com.example.funappmanga.ui.Screens.Components.ErrorState
import com.example.funappmanga.ui.Screens.Components.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    mangaId: Int,
    viewModel: DetailViewModel = viewModel(),
    onBackClick: () -> Unit,
    isSmallMode: Boolean = false // Pasamos esto para quitar el contenido si esta modo small.
) {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    //Extraemos el player del ViewModel para pasárselo al contenido
    val exoPlayer = viewModel.player

    // Al iniciar vamos a cargar el objeto manga.
    LaunchedEffect(mangaId) {
        viewModel.loadManga(mangaId, context)
    }

    // Si estamos en PiP, NO queremos Scaffold, ni TopBar, ni Padding.
    if (isSmallMode) {
        (state as? DetailUiState.Success)?.let { success ->
            //Pasamos el player para que la mini-ventana lo reconozca
            DetailContent(manga = success.manga, isSmallMode = true, exoPlayer = exoPlayer)
        }
    } else {
        // Modo normal de la interfaz.
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Detalles del Manga") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Atras")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (val uiState = state) {
                    is DetailUiState.Success -> {
                        //Pasamos el player aquí también
                        DetailContent(uiState.manga, isSmallMode = false, exoPlayer = exoPlayer)
                    }
                    is DetailUiState.Loading -> LoadingState()
                    is DetailUiState.Error -> {
                        ErrorState(
                            message = uiState.message,
                            onBack = { onBackClick() }
                        )
                    }
                }
            }
        }
    }
}