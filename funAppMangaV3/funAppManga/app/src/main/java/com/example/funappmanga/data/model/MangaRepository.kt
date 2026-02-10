package com.example.funappmanga.data.model

object MangaRepository {

    //mockManga es base inicial.
    private var currentMangas = mockMangas.toMutableList()

    //Funcion para obtener todos los mangas
    fun getMangas(): List<Manga> = currentMangas

    //Funcion para cargar manga por id.
    fun getMangaById(id: Int): Manga? {return currentMangas.find { it.id == id }}

    //Funcion para actualizar favoritos.
    fun updateFavorite(mangaId: Int) {

        val index = currentMangas.indexOfFirst { it.id == mangaId }
        if (index != -1){

            val manga = currentMangas[index]
            currentMangas[index] = manga.copy(isFavorite = !manga.isFavorite)

        }
    }
}