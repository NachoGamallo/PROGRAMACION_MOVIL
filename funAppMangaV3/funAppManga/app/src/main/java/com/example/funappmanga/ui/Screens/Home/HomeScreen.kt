package com.example.funappmanga.ui.Screens.Home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.funappmanga.ui.Screens.Components.MangaCard
import com.example.funappmanga.ui.theme.FunAppMangaTheme
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color


@Composable
fun HomeScreen (
    viewModel: HomeViewModel = viewModel(),
    onMangaClick: (Int) -> Unit
){
    val state by viewModel.uiState.collectAsState()

    Column ( modifier = Modifier.fillMaxSize().padding(top = 15.dp)){
        //Barra de busqueda.
        TextField(
            value = state.search,
            onValueChange = { viewModel.onSearch(it) },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            placeholder = { Text("Buscar manga...")},
            //Icono de filtro de favoritos.
            trailingIcon = {
                IconButton(onClick = { viewModel.alterFavoriteFilter()}) {
                    Icon(
                        imageVector = if (state.isFavoriteFilterActive) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Filtro de favoritos",
                        tint = if (state.isFavoriteFilterActive) Color.Red else Color.Gray
                    )
                }
            },
            //Icono decorativo de busqueda.
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        //Mangas.
        if (state.filteredMangas.isEmpty()){

            Text(
                "No hay mangas cargados...",
                modifier = Modifier.padding(16.dp)
            )

        }else {

            LazyColumn (modifier = Modifier.fillMaxSize()){

                items(state.filteredMangas.toList()) { (genero , lista) ->

                    Text(
                        text = genero.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyRow (contentPadding = PaddingValues(horizontal = 8.dp)){

                        items(lista) { manga -> MangaCard(manga,
                            onFavoriteClick = { viewModel.statusMangaFavorite(manga.id)},
                            onClick = { onMangaClick(manga.id)} )}

                    }

                }

            }

        }

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    FunAppMangaTheme {
        //HomeScreen()
    }
}