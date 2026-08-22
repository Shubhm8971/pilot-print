package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.BindingType
import com.example.model.ColorOutput
import com.example.model.SidesOption
import com.example.ui.components.DocumentPreviewCanvas
import com.example.ui.theme.PrintPilotBackground
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotSurfaceLow
import com.example.ui.theme.PrintPilotSurfaceLowest
import com.example.viewmodel.PrintPilotUiState

@Composable
fun ConfigureEditScreen(
    uiState: PrintPilotUiState,
    onColorChanged: (ColorOutput) -> Unit,
    onSidesChanged: (SidesOption) -> Unit,
    onBindingChanged: (BindingType) -> Unit,
    onDeletePage: () -> Unit,
    onProceedToReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentDoc = uiState.uploadedDocuments.firstOrNull()
    val totalPages = uiState.uploadedDocuments.sumOf { it.pageCount }
    val pricePerPage = if (uiState.printConfig.colorOutput == ColorOutput.COLOR) 0.25 else 0.10
    val estTotal = String.format("%.2f", totalPages * pricePerPage)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PrintPilotBackground)
            .testTag("configure_edit_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Progress Indicator (Step 3)
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = "STEP 3 OF 5",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StepCircle(stepNumber = 1, isCompletedOrActive = true, label = "Shop")
                    StepDivider(isActive = true)
                    StepCircle(stepNumber = 2, isCompletedOrActive = true, label = "Upload")
                    StepDivider(isActive = true)
                    StepCircle(stepNumber = 3, isCompletedOrActive = true, label = "Edit")
                    StepDivider(isActive = false)
                    StepCircle(stepNumber = 4, isCompletedOrActive = false, label = "Review")
                    StepDivider(isActive = false)
                    StepCircle(stepNumber = 5, isCompletedOrActive = false, label = "Pay")
                }
            }
        }

        // 2. Title Section
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Tune,
                    contentDescription = null,
                    tint = PrintPilotPrimary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Configure Print Job",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }

        // 3. Print Options (Segmented Choice Cards)
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLowest,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Option 1: Color Output (B&W vs Color)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Color Output",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            ColorOutput.values().forEach { option ->
                                val isSelected = uiState.printConfig.colorOutput == option
                                SegmentButton(
                                    label = option.label,
                                    isSelected = isSelected,
                                    onClick = { onColorChanged(option) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Option 2: Sides (Single-sided vs Double-sided)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Sides",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            SidesOption.values().forEach { option ->
                                val isSelected = uiState.printConfig.sides == option
                                SegmentButton(
                                    label = option.label,
                                    isSelected = isSelected,
                                    onClick = { onSidesChanged(option) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    // Option 3: Binding (None vs Staple vs Spiral)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Binding",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            BindingType.values().forEach { option ->
                                val isSelected = uiState.printConfig.binding == option
                                SegmentButton(
                                    label = option.label,
                                    isSelected = isSelected,
                                    onClick = { onBindingChanged(option) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Live Document Editor & Auto ID Cover Canvas (Image 10)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Live ID Cover & Document Preview",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                DocumentPreviewCanvas(
                    document = currentDoc,
                    profile = uiState.profile,
                    config = uiState.printConfig,
                    onDeletePage = onDeletePage
                )
            }
        }

        // 5. Total Price & Continue Button
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLow,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Est. Total",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "₹$estTotal",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Button(
                        onClick = onProceedToReview,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrintPilotPrimary),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("btn_confirm_documents")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Confirm Documents",
                                style = MaterialTheme.typography.labelLarge.copy(
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SegmentButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) PrintPilotPrimary else PrintPilotSurfaceLowest,
        border = BorderStroke(
            1.dp,
            if (isSelected) PrintPilotPrimary else PrintPilotBorderLight
        ),
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.padding(vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}
