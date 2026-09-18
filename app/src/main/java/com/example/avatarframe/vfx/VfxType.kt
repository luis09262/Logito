package com.example.avatarframe.vfx

import androidx.compose.ui.graphics.Color

/**
 * 🌌 VfxType: Catálogo de Efectos Visuales Procedurales Modulares.
 * Permite equipar cualquier efecto a cualquier marco 3D sin ataduras.
 */
enum class VfxType(
    val id: String,
    val displayName: String,
    val subtitle: String,
    val icon: String,
    val defaultColor: Color
) {
    NONE(
        id = "none",
        displayName = "Sin Efecto",
        subtitle = "Marco Puro",
        icon = "🚫",
        defaultColor = Color(0xFF78909C)
    ),
    ARC_LIGHTNING(
        id = "arc_lightning",
        displayName = "Rayos Eléctricos",
        subtitle = "Arcos de Plasma Perimétricos",
        icon = "⚡",
        defaultColor = Color(0xFF00E5FF)
    ),
    SOLAR_FIRE_VORTEX(
        id = "solar_fire_vortex",
        displayName = "Vórtice de Fuego",
        subtitle = "Llamas Solares Orgánicas",
        icon = "🔥",
        defaultColor = Color(0xFFFF3D00)
    ),
    COSMIC_RUNES(
        id = "cosmic_runes",
        displayName = "Runas Astrales",
        subtitle = "Círculo de Invocación Místico",
        icon = "🌌",
        defaultColor = Color(0xFFD500F9)
    ),
    BLADE_SLASH_AURA(
        id = "blade_slash_aura",
        displayName = "Tajos de Espada",
        subtitle = "Cortes de Sable Cyber Anime",
        icon = "⚔️",
        defaultColor = Color(0xFF00E5FF)
    ),
    PRISMATIC_CRYSTALS(
        id = "prismatic_crystals",
        displayName = "Cristales Prismáticos",
        subtitle = "Gemas 3D e Iridiscencia",
        icon = "💎",
        defaultColor = Color(0xFF00E5FF)
    ),
    BLOOD_MOON_ECLIPSE(
        id = "blood_moon_eclipse",
        displayName = "Luna de Sangre",
        subtitle = "Corona de Eclipse y Miasma",
        icon = "🩸",
        defaultColor = Color(0xFFFF1744)
    ),
    HYPERSPACE_WARP(
        id = "hyperspace_warp",
        displayName = "Salto Hiperespacial",
        subtitle = "Velocidad Luz Radial 3D",
        icon = "🚀",
        defaultColor = Color(0xFF00E5FF)
    ),
    BIOLUMINESCENT_ABYSS(
        id = "bioluminescent_abyss",
        displayName = "Abismo Oceánico",
        subtitle = "Micro-Medusas Fluorescentes",
        icon = "🧬",
        defaultColor = Color(0xFF00E676)
    ),
    DIVINE_HALO_CROWN(
        id = "divine_halo_crown",
        displayName = "Corona Celestial",
        subtitle = "Mandala Sagrada y Solsticio",
        icon = "👑",
        defaultColor = Color(0xFFFFD700)
    ),
    DRAGON_SOUL(
        id = "dragon_soul",
        displayName = "Alma del Dragón",
        subtitle = "Fuego Espectral Esmeralda",
        icon = "🐉",
        defaultColor = Color(0xFF00E676)
    ),
    SAKURA_STORM(
        id = "sakura_storm",
        displayName = "Viento de Sakura",
        subtitle = "Tormenta de Pétalos 3D",
        icon = "🌸",
        defaultColor = Color(0xFFFF4081)
    ),
    GRAVITATIONAL_SINGULARITY(
        id = "gravitational_singularity",
        displayName = "Singularidad",
        subtitle = "Disco de Acreción Relativista",
        icon = "🕳️",
        defaultColor = Color(0xFF7C4DFF)
    ),
    NEURAL_SYNAPSE(
        id = "neural_synapse",
        displayName = "Sinapsis Neuronal",
        subtitle = "Red Bio-eléctrica Viva",
        icon = "🧠",
        defaultColor = Color(0xFF00E5FF)
    ),
    QUANTUM_ORBITALS(
        id = "quantum_orbitals",
        displayName = "Órbitas Cuánticas",
        subtitle = "Anillos de Singularidad",
        icon = "⚛️",
        defaultColor = Color(0xFF7C4DFF)
    ),
    CELESTIAL_STARDUST(
        id = "celestial_stardust",
        displayName = "Polvo Estelar",
        subtitle = "Constelaciones Astrales",
        icon = "✨",
        defaultColor = Color(0xFFFFD700)
    ),
    CYBER_MATRIX(
        id = "cyber_matrix",
        displayName = "Matriz Cyber HUD",
        subtitle = "Pulsos de Telemetría",
        icon = "🌐",
        defaultColor = Color(0xFF00E676)
    ),
    ARCANE_PLASMA(
        id = "arcane_plasma",
        displayName = "Plasma Arcano",
        subtitle = "Listones de Energía Fluida",
        icon = "🔮",
        defaultColor = Color(0xFFE040FB)
    ),
    SOLAR_CORONA(
        id = "solar_corona",
        displayName = "Corona Solar",
        subtitle = "Lazos Magnéticos y Fulgor",
        icon = "☀️",
        defaultColor = Color(0xFFFF6D00)
    ),
    CHRONO_AETHER(
        id = "chrono_aether",
        displayName = "Éter Cronológico",
        subtitle = "Geometría Sagrada Temporal",
        icon = "⏳",
        defaultColor = Color(0xFFFFAB00)
    ),
    FROST_CRYSTALS(
        id = "frost_crystals",
        displayName = "Glaciar Cúbico",
        subtitle = "Polvo de Diamante Ciro",
        icon = "❄️",
        defaultColor = Color(0xFF80D8FF)
    )
}
