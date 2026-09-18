package com.example.avatarframe.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * Category filter for Avatar Frames
 */
enum class FrameCategory(val title: String) {
    ALL("ALL"),
    ELEMENTAL("ELEMENTAL"),
    MLBB_HEROES("MLBB HEROES"),
    ANGEL_WINGS("ANGELIC WINGS"),
    DEMON_HORNS("HORNS & DEMONS"),
    COSMIC_CYBER("COSMIC & CYBER")
}

/**
 * Special Interactive Magic Features requested by user
 */
enum class FrameSpecialFeature {
    NONE,
    VFX_AURA_SWITCHER   // Selector de Auras Elementales Intercambiables
}

/**
 * Elemental Auras for the VFX Switcher
 */
enum class ElementalAura(
    val title: String,
    val icon: String,
    val primaryColor: Color,
    val secondaryColor: Color,
    val glowColor: Color
) {
    LIGHTNING("Arc Lightning", "⚡", Color(0xFF00E5FF), Color(0xFF2979FF), Color(0xFF80D8FF)),
    SOLAR_FIRE("Solar Embers", "🔥", Color(0xFFFF3D00), Color(0xFFFF9100), Color(0xFFFF6E40)),
    COSMIC_NEBULA("Cosmic Nebula", "🌌", Color(0xFFD500F9), Color(0xFF651FFF), Color(0xFFEA80FC)),
    CHERRY_BLOSSOM("Sakura Wind", "🌸", Color(0xFFFF4081), Color(0xFFFF80AB), Color(0xFFF8BBD0))
}

/**
 * Self-contained Model for an Avatar Frame
 */
data class FrameItem(
    val id: String,
    val name: String,
    val tier: String,
    val category: FrameCategory,
    @DrawableRes val frameRes: Int,
    val primaryColor: Color,
    val secondaryColor: Color,
    val glowColor: Color,
    val isCelestial: Boolean = true,
    val specialFeature: FrameSpecialFeature = FrameSpecialFeature.NONE,
    val evolutionTiers: List<Int> = emptyList(),
    val evolutionNames: List<String> = emptyList(),
    val frameRadiusRatio: Float = 0.43f
)

/**
 * Self-contained Model for Avatar Portraits
 */
data class AvatarPortrait(
    val id: String,
    val name: String,
    @DrawableRes val avatarRes: Int
)

