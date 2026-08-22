package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Store
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.OrderStatus
import com.example.model.PrintOrder
import com.example.ui.theme.PrintPilotBackground
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotPrimaryContainer
import com.example.ui.theme.PrintPilotSuccess
import com.example.ui.theme.PrintPilotSurfaceLow
import com.example.ui.theme.PrintPilotSurfaceLowest
import com.example.viewmodel.AppNavigationScreen

@Composable
fun QueueTrackingScreen(
    order: PrintOrder?,
    onAdvanceStep: (String) -> Unit,
    onRequestBuddy: () -> Unit,
    onBackToHome: () -> Unit,
    modifier: Modifier = Modifier
) {
    val clipboardManager = LocalClipboardManager.current
    val currentOrder = order ?: return

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PrintPilotBackground)
            .testTag("queue_tracking_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Order Status Header Card
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLowest,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Order #${currentOrder.id}",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                                modifier = Modifier.padding(top = 2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Store,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "${currentOrder.shopName} • ${currentOrder.locationInfo}",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = when (currentOrder.status) {
                                OrderStatus.READY -> PrintPilotSuccess.copy(alpha = 0.15f)
                                OrderStatus.PRINTING -> PrintPilotPrimary.copy(alpha = 0.15f)
                                else -> PrintPilotSurfaceLow
                            }
                        ) {
                            Text(
                                text = currentOrder.status.label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = when (currentOrder.status) {
                                        OrderStatus.READY -> PrintPilotSuccess
                                        OrderStatus.PRINTING -> PrintPilotPrimary
                                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                                    }
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                            )
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PrintPilotBorderLight))

                    // Position & Time Counter Bento
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrintPilotSurfaceLow,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = if (currentOrder.queuePosition > 0) "#${currentOrder.queuePosition}" else "Active",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = PrintPilotPrimary
                                    )
                                )
                                Text(
                                    text = "Queue Position",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrintPilotSurfaceLow,
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = "${currentOrder.estWaitMinutes} min",
                                    style = MaterialTheme.typography.headlineMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "Estimated Wait",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Step Progress Tracker (Payment -> Queued -> Printing -> Ready -> Picked Up)
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
                    Text(
                        text = "Live Progress Tracker",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    val steps = listOf(
                        OrderStatus.PAYMENT,
                        OrderStatus.QUEUED,
                        OrderStatus.PRINTING,
                        OrderStatus.READY,
                        OrderStatus.PICKED_UP
                    )

                    val currentStepIndex = currentOrder.status.stepIndex

                    steps.forEachIndexed { index, step ->
                        val isDone = index < currentStepIndex
                        val isCurrent = index == currentStepIndex
                        val isUpcoming = index > currentStepIndex

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Step Icon Indicator
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .then(
                                        if (isCurrent) Modifier.scale(pulseScale) else Modifier
                                    )
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isDone -> PrintPilotSuccess
                                            isCurrent -> PrintPilotPrimary
                                            else -> PrintPilotSurfaceLow
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDone) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else if (isCurrent) {
                                    Icon(
                                        imageVector = Icons.Default.Print,
                                        contentDescription = "Current",
                                        tint = Color.White,
                                        modifier = Modifier.size(18.dp)
                                    )
                                } else {
                                    Text(
                                        text = "${index + 1}",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            color = MaterialTheme.colorScheme.outline,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = step.label,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isCurrent) PrintPilotPrimary else if (isDone) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.outline
                                    )
                                )
                                if (isCurrent) {
                                    Text(
                                        text = when (step) {
                                            OrderStatus.PAYMENT -> "Payment verified & authorized"
                                            OrderStatus.QUEUED -> "Documents sent to shop spooler"
                                            OrderStatus.PRINTING -> "Printer is currently rolling pages"
                                            OrderStatus.READY -> "Waiting at counter for pickup"
                                            OrderStatus.PICKED_UP -> "Completed"
                                        },
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 11.sp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Demo Action button: Advance status
                    OutlinedButton(
                        onClick = { onAdvanceStep(currentOrder.id) },
                        border = BorderStroke(1.dp, PrintPilotBorderLight),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_advance_order_demo")
                    ) {
                        Icon(
                            imageVector = Icons.Default.FastForward,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Simulate Printer Advance (Live Demo)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        // 3. High Security Pickup OTP Card (Image 16)
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLowest,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = PrintPilotPrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Pickup Verification Code",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.QrCode2,
                            contentDescription = "QR Code",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = "Show this 6-digit OTP to the shopkeeper or buddy for pickup.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    // Big Code Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PrintPilotSurfaceLow,
                        border = BorderStroke(1.dp, PrintPilotPrimary.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp, horizontal = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = currentOrder.pickupOtp,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 6.sp,
                                    color = PrintPilotPrimary
                                )
                            )

                            IconButton(
                                onClick = {
                                    clipboardManager.setText(AnnotatedString(currentOrder.pickupOtp))
                                },
                                modifier = Modifier.testTag("btn_copy_otp")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ContentCopy,
                                    contentDescription = "Copy OTP",
                                    tint = PrintPilotPrimary
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. Buddy Pickup Callout
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotPrimaryContainer.copy(alpha = 0.1f),
                border = BorderStroke(1.dp, PrintPilotPrimaryContainer.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Diversity3,
                            contentDescription = null,
                            tint = PrintPilotPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "Can't make it to the shop?",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = PrintPilotPrimary
                            )
                        )
                    }

                    Text(
                        text = "Assign a student Buddy on campus to pick up your documents using your secure OTP.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Button(
                        onClick = onRequestBuddy,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrintPilotPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_assign_buddy_order")
                    ) {
                        Text(
                            text = "I Need a Buddy",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
