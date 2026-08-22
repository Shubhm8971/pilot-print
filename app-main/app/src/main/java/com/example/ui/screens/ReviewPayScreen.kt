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
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Store
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.PrintPilotBackground
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotGold
import com.example.ui.theme.PrintPilotGoldLight
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotPrimaryContainer
import com.example.ui.theme.PrintPilotSuccess
import com.example.ui.theme.PrintPilotSurfaceLow
import com.example.ui.theme.PrintPilotSurfaceLowest
import com.example.viewmodel.PrintPilotUiState

@Composable
fun ReviewPayScreen(
    uiState: PrintPilotUiState,
    onPriorityToggled: (Boolean) -> Unit,
    onCoinsToggled: (Boolean) -> Unit,
    onConfirmAndPay: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedPaymentMethod by remember { mutableStateOf("UPI") }

    val totalPages = uiState.uploadedDocuments.sumOf { it.pageCount }
    val basePricePerPage = if (uiState.printConfig.colorOutput.name == "COLOR") 2.5 else 1.0
    val rawBaseTotal = totalPages * basePricePerPage
    val duplexDiscount = if (uiState.printConfig.sides.name == "DOUBLE") rawBaseTotal * 0.15 else 0.0
    val priorityFee = if (uiState.printConfig.isPriorityQueue) 20.0 else 0.0
    val coinDiscount = if (uiState.printConfig.useCoinsDiscount && uiState.profile.coinsBalance >= 100) 10.0 else 0.0
    val finalTotal = (rawBaseTotal - duplexDiscount + priorityFee - coinDiscount).coerceAtLeast(0.0)

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PrintPilotBackground)
            .testTag("review_pay_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Progress Indicator (Step 4 & 5)
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = "STEP 4 & 5 OF 5",
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
                    StepDivider(isActive = true)
                    StepCircle(stepNumber = 4, isCompletedOrActive = true, label = "Review")
                    StepDivider(isActive = true)
                    StepCircle(stepNumber = 5, isCompletedOrActive = false, label = "Pay")
                }
            }
        }

        // 2. Section Title
        item {
            Text(
                text = "Review & Pay",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
        }

        // 3. Shop & Document Summary Card
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
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Store,
                            contentDescription = null,
                            tint = PrintPilotPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column {
                            Text(
                                text = uiState.selectedShop.name,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = uiState.selectedShop.building,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PrintPilotBorderLight))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        uiState.uploadedDocuments.forEach { doc ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = doc.name,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "${doc.pageCount} pages",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Config: ${uiState.printConfig.colorOutput.label} • ${uiState.printConfig.sides.label} • Binding: ${uiState.printConfig.binding.label}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = PrintPilotPrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }

        // 4. Queue Speed Options (Standard vs Priority)
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Printing Queue Priority",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                // Standard Queue Option
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (!uiState.printConfig.isPriorityQueue) PrintPilotPrimary.copy(alpha = 0.05f) else PrintPilotSurfaceLowest,
                    border = BorderStroke(
                        if (!uiState.printConfig.isPriorityQueue) 1.5.dp else 1.dp,
                        if (!uiState.printConfig.isPriorityQueue) PrintPilotPrimary else PrintPilotBorderLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onPriorityToggled(false) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = !uiState.printConfig.isPriorityQueue,
                                onClick = { onPriorityToggled(false) },
                                colors = RadioButtonDefaults.colors(selectedColor = PrintPilotPrimary)
                            )
                            Column {
                                Text(
                                    text = "Standard Queue",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Est. 12-18 min wait • In queue with other students",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                        Text(
                            text = "Free",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrintPilotSuccess)
                        )
                    }
                }

                // Priority Queue Option
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (uiState.printConfig.isPriorityQueue) PrintPilotPrimary.copy(alpha = 0.05f) else PrintPilotSurfaceLowest,
                    border = BorderStroke(
                        if (uiState.printConfig.isPriorityQueue) 1.5.dp else 1.dp,
                        if (uiState.printConfig.isPriorityQueue) PrintPilotPrimary else PrintPilotBorderLight
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { onPriorityToggled(true) }
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = uiState.printConfig.isPriorityQueue,
                                onClick = { onPriorityToggled(true) },
                                colors = RadioButtonDefaults.colors(selectedColor = PrintPilotPrimary)
                            )
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Bolt,
                                        contentDescription = null,
                                        tint = PrintPilotPrimary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "Priority Fast-Track",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = PrintPilotPrimary
                                        )
                                    )
                                }
                                Text(
                                    text = "Est. 3-5 min • Jump straight to slot #1",
                                    style = MaterialTheme.typography.bodySmall.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
                                )
                            }
                        }
                        Text(
                            text = "+₹20.00",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold, color = PrintPilotPrimary)
                        )
                    }
                }
            }
        }

        // 5. Use Student Coins Discount
        item {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = PrintPilotGoldLight.copy(alpha = 0.4f),
                border = BorderStroke(1.dp, PrintPilotGold.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = PrintPilotGold,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "Use 100 Coins (Save ₹10)",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = "Your balance: ${uiState.profile.coinsBalance} Coins",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Switch(
                        checked = uiState.printConfig.useCoinsDiscount,
                        onCheckedChange = onCoinsToggled,
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = PrintPilotPrimary,
                            checkedTrackColor = PrintPilotPrimaryContainer.copy(alpha = 0.4f)
                        )
                    )
                }
            }
        }

        // 6. Payment Method Selector
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Payment Method",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PaymentMethodCard(
                        title = "UPI / QR",
                        icon = Icons.Default.QrCode,
                        isSelected = selectedPaymentMethod == "UPI",
                        onClick = { selectedPaymentMethod = "UPI" },
                        modifier = Modifier.weight(1f)
                    )
                    PaymentMethodCard(
                        title = "Card",
                        icon = Icons.Default.CreditCard,
                        isSelected = selectedPaymentMethod == "Card",
                        onClick = { selectedPaymentMethod = "Card" },
                        modifier = Modifier.weight(1f)
                    )
                    PaymentMethodCard(
                        title = "Campus Card",
                        icon = Icons.Default.AccountBalanceWallet,
                        isSelected = selectedPaymentMethod == "Campus",
                        onClick = { selectedPaymentMethod = "Campus" },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // 7. Bill Breakdown & Pay Button
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
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Receipt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Bill Details",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PrintPilotBorderLight))

                    BillRow(label = "Print Base ($totalPages Pages)", value = "₹${String.format("%.2f", rawBaseTotal)}")
                    if (duplexDiscount > 0) {
                        BillRow(label = "Double-Sided Saver (15%)", value = "-₹${String.format("%.2f", duplexDiscount)}", isDiscount = true)
                    }
                    if (priorityFee > 0) {
                        BillRow(label = "Priority Fast-Track", value = "+₹${String.format("%.2f", priorityFee)}")
                    }
                    if (coinDiscount > 0) {
                        BillRow(label = "100 Coins Discount", value = "-₹${String.format("%.2f", coinDiscount)}", isDiscount = true)
                    }

                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PrintPilotBorderLight))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Payable",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Text(
                            text = "₹${String.format("%.2f", finalTotal)}",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = PrintPilotPrimary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onConfirmAndPay,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrintPilotPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("btn_pay_order")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "Pay ₹${String.format("%.2f", finalTotal)} 🔒",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PaymentMethodCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isSelected) PrintPilotPrimary.copy(alpha = 0.08f) else PrintPilotSurfaceLowest,
        border = BorderStroke(
            if (isSelected) 1.5.dp else 1.dp,
            if (isSelected) PrintPilotPrimary else PrintPilotBorderLight
        ),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) PrintPilotPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = if (isSelected) PrintPilotPrimary else MaterialTheme.colorScheme.onSurface
                )
            )
        }
    }
}

@Composable
fun BillRow(label: String, value: String, isDiscount: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = if (isDiscount) PrintPilotSuccess else MaterialTheme.colorScheme.onSurface
            )
        )
    }
}
