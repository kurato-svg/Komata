package app.kcs.komata.core.provider

import app.kcs.komata.core.model.MangaDetails
import app.kcs.komata.core.model.MangaSummary
import app.kcs.komata.core.model.ReaderPage

interface MangaProvider {
    val id: String
    val name: String
    val language: String

    suspend fun getHome(): List<MangaSummary>

    suspend fun getDetails(mangaId: String): MangaDetails?

    suspend fun getPages(chapterId: String): List<ReaderPage>
}
