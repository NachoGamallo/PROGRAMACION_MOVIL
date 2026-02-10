package com.example.funappmanga.ui.Screens.Components

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.AsyncImage
import com.example.funappmanga.data.model.Manga

@OptIn(UnstableApi::class)
@Composable
fun DetailContent(
    manga: Manga,
    isSmallMode: Boolean, // Si esto es True, significa que estamos en la ventanita pequeña (PiP)
    exoPlayer: ExoPlayer?  //Recibimos el player ya creado desde el ViewModel
) {
    val scrollState = rememberScrollState()

    // Controlamos si el video ya cargó para quitar la imagen de portada
    var isVideoReady by remember { mutableStateOf(false) }

    //Escuchamos el estado del video para saber cuándo ocultar la carátula
    DisposableEffect(exoPlayer) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) isVideoReady = true
            }
        }
        exoPlayer?.addListener(listener)
        onDispose {
            //Aquí NO hacemos release(), solo quitamos el listener
            exoPlayer?.removeListener(listener)
        }
    }

    // Si estamos en la ventanita pequeña , NO queremos que la pantalla tenga scroll
    val columnModifier = if (isSmallMode) {
        Modifier.fillMaxSize()
    } else {
        Modifier.fillMaxSize().verticalScroll(scrollState)
    }

    Column(modifier = columnModifier) {

        // --- SECCIÓN DEL VIDEO ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // Si estamos en PiP, el video ocupa toda la mini-ventana
                .height(if (isSmallMode) 400.dp else 250.dp)
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            if (exoPlayer != null) {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = !isSmallMode // Sin controles en PiP
                            setBackgroundColor(android.graphics.Color.BLACK)
                        }
                    },
                    update = { view ->
                        // ⚡ Mantenemos la vista actualizada con el mismo player
                        view.player = exoPlayer
                    },
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Mientras el video carga (y solo si no estamos en modo pequeño), mostramos la portada
            if (!isVideoReady && !isSmallMode) {
                AsyncImage(
                    model = manga.imgURL,
                    contentDescription = "Portada de carga",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                Box(
                    modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }

        // --- SECCIÓN DE TEXTOS ---
        // Si isSmallMode es True, todo este bloque directamente NO EXISTE.
        if (!isSmallMode) {
            Column(
                modifier = Modifier.padding(20.dp).fillMaxWidth()
            ) {
                Text(
                    text = manga.name,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(15.dp))

                Text(
                    text = "Género: ${manga.category}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Sinopsis",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = manga.desc ?: "Sin descripción disponible.",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp),
                    lineHeight = 22.sp
                )
            }
        }
    }
}