package com.example.funappmanga.data.model

import com.example.funappmanga.R

enum class MangaCategory{
    SHONEN, SEINEN, SHOJO
}

data class Manga(
    val id: Int,
    val name: String,
    val desc: String,
    val category: MangaCategory,
    val imgURL : String,
    val videoURL: Int,
    val isFavorite: Boolean = false
)

val mockMangas = listOf(
    Manga(
        id = 1,
        name = "One Piece",
        desc = "Monkey D. Luffy se niega a permitir que nadie se interponga en su camino para convertirse en el Rey de los Piratas. Con un vasto océano por delante, reunirá a la tripulación más audaz en una búsqueda legendaria por el tesoro definitivo: el One Piece.",
        category = MangaCategory.SHONEN,
        imgURL = "https://wallpapers.com/images/featured/one-piece-c0pujiakubq5rwas.jpg",
        videoURL = R.raw.onepiecevideo
    ),
    Manga(
        id = 2,
        name = "Berserk",
        desc = "Guts, un guerrero solitario marcado por un destino cruel, blande su enorme espada en un mundo de fantasía oscura lleno de demonios y traiciones. Una lucha visceral por la supervivencia y la venganza contra aquellos que lo sacrificaron todo por el poder.",
        category = MangaCategory.SEINEN,
        imgURL = "https://static0.cbrimages.com/wordpress/wp-content/uploads/2024/02/berserk-dark.jpg?w=1200&h=675&fit=crop",
        videoURL = R.raw.berserk
    ),
    Manga(
        id = 3,
        name = "Nana",
        desc = "Dos chicas con el mismo nombre y vidas opuestas se encuentran en un tren hacia Tokio. Nana Osaki, una punk que busca el éxito con su banda, y Nana Komatsu, que solo busca amor. Sus destinos se entrelazan en esta emotiva historia sobre la amistad y el paso a la adultez.",
        category = MangaCategory.SHOJO,
        imgURL = "https://proassetspdlcom.cdnstatics2.com/usuaris/libros/thumbs/5e2129be-7fcf-4118-8043-72f76ce5234e/d_1200_1200/portada_nana-n-0107_ai-yazawa_202501091631.webp",
        videoURL = R.raw.nana
    ),
    Manga(
        id = 4,
        name = "Naruto",
        desc = "Naruto Uzumaki es un ninja marginado que lleva dentro el espíritu del Zorro de Nueve Colas. Con una voluntad inquebrantable, luchará contra el prejuicio de su aldea para cumplir su sueño de convertirse en Hokage, el líder y guerrero más fuerte de todos.",
        category = MangaCategory.SHONEN,
        imgURL = "https://m.media-amazon.com/images/M/MV5BZTNjOWI0ZTAtOGY1OS00ZGU0LWEyOWYtMjhkYjdlYmVjMDk2XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
        videoURL = R.raw.naruto
    ),
    Manga(
        id = 5,
        name = "Jujutsu Kaisen",
        desc = "En un mundo donde los sentimientos negativos de los humanos se convierten en Maldiciones asesinas, Yuji Itadori se traga un dedo podrido para salvar a sus amigos, convirtiéndose en el recipiente del Rey de las Maldiciones. Ahora, debe aprender a controlar este poder para proteger al mundo.",
        category = MangaCategory.SHONEN,
        imgURL = "https://static.bandainamcoent.eu/high/jujutsu-kaisen/jujutsu-kaisen-cursed-clash/00-page-setup/JJK-header-mobile2.jpg",
        videoURL = R.raw.jujutsukaisen
    )
)