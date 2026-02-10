package com.example.funappmanga.ui.Screens.Home

import androidx.lifecycle.ViewModel
import com.example.funappmanga.data.model.Manga
import com.example.funappmanga.data.model.MangaCategory
import com.example.funappmanga.data.model.mockMangas
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeUiState(

    val search: String = "",
    val isFavoriteFilterActive : Boolean = false,
    val allMangas: List<Manga> = emptyList(),
    val filteredMangas: Map<MangaCategory, List<Manga>> = emptyMap()

)

class HomeViewModel : ViewModel() {

    private val allMangas = mockMangas
    private val _uiState = MutableStateFlow( HomeUiState( allMangas = mockMangas,
        filteredMangas = mockMangas.groupBy { it.category }))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onSearch(newQuery: String){

        _uiState.update { it.copy( search = newQuery ) }
        applyFilter()

    }

    fun alterFavoriteFilter(){

        _uiState.update { it.copy(isFavoriteFilterActive = !it.isFavoriteFilterActive) }
        applyFilter()

    }


    private fun applyFilter(){

        _uiState.update { state ->
            val filteredList = state.allMangas.filter { manga ->
                val search = manga.name.contains( state.search, ignoreCase = true )
                val favorite = if (state.isFavoriteFilterActive) manga.isFavorite else true
                search && favorite

            }

            state.copy(filteredMangas = filteredList.groupBy { it.category })

        }

    }

    fun statusMangaFavorite(mangaId: Int){

        _uiState.update { state ->
            val updatedAllMangas = state.allMangas.map { manga ->
                if (manga.id == mangaId)
                    manga.copy(isFavorite = !manga.isFavorite) else manga
            }
            state.copy(allMangas = updatedAllMangas)
        }

        applyFilter()

    }

}