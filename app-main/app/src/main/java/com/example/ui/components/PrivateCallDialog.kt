package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.PrintPilotBorderLight
import com.example.ui.theme.PrintPilotError
import com.example.ui.theme.PrintPilotPrimary
import com.example.ui.theme.PrintPilotPrimaryContainer
import com.example.ui.theme.PrintPilotSurfaceLow
import com.example.ui.theme.PrintPilotSurfaceLowest

@Composable
fun PrivateCallDialog(
    buddyName: String,
    onDismiss: () -> Unit,
    onOpenOtpVerification: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isMuted by remember { mutableStateOf(false) }
    var isSpeakerOn by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_ring")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = PrintPilotSurfaceLowest,
            border = BorderStroke(1.dp, PrintPilotBorderLight),
            shadowElevation = 12.dp,
            modifier = modifier
                .fillMaxWidth()
                .padding(8.dp)
                .testTag("private_call_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PrintPilotSurfaceLow,
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Private",
                            tint = PrintPilotPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "Print Pilot Private Call",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = PrintPilotPrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Pulsing Avatar
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(100.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(90.dp)
                            .scale(pulseScale)
                            .clip(CircleShape)
                            .background(PrintPilotPrimaryContainer.copy(alpha = 0.2f))
                    )
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(PrintPilotPrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Buddy Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(44.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Calling $buddyName...",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                )

                Text(
                    text = "Your number is masked for privacy",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    modifier = Modifier.padding(top = 4.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Call action buttons: Mute, End Call, Speaker
                Row(
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { isMuted = !isMuted },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(if (isMuted) PrintPilotPrimary.copy(alpha = 0.25f) else PrintPilotSurfaceLow)
                            .testTag("btn_mute_call")
                    ) {
                        Icon(
                            imageVector = if (isMuted) Icons.Default.MicOff else Icons.Default.Mic,
                            contentDescription = "Mute",
                            tint = if (isMuted) PrintPilotPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(PrintPilotError)
                            .testTag("btn_end_call")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CallEnd,
                            contentDescription = "End Call",
                            tint = Color.White
                        )
                    }

                    IconButton(
                        onClick = { isSpeakerOn = !isSpeakerOn },
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(if (isSpeakerOn) PrintPilotPrimary.copy(alpha = 0.25f) else PrintPilotSurfaceLow)
                            .testTag("btn_speaker_call")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Speaker",
                            tint = if (isSpeakerOn) PrintPilotPrimary else MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Shortcut to OTP Handover
                Button(
                    onClick = {
                        onDismiss()
                        onOpenOtpVerification()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = PrintPilotPrimary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_go_to_otp_verify")
                ) {
                    Text("Handover & Verify OTP")
                }
            }
        }
    }
}
