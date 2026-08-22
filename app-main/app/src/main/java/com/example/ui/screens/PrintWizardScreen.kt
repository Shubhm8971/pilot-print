package com.example.ui.screens

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PrintDocument
import com.example.model.PrintShop
import com.example.ui.theme.PrintPilotBackground
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotError
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotSurfaceLow
import com.example.ui.theme.PrintPilotSurfaceLowest
import com.example.viewmodel.PrintPilotUiState
import com.example.viewmodel.PrintWizardStep

@Composable
fun PrintWizardScreen(
    uiState: PrintPilotUiState,
    onSelectShop: (PrintShop) -> Unit,
    onAddDocument: (String, Int) -> Unit,
    onRemoveDocument: (String) -> Unit,
    onContinueToEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddCustomDocDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val filePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val fileName = context.fileName(it)
            val pageCount = context.pdfPageCount(it)
            onAddDocument(fileName, pageCount)
        }
    }

    fun launchFilePicker() {
        filePicker.launch("*/*")
    }

    val totalPages = uiState.uploadedDocuments.sumOf { it.pageCount }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PrintPilotBackground)
            .testTag("print_wizard_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Five-step Progress Indicator (Image 6)
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = "STEP 1 & 2 OF 5",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Stepper bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    StepCircle(stepNumber = 1, isCompletedOrActive = true, label = "Shop")
                    StepDivider(isActive = true)
                    StepCircle(stepNumber = 2, isCompletedOrActive = true, label = "Upload")
                    StepDivider(isActive = false)
                    StepCircle(stepNumber = 3, isCompletedOrActive = false, label = "Edit")
                    StepDivider(isActive = false)
                    StepCircle(stepNumber = 4, isCompletedOrActive = false, label = "Review")
                    StepDivider(isActive = false)
                    StepCircle(stepNumber = 5, isCompletedOrActive = false, label = "Pay")
                }
            }
        }

        // 2. Select a Print Shop Section
        item {
            Text(
                text = "Select a Print Shop",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        items(uiState.shops) { shop ->
            val isSelected = uiState.selectedShop.id == shop.id
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLowest,
                border = BorderStroke(
                    if (isSelected) 2.dp else 1.dp,
                    if (isSelected) PrintPilotPrimary else PrintPilotBorderLight
                ),
                shadowElevation = if (isSelected) 2.dp else 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onSelectShop(shop) }
                    .testTag("shop_option_${shop.id}")
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Blue Left Accent Bar for selected state
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .width(4.dp)
                                .height(96.dp)
                                .background(PrintPilotPrimary)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = shop.name,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(4.dp),
                                color = if (isSelected) PrintPilotPrimary.copy(alpha = 0.1f) else PrintPilotSurfaceLow
                            ) {
                                Text(
                                    text = "${shop.distanceMiles} mi",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) PrintPilotPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    ),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "${shop.waitingStudents} students waiting",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(PrintPilotBorderLight)
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "B&W: ${(shop.bwPricePerPage * 100).toInt()}¢/pg • Color: ${(shop.colorPricePerPage * 100).toInt()}¢/pg",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        )
                    }
                }
            }
        }

        // 3. Upload Documents Section Header
        item {
            Text(
                text = "Upload Documents",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        // 4. Dropzone Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLow,
                border = BorderStroke(1.5.dp, PrintPilotPrimary.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable {
                        launchFilePicker()
                    }
                    .testTag("upload_dropzone")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudUpload,
                        contentDescription = "Upload Documents",
                        tint = PrintPilotPrimary,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Drop your files here",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    Text(
                        text = "Supports PDF, DOCX (Max 50MB)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedButton(
                        onClick = { launchFilePicker() },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, PrintPilotBorderLight),
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = PrintPilotSurfaceLowest
                        ),
                        modifier = Modifier.testTag("btn_browse_files")
                    ) {
                        Text(
                            text = "Browse Files",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                }
            }
        }

        // 5. Uploaded Documents List
        items(uiState.uploadedDocuments) { doc ->
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PrintPilotSurfaceLowest,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("doc_item_${doc.id}")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (doc.isPdf) PrintPilotError.copy(alpha = 0.1f)
                                    else PrintPilotPrimary.copy(alpha = 0.1f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (doc.isPdf) Icons.Default.PictureAsPdf else Icons.Default.Description,
                                contentDescription = null,
                                tint = if (doc.isPdf) PrintPilotError else PrintPilotPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Column {
                            Text(
                                text = doc.name,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "${doc.pageCount} pages • ${doc.sizeLabel}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    IconButton(
                        onClick = { onRemoveDocument(doc.id) },
                        modifier = Modifier.testTag("btn_delete_doc_${doc.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }

        // 6. Total Pages & Action Footer
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLow,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
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
                            text = "Total Document Pages",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            text = "$totalPages Pages",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Button(
                        onClick = onContinueToEdit,
                        enabled = uiState.uploadedDocuments.isNotEmpty(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrintPilotPrimary),
                        modifier = Modifier
                            .height(48.dp)
                            .testTag("btn_continue_to_edit")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Continue to Edit",
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

private fun Context.fileName(uri: Uri): String {
    val fallback = "Selected document.pdf"
    return contentResolver.query(uri, arrayOf("_display_name"), null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) cursor.getString(0) else fallback
    } ?: fallback
}

private fun Context.pdfPageCount(uri: Uri): Int {
    if (fileName(uri).endsWith(".pdf", ignoreCase = true).not()) return 1
    return runCatching {
        contentResolver.openFileDescriptor(uri, "r")?.use { descriptor: ParcelFileDescriptor ->
            PdfRenderer(descriptor).use { renderer -> renderer.pageCount.coerceAtLeast(1) }
        } ?: 1
    }.getOrDefault(1)
}

@Composable
fun StepCircle(
    stepNumber: Int,
    isCompletedOrActive: Boolean,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(
                    if (isCompletedOrActive) PrintPilotPrimary else PrintPilotSurfaceLowest
                )
                .border(
                    width = 1.5.dp,
                    color = if (isCompletedOrActive) PrintPilotPrimary else PrintPilotBorderLight,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber.toString(),
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCompletedOrActive) Color.White else MaterialTheme.colorScheme.outline
                )
            )
        }
    }
}

@Composable
fun StepDivider(isActive: Boolean) {
    Box(
        modifier = Modifier
            .width(28.dp)
            .height(2.dp)
            .background(if (isActive) PrintPilotPrimary else PrintPilotBorderLight)
    )
}
