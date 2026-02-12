package com.stevdza_san.game.domain.target.PowerUp

import androidx.compose.animation.core.Animatable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

class Shield(

    override val x: Float,
    override val y: Animatable<Float, *> = Animatable(0f),
    override val radius: Float = 40f,
    override val fallingSpeed: Float = 14f,
    override val color: Color = Color(0xFF03A9F4), //Azul
    override val time: Long = 10000

) : PowerUp
{

    override fun draw(drawScope: DrawScope) {
        with(drawScope){

            // Dibujamos la silueta de un escudo
            val path = Path().apply {
                moveTo(x, y.value - radius * 0.7f)
                quadraticBezierTo(x + radius, y.value - radius * 0.7f, x + radius, y.value)
                quadraticBezierTo(x + radius, y.value + radius * 0.7f, x, y.value + radius)
                quadraticBezierTo(x - radius, y.value + radius * 0.7f, x - radius, y.value)
                quadraticBezierTo(x - radius, y.value - radius * 0.7f, x, y.value - radius * 0.7f)
                close()
            }

            //Relleno suave y borde grueso.
            drawPath(path, color.copy(alpha = 0.3f))
            drawPath(path, color, style = Stroke(width = 5f))

            //Linea vertical interna.
            drawLine(
                color = color,
                start = Offset(x, y.value - radius * 0.3f),
                end = Offset(x, y.value + radius * 0.4f),
                strokeWidth = 4f
            )
        }
    }
}