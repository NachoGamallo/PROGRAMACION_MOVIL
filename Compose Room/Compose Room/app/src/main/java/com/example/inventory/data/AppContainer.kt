package com.example.inventory.data

import android.content.Context
import com.example.inventory.InventoryApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

interface AppContainer {
    val itemsRepository: ItemsRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val preferenceManager = StoragePreference(context as InventoryApplication)
    private val offlineItemsRepository = OfflineItemsRepository(
        InventoryDatabase.getDatabase(context).itemDao())

    // Lo mantenemos como una única instancia para que el MutableStateFlow sea compartido
    private val fileItemsRepository = FileItemsRepo(context)

    override val itemsRepository: ItemsRepository = DynamicItemsRepository(
        offlineItemsRepository,
        fileItemsRepository,
        preferenceManager
    )
}
class DynamicItemsRepository(
    private val roomRepo: OfflineItemsRepository,
    private val fileRepo: FileItemsRepo,
    private val prefs: StoragePreference
) : ItemsRepository {

    // Implementamos el flujo para que el ViewModel lo escuche
    override val isFileModeStream: Flow<Boolean> = prefs.useFileStorageFlow

    // Implementamos el cambio de modo directamente aquí
    override fun toggleStorageMode() {
        prefs.useFileStorage = !prefs.useFileStorage
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getAllItemsStream(useFile: Boolean): Flow<List<Item>> {
        return prefs.useFileStorageFlow.flatMapLatest { usandoArchivos ->
            if (usandoArchivos) fileRepo.getAllItemsStream(true)
            else roomRepo.getAllItemsStream(false)
        }
    }

    private fun getActiveRepo(): ItemsRepository {
        return if (prefs.useFileStorage) fileRepo else roomRepo
    }

    override suspend fun insertItem(item: Item) = getActiveRepo().insertItem(item)
    override suspend fun deleteItem(item: Item) = getActiveRepo().deleteItem(item)
    override suspend fun updateItem(item: Item) = getActiveRepo().updateItem(item)

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getItemStream(id: Int): Flow<Item?> {
        return prefs.useFileStorageFlow.flatMapLatest { usandoArchivos ->
            if (usandoArchivos) fileRepo.getItemStream(id)
            else roomRepo.getItemStream(id)
        }
    }

    override fun saveEntireList(allItems: List<Item>) {
        fileRepo.saveEntireList(allItems)
    }
}