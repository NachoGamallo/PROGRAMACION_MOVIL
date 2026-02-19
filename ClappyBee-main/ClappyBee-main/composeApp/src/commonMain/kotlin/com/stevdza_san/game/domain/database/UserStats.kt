package com.stevdza_san.game.domain.database

import androidx.room.Database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.RoomDatabase

@Entity(tableName = "user_stats")
data class UserStats(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val score: Int,            // Número de tuberías pasadas
    val shieldsCollected: Int, // Escudos recolectados
    val timestamp: Long // Fecha en milliseconds
)


