package app.kcs.komata.core.extension

data class ExtensionManifest(
    val id: String,
    val name: String,
    val version: String,
    val language: String,
    val providerClass: String? = null,
    val downloadUrl: String? = null,
    val sourceUrl: String? = null,
)

data class RepositorySnapshot(
    val url: String,
    val name: String,
    val extensions: List<ExtensionManifest>,
)
