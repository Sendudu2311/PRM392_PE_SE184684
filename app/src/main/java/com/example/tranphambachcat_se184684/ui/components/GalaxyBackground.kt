package com.example.tranphambachcat_se184684.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.sin
import kotlin.random.Random

/**
 * Galaxy background with animated stars and nebula effects
 * Inspired by SkillVerse AI Roadmap design
 */
@Composable
fun GalaxyBackground(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "galaxy_animation")

    // Twinkle animation for stars
    val twinkleAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(5000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_twinkle"
    )

    // Nebula animation
    val nebulaOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "nebula_float"
    )

    Box(modifier = modifier.fillMaxSize()) {
        // Deep space gradient background
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF0F172A), // Deep Navy
                            Color(0xFF050510)  // Ultra dark
                        ),
                        center = Offset(0.5f, 0.3f),
                        radius = 1200f
                    )
                )
        )

        // Nebula layers with blur effect (simulated with gradients)
        NebulaLayer(nebulaOffset)

        // Star field
        StarField(twinkleAlpha)

        // Content on top
        content()
    }
}

@Composable
private fun NebulaLayer(animationOffset: Float) {
    // Purple Nebula
    Canvas(modifier = Modifier.fillMaxSize()) {
        val purpleNebula = Brush.radialGradient(
            colors = listOf(
                Color(0x594C1D95), // Purple with alpha 0.35
                Color.Transparent
            ),
            center = Offset(
                size.width * (0.7f + sin(animationOffset * Math.PI.toFloat()) * 0.1f),
                size.height * 0.3f
            ),
            radius = 300f
        )

        drawRect(purpleNebula)
    }

    // Cyan Nebula
    Canvas(modifier = Modifier.fillMaxSize()) {
        val cyanNebula = Brush.radialGradient(
            colors = listOf(
                Color(0x3306B6D4), // Cyan with alpha 0.2
                Color.Transparent
            ),
            center = Offset(
                size.width * 0.3f,
                size.height * (0.7f + sin((animationOffset + 0.5f) * Math.PI.toFloat()) * 0.1f)
            ),
            radius = 250f
        )

        drawRect(cyanNebula)
    }

    // Indigo Nebula
    Canvas(modifier = Modifier.fillMaxSize()) {
        val indigoNebula = Brush.radialGradient(
            colors = listOf(
                Color(0x406366F1), // Indigo with alpha 0.25
                Color.Transparent
            ),
            center = Offset(
                size.width * 0.5f,
                size.height * 0.5f
            ),
            radius = 350f
        )

        drawRect(indigoNebula)
    }
}

@Composable
private fun StarField(twinkleAlpha: Float) {
    // Generate static star positions (remember across recompositions)
    val stars = remember {
        List(150) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                size = Random.nextFloat() * 2f + 0.5f,
                brightness = Random.nextFloat() * 0.5f + 0.5f
            )
        }
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        stars.forEach { star ->
            drawStar(star, twinkleAlpha)
        }
    }
}

private data class Star(
    val x: Float,
    val y: Float,
    val size: Float,
    val brightness: Float
)

private fun DrawScope.drawStar(star: Star, twinkleAlpha: Float) {
    val alpha = (star.brightness * twinkleAlpha).coerceIn(0f, 1f)

    // Main star
    drawCircle(
        color = Color.White.copy(alpha = alpha),
        radius = star.size,
        center = Offset(size.width * star.x, size.height * star.y)
    )

    // Glow effect (larger, more transparent circle)
    if (star.size > 1.5f) {
        drawCircle(
            color = Color(0xFF818CF8).copy(alpha = alpha * 0.3f),
            radius = star.size * 2f,
            center = Offset(size.width * star.x, size.height * star.y)
        )
    }
}
