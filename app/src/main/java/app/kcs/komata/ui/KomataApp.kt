package app.kcs.komata.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import app.kcs.komata.core.extension.ExtensionRegistry
import app.kcs.komata.core.extension.ExtensionRepositoryClient
import app.kcs.komata.core.extension.RepositorySnapshot
import app.kcs.komata.core.model.MangaDetails
import app.kcs.komata.core.model.MangaSummary
import app.kcs.komata.core.model.ReaderPage
import app.kcs.komata.providers.demo.DemoProvider
import app.kcs.komata.ui.screens.DetailsScreen
import app.kcs.komata.ui.screens.ExtensionsScreen
import app.kcs.komata.ui.screens.HomeScreen
import app.kcs.komata.ui.screens.ReaderScreen

private sealed interface Screen {
    data object Home : Screen
    data object Extensions : Screen
    data class Details(val mangaId: String) : Screen
    data class Reader(val mangaId: String, val chapterId: String) : Screen
}

@Composable
fun KomataApp() {
    val provider = DemoProvider
    var screen by remember { mutableStateOf<Screen>(Screen.Home) }
    var repositoryUrl by remember { mutableStateOf("") }
    var repositoryRequest by remember { mutableStateOf<String?>(null) }
    var enabledIds by remember { mutableStateOf(setOf(DemoProvider.id)) }

    val repositoryResult by produceState<Result<RepositorySnapshot>?>(initialValue = null, repositoryRequest) {
        val requestedUrl = repositoryRequest
        value = if (requestedUrl.isNullOrBlank()) null else runCatching { ExtensionRepositoryClient.load(requestedUrl) }
    }

    val repository = repositoryResult?.getOrNull()
    val repositoryError = repositoryResult?.exceptionOrNull()?.message?.let { "Repository error: $it" }

    BackHandler(enabled = screen != Screen.Home) {
        screen = when (val current = screen) {
            Screen.Home -> Screen.Home
            Screen.Extensions -> Screen.Home
            is Screen.Details -> Screen.Home
            is Screen.Reader -> Screen.Details(current.mangaId)
        }
    }

    when (val current = screen) {
        Screen.Home -> {
            val manga by produceState<List<MangaSummary>>(initialValue = emptyList(), provider, enabledIds) {
                value = if (provider.id in enabledIds) provider.getHome() else emptyList()
            }
            HomeScreen(
                providerName = if (provider.id in enabledIds) provider.name else "No extension enabled",
                manga = manga,
                onOpenManga = { mangaId -> screen = Screen.Details(mangaId) },
                onOpenExtensions = { screen = Screen.Extensions },
            )
        }

        Screen.Extensions -> {
            ExtensionsScreen(
                repositoryUrl = repositoryUrl,
                onRepositoryUrlChange = {
                    repositoryUrl = it
                    repositoryRequest = null
                },
                repository = repository,
                repositoryError = repositoryError,
                isLoading = repositoryRequest != null && repositoryResult == null,
                builtIns = ExtensionRegistry.builtInExtensions,
                enabledIds = enabledIds,
                onToggleEnabled = { id, enabled ->
                    enabledIds = if (enabled) enabledIds + id else enabledIds - id
                },
                onLoadRepository = { repositoryRequest = repositoryUrl.trim() },
                onBack = { screen = Screen.Home },
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
                    onOpenChapter = { chapterId -> screen = Screen.Reader(current.mangaId, chapterId) },
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
            val chapterTitle = details?.chapters?.firstOrNull { it.id == current.chapterId }?.title ?: "Reader"
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
