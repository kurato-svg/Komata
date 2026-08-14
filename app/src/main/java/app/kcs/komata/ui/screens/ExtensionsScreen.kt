package app.kcs.komata.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.kcs.komata.core.extension.ExtensionManifest
import app.kcs.komata.core.extension.RepositorySnapshot

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExtensionsScreen(
    repositoryUrl: String,
    onRepositoryUrlChange: (String) -> Unit,
    repository: RepositorySnapshot?,
    repositoryError: String?,
    isLoading: Boolean,
    builtIns: List<ExtensionManifest>,
    enabledIds: Set<String>,
    onToggleEnabled: (String, Boolean) -> Unit,
    onLoadRepository: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Extensions") },
                navigationIcon = { TextButton(onClick = onBack) { Text("Back") } },
            )
        },
    ) { padding ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(padding)) {
            item {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Repository", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = repositoryUrl,
                        onValueChange = onRepositoryUrlChange,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("repo.json URL") },
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = onLoadRepository, enabled = repositoryUrl.isNotBlank() && !isLoading) {
                        Text(if (isLoading) "Loading..." else "Load repository")
                    }
                    if (repositoryError != null) {
                        Spacer(Modifier.height(8.dp))
                        Text(repositoryError, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                    }
                    repository?.let {
                        Spacer(Modifier.height(8.dp))
                        Text("${it.name}: ${it.extensions.size} extension(s)")
                    }
                }
            }

            item {
                Text("Installed", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            items(builtIns, key = { it.id }) { extension ->
                ExtensionCard(
                    extension = extension,
                    enabled = extension.id in enabledIds,
                    onEnabledChange = { enabled -> onToggleEnabled(extension.id, enabled) },
                    status = "Built-in",
                )
            }

            if (repository != null && repository.extensions.isNotEmpty()) {
                item {
                    Text("Available from repository", modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                }
                items(repository.extensions, key = { it.id }) { extension ->
                    ExtensionCard(
                        extension = extension,
                        enabled = false,
                        onEnabledChange = {},
                        status = "Available, install engine comes in M2B",
                        toggleEnabled = false,
                    )
                }
            }

            item { Spacer(Modifier.height(24.dp)) }
        }
    }
}

@Composable
private fun ExtensionCard(
    extension: ExtensionManifest,
    enabled: Boolean,
    onEnabledChange: (Boolean) -> Unit,
    status: String,
    toggleEnabled: Boolean = true,
) {
    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(extension.name, fontWeight = FontWeight.SemiBold)
                Text("${extension.language} • v${extension.version}", style = MaterialTheme.typography.bodySmall)
                Text(status, style = MaterialTheme.typography.labelSmall)
            }
            if (toggleEnabled) {
                Switch(checked = enabled, onCheckedChange = onEnabledChange)
            }
        }
    }
}
