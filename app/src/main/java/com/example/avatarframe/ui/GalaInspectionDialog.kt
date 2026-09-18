package com.example.avatarframe.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.avatarframe.model.FrameItem

/**
 * 💎 GalaInspectionDialog: Fullscreen Holographic Gala Inspection Mode.
 * Features an enlarged 340dp avatar frame with an interactive dynamic spotlight
 * that follows the user's finger, illuminating micro-bevels and gem facets.
 */
@Composable
fun GalaInspectionDialog(
    frameItem: FrameItem,
    avatarRes: Int,
    onDismiss: () -> Unit
) {
    var torchPosition by remember { mutableStateOf(Offset(500f, 500f)) }
    var isTorchActive by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "GalaTransition")
    val ambientBreath by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AmbientBreath"
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xF90A0B0E)) // 98% deep obsidian gala darkness
                .pointerInput(Unit) {
                    detectDragGestures(
                        onDragStart = { offset ->
                            torchPosition = offset
                            isTorchActive = true
                        },
                        onDragEnd = { isTorchActive = false },
                        onDragCancel = { isTorchActive = false },
                        onDrag = { change, _ ->
                            change.consume()
                            torchPosition = change.position
                            isTorchActive = true
                        }
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            // 1. Interactive Torchlight / Dynamic Gallery Spotlight
            Canvas(modifier = Modifier.fillMaxSize()) {
                val spotlightCenter = if (isTorchActive) torchPosition else Offset(size.width / 2f, size.height * 0.42f)
                val spotlightRadius = size.width * (if (isTorchActive) 0.55f else 0.70f) * ambientBreath

                // Ambient golden gallery cone
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            frameItem.primaryColor.copy(alpha = 0.22f),
                            frameItem.secondaryColor.copy(alpha = 0.08f),
                            Color.Transparent
                        ),
                        center = spotlightCenter,
                        radius = spotlightRadius
                    ),
                    center = spotlightCenter,
                    radius = spotlightRadius,
                    blendMode = BlendMode.Plus
                )

                // High-intensity torch focus spot
                if (isTorchActive) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.35f),
                                frameItem.glowColor.copy(alpha = 0.18f),
                                Color.Transparent
                            ),
                            center = spotlightCenter,
                            radius = 120.dp.toPx()
                        ),
                        center = spotlightCenter,
                        radius = 120.dp.toPx(),
                        blendMode = BlendMode.Plus
                    )
                }
            }

            // 2. Center Stage Content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 36.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top Header Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.ZoomIn, contentDescription = null, tint = frameItem.primaryColor, modifier = Modifier.size(18.dp))
                            Text(
                                text = "HOLOGRAPHIC GALA INSPECTOR",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = frameItem.primaryColor,
                                letterSpacing = 1.5.sp
                            )
                        }
                        Text(
                            text = frameItem.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    // Close Button
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF1E212B))
                            .border(1.dp, Color(0xFF333846), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                // Middle: Enlarged 330dp Avatar Frame
                Box(
                    modifier = Modifier
                        .size(330.dp),
                    contentAlignment = Alignment.Center
                ) {
                    DivineAvatarFrame(
                        frameItem = frameItem,
                        avatarRes = avatarRes,
                        frameSize = 330.dp,
                        showNeuralVfx = true,
                        enable3DParallax = true,
                        showActionButton = false
                    )
                }

                // Bottom Hint & Actions
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF161822))
                            .border(1.dp, frameItem.primaryColor.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "💡 Desliza tu dedo sobre el marco para iluminarlo con el reflector dinámico",
                            fontSize = 12.sp,
                            color = Color(0xFFCFD8DC),
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = frameItem.primaryColor
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "SALIR DE GALA",
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }
            }
        }
    }
}
