package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.BuddyRequest
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotGoldContainer
import com.example.ui.theme.PrintPilotGoldDark
import com.example.ui.theme.PrintPilotGoldLight
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotSuccess
import com.example.ui.theme.PrintPilotSurfaceLowest

@Composable
fun OtpInputDialog(
    request: BuddyRequest?,
    isVerifiedSuccess: Boolean,
    onVerify: (String) -> Unit,
    onDismiss: () -> Unit,
    onReturnToOrders: () -> Unit,
    modifier: Modifier = Modifier
) {
    var otpValue by remember { mutableStateOf(request?.pickupOtp ?: "4829") }
    val focusManager = LocalFocusManager.current
    val rewardCoins = request?.rewardCoins ?: 25

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = PrintPilotSurfaceLowest,
            border = BorderStroke(1.dp, PrintPilotBorderLight),
            shadowElevation = 16.dp,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("otp_input_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (!isVerifiedSuccess) {
                    // OTP Entry Card (Image 14)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = "Security",
                                tint = PrintPilotPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "Enter Pickup OTP",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }

                        Text(
                            text = "Ask ${request?.requesterName ?: "the Buddy"} for the 4-digit OTP to confirm secure document handover.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )

                        // 4-Box Visual OTP Input
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            // Hidden real text field for keyboard capture
                            BasicTextField(
                                value = otpValue,
                                onValueChange = {
                                    if (it.length <= 4 && it.all { char -> char.isDigit() }) {
                                        otpValue = it
                                    }
                                },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        focusManager.clearFocus()
                                        onVerify(otpValue)
                                    }
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("otp_real_input")
                            )

                            // 4 Stylized Digit Boxes
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (i in 0 until 4) {
                                    val digit = otpValue.getOrNull(i)?.toString() ?: ""
                                    Box(
                                        modifier = Modifier
                                            .size(width = 56.dp, height = 64.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(PrintPilotSurfaceLowest)
                                            .border(
                                                width = if (digit.isNotEmpty()) 2.dp else 1.dp,
                                                color = if (digit.isNotEmpty()) PrintPilotPrimary else PrintPilotBorderLight,
                                                shape = RoundedCornerShape(12.dp)
                                            ),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = digit,
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.ExtraBold,
                                                color = MaterialTheme.colorScheme.onSurface
                                            ),
                                            textAlign = TextAlign.Center
                                        )
                                    }
                                }
                            }
                        }

                        Button(
                            onClick = {
                                focusManager.clearFocus()
                                onVerify(otpValue)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PrintPilotPrimary),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_verify_otp")
                        ) {
                            Text(
                                text = "Verify OTP",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }
                } else {
                    // Success State (Image 14 Bottom Section)
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn() + slideInVertically()
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(PrintPilotSuccess.copy(alpha = 0.12f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = PrintPilotSuccess,
                                    modifier = Modifier.size(36.dp)
                                )
                            }

                            Text(
                                text = "Pickup Verified ✓",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )

                            Text(
                                text = "Documents securely handed over.",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            )

                            // Gold Reward Banner
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = PrintPilotGoldLight,
                                border = BorderStroke(1.dp, PrintPilotGoldDark.copy(alpha = 0.3f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Stars,
                                            contentDescription = "Reward",
                                            tint = PrintPilotGoldDark,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Text(
                                            text = "Reward Earned",
                                            style = MaterialTheme.typography.labelMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = PrintPilotGoldDark
                                            )
                                        )
                                    }

                                    Text(
                                        text = "+$rewardCoins Coins 🎉",
                                        style = MaterialTheme.typography.titleSmall.copy(
                                            fontWeight = FontWeight.ExtraBold,
                                            color = PrintPilotGoldDark
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            OutlinedButton(
                                onClick = onReturnToOrders,
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, PrintPilotBorderLight),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("btn_return_orders")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Return to Orders",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
