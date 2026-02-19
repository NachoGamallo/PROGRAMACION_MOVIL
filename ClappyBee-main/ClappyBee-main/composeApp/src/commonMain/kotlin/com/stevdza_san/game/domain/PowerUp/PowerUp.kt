package com.stevdza_san.game.domain.PowerUp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class PowerUpType { SHIELD }

class PowerUp (

    val type: PowerUpType,
    initialX: Float,
    initialY: Float,
    val radius: Float = 25f)
{

    var x by mutableStateOf(initialX)
    var y by mutableStateOf(initialY)
    var isCollected by mutableStateOf(false)

    fun move(speed: Float){
        x -= speed
    }
}