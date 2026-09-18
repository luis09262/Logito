package com.example.avatarframe.ui

import androidx.annotation.DrawableRes
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.avatarframe.cache.FrameImageCache
import com.example.avatarframe.model.ElementalAura
import com.example.avatarframe.model.FrameItem
import com.example.avatarframe.model.FrameSpecialFeature
import com.example.avatarframe.vfx.ElementalAurasEffect
import com.example.avatarframe.vfx.NeuralSynapseCanvas
import com.example.avatarframe.vfx.VfxRenderer
import com.example.avatarframe.vfx.VfxType

/**
 * 👑 DivineAvatarFrame: Modular, Plug-and-Play Jetpack Compose Component.
 * Full alpha transparency preservation with decoupled procedural visual effects (VFX).
 */
@Composable
fun DivineAvatarFrame(
    modifier: Modifier = Modifier,
    @DrawableRes frameRes: Int = R.drawable.frame_celestial_gold_1789374233057,
    @DrawableRes avatarRes: Int = R.drawable.avatar_celestial_mage_1789374278416,
    frameItem: FrameItem? = null,
    frameSize: Dp = 320.dp,
    vfxType: VfxType = VfxType.NEURAL_SYNAPSE,
    showNeuralVfx: Boolean = true,
    enable3DParallax: Boolean = true,
    showActionButton: Boolean = true,
    activeAura: ElementalAura = ElementalAura.LIGHTNING,
    onActionClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val effectiveFrameRes = frameItem?.frameRes ?: frameRes

    val primaryColor = if (frameItem?.specialFeature == FrameSpecialFeature.VFX_AURA_SWITCHER) {
        activeAura.primaryColor
    } else {
        frameItem?.primaryColor ?: Color(0xFFFFD700)
    }

    val secondaryColor = if (frameItem?.specialFeature == FrameSpecialFeature.VFX_AURA_SWITCHER) {
        activeAura.secondaryColor
    } else {
        frameItem?.secondaryColor ?: Color(0xFF00E5FF)
    }

    val glowColor = if (frameItem?.specialFeature == FrameSpecialFeature.VFX_AURA_SWITCHER) {
        activeAura.glowColor
    } else {
        frameItem?.glowColor ?: Color(0xFF80D8FF)
    }

    // Ultra-Fast in-memory cache lookup (preserves full ears, wings, staffs)
    val frameBitmap = remember(effectiveFrameRes) {
        FrameImageCache.getCachedBitmap(effectiveFrameRes)
            ?: FrameImageCache.processBitmapSynchronous(context, effectiveFrameRes)
    }

    var tiltX by remember { mutableFloatStateOf(0f) }
    var tiltY by remember { mutableFloatStateOf(0f) }

    val animatedTiltX by animateFloatAsState(
        targetValue = tiltX,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "TiltX"
    )
    val animatedTiltY by animateFloatAsState(
        targetValue = tiltY,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "TiltY"
    )

    Box(
        modifier = modifier
            .size(frameSize)
            .then(
                if (enable3DParallax) {
                    Modifier.pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = { tiltX = 0f; tiltY = 0f },
                            onDragCancel = { tiltX = 0f; tiltY = 0f },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                tiltX = (tiltX - dragAmount.y * 0.12f).coerceIn(-14f, 14f)
                                tiltY = (tiltY + dragAmount.x * 0.12f).coerceIn(-14f, 14f)
                            }
                        )
                    }
                } else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        // 1. DECOUPLED BACKGROUND VFX LAYER: Modular Procedural Particle/Geometry Engines
        VfxRenderer(
            vfxType = vfxType,
            primaryColor = primaryColor,
            secondaryColor = secondaryColor,
            glowColor = glowColor,
            modifier = Modifier.fillMaxSize(),
            intensity = 1.0f,
            frameRadiusRatio = frameItem?.frameRadiusRatio ?: 0.43f
        )

        // 2. 3D ORNAMENTAL FRAME + INNER AVATAR
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    if (enable3DParallax) {
                        rotationX = animatedTiltX
                        rotationY = animatedTiltY
                        cameraDistance = 18f * density
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            // Inner Circular Avatar Window
            Box(
                modifier = Modifier
                    .fillMaxSize(0.54f)
                    .clip(CircleShape)
                    .background(Color(0xFF1E2128)),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = avatarRes,
                    animationSpec = tween(180),
                    label = "AvatarFade"
                ) { targetAvatar ->
                    Image(
                        painter = painterResource(id = targetAvatar),
                        contentDescription = "Avatar Character Portrait",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Inner Ambient Occlusion
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val r = kotlin.math.min(size.width, size.height) / 2f
                    drawCircle(
                        brush = Brush.radialGradient(
                            colorStops = arrayOf(
                                0.0f to Color.Transparent,
                                0.80f to Color.Transparent,
                                0.94f to Color(0x990D0E12),
                                1.0f to Color(0xF20D0E12)
                            ),
                            center = center,
                            radius = r
                        ),
                        radius = r,
                        center = center
                    )
                }
            }

            // 3D Transparent Frame Cutout (Full resolution, Zero Clipping, Ears and Staffs Preserved)
            Crossfade(
                targetState = frameBitmap,
                animationSpec = tween(150),
                label = "FrameFade"
            ) { bitmap ->
                Image(
                    bitmap = bitmap,
                    contentDescription = "Transparent Divine Avatar Frame",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            // Precision Action Button (+)
            if (showActionButton) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = 64.dp, y = 64.dp)
                ) {
                    ModularActionButton(
                        accentColor = secondaryColor,
                        goldBorderColor = primaryColor,
                        onClick = onActionClick
                    )
                }
            }
        }
    }
}

/**
 * Modular Precision (+) Button
 */
@Composable
fun ModularActionButton(
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFF00E5FF),
    goldBorderColor: Color = Color(0xFFFFD700),
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }

    Box(
        modifier = modifier
            .size(38.dp)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor = Color(0xFF0A0B0E),
                spotColor = accentColor
            )
            .border(
                width = 2.dp,
                brush = Brush.linearGradient(listOf(Color.White, accentColor, goldBorderColor)),
                shape = CircleShape
            )
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(accentColor, accentColor.copy(alpha = 0.9f), Color(0xFF16181F)),
                    center = Offset(20f, 20f),
                    radius = 50f
                ),
                shape = CircleShape
            )
            .clip(CircleShape)
            .clickable(
                interactionSource = interactionSource,
                indication = ripple(bounded = true, color = Color.White),
                onClick = onClick
            )
            .testTag("modular_add_button"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            val strokeW = 2.4.dp.toPx()
            val w = size.width
            val h = size.height
            val pad = 2.5.dp.toPx()

            drawLine(
                color = Color.White,
                start = Offset(pad, h / 2f),
                end = Offset(w - pad, h / 2f),
                strokeWidth = strokeW,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White,
                start = Offset(w / 2f, pad),
                end = Offset(w / 2f, h - pad),
                strokeWidth = strokeW,
                cap = StrokeCap.Round
            )
        }
    }
}
