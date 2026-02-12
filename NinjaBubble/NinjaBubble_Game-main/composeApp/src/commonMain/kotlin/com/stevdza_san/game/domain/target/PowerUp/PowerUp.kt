package com.stevdza_san.game.domain.target.PowerUp

import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.lifecycle.ViewModel
import com.stevdza_san.game.domain.target.Target

interface PowerUp : Target {

    val time: Long
    fun draw(drawScope: DrawScope)

}

