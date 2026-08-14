package app.kcs.komata.core.extension

import app.kcs.komata.core.provider.MangaProvider
import app.kcs.komata.providers.demo.DemoProvider

object ExtensionRegistry {
    val builtInExtensions = listOf(
        ExtensionManifest(
            id = DemoProvider.id,
            name = DemoProvider.name,
            version = "1.0.0",
            language = DemoProvider.language,
            providerClass = "app.kcs.komata.providers.demo.DemoProvider",
        ),
    )

    fun providerFor(extensionId: String): MangaProvider? = when (extensionId) {
        DemoProvider.id -> DemoProvider
        else -> null
    }
}
