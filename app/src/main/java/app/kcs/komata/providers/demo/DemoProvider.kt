package app.kcs.komata.providers.demo

import app.kcs.komata.R
import app.kcs.komata.core.model.Chapter
import app.kcs.komata.core.model.ImageRef
import app.kcs.komata.core.model.MangaDetails
import app.kcs.komata.core.model.MangaSummary
import app.kcs.komata.core.model.MangaType
import app.kcs.komata.core.model.ReaderPage
import app.kcs.komata.core.model.ReadingMode
import app.kcs.komata.core.provider.MangaProvider

object DemoProvider : MangaProvider {
    override val id = "demo"
    override val name = "Komata Demo"
    override val language = "en"

    private val manga = MangaSummary(
        id = "demo-solo-reader",
        title = "The Last Reader",
        poster = ImageRef.Resource(R.drawable.demo_poster),
        type = MangaType.MANHWA,
    )

    private val chapters = listOf(
        Chapter(id = "demo-chapter-2", title = "Chapter 2: The Gate", number = 2f),
        Chapter(id = "demo-chapter-1", title = "Chapter 1: Awakening", number = 1f),
    )

    override suspend fun getHome(): List<MangaSummary> = listOf(manga)

    override suspend fun getDetails(mangaId: String): MangaDetails? {
        if (mangaId != manga.id) return null

        return MangaDetails(
            id = manga.id,
            title = manga.title,
            poster = manga.poster,
            description = "A built-in Komata test title used to validate the catalogue, chapter flow and vertical reader before real website extensions are added.",
            type = manga.type,
            status = "Demo",
            chapters = chapters,
            defaultReadingMode = ReadingMode.WEBTOON,
        )
    }

    override suspend fun getPages(chapterId: String): List<ReaderPage> {
        val pages = when (chapterId) {
            "demo-chapter-2" -> listOf(
                R.drawable.demo_page_4,
                R.drawable.demo_page_5,
                R.drawable.demo_page_6,
            )
            else -> listOf(
                R.drawable.demo_page_1,
                R.drawable.demo_page_2,
                R.drawable.demo_page_3,
            )
        }

        return pages.mapIndexed { index, resId ->
            ReaderPage(
                index = index,
                image = ImageRef.Resource(resId),
                aspectRatio = 2f / 3f,
            )
        }
    }
}
