package com.stevdza_san.game

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.stevdza_san.game.domain.AudioPlayer
import com.stevdza_san.game.domain.database.UserStatsDatabase
import org.koin.dsl.module

actual val targetModule = module {
    single {
        val context = get<Context>()
        val dbFile = context.getDatabasePath("clappy_bee.db")

        Room.databaseBuilder<UserStatsDatabase>(
            context = context,
            name = dbFile.absolutePath
        )
            .setDriver(BundledSQLiteDriver())
            .build()
    }

    // Aquí especificamos el tipo explícitamente para evitar el error de inferencia
    single {
        val db: UserStatsDatabase = get()
        db.userStatsDAO()
    }

    single { AudioPlayer(get()) }

}