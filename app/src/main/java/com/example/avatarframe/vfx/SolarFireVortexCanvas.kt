package com.example.avatarframe.vfx

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import kotlin.math.*
import kotlin.random.Random

/**
 * 🔥 SolarFireVortexCanvas: Vórtice de Fuego Solar y Lenguas de Llama Ígneas.
 * Sin ningún círculo tosco o disco oscuro de fondo: las lenguas de fuego se generan
 * de forma puramente orgánica con curvas Bézier cúbicas que emergen naturalmente
 * desde atrás del contorno físico del marco (adaptativo vía frameRadiusRatio).
 */
@Composable
fun SolarFireVortexCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFFFF3D00),
    secondaryColor: Color = Color(0xFFFF9100),
    glowColor: Color = Color(0xFFFFD600),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "SolarFireEngine")

    // Rotación suave del vórtice de llamas
    val vortexSpin by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "VortexSpin"
    )

    // Agitación y turbulencia rápida de las lenguas de llama viva (30-60fps)
    val flameFlicker by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "FlameFlicker"
    )

    // Pulso térmico de resplandor
    val thermalBreath by infiniteTransition.animateFloat(
        initialValue = 0.90f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ThermalBreath"
    )

    val embers = remember {
        val rand = Random(707)
        List(26) {
            FireEmber(
                baseAngle = rand.nextFloat() * 360f,
                speed = 0.5f + rand.nextFloat() * 0.8f,
                initialRadius = 0.96f + rand.nextFloat() * 0.38f,
                size = 1.0f + rand.nextFloat() * 2.2f,
                swayFreq = 2.0f + rand.nextFloat() * 2.5f,
                phase = rand.nextFloat() * 2 * PI.toFloat()
            )
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor térmico difuso y continuo (sin discos ni anillos oscuros)
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to glowColor.copy(alpha = 0.14f * intensity * thermalBreath),
                    0.45f to primaryColor.copy(alpha = 0.08f * intensity * thermalBreath),
                    0.85f to secondaryColor.copy(alpha = 0.02f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = size.minDimension * 0.65f
            ),
            center = center,
            radius = size.minDimension * 0.65f
        )

        // 2. 14 Lenguas de Fuego Bézier que lamen el contorno del marco sin bordes circulares
        val tongueCount = 14
        for (t in 0 until tongueCount) {
            val baseAngle = (t * (360f / tongueCount) + vortexSpin) * PI / 180.0
            val dynamicTurbulence = sin(flameFlicker * 3.5f + t * 1.7f)
            val flameHeight = (14.dp.toPx() + 11.dp.toPx() * dynamicTurbulence) * thermalBreath
            val baseSpread = 0.16 // radianes

            // Puntos de anclaje que nacen ligeramente dentro del borde del marco para una fusión perfecta
            val baseR = frameRadius * 0.94f
            val pLeft = Offset(
                center.x + (baseR * cos(baseAngle - baseSpread)).toFloat(),
                center.y + (baseR * sin(baseAngle - baseSpread)).toFloat()
            )
            val pRight = Offset(
                center.x + (baseR * cos(baseAngle + baseSpread)).toFloat(),
                center.y + (baseR * sin(baseAngle + baseSpread)).toFloat()
            )

            // Punta curvada por el viento centrífugo del vórtice
            val tipAngle = baseAngle + 0.14 * sin(flameFlicker * 2.2f + t)
            val tipR = frameRadius + flameHeight
            val pTip = Offset(
                center.x + (tipR * cos(tipAngle)).toFloat(),
                center.y + (tipR * sin(tipAngle)).toFloat()
            )

            // Puntos de control Bézier para curvatura orgánica de llamarada
            val ctrl1 = Offset(
                center.x + ((frameRadius + flameHeight * 0.45f) * cos(baseAngle - baseSpread * 0.35)).toFloat(),
                center.y + ((frameRadius + flameHeight * 0.45f) * sin(baseAngle - baseSpread * 0.35)).toFloat()
            )
            val ctrl2 = Offset(
                center.x + ((frameRadius + flameHeight * 0.45f) * cos(baseAngle + baseSpread * 0.35)).toFloat(),
                center.y + ((frameRadius + flameHeight * 0.45f) * sin(baseAngle + baseSpread * 0.35)).toFloat()
            )

            val flamePath = Path().apply {
                moveTo(pLeft.x, pLeft.y)
                quadraticBezierTo(ctrl1.x, ctrl1.y, pTip.x, pTip.y)
                quadraticBezierTo(ctrl2.x, ctrl2.y, pRight.x, pRight.y)
                close()
            }

            // Capa exterior de fuego (carmesí / ámbar)
            drawPath(
                path = flamePath,
                brush = Brush.radialGradient(
                    listOf(
                        glowColor.copy(alpha = 0.80f * intensity),
                        primaryColor.copy(alpha = 0.45f * intensity),
                        Color.Transparent
                    ),
                    center = pTip,
                    radius = flameHeight * 1.6f
                ),
                blendMode = BlendMode.Plus
            )

            // Filamento central incandescente blanco-dorado
            drawLine(
                brush = Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.92f), Color.Transparent),
                    start = Offset(
                        center.x + (frameRadius * cos(baseAngle)).toFloat(),
                        center.y + (frameRadius * sin(baseAngle)).toFloat()
                    ),
                    end = pTip
                ),
                start = Offset(
                    center.x + (frameRadius * cos(baseAngle)).toFloat(),
                    center.y + (frameRadius * sin(baseAngle)).toFloat()
                ),
                end = pTip,
                strokeWidth = 1.3.dp.toPx(),
                blendMode = BlendMode.Plus
            )
        }

        // 3. Micro-ascuas flotantes con turbulencia térmica
        embers.forEach { ember ->
            val angle = ember.baseAngle * PI / 180.0 + (vortexSpin * ember.speed * 0.018)
            val r = frameRadius * ember.initialRadius + sin(flameFlicker * ember.swayFreq + ember.phase) * 7.dp.toPx()
            val pos = Offset(
                center.x + (r * cos(angle)).toFloat(),
                center.y + (r * sin(angle)).toFloat()
            )

            val emberAlpha = (0.35f + 0.65f * abs(sin(flameFlicker * 2.5f + ember.phase))) * intensity

            // Halo suave
            drawCircle(
                color = secondaryColor.copy(alpha = 0.45f * emberAlpha),
                center = pos,
                radius = ember.size.dp.toPx() * 1.6f,
                blendMode = BlendMode.Plus
            )
            // Núcleo brillante
            drawCircle(
                color = Color.White.copy(alpha = emberAlpha),
                center = pos,
                radius = ember.size.dp.toPx() * 0.6f,
                blendMode = BlendMode.Plus
            )
        }
    }
}

private data class FireEmber(
    val baseAngle: Float,
    val speed: Float,
    val initialRadius: Float,
    val size: Float,
    val swayFreq: Float,
    val phase: Float
)
