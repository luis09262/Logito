package com.example.avatarframe.vfx

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * 🌌 VfxRenderer: Renderizador Central Modular de Efectos Visuales para Marcos de Avatar.
 * Desacopla la lógica procedural del marco y permite animar y renderizar cualquier efecto
 * a cualquier marco de manera independiente, adaptándose geométricamente con frameRadiusRatio.
 */
@Composable
fun VfxRenderer(
    vfxType: VfxType,
    primaryColor: Color,
    secondaryColor: Color,
    glowColor: Color,
    modifier: Modifier = Modifier,
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f
) {
    Box(modifier = modifier.fillMaxSize()) {
        Crossfade(
            targetState = vfxType,
            animationSpec = tween(350),
            label = "VfxTransition"
        ) { targetVfx ->
            when (targetVfx) {
                VfxType.NONE -> {
                    // Sin efecto visual añadido
                }
                VfxType.ARC_LIGHTNING -> {
                    ArcLightningCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.SOLAR_FIRE_VORTEX -> {
                    SolarFireVortexCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.COSMIC_RUNES -> {
                    CosmicRunesCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.BLADE_SLASH_AURA -> {
                    BladeSlashAuraCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.PRISMATIC_CRYSTALS -> {
                    PrismaticCrystalsCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.BLOOD_MOON_ECLIPSE -> {
                    BloodMoonEclipseCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.HYPERSPACE_WARP -> {
                    HyperspaceWarpCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.BIOLUMINESCENT_ABYSS -> {
                    BioluminescentAbyssCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.DIVINE_HALO_CROWN -> {
                    DivineHaloCrownCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.DRAGON_SOUL -> {
                    DragonSoulCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.SAKURA_STORM -> {
                    SakuraStormCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.GRAVITATIONAL_SINGULARITY -> {
                    GravitationalSingularityCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity,
                        frameRadiusRatio = frameRadiusRatio
                    )
                }
                VfxType.NEURAL_SYNAPSE -> {
                    NeuralSynapseCanvas(
                        modifier = Modifier.fillMaxSize(),
                        frameRadiusFraction = frameRadiusRatio * 0.9f,
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity
                    )
                }
                VfxType.QUANTUM_ORBITALS -> {
                    QuantumOrbitalsCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity
                    )
                }
                VfxType.CELESTIAL_STARDUST -> {
                    CelestialStardustCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity
                    )
                }
                VfxType.CYBER_MATRIX -> {
                    CyberMatrixCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity
                    )
                }
                VfxType.ARCANE_PLASMA -> {
                    ArcanePlasmaCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity
                    )
                }
                VfxType.SOLAR_CORONA -> {
                    SolarCoronaCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity
                    )
                }
                VfxType.CHRONO_AETHER -> {
                    ChronoAetherCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity
                    )
                }
                VfxType.FROST_CRYSTALS -> {
                    FrostCrystalsCanvas(
                        modifier = Modifier.fillMaxSize(),
                        primaryColor = primaryColor,
                        secondaryColor = secondaryColor,
                        glowColor = glowColor,
                        intensity = intensity
                    )
                }
            }
        }
    }
}
