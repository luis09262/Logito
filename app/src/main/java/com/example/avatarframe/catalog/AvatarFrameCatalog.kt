package com.example.avatarframe.catalog

import androidx.compose.ui.graphics.Color
import com.example.R
import com.example.avatarframe.model.AvatarPortrait
import com.example.avatarframe.model.FrameCategory
import com.example.avatarframe.model.FrameItem
import com.example.avatarframe.model.FrameSpecialFeature

/**
 * Modular Catalog Repository:
 * Contains the complete collection of 3D Avatar Frames with Elemental Chameleon VFX Switcher.
 */
object AvatarFrameCatalog {

    val frames: List<FrameItem> = listOf(
        // ========================================================
        // ⚡ ELEMENTAL CHAMELEON (INTERACTIVE VFX AURA SWITCHER)
        // ========================================================
        FrameItem(
            id = "magic_elemental_switcher",
            name = "ELEMENTAL CHAMELEON",
            tier = "⚡ 4 INTERCHANGEABLE AURAS",
            category = FrameCategory.ELEMENTAL,
            frameRes = R.drawable.frame_elemental_conduit_1789610709285,
            primaryColor = Color(0xFF00E5FF),
            secondaryColor = Color(0xFFFF3D00),
            glowColor = Color(0xFFD500F9),
            specialFeature = FrameSpecialFeature.VFX_AURA_SWITCHER
        ),

        // ========================================================
        // 👑 MOBILE LEGENDS HEROES COLLECTION
        // ========================================================
        FrameItem(
            id = "sun_monkey_king",
            name = "SUN - MONKEY KING",
            tier = "MLBB MYTHIC COLLECTOR",
            category = FrameCategory.MLBB_HEROES,
            frameRes = R.drawable.frame_mlbb_sun_wukong_1789609426676,
            primaryColor = Color(0xFFFFD700),
            secondaryColor = Color(0xFFFF3D00),
            glowColor = Color(0xFFFFAB00),
            frameRadiusRatio = 0.40f
        ),
        FrameItem(
            id = "nana_leonin",
            name = "NANA - SWEET LEONIN",
            tier = "MLBB SPECIAL EDITION",
            category = FrameCategory.MLBB_HEROES,
            frameRes = R.drawable.frame_mlbb_nana_leonin_1789609847609,
            primaryColor = Color(0xFFFF4081),
            secondaryColor = Color(0xFFE040FB),
            glowColor = Color(0xFFFF80AB),
            frameRadiusRatio = 0.39f
        ),
        FrameItem(
            id = "yuzhong_dragon",
            name = "YU ZHONG - BLACK DRAGON",
            tier = "MLBB DRAGON EXORCIST",
            category = FrameCategory.MLBB_HEROES,
            frameRes = R.drawable.frame_mlbb_yuzhong_dragon_1789610161334,
            primaryColor = Color(0xFF9C27B0),
            secondaryColor = Color(0xFFFF1744),
            glowColor = Color(0xFFE040FB),
            frameRadiusRatio = 0.42f
        ),
        FrameItem(
            id = "vexana_queen",
            name = "VEXANA - NECRO QUEEN",
            tier = "MLBB CURSED EMPRESS",
            category = FrameCategory.MLBB_HEROES,
            frameRes = R.drawable.frame_mlbb_vexana_queen_1789609886655,
            primaryColor = Color(0xFF00E676),
            secondaryColor = Color(0xFF7C4DFF),
            glowColor = Color(0xFF69F0AE),
            frameRadiusRatio = 0.40f
        ),

        // ========================================================
        // ⚡ ELEMENTAL & MYTHIC
        // ========================================================
        FrameItem(
            id = "emerald_dragon",
            name = "EMERALD DRAGON",
            tier = "IMPERIAL JADE",
            category = FrameCategory.ELEMENTAL,
            frameRes = R.drawable.frame_emerald_dragon_1789375519067,
            primaryColor = Color(0xFFFFD700),
            secondaryColor = Color(0xFF00E676),
            glowColor = Color(0xFF69F0AE)
        ),
        FrameItem(
            id = "archon_ignis",
            name = "ARCHON IGNIS",
            tier = "SOLAR APEX",
            category = FrameCategory.ELEMENTAL,
            frameRes = R.drawable.frame_magma_fire_1789374249446,
            primaryColor = Color(0xFFFFB300),
            secondaryColor = Color(0xFFFF3D00),
            glowColor = Color(0xFFFF9E80)
        ),
        FrameItem(
            id = "thunder_raijin",
            name = "THUNDER RAIJIN",
            tier = "STORM GOD",
            category = FrameCategory.ELEMENTAL,
            frameRes = R.drawable.frame_thunder_raijin_1789376621588,
            primaryColor = Color(0xFF00E5FF),
            secondaryColor = Color(0xFF2979FF),
            glowColor = Color(0xFF82B1FF)
        ),
        FrameItem(
            id = "sakura_fox",
            name = "SAKURA KITSUNE",
            tier = "SPIRIT DEITY",
            category = FrameCategory.ELEMENTAL,
            frameRes = R.drawable.frame_sakura_fox_1789376672235,
            primaryColor = Color(0xFFFF80AB),
            secondaryColor = Color(0xFFFF4081),
            glowColor = Color(0xFFFF80AB)
        ),
        FrameItem(
            id = "ouroboros_dragon",
            name = "ABYSSAL OUROBOROS",
            tier = "MYTHIC DRAGON SERPENT",
            category = FrameCategory.ELEMENTAL,
            frameRes = R.drawable.frame_ouroboros_dragon_1789465933413,
            primaryColor = Color(0xFF00E5FF),
            secondaryColor = Color(0xFF1DE9B6),
            glowColor = Color(0xFF80D8FF)
        ),

        // ========================================================
        // 🪽 ANGELIC WINGS & CELESTIAL
        // ========================================================
        FrameItem(
            id = "archangel_wings",
            name = "SERAPH ARCHANGEL",
            tier = "HOLY ELYSIUM",
            category = FrameCategory.ANGEL_WINGS,
            frameRes = R.drawable.frame_archangel_wings_1789376601076,
            primaryColor = Color(0xFFFFD700),
            secondaryColor = Color(0xFF40C4FF),
            glowColor = Color(0xFF80D8FF),
            frameRadiusRatio = 0.40f
        ),
        FrameItem(
            id = "celestial_seraph",
            name = "CELESTIAL SERAPH",
            tier = "MYTHIC DIVINE",
            category = FrameCategory.ANGEL_WINGS,
            frameRes = R.drawable.frame_celestial_gold_1789374233057,
            primaryColor = Color(0xFFFFD700),
            secondaryColor = Color(0xFF00E5FF),
            glowColor = Color(0xFF80D8FF),
            frameRadiusRatio = 0.40f
        ),
        FrameItem(
            id = "fallen_angel",
            name = "FALLEN ANGEL",
            tier = "DARK ECLIPSE",
            category = FrameCategory.ANGEL_WINGS,
            frameRes = R.drawable.frame_fallen_angel_1789376653027,
            primaryColor = Color(0xFFE040FB),
            secondaryColor = Color(0xFF7C4DFF),
            glowColor = Color(0xFFB388FF),
            frameRadiusRatio = 0.40f
        ),
        FrameItem(
            id = "solar_phoenix",
            name = "SOLAR PHOENIX",
            tier = "INCANDESCENT GOLD",
            category = FrameCategory.ANGEL_WINGS,
            frameRes = R.drawable.frame_solar_phoenix_1789376053052,
            primaryColor = Color(0xFFFFD700),
            secondaryColor = Color(0xFFFF6D00),
            glowColor = Color(0xFFFFAB00),
            frameRadiusRatio = 0.39f
        ),
        FrameItem(
            id = "valkyrie_gold_wings",
            name = "DIVINE SERAPHIN",
            tier = "ARCHON GOLDEN WINGS",
            category = FrameCategory.ANGEL_WINGS,
            frameRes = R.drawable.frame_valkyrie_gold_wings_1789465947982,
            primaryColor = Color(0xFFFFD700),
            secondaryColor = Color(0xFF2979FF),
            glowColor = Color(0xFFFFE082),
            frameRadiusRatio = 0.40f
        ),

        // ========================================================
        // 😈 HORNS & DEMONS & DARK FORCES
        // ========================================================
        FrameItem(
            id = "infernal_molten",
            name = "INFERNAL MOLTEN",
            tier = "VOLCANIC OVERLORD",
            category = FrameCategory.DEMON_HORNS,
            frameRes = R.drawable.frame_infernal_molten_1789376103257,
            primaryColor = Color(0xFFFF3D00),
            secondaryColor = Color(0xFFFF9100),
            glowColor = Color(0xFFFF6E40),
            frameRadiusRatio = 0.41f
        ),
        FrameItem(
            id = "shadow_demon_horns",
            name = "SHADOW ARCHDEMON",
            tier = "ONYX ABYSSAL",
            category = FrameCategory.DEMON_HORNS,
            frameRes = R.drawable.frame_shadow_demon_horns_1789376611232,
            primaryColor = Color(0xFFD500F9),
            secondaryColor = Color(0xFF651FFF),
            glowColor = Color(0xFFEA80FC),
            frameRadiusRatio = 0.40f
        ),
        FrameItem(
            id = "cyber_demon",
            name = "CYBER MECHA DEMON",
            tier = "TITANIUM INFERNO",
            category = FrameCategory.DEMON_HORNS,
            frameRes = R.drawable.frame_cyber_demon_1789376662624,
            primaryColor = Color(0xFFFF1744),
            secondaryColor = Color(0xFFFF5252),
            glowColor = Color(0xFFFF8A80),
            frameRadiusRatio = 0.40f
        ),
        FrameItem(
            id = "necromancer_skull",
            name = "NECRO SKULL LORD",
            tier = "SOUL REAPER",
            category = FrameCategory.DEMON_HORNS,
            frameRes = R.drawable.frame_necromancer_skull_1789376682701,
            primaryColor = Color(0xFF00E676),
            secondaryColor = Color(0xFF69F0AE),
            glowColor = Color(0xFFB9F6CA),
            frameRadiusRatio = 0.40f
        ),
        FrameItem(
            id = "blood_reaper",
            name = "BLOOD REAPER",
            tier = "CRIMSON OBSIDIAN",
            category = FrameCategory.DEMON_HORNS,
            frameRes = R.drawable.frame_blood_reaper_1789375541014,
            primaryColor = Color(0xFFFF1744),
            secondaryColor = Color(0xFFD50000),
            glowColor = Color(0xFFFF5252),
            frameRadiusRatio = 0.41f
        ),
        FrameItem(
            id = "dark_lord_crown",
            name = "DARK LORD CROWN",
            tier = "VOID AMETHYST CROWN",
            category = FrameCategory.DEMON_HORNS,
            frameRes = R.drawable.frame_dark_lord_crown_1789465962575,
            primaryColor = Color(0xFFD500F9),
            secondaryColor = Color(0xFF7C4DFF),
            glowColor = Color(0xFFEA80FC),
            frameRadiusRatio = 0.40f
        ),

        // ========================================================
        // 🌌 COSMIC & CYBER & VOID
        // ========================================================
        FrameItem(
            id = "cosmic_nebula",
            name = "COSMIC NEBULA",
            tier = "GALAXY SINGULARITY",
            category = FrameCategory.COSMIC_CYBER,
            frameRes = R.drawable.frame_cosmic_nebula_1789376702083,
            primaryColor = Color(0xFF7C4DFF),
            secondaryColor = Color(0xFFE040FB),
            glowColor = Color(0xFFEA80FC)
        ),
        FrameItem(
            id = "void_sovereign",
            name = "VOID SOVEREIGN",
            tier = "ABYSSAL TITAN",
            category = FrameCategory.COSMIC_CYBER,
            frameRes = R.drawable.frame_void_amethyst_1789374265489,
            primaryColor = Color(0xFFE0E0E0),
            secondaryColor = Color(0xFF9C27B0),
            glowColor = Color(0xFFE040FB)
        ),
        FrameItem(
            id = "cyber_valkyrie",
            name = "CYBER VALKYRIE",
            tier = "ROSE TITANIUM",
            category = FrameCategory.COSMIC_CYBER,
            frameRes = R.drawable.frame_cyber_valkyrie_1789375551322,
            primaryColor = Color(0xFFFF4081),
            secondaryColor = Color(0xFFFF80AB),
            glowColor = Color(0xFFFF007F)
        ),
        FrameItem(
            id = "phantom_shadow",
            name = "PHANTOM SHADOW",
            tier = "SPECTRAL ASSASSIN",
            category = FrameCategory.COSMIC_CYBER,
            frameRes = R.drawable.frame_phantom_shadow_1789376071016,
            primaryColor = Color(0xFF00E5FF),
            secondaryColor = Color(0xFF1DE9B6),
            glowColor = Color(0xFF00B0FF)
        )
    )

    val avatars: List<AvatarPortrait> = listOf(
        AvatarPortrait("mystic", "Celestial Mage", R.drawable.avatar_celestial_mage_1789374278416),
        AvatarPortrait("ignis", "Archon Warrior", R.drawable.avatar_hero_portrait_1789373515813),
        AvatarPortrait("valkyrie", "Cyber Valkyrie", R.drawable.avatar_valkyrie_warrior_1789375569453)
    )
}
