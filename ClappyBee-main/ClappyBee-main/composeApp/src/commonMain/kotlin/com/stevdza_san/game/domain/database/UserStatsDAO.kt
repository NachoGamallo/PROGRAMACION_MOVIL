package com.stevdza_san.game.domain.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserStatsDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStat(stat: UserStats)

    @Query("SELECT * FROM user_stats ORDER BY score DESC LIMIT 1")
    suspend fun getBestScore(): UserStats?

    @Query("SELECT * FROM user_stats ORDER BY timestamp DESC")
    suspend fun getAllStats(): List<UserStats>

    @Query("SELECT SUM(shieldsCollected) FROM user_stats")
    suspend fun getTotalShields(): Int?

}
