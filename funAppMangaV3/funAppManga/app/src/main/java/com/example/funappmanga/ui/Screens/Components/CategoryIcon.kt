package com.example.funappmanga.ui.Screens.Components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import com.example.funappmanga.data.model.MangaCategory

@Composable
fun CategoryIcon (category : MangaCategory){

    Canvas(modifier = Modifier.size(16.dp)) {

        when (category) {

            MangaCategory.SHONEN -> drawCircle(Color.Red)
            MangaCategory.SEINEN -> drawRect(Color.DarkGray)
            MangaCategory.SHOJO -> drawPath(
                path = Path().apply {

                    moveTo(size.width / 2, 0f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()

                },
                color = Color(0xFFFFC0CB)
            )

        }

    }

}