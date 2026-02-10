package com.example.funappmanga.ui.Screens.Components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.funappmanga.data.model.Manga

@Composable
fun MangaCard (
    manga: Manga,
    onFavoriteClick: () -> Unit,
    onClick: () -> Unit
){

    Card(
        shape = RoundedCornerShape(15.dp),
        modifier = Modifier
            .padding(8.dp)
            .width(160.dp)
            .clickable {
                Log.d("NAVEGACIÓN", "Clic detectado en manga: ${manga.id}")
                onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
    ) {

        Column {

            AsyncImage(//Usamos librería COIL
                model = manga.imgURL,
                contentDescription = null,
                modifier = Modifier.height(200.dp).fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Column (modifier = Modifier.padding(8.dp)){

                Row ( verticalAlignment = Alignment.CenterVertically ){

                    CategoryIcon(manga.category)
                    Spacer(Modifier.width(4.dp))
                    Text(manga.category.name, style = MaterialTheme.typography.bodySmall)

                    IconButton(
                        onClick = onFavoriteClick,
                        modifier = Modifier.align(Alignment.Top)
                    ) {
                        Icon(
                            imageVector = if (manga.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (manga.isFavorite) Color.Red else Color.Gray)
                    }

                }

            }
        }

    }

}