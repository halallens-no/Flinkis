package com.halallens.flinkis.ui.screens.templates

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.Spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.halallens.flinkis.R
import com.halallens.flinkis.ui.util.localizedName
import com.halallens.flinkis.domain.model.RoutineTemplate
import com.halallens.flinkis.domain.model.TemplateCategory
import com.halallens.flinkis.domain.model.TimeSlot
import com.halallens.flinkis.domain.usecase.ApplyTemplateUseCase
import com.halallens.flinkis.ui.animation.pressScale
import com.halallens.flinkis.ui.animation.staggeredSlideIn
import com.halallens.flinkis.ui.components.GlassCard
import com.halallens.flinkis.ui.components.GlassPillButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplatePickerScreen(
    onTemplateApplied: () -> Unit,
    onCreateCustom: () -> Unit,
    viewModel: TemplatePickerViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()
    var visible by remember { mutableStateOf(true) }

    val filteredTemplates = state.allTemplates.filter {
        it.category == state.selectedCategory
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surface,
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                        MaterialTheme.colorScheme.surface
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Polished header with accent bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .size(4.dp, 32.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            ),
                            shape = RoundedCornerShape(2.dp)
                        )
                )
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = stringResource(R.string.template_choose_routine),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = stringResource(R.string.template_pick_subtitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Category tabs
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TemplateCategory.entries.forEach { category ->
                    CategoryTab(
                        category = category,
                        isSelected = state.selectedCategory == category,
                        onClick = { viewModel.selectCategory(category) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Template grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(
                    items = filteredTemplates,
                    key = { _, template -> template.id }
                ) { index, template ->
                    TemplateCard(
                        template = template,
                        isSelected = state.selectedTemplate?.id == template.id,
                        index = index,
                        visible = visible,
                        onClick = { viewModel.selectTemplate(template) }
                    )
                }

                if (state.selectedCategory == TemplateCategory.CUSTOM) {
                    item {
                        CreateOwnCard(
                            index = filteredTemplates.size,
                            visible = visible,
                            onClick = onCreateCustom
                        )
                    }
                }
            }
        }

        // Confirmation bottom sheet
        if (state.showConfirmation && state.diff != null && state.selectedTemplate != null) {
            ConfirmationSheet(
                templateName = state.selectedTemplate!!.name,
                diff = state.diff!!,
                editableRoutines = state.editableRoutines,
                isApplying = state.isApplying,
                onDismiss = { viewModel.dismissConfirmation() },
                onToggleRoutine = { index -> viewModel.toggleRoutine(index) },
                onAddRoutine = { name, slot, points ->
                    viewModel.addRoutine(name, slot, points)
                },
                onConfirm = { viewModel.confirmApply(onTemplateApplied) }
            )
        }
    }
}

@Composable
private fun CategoryTab(
    category: TemplateCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        },
        label = "tab_bg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            MaterialTheme.colorScheme.onPrimary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        },
        label = "tab_content"
    )

    Box(
        modifier = Modifier
            .pressScale(interactionSource)
            .background(color = backgroundColor, shape = RoundedCornerShape(20.dp))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        Text(
            text = category.localizedName(),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
            color = contentColor
        )
    }
}

/**
 * Template card with emoji header, gradient tint, and routine count pill.
 * Each template gets a unique emoji and accent color for visual identity.
 */
