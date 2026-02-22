package com.example.inventory.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

interface ItemsRepository {

    val isFileModeStream: Flow<Boolean> get() = flowOf(false) // Añade esto
    fun toggleStorageMode()
    fun getAllItemsStream(useFile: Boolean): Flow<List<Item>>
    fun getItemStream(id: Int): Flow<Item?>
    suspend fun insertItem(item: Item)
    suspend fun deleteItem(item: Item)
    suspend fun updateItem(item: Item)

    // Añade esto para el Worker
    fun saveEntireList(allItems: List<Item>)
}