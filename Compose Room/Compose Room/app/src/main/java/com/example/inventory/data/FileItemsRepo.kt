package com.example.inventory.data

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import java.io.File

class FileItemsRepo(private val context: Context) : ItemsRepository {
    private val gson = Gson()
    private val file = File(context.filesDir, "items.json")

    // La "fuente de la verdad" para la UI en modo fichero
    private val _itemsFlow = MutableStateFlow<List<Item>>(loadFromFile())

    override fun toggleStorageMode() {
        TODO("Not yet implemented")
    }

    override fun getAllItemsStream(useFile: Boolean): Flow<List<Item>> = _itemsFlow.asStateFlow()

    private fun loadFromFile(): List<Item> {
        if (!file.exists()) return emptyList()
        return try {
            val json = file.readText()
            val type = object : TypeToken<List<Item>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // El Worker llamará a esto. Es vital actualizar el Flow después de escribir.
    override fun saveEntireList(allItems: List<Item>) {
        val json = gson.toJson(allItems)
        context.openFileOutput("items.json", Context.MODE_PRIVATE).use {
            it.write(json.toByteArray())
        }
        _itemsFlow.value = allItems // Esto refresca la pantalla
    }

    override suspend fun insertItem(item: Item) {
        val list = loadFromFile().toMutableList()
        // Generamos un ID manual para el modo archivo para evitar conflictos
        val maxId = list.maxOfOrNull { it.id } ?: 0
        val newItem = item.copy(id = maxId + 1)

        list.add(newItem)
        saveEntireList(list)
    }

    override fun getItemStream(id: Int): Flow<Item?> =
        _itemsFlow.map { list -> list.find { it.id == id } }

    override suspend fun deleteItem(item: Item) {
        val list = loadFromFile().toMutableList()
        list.removeAll { it.id == item.id }
        saveEntireList(list)
    }

    override suspend fun updateItem(item: Item) {
        val list = loadFromFile().toMutableList()
        val index = list.indexOfFirst { it.id == item.id }
        if (index != -1) {
            list[index] = item
            saveEntireList(list)
        }
    }
}