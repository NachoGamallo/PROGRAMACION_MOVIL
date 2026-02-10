package com.example.funappmanga

import android.app.PictureInPictureParams
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Rational
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.funappmanga.ui.navigation.AppNavigation
import com.example.funappmanga.ui.theme.FunAppMangaTheme

class MainActivity : ComponentActivity() {

    private var isSmallMode by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FunAppMangaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                )
                {
                    AppNavigation(isSmallMode = isSmallMode)
                }
            }
        }
    }

    // Esta función se activa cuando el usuario le da al botón Home o minimiza
    override fun onUserLeaveHint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val aspectRatio = Rational(16, 9) // Proporción estándar de video
            val params = PictureInPictureParams.Builder()
                .setAspectRatio(aspectRatio)
                .build()
            enterPictureInPictureMode(params)
        }
    }

    // Opcional: Para ocultar la UI de detalles (botones, texto) y dejar solo el video en PiP
    override fun onPictureInPictureModeChanged(isInPip: Boolean, config: Configuration) {
        super.onPictureInPictureModeChanged(isInPip, config)
        isSmallMode = isInPip
    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FunAppMangaTheme {
        AppNavigation(isSmallMode = false)
    }
}