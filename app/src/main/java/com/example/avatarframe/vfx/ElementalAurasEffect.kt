package com.example.avatarframe.vfx

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.avatarframe.model.ElementalAura
import kotlin.math.*
import kotlin.random.Random

/**
 * ⚡ ElementalAurasEffect: Ultra-Refined, High-End Ambient Elemental Visual Effects.
 * Designed with smooth atmospheric gradients, zero hard-edge discs, and delicate organic particles.
 */
@Composable
fun ElementalAurasEffect(
    modifier: Modifier = Modifier,
    aura: ElementalAura = ElementalAura.LIGHTNING,
    intensity: Float = 1.0f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "ElementalAurasVfx")

    // Slow, serene primary cycle
    val loopTime by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "LoopTime"
    )

    // Breathing pulse for soft radiance
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "RadiancePulse"
    )

    // Jitter/shimmer for lightning & ember sparks
    val shimmer by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(350, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Shimmer"
    )

    // Deterministic particle data
    val solarEmbers = remember {
        val rand = Random(101)
        List(22) { i ->
            EmberParticle(
                startAngle = rand.nextFloat() * 360f,
                speed = 0.35f + rand.nextFloat() * 0.45f,
                radiusScale = 0.82f + rand.nextFloat() * 0.38f,
                size = 1.8f + rand.nextFloat() * 2.8f,
                swayFreq = 1.5f + rand.nextFloat() * 2.5f,
                phase = rand.nextFloat()
            )
        }
    }

    val cosmicStars = remember {
        val rand = Random(202)
        List(20) { i ->
            CosmicStarParticle(
                baseAngle = rand.nextFloat() * 360f,
                orbitSpeed = 0.2f + rand.nextFloat() * 0.4f,
                radiusRatio = 0.85f + rand.nextFloat() * 0.42f,
                size = 1.5f + rand.nextFloat() * 2.5f,
                twinkleSpeed = 2f + rand.nextFloat() * 3f,
                phase = rand.nextFloat()
            )
        }
    }

    val sakuraPetals = remember {
        val rand = Random(303)
        List(14) { i ->
            SakuraPetalParticle(
                baseAngle = rand.nextFloat() * 360f,
                speed = 0.25f + rand.nextFloat() * 0.35f,
                distance = 0.88f + rand.nextFloat() * 0.36f,
                scale = 2.5f + rand.nextFloat() * 3.5f,
                spinSpeed = 1.5f + rand.nextFloat() * 2f,
                phase = rand.nextFloat()
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        val center = Offset(w / 2f, h / 2f)
        val frameRadius = min(w, h) * 0.42f

        if (frameRadius <= 0f) return@Canvas

        // 1. UNIVERSAL SEAMLESS ATMOSPHERIC RADIANCE (Zero Hard Cutoffs / Zero Circular Discs)
        val maxGlowRadius = frameRadius * 1.55f * pulse
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to aura.primaryColor.copy(alpha = 0.09f * intensity),
                    0.40f to aura.secondaryColor.copy(alpha = 0.05f * intensity),
                    0.72f to aura.glowColor.copy(alpha = 0.015f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = maxGlowRadius
            ),
            center = center,
            radius = maxGlowRadius
        )

        // 2. ELEMENT SPECIFIC REFINED VFX
        when (aura) {
            ElementalAura.LIGHTNING -> {
                drawRefinedLightning(
                    center = center,
                    radius = frameRadius,
                    aura = aura,
                    loopTime = loopTime,
                    shimmer = shimmer,
                    intensity = intensity
                )
            }

            ElementalAura.SOLAR_FIRE -> {
                drawRefinedSolarEmbers(
                    center = center,
                    radius = frameRadius,
                    aura = aura,
                    loopTime = loopTime,
                    embers = solarEmbers,
                    intensity = intensity
                )
            }

            ElementalAura.COSMIC_NEBULA -> {
                drawRefinedCosmicNebula(
                    center = center,
                    radius = frameRadius,
                    aura = aura,
                    loopTime = loopTime,
                    stars = cosmicStars,
                    intensity = intensity
                )
            }

            ElementalAura.CHERRY_BLOSSOM -> {
                drawRefinedSakuraWind(
                    center = center,
                    radius = frameRadius,
                    aura = aura,
                    loopTime = loopTime,
                    petals = sakuraPetals,
                    intensity = intensity
                )
            }
        }
    }
}

/**
 * ⚡ Refined Arc Lightning: Delicate electric arcs crawling cleanly with micro plasma ionization
 */
