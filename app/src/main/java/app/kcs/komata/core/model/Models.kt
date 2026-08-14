package app.kcs.komata.core.model

sealed interface ImageRef {
    data class Resource(val resId: Int) : ImageRef
    data class Network(
        val url: String,
        val headers: Map<String, String> = emptyMap(),
    ) : ImageRef
}

enum class MangaType {
    MANHWA,
    MANGA,
    MANHUA,
    COMIC,
}

enum class ReadingMode {
    WEBTOON,
    LEFT_TO_RIGHT,
    RIGHT_TO_LEFT,
}

data class MangaSummary(
    val id: String,
    val title: String,
    val poster: ImageRef,
    val type: MangaType,
)

data class Chapter(
    val id: String,
    val title: String,
    val number: Float,
)

data class MangaDetails(
    val id: String,
    val title: String,
    val poster: ImageRef,
    val description: String,
    val type: MangaType,
    val status: String,
    val chapters: List<Chapter>,
    val defaultReadingMode: ReadingMode = ReadingMode.WEBTOON,
)

data class ReaderPage(
    val index: Int,
    val image: ImageRef,
    val aspectRatio: Float,
)
