package com.stevdza_san.game.domain.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor

@Database(entities = [UserStats::class], version = 1)
@ConstructedBy(UserStatsDatabaseConstructor::class)
abstract class UserStatsDatabase : RoomDatabase(){

    abstract fun userStatsDAO() : UserStatsDAO

}

//Implementacion de KMP
@Suppress("NO_ACTUAL_FOR_EXPECT")
expect object UserStatsDatabaseConstructor : RoomDatabaseConstructor<UserStatsDatabase> {
    override fun initialize(): UserStatsDatabase
}