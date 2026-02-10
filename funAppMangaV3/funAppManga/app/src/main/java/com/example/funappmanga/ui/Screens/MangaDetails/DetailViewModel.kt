package com.example.funappmanga.ui.Screens.MangaDetails

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.funappmanga.data.model.Manga
import com.example.funappmanga.data.model.MangaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<DetailUiState>(DetailUiState.Loading)
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    // Mantenemos el Player aquí para que sobreviva a la recomposición de la UI
    var player: ExoPlayer? by mutableStateOf(null)
        private set

    fun loadManga(mangaId: Int, context: Context){
        // Buscamos el manga en nuestro repo.
        val manga = MangaRepository.getMangaById(mangaId)

        if (manga != null){
            _uiState.value = DetailUiState.Success(manga)

            if (player == null) {
                val videoPath = "android.resource://${context.packageName}/${manga.videoURL}"
                setPlayer(context, videoPath)
            }
        } else {
            _uiState.value = DetailUiState.Error("Manga no encontrado...")
        }
    }

    private fun setPlayer(context: Context, url: String){
        player = ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
            // playWhenReady = true asegura que el video siga rodando al entrar en PiP
            playWhenReady = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Solo liberamos la memoria cuando el ViewModel se destruye definitivamente
        player?.release()
        player = null
    }
}

sealed class DetailUiState {
    object Loading : DetailUiState()
    data class Success(val manga: Manga): DetailUiState()
    data class Error(val message: String): DetailUiState()
}