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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Redeem
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.RewardItem
import com.example.ui.theme.PrintPilotBackground
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotGold
import com.example.ui.theme.PrintPilotGoldContainer
import com.example.ui.theme.PrintPilotGoldDark
import com.example.ui.theme.PrintPilotGoldLight
import com.example.ui.theme.PrintPilotOutline
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotPrimaryContainer
import com.example.ui.theme.PrintPilotSurfaceLow
import com.example.ui.theme.PrintPilotSurfaceLowest
import com.example.viewmodel.PrintPilotUiState

@Composable
fun RewardsScreen(
    uiState: PrintPilotUiState,
    onRedeemReward: (RewardItem) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PrintPilotBackground)
            .testTag("rewards_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Balance Hero Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrintPilotPrimary),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(
                                    Color(0xFF906600),
                                    PrintPilotGoldDark,
                                    PrintPilotGold
                                )
                            )
                        )
                        .padding(22.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Print Pilot Coins",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "Level 2 Scholar",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    ),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                            )
                            Text(
                                text = "${uiState.profile.coinsBalance}",
                                style = MaterialTheme.typography.displaySmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "Coins",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.9f)
                                ),
                                modifier = Modifier.align(Alignment.Bottom).padding(bottom = 6.dp)
                            )
                        }

                        Text(
                            text = "Earn coins by delivering for peers or using double-sided printing!",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                    }
                }
            }
        }

        // 2. Ways to Earn Coins
        item {
            Text(
                text = "EARN COINS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLowest,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    EarnTaskRow(
                        title = "Pick up a Peer's Prints",
                        desc = "Accept a Buddy request and verify handover OTP",
                        reward = "+25 Coins"
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PrintPilotBorderLight))
                    EarnTaskRow(
                        title = "Print Double-Sided (Eco Hero)",
                        desc = "Save paper on any document 10+ pages",
                        reward = "+15 Coins"
                    )
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(PrintPilotBorderLight))
                    EarnTaskRow(
                        title = "Early Bird Print (Before 10 AM)",
                        desc = "Beat the campus rush with morning queues",
                        reward = "+10 Coins"
                    )
                }
            }
        }

        // 3. Redeem Catalog
        item {
            Text(
                text = "REDEEM REWARDS",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(uiState.rewards) { reward ->
            val canAfford = uiState.profile.coinsBalance >= reward.coinCost

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLowest,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(PrintPilotGoldLight.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (reward.iconName) {
                                    "local_cafe" -> Icons.Default.LocalCafe
                                    "bolt" -> Icons.Default.Bolt
                                    "menu_book" -> Icons.Default.CardGiftcard
                                    else -> Icons.Default.Stars
                                },
                                contentDescription = null,
                                tint = PrintPilotGoldDark,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = reward.title,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                            Text(
                                text = reward.description,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )
                        }
                    }

                    Button(
                        onClick = { onRedeemReward(reward) },
                        enabled = canAfford,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrintPilotGoldContainer,
                            disabledContainerColor = PrintPilotSurfaceLow
                        ),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${reward.coinCost} 🪙",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (canAfford) PrintPilotGoldDark else PrintPilotOutline
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EarnTaskRow(title: String, desc: String, reward: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )
            Text(
                text = desc,
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = PrintPilotGoldLight.copy(alpha = 0.6f)
        ) {
            Text(
                text = reward,
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = PrintPilotGoldDark
                ),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}
