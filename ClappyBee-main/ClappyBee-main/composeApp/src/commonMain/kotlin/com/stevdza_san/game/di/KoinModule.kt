package com.stevdza_san.game

import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.Settings
import com.russhwolf.settings.observable.makeObservable
import com.stevdza_san.game.domain.database.UserStatsDatabase
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

@OptIn(ExperimentalSettingsApi::class)
val sharedModule = module {
    single<ObservableSettings> { Settings().makeObservable() }

    // Obtiene el DAO pidiendo primero la instancia de la DB
    single { get<UserStatsDatabase>().userStatsDAO() }
}

expect val targetModule: Module

fun initializeKoin(
    config: (KoinApplication.() -> Unit)? = null
) {
    startKoin {
        config?.invoke(this)
        modules(sharedModule, targetModule)
    }
}