@Composable
private fun TemplateCard(
    template: RoutineTemplate,
    isSelected: Boolean,
    index: Int,
    visible: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.03f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "card_scale"
    )

    val visual = templateVisual(template)

    GlassCard(
        modifier = Modifier
            .pressScale(interactionSource)
            .staggeredSlideIn(index = index, visible = visible)
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        isSelected = isSelected,
        cornerRadius = 20.dp,
        fillAlpha = 0.10f
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Gradient emoji header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                visual.color.copy(alpha = 0.15f),
                                visual.color.copy(alpha = 0.03f)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(text = visual.emoji, fontSize = 36.sp)

                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Filled.CheckCircle,
                            contentDescription = stringResource(R.string.a11y_selected),
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Card content
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = template.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                // Routine count pill
                Box(
                    modifier = Modifier
                        .background(
                            color = visual.color.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = stringResource(R.string.template_routines_count, template.routines.size),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium,
                        color = visual.color
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateOwnCard(
    index: Int,
    visible: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    GlassCard(
        modifier = Modifier
            .pressScale(interactionSource)
            .staggeredSlideIn(index = index, visible = visible)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        fillAlpha = 0.05f,
        cornerRadius = 20.dp
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.06f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(R.string.a11y_create_own),
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp)
            ) {
                Text(
                    text = stringResource(R.string.template_create_own),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.template_build_scratch),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConfirmationSheet(
    templateName: String,
    diff: ApplyTemplateUseCase.TemplateDiff,
    editableRoutines: List<EditableRoutine>,
    isApplying: Boolean,
    onDismiss: () -> Unit,
    onToggleRoutine: (Int) -> Unit,
    onAddRoutine: (String, TimeSlot, Int) -> Unit,
    onConfirm: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showAddForm by remember { mutableStateOf(false) }
    var newRoutineName by remember { mutableStateOf("") }
    var selectedSlot by remember { mutableStateOf(TimeSlot.MORNING) }
    var selectedPoints by remember { mutableStateOf(1) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
        ) {
            Text(
                text = stringResource(R.string.template_switching_to, templateName),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (diff.removing.isNotEmpty()) {
                GlassCard(fillAlpha = 0.06f) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = stringResource(R.string.template_replacing_count, diff.removing.size),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Header row with "Customize" + "Add" button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.template_customize),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                        .clickable { showAddForm = !showAddForm }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = stringResource(R.string.a11y_add_routine),
                            modifier = Modifier.size(14.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stringResource(R.string.add),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Inline add-routine form
            if (showAddForm) {
                Spacer(modifier = Modifier.height(8.dp))
                GlassCard(fillAlpha = 0.06f, cornerRadius = 16.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Routine name input
                        OutlinedTextField(
                            value = newRoutineName,
                            onValueChange = { newRoutineName = it },
                            label = { Text(stringResource(R.string.edit_routine_name)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        // Time slot picker row
                        Text(
                            text = stringResource(R.string.edit_routine_time_slot),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            TimeSlot.entries.forEach { slot ->
                                val isSel = selectedSlot == slot
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(
                                            if (isSel) MaterialTheme.colorScheme.primary
                                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                        )
                                        .clickable { selectedSlot = slot }
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = slot.localizedName().take(3),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 10.sp,
                                        color = if (isSel) MaterialTheme.colorScheme.onPrimary
                                               else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                        // Points picker + Add button
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.points_label),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                listOf(1, 2, 3).forEach { pts ->
                                    Box(
                                        modifier = Modifier
                                            .size(28.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (selectedPoints == pts) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                            )
                                            .clickable { selectedPoints = pts },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "$pts",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selectedPoints == pts) MaterialTheme.colorScheme.onPrimary
                                                   else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                            GlassPillButton(
                                onClick = {
                                    if (newRoutineName.isNotBlank()) {
                                        onAddRoutine(newRoutineName.trim(), selectedSlot, selectedPoints)
                                        newRoutineName = ""
                                        showAddForm = false
                                    }
                                },
                                enabled = newRoutineName.isNotBlank()
                            ) {
                                Text(stringResource(R.string.add), style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val grouped = editableRoutines.withIndex().toList()
                .groupBy { it.value.timeSlot }

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 280.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                grouped.forEach { (slot, routinesWithIndex) ->
                    item {
                        Text(
                            text = slot.localizedName(),
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                        )
                    }
                    items(routinesWithIndex) { (index, routine) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onToggleRoutine(index) }
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = routine.isEnabled,
                                onCheckedChange = { onToggleRoutine(index) }
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = routine.name,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f),
                                color = if (routine.isEnabled) {
                                    MaterialTheme.colorScheme.onSurface
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                }
                            )
                            Text(
                                text = "${routine.points}\u2B50",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val enabledCount = editableRoutines.count { it.isEnabled }
            Text(
                text = stringResource(R.string.template_routines_selected, enabledCount, editableRoutines.size),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlassPillButton(
                    onClick = onDismiss,
                    modifier = Modifier.weight(1f),
                    outlined = true,
                    enabled = !isApplying
                ) {
                    Text(stringResource(R.string.cancel))
                }

                GlassPillButton(
                    onClick = onConfirm,
                    modifier = Modifier.weight(1f),
                    enabled = !isApplying && enabledCount > 0
                ) {
                    if (isApplying) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.template_applying))
                        }
                    } else {
                        Text(stringResource(R.string.template_apply_count, enabledCount))
                    }
                }
            }
        }
    }
}

/**
 * Visual identity for each template — unique emoji + accent color.
 */
private data class TemplateVisual(val emoji: String, val color: Color)

@Composable
private fun templateVisual(template: RoutineTemplate): TemplateVisual {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val tertiary = MaterialTheme.colorScheme.tertiary

    return when {
        template.name.contains("Ramadan", ignoreCase = true) ->
            TemplateVisual("\uD83C\uDF19", primary)
        template.name.contains("Daily Islamic", ignoreCase = true) ->
            TemplateVisual("\uD83D\uDD4C", secondary)
        template.name.contains("Weekend Islamic", ignoreCase = true) ->
            TemplateVisual("\u2B50", tertiary)
        template.name.contains("School", ignoreCase = true) ->
            TemplateVisual("\uD83D\uDCDA", primary)
        template.name.contains("Summer", ignoreCase = true) ->
            TemplateVisual("\u2600\uFE0F", secondary)
        template.name.contains("Weekend", ignoreCase = true) ->
            TemplateVisual("\uD83C\uDF89", tertiary)
        template.name.contains("Sport", ignoreCase = true) ||
            template.name.contains("Active", ignoreCase = true) ->
            TemplateVisual("\u26BD", primary)
        template.name.contains("Toddler", ignoreCase = true) ||
            template.name.contains("Little", ignoreCase = true) ||
            template.name.contains("Explorer", ignoreCase = true) ->
            TemplateVisual("\uD83E\uDDF8", secondary)
        template.name.contains("Homework", ignoreCase = true) ||
            template.name.contains("Hero", ignoreCase = true) ->
            TemplateVisual("\uD83D\uDCDD", primary)
        template.name.contains("Chore", ignoreCase = true) ->
            TemplateVisual("\uD83E\uDDF9", tertiary)
        template.name.contains("Health", ignoreCase = true) ->
            TemplateVisual("\uD83D\uDC9A", secondary)
        template.name.contains("Creative", ignoreCase = true) ||
            template.name.contains("Art", ignoreCase = true) ->
            TemplateVisual("\uD83C\uDFA8", tertiary)
        template.category == TemplateCategory.ISLAMIC ->
            TemplateVisual("\uD83D\uDD4C", primary)
        template.category == TemplateCategory.GENERAL ->
            TemplateVisual("\u2728", secondary)
        else -> TemplateVisual("\uD83D\uDCCB", primary)
    }
}
