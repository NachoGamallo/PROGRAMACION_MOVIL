package com.example.inventory.data

import android.content.Context
import com.example.inventory.InventoryApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StoragePreference(context: InventoryApplication) {
    private val sharedPrefs = context.getSharedPreferences("inventory_prefs", Context.MODE_PRIVATE)

    // Iniciamos el flujo con el valor actual de SharedPreferences
    private val _useFileStorageFlow = MutableStateFlow(useFileStorage)
    val useFileStorageFlow: StateFlow<Boolean> = _useFileStorageFlow

    var useFileStorage: Boolean
        get() = sharedPrefs.getBoolean("use_file", false)
        set(value) {
            sharedPrefs.edit().putBoolean("use_file", value).apply()
            _useFileStorageFlow.value = value // Esto dispara el flatMapLatest del ViewModel
        }
}