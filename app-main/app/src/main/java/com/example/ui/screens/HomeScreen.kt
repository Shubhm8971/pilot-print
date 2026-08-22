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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.outlined.RocketLaunch
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.PrintOrder
import com.example.model.PrintShop
import com.example.ui.theme.PrintPilotBackground
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotGold
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotPrimaryContainer
import com.example.ui.theme.PrintPilotSurfaceLow
import com.example.ui.theme.PrintPilotSurfaceLowest
import com.example.viewmodel.AppNavigationScreen
import com.example.viewmodel.PrintPilotUiState

@Composable
fun HomeScreen(
    uiState: PrintPilotUiState,
    onStartNewPrint: (PrintShop?) -> Unit,
    onViewOrderTracking: (PrintOrder) -> Unit,
    onFilterChanged: (String) -> Unit,
    onNavigateTo: (AppNavigationScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PrintPilotBackground)
            .testTag("home_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // 1. Greeting Section
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = "Good afternoon, ${uiState.profile.name.split(" ").first()} 👋",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground,
                        letterSpacing = (-0.5).sp
                    ),
                    modifier = Modifier.testTag("greeting_title")
                )
                Text(
                    text = "Let's get those documents ready.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        // 2. Bento Grid: New Print Job + Active Order
        item {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // New Print Card (Blue Hero Bento)
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = PrintPilotPrimaryContainer
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .clickable { onStartNewPrint(null) }
                        .testTag("new_print_job_card")
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        PrintPilotPrimaryContainer,
                                        PrintPilotPrimary
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        // Decorative background watermark icon
                        Icon(
                            imageVector = Icons.Default.AddCircle,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.12f),
                            modifier = Modifier
                                .size(110.dp)
                                .align(Alignment.TopEnd)
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.UploadFile,
                                    contentDescription = "Upload",
                                    tint = Color.White,
                                    modifier = Modifier.size(26.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "New Print Job",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Upload documents from your device.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = Color.White.copy(alpha = 0.85f)
                                ),
                                modifier = Modifier.padding(top = 2.dp)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Start",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Active Order Card (Matching Image 4)
                val activeOrder = uiState.activeOrders.firstOrNull()
                if (activeOrder != null) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = PrintPilotSurfaceLowest,
                        border = BorderStroke(1.dp, PrintPilotBorderLight),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable { onViewOrderTracking(activeOrder) }
                            .testTag("active_order_card")
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = PrintPilotPrimary.copy(alpha = 0.1f)
                                        ) {
                                            Text(
                                                text = activeOrder.status.label,
                                                style = MaterialTheme.typography.labelSmall.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = PrintPilotPrimary
                                                ),
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                            )
                                        }

                                        Text(
                                            text = "Order #${activeOrder.id}",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = activeOrder.shopName,
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    Text(
                                        text = "${activeOrder.totalPages} Pages • ${activeOrder.config.colorOutput.label} • ${activeOrder.config.sides.label}",
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        ),
                                        modifier = Modifier.padding(top = 2.dp)
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "${activeOrder.estWaitMinutes} min",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = PrintPilotPrimary
                                        )
                                    )
                                    Text(
                                        text = "Wait time",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Queue position: #${activeOrder.queuePosition}",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                                Text(
                                    text = "View details",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrintPilotPrimary
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Queue progress bar (45%)
                            LinearProgressIndicator(
                                progress = { 0.45f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = PrintPilotPrimary,
                                trackColor = PrintPilotBorderLight
                            )
                        }
                    }
                }
            }
        }

        // 3. Nearby Shops Section Header & Filter Tabs
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Nearby Shops",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

                // Filter Tabs: Nearest, Fastest, Cheapest
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("Nearest", "Fastest", "Cheapest").forEach { filter ->
                        val isSelected = uiState.shopFilter == filter
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) PrintPilotPrimary else PrintPilotSurfaceLowest,
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) PrintPilotPrimary else PrintPilotBorderLight
                            ),
                            shadowElevation = if (isSelected) 2.dp else 0.dp,
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .clickable { onFilterChanged(filter) }
                        ) {
                            Text(
                                text = filter,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        // 4. Shop Cards List
        items(uiState.shops) { shop ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLowest,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onStartNewPrint(shop) }
                    .testTag("shop_card_${shop.id}")
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Shop Header Image Banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(PrintPilotSurfaceLow)
                    ) {
                        AsyncImage(
                            model = shop.imageUrl,
                            contentDescription = shop.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )

                        // Rating badge overlay
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = PrintPilotSurfaceLowest.copy(alpha = 0.9f),
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = PrintPilotGold,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = shop.rating.toString(),
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

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
                    }

                    Text(
                        text = "${shop.distanceMiles} miles away • ${shop.building}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Tags: Fast, $, Queue count
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (shop.isFast) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = PrintPilotSurfaceLow
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = "Fast",
                                        tint = PrintPilotPrimary,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = "Fast",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    )
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = PrintPilotSurfaceLow
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = "Price",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "$",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Text(
                            text = "${shop.waitingStudents} in queue",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }

        // 5. Promotional Hero Banner Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotPrimary.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, PrintPilotPrimary.copy(alpha = 0.2f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .clickable { onNavigateTo(AppNavigationScreen.HERO_LANDING) }
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Print without the queue 🚀",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrintPilotPrimary
                            )
                        )
                        Text(
                            text = "Learn how Print Pilot syncs your files straight to campus print shops.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "View",
                        tint = PrintPilotPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}