private fun DrawScope.drawRefinedLightning(
    center: Offset,
    radius: Float,
    aura: ElementalAura,
    loopTime: Float,
    shimmer: Float,
    intensity: Float
) {
    val boltCount = 4
    val rand = Random((shimmer * 100).toInt())

    for (b in 0 until boltCount) {
        val baseAngle = (b * (360f / boltCount) + loopTime * 120f) % 360f
        val boltPath = Path()
        val r0 = radius * (0.94f + rand.nextFloat() * 0.06f)
        val x0 = center.x + r0 * cos(Math.toRadians(baseAngle.toDouble())).toFloat()
        val y0 = center.y + r0 * sin(Math.toRadians(baseAngle.toDouble())).toFloat()
        boltPath.moveTo(x0, y0)

        var cx = x0
        var cy = y0
        val steps = 5
        val arcSpan = 22f

        for (s in 1..steps) {
            val curAngle = baseAngle + (s * (arcSpan / steps))
            val curR = radius * (0.95f + sin(s * 1.5f + shimmer * 10f) * 0.08f)
            val nx = center.x + curR * cos(Math.toRadians(curAngle.toDouble())).toFloat()
            val ny = center.y + curR * sin(Math.toRadians(curAngle.toDouble())).toFloat()
            boltPath.lineTo(nx, ny)
            cx = nx
            cy = ny
        }

        // Soft outer electric bloom
        drawPath(
            path = boltPath,
            color = aura.primaryColor.copy(alpha = 0.55f * intensity),
            style = Stroke(width = 3.5f, cap = StrokeCap.Round),
            blendMode = BlendMode.Plus
        )
        // Sharp white ionization core
        drawPath(
            path = boltPath,
            color = Color.White.copy(alpha = 0.90f * intensity),
            style = Stroke(width = 1.2f, cap = StrokeCap.Round),
            blendMode = BlendMode.Plus
        )

        // Spark sparklet at tip
        drawCircle(
            color = Color.White,
            radius = 1.8f,
            center = Offset(cx, cy),
            blendMode = BlendMode.Plus
        )
    }
}

/**
 * 🔥 Refined Solar Embers: Gentle glowing embers floating upward with natural air currents
 */
private fun DrawScope.drawRefinedSolarEmbers(
    center: Offset,
    radius: Float,
    aura: ElementalAura,
    loopTime: Float,
    embers: List<EmberParticle>,
    intensity: Float
) {
    // Subtle top warm crown glow (seamless radial gradient)
    val crownCenter = center - Offset(0f, radius * 0.7f)
    val crownRadius = radius * 0.85f
    drawCircle(
        brush = Brush.radialGradient(
            colorStops = arrayOf(
                0.0f to aura.secondaryColor.copy(alpha = 0.12f * intensity),
                0.50f to aura.primaryColor.copy(alpha = 0.04f * intensity),
                1.0f to Color.Transparent
            ),
            center = crownCenter,
            radius = crownRadius
        ),
        center = crownCenter,
        radius = crownRadius,
        blendMode = BlendMode.Plus
    )

    // Upward drifting embers
    embers.forEachIndexed { idx, ember ->
        val progress = (loopTime * ember.speed + ember.phase) % 1f
        val currentAngle = ember.startAngle + sin(progress * 6.28f * ember.swayFreq) * 15f
        val angleRad = Math.toRadians(currentAngle.toDouble())

        val r = radius * (ember.radiusScale + progress * 0.35f)
        val px = center.x + (r * cos(angleRad)).toFloat()
        val py = (center.y + (r * sin(angleRad)).toFloat()) - (progress * radius * 0.6f) // Ascend upward

        val lifeFade = sin(progress * Math.PI.toFloat()).coerceIn(0f, 1f)
        val alpha = lifeFade * 0.85f * intensity
        val emberColor = if (idx % 2 == 0) aura.primaryColor else aura.secondaryColor

        // Soft halo
        drawCircle(
            color = emberColor.copy(alpha = alpha * 0.5f),
            radius = ember.size * 2f,
            center = Offset(px, py),
            blendMode = BlendMode.Plus
        )
        // Bright core
        drawCircle(
            color = Color(0xFFFFF3E0).copy(alpha = alpha),
            radius = ember.size * 0.7f,
            center = Offset(px, py),
            blendMode = BlendMode.Plus
        )
    }
}

/**
 * 🌌 Refined Cosmic Nebula: Atmospheric starlight, soft cosmic mist puffs, zero hard circular boundaries
 */
