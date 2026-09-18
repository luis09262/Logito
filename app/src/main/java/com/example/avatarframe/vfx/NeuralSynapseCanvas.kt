package com.example.avatarframe.vfx

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.dp
import kotlin.math.*

/**
 * Living Harmonic Neural Network & Synapse Simulation (Medium / Harmonious Pace):
 * - Gentle drifting neural soma nodes with smooth harmonic kinematics
 * - Synaptic action potential pulses traveling smoothly at a comfortable medium pace
 * - Synaptic firing blooms when electrical pulses arrive at destination nodes
 * - Bio-luminescent neurotransmitter micro-sparks floating organically
 */
@Composable
fun NeuralSynapseCanvas(
    modifier: Modifier = Modifier,
    frameRadiusFraction: Float = 0.36f,
    primaryColor: Color = Color(0xFFFFD700),
    secondaryColor: Color = Color(0xFF00E5FF),
    glowColor: Color = Color(0xFF80D8FF),
    intensity: Float = 1.0f,
    isCelestialTheme: Boolean = true
) {
    val infiniteTransition = rememberInfiniteTransition(label = "NeuralNetworkEngine")

    // Continuous time ticker tuned to a smooth, relaxed, medium pace
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 280_000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "NeuralTime"
    )

    // Breathing pulse for ambient field
    val ambientPulse by infiniteTransition.animateFloat(
        initialValue = 0.90f,
        targetValue = 1.10f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "AmbientPulse"
    )

    // Neural Nodes Configuration around the avatar perimeter
    val neuralNodes = remember {
        listOf(
            // Left Hemisphere Synaptic Clusters
            NeuralNodeConfig(baseAngle = 145f, baseRadiusMultiplier = 1.28f, freqX = 0.5f, freqY = 0.6f, amp = 0.04f, size = 3.2f),
            NeuralNodeConfig(baseAngle = 170f, baseRadiusMultiplier = 1.45f, freqX = 0.7f, freqY = 0.5f, amp = 0.05f, size = 2.8f),
            NeuralNodeConfig(baseAngle = 195f, baseRadiusMultiplier = 1.34f, freqX = 0.4f, freqY = 0.7f, amp = 0.04f, size = 3.5f),
            NeuralNodeConfig(baseAngle = 220f, baseRadiusMultiplier = 1.48f, freqX = 0.6f, freqY = 0.5f, amp = 0.05f, size = 2.6f),
            NeuralNodeConfig(baseAngle = 245f, baseRadiusMultiplier = 1.30f, freqX = 0.5f, freqY = 0.7f, amp = 0.04f, size = 3.0f),

            // Right Hemisphere Synaptic Clusters
            NeuralNodeConfig(baseAngle = 35f, baseRadiusMultiplier = 1.30f, freqX = 0.6f, freqY = 0.5f, amp = 0.04f, size = 3.2f),
            NeuralNodeConfig(baseAngle = 10f, baseRadiusMultiplier = 1.46f, freqX = 0.5f, freqY = 0.7f, amp = 0.05f, size = 2.8f),
            NeuralNodeConfig(baseAngle = 345f, baseRadiusMultiplier = 1.35f, freqX = 0.7f, freqY = 0.4f, amp = 0.04f, size = 3.6f),
            NeuralNodeConfig(baseAngle = 320f, baseRadiusMultiplier = 1.48f, freqX = 0.5f, freqY = 0.6f, amp = 0.05f, size = 2.5f),
            NeuralNodeConfig(baseAngle = 295f, baseRadiusMultiplier = 1.28f, freqX = 0.7f, freqY = 0.6f, amp = 0.04f, size = 3.0f),

            // Top Crown & Bottom Interlink Bridge Nodes
            NeuralNodeConfig(baseAngle = 90f, baseRadiusMultiplier = 1.42f, freqX = 0.5f, freqY = 0.4f, amp = 0.03f, size = 2.4f),
            NeuralNodeConfig(baseAngle = 270f, baseRadiusMultiplier = 1.40f, freqX = 0.4f, freqY = 0.5f, amp = 0.03f, size = 2.4f)
        )
    }

    // Axon Synaptic Pathways (Connected pairs with harmonious medium pulse velocity)
    val axonConnections = remember {
        listOf(
            // Left Hemisphere Axon Chain & Cross-links
            AxonConnection(0, 1, speed = 0.22f, phase = 0.0f),
            AxonConnection(1, 2, speed = 0.25f, phase = 0.35f),
            AxonConnection(2, 3, speed = 0.20f, phase = 0.70f),
            AxonConnection(3, 4, speed = 0.26f, phase = 0.15f),
            AxonConnection(0, 2, speed = 0.18f, phase = 0.50f),
            AxonConnection(2, 4, speed = 0.19f, phase = 0.85f),

            // Right Hemisphere Axon Chain & Cross-links
            AxonConnection(5, 6, speed = 0.24f, phase = 0.10f),
            AxonConnection(6, 7, speed = 0.22f, phase = 0.45f),
            AxonConnection(7, 8, speed = 0.26f, phase = 0.80f),
            AxonConnection(8, 9, speed = 0.20f, phase = 0.25f),
            AxonConnection(5, 7, speed = 0.19f, phase = 0.60f),
            AxonConnection(7, 9, speed = 0.18f, phase = 0.95f),

            // Bridge Synapses (Crown & Base interconnections)
            AxonConnection(0, 10, speed = 0.16f, phase = 0.20f),
            AxonConnection(5, 10, speed = 0.16f, phase = 0.70f),
            AxonConnection(4, 11, speed = 0.15f, phase = 0.40f),
            AxonConnection(9, 11, speed = 0.15f, phase = 0.90f)
        )
    }

    // Micro neurotransmitter floating sparks
    val neurotransmitters = remember {
        List(14) { index ->
            NeurotransmitterSpark(
                seedAngle = (index * (360f / 14f)),
                speed = 0.06f + (index % 4) * 0.03f,
                radialDistance = 1.25f + (index % 5) * 0.06f,
                wobblePhase = index * 0.8f,
                size = 1.4f + (index % 3) * 0.6f
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val baseRadius = min(size.width, size.height) * frameRadiusFraction

        if (baseRadius <= 0f) return@Canvas

        // 1. SUBTLE ATMOSPHERIC ENERGY BLOOM (Non-intrusive ambient halo)
        val ambientRadius = baseRadius * 1.35f * ambientPulse
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to secondaryColor.copy(alpha = 0.10f * intensity),
                    0.65f to glowColor.copy(alpha = 0.04f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = ambientRadius
            ),
            radius = ambientRadius,
            center = center
        )

        // 2. COMPUTE DYNAMIC LIVING NODE POSITIONS (Smooth Kinematic Drift)
        val calculatedNodePositions = neuralNodes.map { node ->
            val harmonicAngleRad = Math.toRadians((node.baseAngle + sin(time * node.freqX) * 2.0).toDouble()).toFloat()
            val dynamicRadius = baseRadius * (node.baseRadiusMultiplier + sin(time * node.freqY) * node.amp)
            Offset(
                x = center.x + dynamicRadius * cos(harmonicAngleRad),
                y = center.y + dynamicRadius * sin(harmonicAngleRad)
            )
        }

        // 3. DRAW AXON SYNAPTIC PATHWAYS & TRAVELING ACTION POTENTIAL PULSES
        for (axon in axonConnections) {
            val startPos = calculatedNodePositions.getOrNull(axon.startIndex) ?: continue
            val endPos = calculatedNodePositions.getOrNull(axon.endIndex) ?: continue

            val dx = endPos.x - startPos.x
            val dy = endPos.y - startPos.y

            // Dynamic Axon Filament (Breathes gently)
            val axonAlpha = (0.16f + 0.10f * sin(time * 0.8f + axon.phase * 6.28f)) * intensity
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(
                        secondaryColor.copy(alpha = axonAlpha * 0.6f),
                        glowColor.copy(alpha = axonAlpha),
                        primaryColor.copy(alpha = axonAlpha * 0.6f)
                    ),
                    start = startPos,
                    end = endPos
                ),
                start = startPos,
                end = endPos,
                strokeWidth = 1.1.dp.toPx()
            )

            // Synaptic Action Potential Pulse
            val pulseProgress = (time * axon.speed + axon.phase) % 1.0f
            val pulseX = startPos.x + dx * pulseProgress
            val pulseY = startPos.y + dy * pulseProgress
            val pulsePos = Offset(pulseX, pulseY)

            // Pulse trailing tail
            val trailProgress = (pulseProgress - 0.12f).coerceIn(0f, 1f)
            val trailPos = Offset(startPos.x + dx * trailProgress, startPos.y + dy * trailProgress)
            drawLine(
                brush = Brush.linearGradient(
                    colors = listOf(Color.Transparent, glowColor.copy(alpha = 0.8f * intensity)),
                    start = trailPos,
                    end = pulsePos
                ),
                start = trailPos,
                end = pulsePos,
                strokeWidth = 2.0.dp.toPx()
            )

            // Glowing Synaptic Pulse Head
            drawCircle(
                color = glowColor.copy(alpha = 0.5f * intensity),
                radius = 3.5.dp.toPx(),
                center = pulsePos
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.95f * intensity),
                radius = 1.4.dp.toPx(),
                center = pulsePos
            )
        }

        // 4. DRAW NEURAL SOMA NODES (Living Synapses with Harmonic Firing)
        for ((index, node) in neuralNodes.withIndex()) {
            val nodePos = calculatedNodePositions[index]
            val nodeSizePx = node.size.dp.toPx()

            // Harmonic firing flash (organic periodic activation at medium tempo)
            val firePhase = sin(time * 1.2f + index * 1.1f)
            val isFiring = firePhase > 0.65f
            val flashIntensity = if (isFiring) ((firePhase - 0.65f) / 0.35f) else 0f

            // Outer Synapse Aura Bloom
            val auraRadius = nodeSizePx * (2.2f + flashIntensity * 1.8f)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = (0.40f + flashIntensity * 0.45f) * intensity),
                        secondaryColor.copy(alpha = (0.15f + flashIntensity * 0.20f) * intensity),
                        Color.Transparent
                    ),
                    center = nodePos,
                    radius = auraRadius
                ),
                radius = auraRadius,
                center = nodePos
            )

            // Synapse Node Core
            drawCircle(
                color = primaryColor.copy(alpha = (0.75f + flashIntensity * 0.25f) * intensity),
                radius = nodeSizePx * (0.9f + flashIntensity * 0.3f),
                center = nodePos
            )

            // Center Pure White Ion Core
            drawCircle(
                color = Color.White.copy(alpha = (0.85f + flashIntensity * 0.15f) * intensity),
                radius = nodeSizePx * 0.45f,
                center = nodePos
            )
        }

        // 5. ORGANIC NEUROTRANSMITTER MICRO-SPARKS
        for (spark in neurotransmitters) {
            val curAngleRad = Math.toRadians((spark.seedAngle + time * spark.speed * 8f).toDouble()).toFloat()
            val wobble = sin(time * 1.0f + spark.wobblePhase) * 0.08f
            val sparkRadius = baseRadius * (spark.radialDistance + wobble)

            val sparkPos = Offset(
                x = center.x + sparkRadius * cos(curAngleRad),
                y = center.y + sparkRadius * sin(curAngleRad)
            )

            val sparkAlpha = (0.35f + 0.35f * sin(time * 1.5f + spark.wobblePhase)) * intensity

            drawCircle(
                color = glowColor.copy(alpha = sparkAlpha * 0.8f),
                radius = spark.size.dp.toPx(),
                center = sparkPos
            )
            drawCircle(
                color = Color.White.copy(alpha = sparkAlpha),
                radius = (spark.size * 0.5f).dp.toPx(),
                center = sparkPos
            )
        }
    }
}

private class NeuralNodeConfig(
    val baseAngle: Float,
    val baseRadiusMultiplier: Float,
    val freqX: Float,
    val freqY: Float,
    val amp: Float,
    val size: Float
)

private class AxonConnection(
    val startIndex: Int,
    val endIndex: Int,
    val speed: Float,
    val phase: Float
)

private class NeurotransmitterSpark(
    val seedAngle: Float,
    val speed: Float,
    val radialDistance: Float,
    val wobblePhase: Float,
    val size: Float
)
