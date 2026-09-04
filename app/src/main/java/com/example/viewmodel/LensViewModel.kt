package com.example.viewmodel

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.LensRepository
import com.example.data.ScanEntity
import com.example.data.ScanResultData
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class LensViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LensRepository

    val allScans: StateFlow<List<ScanEntity>>
    val favoriteScans: StateFlow<List<ScanEntity>>

    init {
        val db = AppDatabase.getDatabase(application)
        repository = LensRepository(db.scanDao())
        allScans = repository.allScans.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
        favoriteScans = repository.favoriteScans.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )
    }

    suspend fun analyzeAndSave(bitmap: Bitmap?, hint: String?): ScanEntity {
        return repository.analyzeAndSave(getApplication(), bitmap, hint)
    }

    suspend fun getScanById(id: Long): ScanEntity? {
        return repository.getScanById(id)
    }

    fun toggleFavorite(scan: ScanEntity) {
        viewModelScope.launch {
            repository.updateScan(scan.copy(isFavorite = !scan.isFavorite))
        }
    }

    fun updateNotes(scan: ScanEntity, newNotes: String) {
        viewModelScope.launch {
            repository.updateScan(scan.copy(notes = newNotes))
        }
    }

    fun deleteScan(scan: ScanEntity) {
        viewModelScope.launch {
            repository.deleteScan(scan)
        }
    }

    fun saveQuickScan(title: String, whatIsIt: String, howItWorks: String, components: String, facts: String, similar: String, simple: String, advanced: String) {
        viewModelScope.launch {
            val entity = ScanEntity(
                title = title,
                confidence = 99,
                whatIsIt = whatIsIt,
                howItWorks = howItWorks,
                components = components,
                interestingFacts = facts,
                similarObjects = similar,
                simpleExplanation = simple,
                advancedExplanation = advanced
            )
            repository.saveScan(entity)
        }
    }
}
