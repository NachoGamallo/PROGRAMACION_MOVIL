package com.stevdza_san.game

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.stevdza_san.game.domain.database.UserStatsDatabase
import org.koin.dsl.module
import java.io.File

actual val targetModule = module {
    single {
        val dbFile = File(System.getProperty("java.io.tmpdir"), "clappy_bee.db")
        Room.databaseBuilder<UserStatsDatabase>(
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }
    single {
        val db: UserStatsDatabase = get()
        db.userStatsDAO()
    }
}