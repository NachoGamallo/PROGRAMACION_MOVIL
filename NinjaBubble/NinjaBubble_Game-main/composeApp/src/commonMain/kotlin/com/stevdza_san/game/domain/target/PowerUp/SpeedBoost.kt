package com.stevdza_san.game.domain.target.PowerUp

import androidx.compose.animation.core.Animatable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope

class SpeedBoost (
    override val x: Float,
    override val y: Animatable<Float, *> = Animatable(0f),
    override val radius: Float = 35f,
    override val fallingSpeed: Float = 16f,
    override val color: Color = Color(0xFFFFEB3B), //Amarillo brillante
    override val time: Long = 10000
) : PowerUp
{
    override fun draw(drawScope: DrawScope) {
        with(drawScope){

            //Un brillo circular en el fondo.
            drawCircle(color.copy(alpha = 0.2f), radius = radius * 1.5f, center = Offset(x, y.value))

            // Dibujamos el icono del Rayo con un Path
            val path = Path().apply {
                moveTo(x + radius * 0.2f, y.value - radius * 0.8f)
                lineTo(x - radius * 0.4f, y.value + radius * 0.1f)
                lineTo(x + radius * 0.1f, y.value + radius * 0.1f)
                lineTo(x - radius * 0.2f, y.value + radius * 0.8f)
                lineTo(x + radius * 0.4f, y.value - radius * 0.1f)
                lineTo(x - radius * 0.1f, y.value - radius * 0.1f)
                close()
            }

            drawPath(path, color)

        }
    }
}