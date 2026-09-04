package com.example.data

import android.content.Context
import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow

class LensRepository(private val scanDao: ScanDao) {
    val allScans: Flow<List<ScanEntity>> = scanDao.getAllScans()
    val favoriteScans: Flow<List<ScanEntity>> = scanDao.getFavoriteScans()

    suspend fun getScanById(id: Long): ScanEntity? = scanDao.getScanById(id)

    suspend fun saveScan(scan: ScanEntity): Long = scanDao.insertScan(scan)

    suspend fun updateScan(scan: ScanEntity) = scanDao.updateScan(scan)

    suspend fun deleteScan(scan: ScanEntity) = scanDao.deleteScan(scan)

    suspend fun analyzeAndSave(context: Context, bitmap: Bitmap?, hint: String?): ScanEntity {
        val result = GeminiHelper.analyzeImage(context, bitmap, hint)
        val entity = ScanEntity(
            title = result.title,
            confidence = result.confidence,
            whatIsIt = result.whatIsIt,
            howItWorks = result.howItWorks,
            components = result.components.joinToString(","),
            interestingFacts = result.interestingFacts.joinToString("|"),
            similarObjects = result.similarObjects.joinToString(","),
            simpleExplanation = result.simpleExplanation,
            advancedExplanation = result.advancedExplanation
        )
        val id = scanDao.insertScan(entity)
        return entity.copy(id = id)
    }
}
