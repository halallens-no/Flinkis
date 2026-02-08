package com.halallens.flinkis.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import com.halallens.flinkis.ui.util.localizedName
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.halallens.flinkis.R
import com.halallens.flinkis.domain.model.ThemeType
import com.halallens.flinkis.ui.components.AvatarPicker

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageChildrenScreen(
    viewModel: ManageChildrenViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit = {},
    onNavigateToTemplatePicker: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.manage_children_title)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.a11y_back)
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.a11y_add_child))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(uiState.children, key = { it.id }) { child ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.switchToChild(child.id) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (child.isActive) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        val avatars = listOf(
                            "\uD83E\uDD81", "\uD83E\uDD84", "\uD83D\uDC3B",
                            "\uD83D\uDC31", "\uD83D\uDC36", "\uD83E\uDD8B",
                            "\uD83D\uDC22", "\uD83E\uDD89", "\uD83D\uDC2C",
                            "\uD83D\uDC3C", "\uD83E\uDD85", "\uD83D\uDC30"
                        )
                        Text(
                            text = avatars.getOrElse(child.avatarId) { "\uD83E\uDD81" },
                            style = MaterialTheme.typography.displaySmall
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = child.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                            Text(
                                text = stringResource(R.string.settings_theme_suffix, child.themeType.localizedName()),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        if (child.isActive) {
                            Icon(
                                imageVector = Icons.Filled.CheckCircle,
                                contentDescription = stringResource(R.string.a11y_active),
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddChildDialog(
            onDismiss = { showAddDialog = false },
            onAdd = { name, avatarId, themeType ->
                viewModel.addChild(name, avatarId, themeType) {
                    onNavigateToTemplatePicker()
                }
                showAddDialog = false
            }
        )
    }
}

@Composable
private fun AddChildDialog(
    onDismiss: () -> Unit,
    onAdd: (String, Int, ThemeType) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var avatarId by remember { mutableIntStateOf(0) }
    var selectedTheme by remember { mutableStateOf(ThemeType.NEUTRAL) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.manage_children_add)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.manage_children_child_name)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(stringResource(R.string.manage_children_pick_avatar), style = MaterialTheme.typography.bodyMedium)
                AvatarPicker(
                    selectedAvatar = avatarId,
                    onAvatarSelected = { avatarId = it }
                )

                Text(stringResource(R.string.manage_children_theme), style = MaterialTheme.typography.bodyMedium)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ThemeType.entries.forEach { theme ->
                        TextButton(onClick = { selectedTheme = theme }) {
                            Text(
                                text = theme.localizedName(),
                                color = if (selectedTheme == theme) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onAdd(name.trim(), avatarId, selectedTheme) },
                enabled = name.isNotBlank()
            ) {
                Text(stringResource(R.string.add))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(R.string.cancel)) }
        }
    )
}
