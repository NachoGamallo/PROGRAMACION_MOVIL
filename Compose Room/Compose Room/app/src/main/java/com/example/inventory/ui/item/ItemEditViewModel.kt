package com.example.inventory.ui.item

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.inventory.data.ItemsRepository
import com.example.inventory.ui.navigation.StorageGlobals
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File

class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: ItemsRepository,
    application: Application
) : AndroidViewModel(application) {

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])
    private val file = File(getApplication<Application>().filesDir, "inventory_data.txt")

    init {
        viewModelScope.launch {
            itemUiState = itemsRepository.getItemStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }

    suspend fun updateItem(value: Boolean) {
        if (validateInput(itemUiState.itemDetails)) {
            if (StorageGlobals.isFileMode.value) {
                // Actualizar en Archivo
                updateItemInFile(itemUiState.itemDetails)
            } else {
                // Actualizar en Base de Datos
                itemsRepository.updateItem(itemUiState.itemDetails.toItem())
            }
        }
    }

    private fun updateItemInFile(details: ItemDetails) {
        if (file.exists()) {
            val lines = file.readLines()
            val updatedLines = lines.map { line ->
                val id = line.split("|").firstOrNull()?.toIntOrNull()
                if (id == itemId) {
                    "${details.id}|${details.name}|${details.price}|${details.quantity}"
                } else {
                    line
                }
            }
            file.writeText(updatedLines.joinToString("\n") + "\n")
        }
    }

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }
}