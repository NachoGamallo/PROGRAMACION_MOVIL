package com.example.inventory.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import com.example.inventory.ui.navigation.StorageGlobals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class HomeViewModel(
    private val itemsRepository: ItemsRepository,
    application: Application
) : AndroidViewModel(application) {

    private val _isFileMode = MutableStateFlow(StorageGlobals.isFileMode.value)
    val isFileMode: StateFlow<Boolean> = _isFileMode.asStateFlow()

    private val fileName = "inventory_data.txt"
    private val file = File(getApplication<Application>().filesDir, fileName)

    @OptIn(ExperimentalCoroutinesApi::class)
    val homeUiState: StateFlow<HomeUiState> = _isFileMode.flatMapLatest { mode ->
        if (mode) {
            readItemsFromFile()
        } else {
            itemsRepository.getAllItemsStream(false).map { HomeUiState(it) }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    fun toggleStorageMode() {
        val newMode = !_isFileMode.value
        _isFileMode.value = newMode
        StorageGlobals.isFileMode.value = newMode
    }

    private fun readItemsFromFile(): Flow<HomeUiState> = flow {
        if (file.exists()) {
            val items = file.readLines().filter { it.isNotBlank() }.map { line ->
                val parts = line.split("|")
                Item(parts[0].toInt(), parts[1], parts[2].toDouble(), parts[3].toInt())
            }
            emit(HomeUiState(items))
        } else {
            emit(HomeUiState(emptyList()))
        }
    }

    fun deleteItemFromFile(itemId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (file.exists()) {
                val lines = file.readLines()
                val updatedLines = lines.filter { line ->
                    val id = line.split("|").firstOrNull()?.toIntOrNull()
                    id != itemId
                }
                file.writeText(updatedLines.joinToString("\n"))
                _isFileMode.value = _isFileMode.value
            }
        }
    }

    fun updateItemInFile(updatedItem: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            if (file.exists()) {
                val lines = file.readLines()
                val updatedLines = lines.map { line ->
                    val id = line.split("|").firstOrNull()?.toIntOrNull()
                    if (id == updatedItem.id) {
                        "${updatedItem.id}|${updatedItem.name}|${updatedItem.price}|${updatedItem.quantity}"
                    } else {
                        line
                    }
                }
                file.writeText(updatedLines.joinToString("\n"))
                _isFileMode.value = _isFileMode.value
            }
        }
    }

    fun addItemToFile(name: String, price: Double, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
            val newLine = "$id|$name|$price|$quantity\n"
            try {
                file.appendText(newLine)
                _isFileMode.value = _isFileMode.value
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class HomeUiState(val itemList: List<Item> = listOf())