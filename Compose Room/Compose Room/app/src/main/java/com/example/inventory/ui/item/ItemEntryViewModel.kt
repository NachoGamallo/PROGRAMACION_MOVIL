package com.example.inventory.ui.item

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import com.example.inventory.ui.navigation.StorageGlobals
import java.io.File
import java.text.NumberFormat

class ItemEntryViewModel(
    private val itemsRepository: ItemsRepository,
    application: Application
) : AndroidViewModel(application) {

    private val file = File(getApplication<Application>().filesDir, "inventory_data.txt")

    var itemUiState by mutableStateOf(ItemUiState())
        private set

    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState = ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun saveItem(value: Boolean) {
        if (validateInput()) {
            if (StorageGlobals.isFileMode.value) {
                saveItemToFile()
            } else {
                itemsRepository.insertItem(itemUiState.itemDetails.toItem())
            }
        }
    }

    private fun saveItemToFile() {
        val item = itemUiState.itemDetails
        val id = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()
        val line = "$id|${item.name}|${item.price}|${item.quantity}\n"
        file.appendText(line)
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() && quantity.isNotBlank()
        }
    }
}

data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val quantity: String = "",
)

fun ItemDetails.toItem(): Item = Item(
    id = id,
    name = name,
    price = price.toDoubleOrNull() ?: 0.0,
    quantity = quantity.toIntOrNull() ?: 0
)

fun Item.formatedPrice(): String {
    return NumberFormat.getCurrencyInstance().format(price)
}

fun Item.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)

fun Item.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    quantity = quantity.toString()
)