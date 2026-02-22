package com.example.inventory.data

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import java.io.File

class DatabaseToFileWorker (
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val itemsData = inputData.getString("KEY_ITEMS_JSON") ?: return Result.failure()
        return try {
            val file = File(applicationContext.filesDir, "inventory_data.txt")
            // Escribimos el archivo. Ahora este archivo vive su propia vida.
            file.writeText(itemsData)
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }

}