package app.kcs.komata.ui

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.kcs.komata.core.model.MangaDetails
import app.kcs.komata.core.model.MangaSummary
import app.kcs.komata.core.model.ReaderPage
import app.kcs.komata.providers.demo.DemoProvider
import app.kcs.komata.ui.screens.DetailsScreen
import app.kcs.komata.ui.screens.HomeScreen
import app.kcs.komata.ui.screens.ReaderScreen

private sealed interface Screen {
    data object Home : Screen
    data class Details(val mangaId: String) : Screen
    data class Reader(val mangaId: String, val chapterId: String) : Screen
}

@Composable
fun KomataApp() {
    val provider = DemoProvider
    var screen by remember { mutableStateOf<Screen>(Screen.Home) }

    when (val current = screen) {
        Screen.Home -> {
            val manga by produceState<List<MangaSummary>>(initialValue = emptyList(), provider) {
                value = provider.getHome()
            }
            HomeScreen(
                providerName = provider.name,
                manga = manga,
                onOpenManga = { mangaId -> screen = Screen.Details(mangaId) },
            )
        }

        is Screen.Details -> {
            val details by produceState<MangaDetails?>(initialValue = null, current.mangaId) {
                value = provider.getDetails(current.mangaId)
            }

            if (details == null) {
                LoadingScreen()
            } else {
                DetailsScreen(
                    details = details!!,
                    onBack = { screen = Screen.Home },
                    onOpenChapter = { chapterId ->
                        screen = Screen.Reader(current.mangaId, chapterId)
                    },
                )
            }
        }

        is Screen.Reader -> {
            val details by produceState<MangaDetails?>(initialValue = null, current.mangaId) {
                value = provider.getDetails(current.mangaId)
            }
            val pages by produceState<List<ReaderPage>>(initialValue = emptyList(), current.chapterId) {
                value = provider.getPages(current.chapterId)
            }
            val chapterTitle = details
                ?.chapters
                ?.firstOrNull { it.id == current.chapterId }
                ?.title
                ?: "Reader"

            ReaderScreen(
                chapterTitle = chapterTitle,
                pages = pages,
                onBack = { screen = Screen.Details(current.mangaId) },
            )
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
