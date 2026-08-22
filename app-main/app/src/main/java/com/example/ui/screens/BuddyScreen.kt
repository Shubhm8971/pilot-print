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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.model.BuddyRequest
import com.example.ui.theme.PrintPilotBackground
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotGold
import com.example.ui.theme.PrintPilotGoldDark
import com.example.ui.theme.PrintPilotGoldLight
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotPrimaryContainer
import com.example.ui.theme.PrintPilotSurfaceLow
import com.example.ui.theme.PrintPilotSurfaceLowest
import com.example.viewmodel.PrintPilotUiState

@Composable
fun BuddyScreen(
    uiState: PrintPilotUiState,
    onAcceptRequest: (BuddyRequest) -> Unit,
    onOpenOtpVerify: (BuddyRequest) -> Unit,
    onCreateRequestClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(PrintPilotBackground)
            .testTag("buddy_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // 1. Header (Image 12)
        item {
            Column(modifier = Modifier.padding(top = 4.dp)) {
                Text(
                    text = "Buddy",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                )
                Text(
                    text = "Going to the print shop anyway? Help a peer out.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }

        // 2. "HELP SOMEONE" Section Header with "Earn Coins" Pill
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "HELP SOMEONE",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrintPilotGoldLight.copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, PrintPilotGold.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = PrintPilotGoldDark,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "Earn Coins",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrintPilotGoldDark
                            )
                        )
                    }
                }
            }
        }

        // 3. Buddy Requests List (John D., Alice S., Rahul K.)
        items(uiState.buddyRequests) { request ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = PrintPilotSurfaceLowest,
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                shadowElevation = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("buddy_card_${request.id}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Student initials avatar
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(PrintPilotPrimary.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = request.initials,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = PrintPilotPrimary
                                    )
                                )
                            }

                            Column {
                                Text(
                                    text = request.requesterName,
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(
                                    text = "${request.shopName} • ${request.pageCount} pages",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                )
                            }
                        }

                        // Gold coins reward badge
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrintPilotGoldLight,
                            border = BorderStroke(0.5.dp, PrintPilotGoldDark.copy(alpha = 0.3f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.MonetizationOn,
                                    contentDescription = null,
                                    tint = PrintPilotGoldDark,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "+${request.rewardCoins} Coins",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = PrintPilotGoldDark
                                    )
                                )
                            }
                        }
                    }

                    // Student Note
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PrintPilotSurfaceLow,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = request.note,
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            ),
                            modifier = Modifier.padding(10.dp)
                        )
                    }

                    // Action buttons: "Accept Request" & "Verify OTP"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { onAcceptRequest(request) },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrintPilotPrimary),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("btn_accept_request_${request.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Accept & Call",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                                )
                            }
                        }

                        OutlinedButton(
                            onClick = { onOpenOtpVerify(request) },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, PrintPilotBorderLight),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                                .testTag("btn_verify_otp_${request.id}")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = PrintPilotPrimary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "Verify OTP",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // 4. "NEED A BUDDY?" Card (Image 12 Bottom Hero)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = PrintPilotSurfaceLowest
                ),
                border = BorderStroke(1.dp, PrintPilotBorderLight),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("need_a_buddy_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrintPilotPrimary.copy(alpha = 0.1f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Diversity3,
                                contentDescription = null,
                                tint = PrintPilotPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Text(
                            text = "NEED A BUDDY?",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    Text(
                        text = "Stuck in class? Have someone else grab your prints for you. Offer coins as a reward.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    )

                    Button(
                        onClick = onCreateRequestClick,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrintPilotPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("btn_create_buddy_request")
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Create Request",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }

        // 5. Interactive Campus Buddy Map Radar Preview
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
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = PrintPilotPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Active Buddies on Campus",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }

                    // Stylized Campus Radar Map
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(
                                        Color(0xFF26242B),
                                        Color(0xFF332F3B)
                                    )
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            BuddyRadarPin(label = "Library (3)", isHighlighted = true)
                            BuddyRadarPin(label = "Tech Park (2)", isHighlighted = false)
                            BuddyRadarPin(label = "Hostel B (4)", isHighlighted = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BuddyRadarPin(label: String, isHighlighted: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(if (isHighlighted) 28.dp else 22.dp)
                .clip(CircleShape)
                .background(if (isHighlighted) PrintPilotPrimary else PrintPilotPrimaryContainer.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Diversity3,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(if (isHighlighted) 16.dp else 12.dp)
            )
        }
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            ),
            modifier = Modifier.padding(top = 2.dp)
        )
    }
}
