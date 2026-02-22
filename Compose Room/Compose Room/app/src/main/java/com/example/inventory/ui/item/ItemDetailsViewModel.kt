package com.example.inventory.ui.item

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.ItemsRepository
import com.example.inventory.ui.navigation.StorageGlobals
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File

class ItemDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    application: Application
) : AndroidViewModel(application) {

    private val itemId: Int = checkNotNull(savedStateHandle[ItemDetailsDestination.itemIdArg])
    private val file = File(getApplication<Application>().filesDir, "inventory_data.txt")

    val uiState: StateFlow<ItemDetailsUiState> =
        itemsRepository.getItemStream(itemId)
            .filterNotNull()
            .map {
                ItemDetailsUiState(outOfStock = it.quantity <= 0, itemDetails = it.toItemDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = ItemDetailsUiState()
            )

    fun reduceQuantityByOne(value: Boolean) {
        viewModelScope.launch {
            if (StorageGlobals.isFileMode.value) {
                // Lógica de archivo
                updateQuantityInFile(-1)
            } else {
                // Lógica de Base de Datos
                val currentItem = uiState.value.itemDetails.toItem()
                if (currentItem.quantity > 0) {
                    itemsRepository.updateItem(currentItem.copy(quantity = currentItem.quantity - 1))
                }
            }
        }
    }

    suspend fun deleteItem(value: Boolean) {
        if (StorageGlobals.isFileMode.value) {
            if (file.exists()) {
                val lines = file.readLines()
                val updatedLines = lines.filter { line ->
                    val id = line.split("|").firstOrNull()?.toIntOrNull()
                    id != itemId
                }
                file.writeText(updatedLines.joinToString("\n") + if (updatedLines.isNotEmpty()) "\n" else "")
            }
        } else {
            itemsRepository.deleteItem(uiState.value.itemDetails.toItem())
        }
    }

    private fun updateQuantityInFile(delta: Int) {
        if (file.exists()) {
            val lines = file.readLines()
            val updatedLines = lines.map { line ->
                val parts = line.split("|")
                if (parts.size == 4 && parts[0].toInt() == itemId) {
                    val newQty = (parts[3].toInt() + delta).coerceAtLeast(0)
                    "${parts[0]}|${parts[1]}|${parts[2]}|$newQty"
                } else {
                    line
                }
            }
            file.writeText(updatedLines.joinToString("\n") + "\n")
        }
    }
}

data class ItemDetailsUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ItemDetails = ItemDetails()
)