package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.model.PrintShop
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotGold
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotSurfaceLowest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBuddyRequestDialog(
    shops: List<PrintShop>,
    coinsBalance: Int,
    onDismiss: () -> Unit,
    onSubmit: (shopName: String, rewardCoins: Int, pages: Int, note: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedShop by remember { mutableStateOf(shops.firstOrNull()?.name ?: "Campus Print Hub") }
    var expandedShopDropdown by remember { mutableStateOf(false) }
    var rewardCoins by remember { mutableIntStateOf(25) }
    var pagesCount by remember { mutableIntStateOf(16) }
    var noteText by remember { mutableStateOf("Need lab assignment print before 2 PM. Paid & waiting at desk.") }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = PrintPilotSurfaceLowest,
            border = BorderStroke(1.dp, PrintPilotBorderLight),
            shadowElevation = 16.dp,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("create_buddy_request_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = PrintPilotPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "Request a Buddy",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Text(
                    text = "Offer coins to a student already heading to the print shop.",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                // Select Shop
                ExposedDropdownMenuBox(
                    expanded = expandedShopDropdown,
                    onExpandedChange = { expandedShopDropdown = !expandedShopDropdown }
                ) {
                    OutlinedTextField(
                        value = selectedShop,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Print Shop Location") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedShopDropdown) },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = PrintPilotBorderLight,
                            focusedBorderColor = PrintPilotPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedShopDropdown,
                        onDismissRequest = { expandedShopDropdown = false }
                    ) {
                        shops.forEach { shop ->
                            DropdownMenuItem(
                                text = { Text("${shop.name} (${shop.building})") },
                                onClick = {
                                    selectedShop = shop.name
                                    expandedShopDropdown = false
                                }
                            )
                        }
                    }
                }

                // Reward Coins & Pages
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = rewardCoins.toString(),
                        onValueChange = { rewardCoins = it.toIntOrNull() ?: 0 },
                        label = { Text("Coin Reward") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = PrintPilotGold,
                                modifier = Modifier.size(18.dp)
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = PrintPilotBorderLight,
                            focusedBorderColor = PrintPilotPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = pagesCount.toString(),
                        onValueChange = { pagesCount = it.toIntOrNull() ?: 1 },
                        label = { Text("Pages") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = PrintPilotBorderLight,
                            focusedBorderColor = PrintPilotPrimary
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Note
                OutlinedTextField(
                    value = noteText,
                    onValueChange = { noteText = it },
                    label = { Text("Instructions for Buddy") },
                    placeholder = { Text("e.g. Please drop off at Tech Park Room 302") },
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = PrintPilotBorderLight,
                        focusedBorderColor = PrintPilotPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                // Submit Button
                Button(
                    onClick = {
                        onSubmit(selectedShop, rewardCoins, pagesCount, noteText)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrintPilotPrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_submit_buddy_request")
                ) {
                    Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Post Request (Balance: $coinsBalance)",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
