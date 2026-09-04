package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scans")
data class ScanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val title: String,
    val confidence: Int,
    val whatIsIt: String,
    val howItWorks: String,
    val components: String, // comma separated or JSON
    val interestingFacts: String,
    val similarObjects: String,
    val simpleExplanation: String,
    val advancedExplanation: String,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false,
    val notes: String = "",
    val imageUri: String = ""
)
