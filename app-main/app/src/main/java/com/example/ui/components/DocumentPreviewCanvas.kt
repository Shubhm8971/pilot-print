package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.RotateRight
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PrintConfig
import com.example.model.PrintDocument
import com.example.model.StudentProfile
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotSurfaceLow
import com.example.ui.theme.PrintPilotSurfaceLowest

@Composable
fun DocumentPreviewCanvas(
    document: PrintDocument?,
    profile: StudentProfile,
    config: PrintConfig,
    onDeletePage: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedThumbnailIndex by remember { mutableIntStateOf(0) }
    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var rotationAngle by remember { mutableFloatStateOf(0f) }

    val docTitle = document?.name ?: "Assignment_Final.pdf"
    val pageCount = document?.pageCount ?: 24

    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, PrintPilotBorderLight),
        color = PrintPilotSurfaceLow,
        shadowElevation = 2.dp,
        modifier = modifier
            .fillMaxWidth()
            .testTag("document_preview_canvas")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Editor Toolbar
            Surface(
                color = PrintPilotSurfaceLowest,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = docTitle,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        IconButton(
                            onClick = { zoomScale = (zoomScale - 0.15f).coerceAtLeast(0.7f) },
                            modifier = Modifier.size(32.dp).testTag("btn_zoom_out")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomOut,
                                contentDescription = "Zoom Out",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = { zoomScale = (zoomScale + 0.15f).coerceAtMost(1.35f) },
                            modifier = Modifier.size(32.dp).testTag("btn_zoom_in")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ZoomIn,
                                contentDescription = "Zoom In",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        Box(
                            modifier = Modifier
                                .height(20.dp)
                                .width(1.dp)
                                .background(PrintPilotBorderLight)
                        )

                        IconButton(
                            onClick = { rotationAngle = (rotationAngle + 90f) % 360f },
                            modifier = Modifier.size(32.dp).testTag("btn_rotate_page")
                        ) {
                            Icon(
                                imageVector = Icons.Default.RotateRight,
                                contentDescription = "Rotate",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        IconButton(
                            onClick = onDeletePage,
                            modifier = Modifier.size(32.dp).testTag("btn_delete_page")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete Page",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            // Preview Workspace: Left Sidebar + Center Paper Sheet
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) {
                // Left Thumbnails Sidebar
                Column(
                    modifier = Modifier
                        .width(72.dp)
                        .fillMaxHeight()
                        .background(PrintPilotSurfaceLowest)
                        .border(BorderStroke(1.dp, PrintPilotBorderLight))
                        .padding(6.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // ID Page Thumbnail
                    ThumbnailCard(
                        label = "ID",
                        isSelected = selectedThumbnailIndex == 0,
                        onClick = { selectedThumbnailIndex = 0 },
                        isIdCover = true
                    )

                    // Page 1 Thumbnail
                    ThumbnailCard(
                        label = "1",
                        isSelected = selectedThumbnailIndex == 1,
                        onClick = { selectedThumbnailIndex = 1 },
                        isIdCover = false
                    )

                    // Page 2 Thumbnail
                    ThumbnailCard(
                        label = "2",
                        isSelected = selectedThumbnailIndex == 2,
                        onClick = { selectedThumbnailIndex = 2 },
                        isIdCover = false
                    )
                }

                // Center Paper Canvas
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedThumbnailIndex == 0) {
                        // High-Fidelity Auto-Generated Cover Page
                        AutoGeneratedIdCoverPaper(
                            profile = profile,
                            docTitle = docTitle,
                            pageCount = pageCount,
                            config = config,
                            scale = zoomScale,
                            rotation = rotationAngle
                        )
                    } else {
                        // Regular Document Page Simulation
                        StandardDocumentPagePaper(
                            pageNumber = selectedThumbnailIndex,
                            docTitle = docTitle,
                            scale = zoomScale,
                            rotation = rotationAngle
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThumbnailCard(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isIdCover: Boolean
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f / 1.35f)
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) PrintPilotPrimary else PrintPilotBorderLight,
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { onClick() }
            .padding(4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isIdCover) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .background(PrintPilotPrimary.copy(alpha = 0.4f))
                )
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.fillMaxWidth(0.8f).height(2.dp).background(Color(0xFFCBD5E1)))
                    Box(modifier = Modifier.fillMaxWidth(0.5f).height(2.dp).background(Color(0xFFCBD5E1)))
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFE2E8F0)))
                    Box(modifier = Modifier.fillMaxWidth().height(2.dp).background(Color(0xFFE2E8F0)))
                    Box(modifier = Modifier.fillMaxWidth(0.7f).height(2.dp).background(Color(0xFFE2E8F0)))
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .background(Color(0xFFF1F5F9), RoundedCornerShape(2.dp))
                    .padding(horizontal = 3.dp, vertical = 1.dp)
            ) {
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) PrintPilotPrimary else Color(0xFF64748B)
                )
            }
        }
    }
}

