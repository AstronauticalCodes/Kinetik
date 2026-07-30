package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val type: String, // "BUY", "SELL", "SWAP"
    val fromAsset: String,
    val toAsset: String,
    val fromAmount: Double,
    val toAmount: Double,
    val priceUsd: Double,
    val status: String = "COMPLETED"
)