private fun DrawScope.drawRefinedCosmicNebula(
    center: Offset,
    radius: Float,
    aura: ElementalAura,
    loopTime: Float,
    stars: List<CosmicStarParticle>,
    intensity: Float
) {
    // Soft orbital nebula cloud puffs (smooth radial gradients with NO outer edges)
    val cloudCount = 3
    for (c in 0 until cloudCount) {
        val cloudAngle = (c * (360f / cloudCount) + loopTime * 90f) % 360f
        val cloudRad = Math.toRadians(cloudAngle.toDouble())
        val cloudDistance = radius * 1.05f
        val cloudCenter = Offset(
            center.x + (cloudDistance * cos(cloudRad)).toFloat(),
            center.y + (cloudDistance * sin(cloudRad)).toFloat()
        )
        val puffRadius = radius * 0.65f

        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to aura.primaryColor.copy(alpha = 0.08f * intensity),
                    0.5f to aura.secondaryColor.copy(alpha = 0.03f * intensity),
                    1.0f to Color.Transparent
                ),
                center = cloudCenter,
                radius = puffRadius
            ),
            center = cloudCenter,
            radius = puffRadius,
            blendMode = BlendMode.Plus
        )
    }

    // Gentle twinkling starlight
    stars.forEach { star ->
        val currentAngle = (star.baseAngle + loopTime * 360f * star.orbitSpeed) % 360f
        val angleRad = Math.toRadians(currentAngle.toDouble())
        val r = radius * star.radiusRatio
        val sx = center.x + (r * cos(angleRad)).toFloat()
        val sy = center.y + (r * sin(angleRad)).toFloat()

        val twinkle = (0.5f + 0.5f * sin((loopTime * 360f * star.twinkleSpeed + star.phase * 360f) * (Math.PI.toFloat() / 180f))).coerceIn(0.2f, 1f)
        val alpha = twinkle * 0.9f * intensity

        // Micro cross flare for stars
        drawLine(
            color = Color.White.copy(alpha = alpha * 0.7f),
            start = Offset(sx - star.size * 2f, sy),
            end = Offset(sx + star.size * 2f, sy),
            strokeWidth = 0.8f,
            blendMode = BlendMode.Plus
        )
        drawLine(
            color = Color.White.copy(alpha = alpha * 0.7f),
            start = Offset(sx, sy - star.size * 2f),
            end = Offset(sx, sy + star.size * 2f),
            strokeWidth = 0.8f,
            blendMode = BlendMode.Plus
        )
        drawCircle(
            color = Color.White.copy(alpha = alpha),
            radius = star.size * 0.6f,
            center = Offset(sx, sy),
            blendMode = BlendMode.Plus
        )
    }
}

/**
 * 🌸 Refined Sakura Wind: Delicate cherry blossom petals gracefully floating in the breeze
 */
private fun DrawScope.drawRefinedSakuraWind(
    center: Offset,
    radius: Float,
    aura: ElementalAura,
    loopTime: Float,
    petals: List<SakuraPetalParticle>,
    intensity: Float
) {
    petals.forEach { petal ->
        val progress = (loopTime * petal.speed + petal.phase) % 1f
        val angle = (petal.baseAngle + loopTime * 140f) % 360f
        val angleRad = Math.toRadians(angle.toDouble())

        val r = radius * (petal.distance + sin(progress * 3.14f) * 0.18f)
        val px = center.x + (r * cos(angleRad)).toFloat()
        val py = center.y + (r * sin(angleRad)).toFloat()

        val rotation = (progress * 360f * petal.spinSpeed + petal.phase * 180f)
        val lifeFade = sin(progress * Math.PI.toFloat()).coerceIn(0f, 1f)
        val alpha = lifeFade * 0.85f * intensity

        rotate(degrees = rotation, pivot = Offset(px, py)) {
            val petalPath = Path().apply {
                val s = petal.scale
                moveTo(px, py - s)
                cubicTo(px + s * 0.7f, py - s * 0.5f, px + s * 0.7f, py + s * 0.5f, px, py + s)
                cubicTo(px - s * 0.7f, py + s * 0.5f, px - s * 0.7f, py - s * 0.5f, px, py - s)
                close()
            }
            drawPath(
                path = petalPath,
                color = aura.primaryColor.copy(alpha = alpha * 0.75f),
                blendMode = BlendMode.Plus
            )
            drawCircle(
                color = Color.White.copy(alpha = alpha * 0.5f),
                radius = petal.scale * 0.25f,
                center = Offset(px, py),
                blendMode = BlendMode.Plus
            )
        }
    }
}

private data class EmberParticle(
    val startAngle: Float,
    val speed: Float,
    val radiusScale: Float,
    val size: Float,
    val swayFreq: Float,
    val phase: Float
)

private data class CosmicStarParticle(
    val baseAngle: Float,
    val orbitSpeed: Float,
    val radiusRatio: Float,
    val size: Float,
    val twinkleSpeed: Float,
    val phase: Float
)

private data class SakuraPetalParticle(
    val baseAngle: Float,
    val speed: Float,
    val distance: Float,
    val scale: Float,
    val spinSpeed: Float,
    val phase: Float
)