@Composable
private fun AutoGeneratedIdCoverPaper(
    profile: StudentProfile,
    docTitle: String,
    pageCount: Int,
    config: PrintConfig,
    scale: Float,
    rotation: Float
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f / 1.38f)
            .scale(scale)
            .rotate(rotation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Section: Brand Title
            Column {
                Text(
                    text = "Print Pilot",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Black,
                        color = PrintPilotPrimary,
                        letterSpacing = (-0.5).sp
                    )
                )
                Text(
                    text = "Identification",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = PrintPilotPrimary
                    )
                )
                Text(
                    text = "AUTO-GENERATED COVER",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
                Box(
                    modifier = Modifier
                        .padding(top = 6.dp)
                        .fillMaxWidth()
                        .height(2.dp)
                        .background(PrintPilotPrimary)
                )
            }

            // Middle Section: Student Identity
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Column {
                    Text(
                        text = "Student Name",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                Column {
                    Text(
                        text = "Registration Number",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = PrintPilotSurfaceLow,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = profile.registrationNumber,
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }
            }

            // Bottom Section: Job Details + QR Code
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(BorderStroke(0.5.dp, Color(0xFFCBD5E1)), RoundedCornerShape(4.dp))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Job Details",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    Text(
                        text = "File: $docTitle",
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                    Text(
                        text = "Pages: $pageCount (${config.colorOutput.label}, ${config.sides.label})",
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Time: 14:32 PM, Oct 24",
                        fontSize = 8.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Decorative QR Code
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .border(1.dp, PrintPilotBorderLight, RoundedCornerShape(4.dp))
                        .background(PrintPilotSurfaceLow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode2,
                        contentDescription = "QR Code",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StandardDocumentPagePaper(
    pageNumber: Int,
    docTitle: String,
    scale: Float,
    rotation: Float
) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = Color.White,
        shadowElevation = 6.dp,
        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f / 1.38f)
            .scale(scale)
            .rotate(rotation)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "$docTitle — Page $pageNumber",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF334155),
                        fontSize = 9.sp
                    )
                )
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color(0xFFE2E8F0)))
                Box(modifier = Modifier.fillMaxWidth().height(5.dp).background(Color(0xFFF1F5F9)))
                Box(modifier = Modifier.fillMaxWidth(0.9f).height(5.dp).background(Color(0xFFF1F5F9)))
                Box(modifier = Modifier.fillMaxWidth(0.95f).height(5.dp).background(Color(0xFFF1F5F9)))
                Box(modifier = Modifier.fillMaxWidth(0.75f).height(5.dp).background(Color(0xFFF1F5F9)))
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp)
                        .background(Color(0xFFE2E8F0), RoundedCornerShape(2.dp))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(modifier = Modifier.fillMaxWidth().height(5.dp).background(Color(0xFFF1F5F9)))
                Box(modifier = Modifier.fillMaxWidth(0.85f).height(5.dp).background(Color(0xFFF1F5F9)))
            }

            Box(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(2.dp)
            ) {
                Text(
                    text = "Page $pageNumber",
                    fontSize = 8.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }
    }
}
