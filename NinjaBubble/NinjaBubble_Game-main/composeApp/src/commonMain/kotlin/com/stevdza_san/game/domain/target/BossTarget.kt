package com.stevdza_san.game.domain.target

import androidx.compose.animation.core.Animatable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Interval

data class BossTarget(

    override val x: Float = 0f,
    override val y: Animatable<Float, *> = Animatable(0f),
    override val radius: Float = 0f,
    override val fallingSpeed: Float = 0f,
    override val color: Color = Color.Magenta,
    val lives: Int = 10

):Target
