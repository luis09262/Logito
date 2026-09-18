# ⚡ GUÍA MAESTRA: Sistema de Efectos Visuales Procedurales (VFX) para Marcos de Avatar en Android

Esta guía documenta de forma exhaustiva e independiente la **arquitectura modular de efectos visuales procedurales (VFX)** desarrollados a puro código con **Jetpack Compose Canvas**, sin depender de videos pre-renderizados ni archivos GIF/PNG pesados.

---

## 📑 ÍNDICE GENERAL

1. [Filosofía: Desacoplamiento de Marcos y Efectos](#1-filosofía-desacoplamiento-de-marcos-y-efectos)
2. [Corrección Crítica: Erradicación del Disco/Aro Negro Central](#2-corrección-crítica-erradicación-del-discoaro-negro-central)
3. [Sistema de Detección y Adaptación Geométrica (`frameRadiusRatio`)](#3-sistema-de-detección-y-adaptación-geométrica-frameradiusratio)
4. [Arquitectura Modular del Sistema VFX](#4-arquitectura-modular-del-sistema-vfx)
5. [Catálogo Completo de Efectos Procedurales (21 Efectos)](#5-catálogo-completo-de-efectos-procedurales-21-efectos)
6. [Guía Paso a Paso para Crear un Nuevo Efecto](#6-guía-paso-a-paso-para-crear-un-nuevo-efecto)
7. [Buenas Prácticas de Rendimiento (60-120 FPS en GPU)](#7-buenas-prácticas-de-rendimiento-60-120-fps-en-gpu)

---

## 1. Filosofía: Desacoplamiento de Marcos y Efectos

### El Problema Anterior
En versiones tempranas, ciertos efectos (como el plasma o las auras) estaban programados de forma rígida dentro de un marco específico (por ejemplo, *Elemental Chameleon*). Esto impedía:
- Equipar rayos eléctricos a marcos de *Mobile Legends* (como *Sun Wukong* o *Yu Zhong*).
- Poner fuego solar en las alas de *Seraph Archangel* o *Solar Phoenix*.
- Reutilizar, testear y optimizar cada efecto de forma aislada.

### La Solución Modular
Se separó la capa de presentación en **dos subsistemas independientes**:
```
┌────────────────────────────────────────────────────────┐
│               DivineAvatarFrame (Host)                 │
│                                                        │
│  [ Capa 0: Fondo / Resplandor Ambiental ]             │
│  [ Capa 1: VfxRenderer(vfxType, frameRadiusRatio) ]   │ <--- Sub-sistema VFX
│  [ Capa 2: Avatar Circular (Foto del Usuario) ]        │
│  [ Capa 3: Textura 3D del Marco (PNG transparente) ]   │ <--- Sub-sistema Marcos
│  [ Capa 4: Micro-destellos especulares frontales ]     │
└────────────────────────────────────────────────────────┘
```
Gracias a esta separación:
- **Cualquier marco** puede combinarse con **cualquier efecto** del catálogo.
- La lógica de animación vive en su propio paquete `com.example.avatarframe.vfx`.
- El marco informa su silueta física (`frameRadiusRatio`) para que el efecto se ciña a su contorno sin deformarse.

---

## 2. Corrección Crítica: Erradicación del Disco/Aro Negro Central

### 🔍 ¿Qué sucedía en los videos?
Al seleccionar ciertos efectos (como *Solar Fire Vortex* o *Arc Lightning*), se apreciaba un **disco o aro circular oscuro/negro** que rodeaba el marco, semejante a una "placa circular tosca o neumático oscuro" detrás de la foto.

### 🔬 Causa Raíz Técnica Identificada
En la versión inicial de los efectos se utilizaban dos patrones defectuosos en el `Canvas`:

1. **Gradientes Radiales con Escalones de Transición (Donuts)**:
   ```kotlin
   // ❌ CÓDIGO DEFECTUOSO ANTERIOR:
   Brush.radialGradient(
       colorStops = arrayOf(
           0.70f to Color.Transparent,  // <--- ¡Corte abrupto!
           0.82f to primaryColor.copy(alpha = 0.25f),
           0.92f to glowColor.copy(alpha = 0.60f),
           1.00f to Color.Transparent
       ),
       radius = frameRadius * 1.3f
   )
   ```
   **Por qué fallaba**: Al forzar un corte transparente en `0.70f` y elevar bruscamente la opacidad en `0.82f`, la interpolación sobre el fondo oscuro de la app creaba una "corona o dona" con una banda interna de sombra artificial perceptible.

2. **Líneas de Base Circulares Rígidas**:
   En el fuego se dibujaba una circunferencia o arco continuo con `drawArc()` o `drawCircle()` en la raíz de las llamas para darles "soporte", lo cual dibujaba un plato circular visible detrás de marcos con formas irregulares (alas o cuernos).

---

###  Solución Implementada y Blindaje Definitivo

1. **Resplandor Continuo de Caída Gaussiana Pura (Sin Saltos ni Discos)**:
   Se reemplazó cualquier gradiente con escalones por una **curva continua monótona descendente** desde el centro hacia el exterior:
   ```kotlin
   //  CÓDIGO OPTIMIZADO DEFINITIVO:
   drawCircle(
       brush = Brush.radialGradient(
           colorStops = arrayOf(
               0.00f to glowColor.copy(alpha = 0.12f * intensity * pulse),
               0.45f to primaryColor.copy(alpha = 0.06f * intensity * pulse),
               0.85f to secondaryColor.copy(alpha = 0.02f * intensity),
               1.00f to Color.Transparent // <--- Se disuelve suavemente al infinito
           ),
           center = center,
           radius = size.minDimension * 0.65f
       ),
       center = center,
       radius = size.minDimension * 0.65f
   )
   ```
   - **Resultado**: No existe ningún aro, ni borde interior, ni dona oscura. Es una atmósfera de luz difusa y suave que se funde perfectamente con cualquier fondo.

2. **Lenguas de Llama Orgánicas con Curvas Bézier Cúbicas (Sin Anillo Base)**:
   En `SolarFireVortexCanvas`, las llamas se generan con curvas Bézier cúbicas cuyos puntos de anclaje nacen ligeramente *adentro* del marco (`baseR = frameRadius * 0.94f`) y se proyectan hacia afuera mediante:
   ```kotlin
   val flamePath = Path().apply {
       moveTo(pLeft.x, pLeft.y)
       quadraticBezierTo(ctrl1.x, ctrl1.y, pTip.x, pTip.y)
       quadraticBezierTo(ctrl2.x, ctrl2.y, pRight.x, pRight.y)
       close()
   }
   ```
   Cada lengua tiene su propio gradiente individual enfocado en su punta (`pTip`), eliminando cualquier anillo de soporte circular.

---

## 3. Sistema de Detección y Adaptación Geométrica (`frameRadiusRatio`)

### 🎯 El Desafío de los Diferentes Tamaños y Siluetas
No todos los marcos de avatar tienen las mismas proporciones ni el mismo radio interior:
- **Marcos con Alas Grandes** (*Seraph Archangel*, *Solar Phoenix*, *Valkyrie*): El círculo interior donde va el avatar es más compacto (`~39-40%` del ancho total) porque las alas ocupan una gran extensión hacia los lados.
- **Marcos con Cuernos Altos** (*Sun Wukong*, *Nana*, *Vexana*, *Shadow Demon*): Requieren que los efectos abracen un radio de `40-41%`.
- **Marcos Circulares Compactos** (*Emerald Dragon*, *Archon Ignis*, *Abyssal Ouroboros*): Su cuerpo metálico es grueso y el radio óptimo ronda el `43-44%`.

Si usáramos un radio fijo de `0.43f` para todos:
- En marcos con alas grandes, los rayos y el fuego saldrían "flotando en el aire" a varios píxeles de distancia del marco.
- En marcos compactos, las llamas quedarían ocultas detrás del metal del marco.

---

### ⚙️ Implementación del Sistema Adaptativo

#### 1. Atributo en el Modelo de Datos (`AvatarFrameModels.kt`)
Se dotó a cada marco del atributo `frameRadiusRatio`:
```kotlin
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
    val frameRadiusRatio: Float = 0.43f // <--- Radio adaptativo específico del marco
)
```

#### 2. Calibración en el Catálogo (`AvatarFrameCatalog.kt`)
Cada marco tiene asignado su valor exacto según su morfología:
```kotlin
// Marcos con alas o cuernos anchos: radio más recogido para un abrazo perfecto
FrameItem(id = "sun_monkey_king", frameRadiusRatio = 0.40f, ...)
FrameItem(id = "nana_leonin",      frameRadiusRatio = 0.39f, ...)
FrameItem(id = "solar_phoenix",    frameRadiusRatio = 0.39f, ...)
FrameItem(id = "archangel_wings",  frameRadiusRatio = 0.40f, ...)
FrameItem(id = "infernal_molten",  frameRadiusRatio = 0.41f, ...)

// Marcos circulares estándar:
FrameItem(id = "emerald_dragon",   frameRadiusRatio = 0.44f, ...)
FrameItem(id = "ouroboros_dragon", frameRadiusRatio = 0.43f, ...)
```

#### 3. Propagación en Cascada (`DivineAvatarFrame.kt` ➔ `VfxRenderer.kt`)
El componente anfitrión extrae el ratio del marco activo y lo transfiere directamente al renderizador:
```kotlin
VfxRenderer(
    vfxType = selectedVfx,
    primaryColor = frameItem.primaryColor,
    secondaryColor = frameItem.secondaryColor,
    glowColor = frameItem.glowColor,
    modifier = Modifier.fillMaxSize(),
    intensity = 1.0f,
    frameRadiusRatio = frameItem.frameRadiusRatio // <--- Inyección dinámica
)
```

#### 4. Consumo en los Canvas Procedurales
Cada lienzo de efectos calcula su punto de inicio basándose en este valor:
```kotlin
val center = Offset(size.width / 2f, size.height / 2f)
val frameRadius = min(size.width, size.height) * frameRadiusRatio
```
**Resultado**: Sin importar si el marco mide `200.dp`, `320.dp` o si tiene alas de serafín o cuernos de demonio, los rayos, llamas y partículas se anclan milimétricamente al borde exterior del marco activo.

---

## 4. Arquitectura Modular del Sistema VFX

La estructura de archivos dentro del proyecto se encuentra organizada en `com.example.avatarframe.vfx`:

```
app/src/main/java/com/example/avatarframe/vfx/
├── VfxType.kt                     # Enum maestro con los 21 tipos de efectos y metadatos
├── VfxRenderer.kt                 # Coordinador con Crossfade que conmuta entre efectos
├── VfxSelectorBar.kt              # Barra de selección flotante interactiva
│
├── ArcLightningCanvas.kt          # ⚡ Rayos eléctricos fractales perimétricos
├── SolarFireVortexCanvas.kt       # 🔥 Llamas orgánicas Bézier y ascuas térmicas
├── CosmicRunesCanvas.kt           # 🌌 Círculo astral con 12 glifos matemáticos y constelaciones
├── BladeSlashAuraCanvas.kt        # ⚔️ Cortes de espada de energía y ráfagas anime
├── PrismaticCrystalsCanvas.kt     # 💎 Gemas 3D facetadas con aberración cromática
├── BloodMoonEclipseCanvas.kt      # 🩸 Eclipse carmesí, miasma y cenizas
├── HyperspaceWarpCanvas.kt        # 🚀 Salto hiperespacial radial en 3D
├── BioluminescentAbyssCanvas.kt   # 🧬 Micro-medusas fluorescentes y esporas marinas
├── DivineHaloCrownCanvas.kt       # 👑 Corona sagrada de 24 rayos y destellos solares
├── DragonSoulCanvas.kt            # 🐉 Fuego espectral ondulante en doble hélice
├── SakuraStormCanvas.kt           # 🌸 Pétalos de cerezo 3D con muesca anatómica
├── GravitationalSingularityCanvas.kt # 🕳️ Disco de acreción y horizonte de sucesos
├── NeuralSynapseCanvas.kt         # 🧠 Red bioeléctrica neuronal interactiva
├── QuantumOrbitalsCanvas.kt       # ⚛️ Anillos de singularidad cuántica
├── CelestialStardustCanvas.kt     # ✨ Polvo estelar y constelaciones pulsantes
├── CyberMatrixCanvas.kt           # 🌐 Arcos de telemetría y HUD cyber militar
├── ArcanePlasmaCanvas.kt          # 🔮 Cintas fluidas de plasma etéreo
├── SolarCoronaCanvas.kt           # ☀️ Lazos magnéticos y fulguraciones solares
├── ChronoAetherCanvas.kt          # ⏳ Geometría temporal y ritmos de reloj de arena
└── FrostCrystalsCanvas.kt         # ❄️ Cristales de hielo y fractales glaciares
```

---

## 5. Catálogo Completo de Efectos Procedurales (21 Efectos)

| # | Icono | Nombre (`VfxType`) | Descripción Visual | Fórmulas Matemáticas y Algoritmos Clave |
|---|---|---|---|---|
| 1 | ⚡ | `ARC_LIGHTNING` | Rayos de plasma vivo que reptan por la silueta | Desplazamiento fractal estocástico con `Random(seed)` sincronizado a 60fps; ramificaciones en árbol (forks); filamento central blanco en `BlendMode.Plus`. |
| 2 | 🔥 | `SOLAR_FIRE_VORTEX` | Lenguas de fuego solar y ascuas térmicas | Curvas Bézier cúbicas con turbulencia sinusoidal `sin(t * 3.5 + i)`; convección de partículas con oscilación lateral. |
| 3 | 🌌 | `COSMIC_RUNES` | Rueda astral de 12 glifos rúnicos ancestrales | Glifos vectoriales procedurales rotados sobre sus ejes (`rotate(angle + 90)`); líneas de constelación interconectadas; destellos estelares en cruz. |
| 4 | ⚔️ | `BLADE_SLASH_AURA` | Cortes de sable cyber katana a alta velocidad | Arcos dinámicos con progreso no lineal `(t / 1.4).coerceIn(0,1)`; estelas luminosas decrecientes en grosor; chispas de impacto radial. |
| 5 | 💎 | `PRISMATIC_CRYSTALS` | Gemas 3D facetadas con refractancia | Polígonos de facetas divididas (sombra izquierda / luz derecha); auto-rotación giroscópica; aberración cromática cian/magenta. |
| 6 | 🩸 | `BLOOD_MOON_ECLIPSE` | Eclipse de luna de sangre y miasma | 16 rayos estriados con pulso de corona; rotación lenta de miasma; partículas de ceniza ascendentes con desvanecimiento alfa. |
| 7 | 🚀 | `HYPERSPACE_WARP` | Salto hiperespacial a velocidad de la luz | Proyección 3D radial de estelas de luz (`startDist` acelerando a `endDist`); interpolación de longitud de estela en función de la velocidad. |
| 8 | 🧬 | `BIOLUMINESCENT_ABYSS` | Abismo oceánico con micro-medusas | Cúpulas de campana hidrodinámicas con curvas cuadráticas; filamentos de tentáculos ondulantes con fase de corriente marina; esporas de neón. |
| 9 | 👑 | `DIVINE_HALO_CROWN` | Corona celestial de solsticio dorado | 24 rayos radiantes con alternancia de longitud (rayos mayores/menores); destellos especulares en las puntas; respiración sagrada sinusoidal. |
| 10 | 🐉 | `DRAGON_SOUL` | Alma del dragón y fuego espectral esmeralda | Ondulación en doble hélice con `sin(wavePhase + p * 0.45)`; escamas romboidales escalonadas por el cuerpo; cabeza con halo concentrado. |
| 11 | 🌸 | `SAKURA_STORM` | Tormenta de pétalos de cerezo en 3D | Silueta anatómica de pétalo con muesca apical en la punta; rotación angular en 3 planos; viento con deriva sinusoidal y micro-polen. |
| 12 | 🕳️ | `GRAVITATIONAL_SINGULARITY` | Disco de acreción y distorsión gravitacional | 4 espirales hiperbólicas `r = frameRadius * (1.25 - progress * 0.25)`; anillo de fotones relativista; polvo gravitacional en caída. |
| 13 | 🧠 | `NEURAL_SYNAPSE` | Sinapsis neuronal bio-eléctrica | Grafo de nodos somáticos interconectados; potenciales de acción que viajan por los axones; destellos de sinapsis activos. |
| 14 | ⚛️ | `QUANTUM_ORBITALS` | Anillos giroscópicos cuánticos | 3 anillos elípticos inclinados en el espacio con oscilación armónica; electrones/quarks orbitando a velocidades relativistas. |
| 15 | ✨ | `CELESTIAL_STARDUST` | Polvo cósmico y constelaciones astrales | Nubes difusas de polvo de estrellas con atenuación cúbica; centelleo de micro-estrellas con fases aleatorias desfasadas. |
| 16 | 🌐 | `CYBER_MATRIX` | Interfaz táctica militar y HUD cibernético | Arcos de telemetría segmentados; retículas angulares; pulsos de barrido tipo radar con degradado angular. |
| 17 | 🔮 | `ARCANE_PLASMA` | Cintas de energía fluida mística | Listones continuos con degradados lineales multicolores; deformación fluida mediante superposición de armónicos sinusoidales. |
| 18 | ☀️ | `SOLAR_CORONA` | Fulguraciones magnéticas solares | Lazos de arco perimétrico que entran y salen del marco simulando protuberancias solares de plasma caliente. |
| 19 | ⏳ | `CHRONO_AETHER` | Geometría temporal sagrada | Círculos concéntricos divididos en cuadrantes temporales que giran en direcciones opuestas como engranajes de un reloj celestial. |
| 20 | ❄️ | `FROST_CRYSTALS` | Fractales de escarcha y polvo de diamante | Ramas de copo de nieve hexagonales que crecen y titilan con refracción de luz fría azul/blanca. |
| 21 | 🚫 | `NONE` | Marco puro sin efectos añadidos | Deja visible únicamente el marco 3D y la foto sin añadir capas en GPU. |

---

## 6. Guía Paso a Paso para Crear un Nuevo Efecto

Para añadir un nuevo efecto (ejemplo: `QUANTUM_PORTAL`), se siguen **3 pasos simples**:

### Paso 1: Registrar en `VfxType.kt`
```kotlin
enum class VfxType(...) {
    // ...
    QUANTUM_PORTAL(
        id = "quantum_portal",
        displayName = "Portal Cuántico",
        subtitle = "Vórtice Interdimensional",
        icon = "🌀",
        defaultColor = Color(0xFF00E5FF)
    )
}
```

### Paso 2: Crear el Composable Canvas en `com.example.avatarframe.vfx`
Crea el archivo `QuantumPortalCanvas.kt`:
```kotlin
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

@Composable
fun QuantumPortalCanvas(
    modifier: Modifier = Modifier,
    primaryColor: Color = Color(0xFF00E5FF),
    secondaryColor: Color = Color(0xFF7C4DFF),
    glowColor: Color = Color(0xFF80D8FF),
    intensity: Float = 1.0f,
    frameRadiusRatio: Float = 0.43f // <--- REGLA DE ORO: SIEMPRE RECIBIR frameRadiusRatio
) {
    val infiniteTransition = rememberInfiniteTransition(label = "PortalEngine")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "PortalRotation"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val frameRadius = min(size.width, size.height) * frameRadiusRatio

        if (frameRadius <= 0f) return@Canvas

        // 1. Resplandor difuso continuo (CERO DISCOS NEGROS)
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to glowColor.copy(alpha = 0.12f * intensity),
                    0.5f to primaryColor.copy(alpha = 0.05f * intensity),
                    1.0f to Color.Transparent
                ),
                center = center,
                radius = size.minDimension * 0.65f
            ),
            center = center,
            radius = size.minDimension * 0.65f
        )

        // 2. Tu geometría procedural personalizada anclada a frameRadius
        // ...
    }
}
```

### Paso 3: Enlazar en `VfxRenderer.kt`
Añade la rama en el `when`:
```kotlin
VfxType.QUANTUM_PORTAL -> {
    QuantumPortalCanvas(
        modifier = Modifier.fillMaxSize(),
        primaryColor = primaryColor,
        secondaryColor = secondaryColor,
        glowColor = glowColor,
        intensity = intensity,
        frameRadiusRatio = frameRadiusRatio
    )
}
```
¡Listo! La barra `VfxSelectorBar` detecta automáticamente la nueva entrada en `VfxType.entries` y la expone en la interfaz de usuario con su icono y colores sin requerir código adicional.

---

## 7. Buenas Prácticas de Rendimiento (60-120 FPS en GPU)

Para garantizar animaciones fluidas a 60-120 FPS sin calentar el dispositivo:

1. **Evitar Alojar Objetos en el Ciclo `onDraw`**:
   - ❌ **Mala práctica**: Hacer `List(30) { Particle(...) }` dentro del bloque `Canvas { ... }`. Esto provoca recolecciones de basura (GC pauses) constantes.
   -  **Buena práctica**: Generar las listas estáticas de partículas dentro de un `remember { ... }` fuera del canvas y consumir sus propiedades en cada cuadro.

2. **Uso de `BlendMode.Plus` para Iluminación Aditiva**:
   - En lugar de renderizar texturas pesadas con canales alfa complejos, dibujar líneas finas (`1.dp..3.dp`) y puntos en modo `BlendMode.Plus`. Los colores se suman matemáticamente en GPU (`C_res = C_src + C_dst`), logrando un brillo incandescente idéntico a los motores AAA (Unreal Engine / Unity).

3. **Reutilización de Objetos `Path`**:
   - Si la forma matemática es compleja pero repetitiva, usar transformaciones de matriz (`rotate`, `translate`, `scale`) provistas por el `DrawScope` en lugar de recalcular todas las coordenadas trigonométricas desde cero.

4. **Desvanecimiento Suave entre Estados con `Crossfade`**:
   - En `VfxRenderer`, el cambio de un efecto a otro está protegido con `Crossfade(animationSpec = tween(350))`, garantizando que la transición sea cinematográfica sin parpadeos bruscos.
