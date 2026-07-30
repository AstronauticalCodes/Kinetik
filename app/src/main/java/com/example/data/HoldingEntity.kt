package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "holdings")
data class HoldingEntity(
    @PrimaryKey val assetSymbol: String,
    val name: String,
    val amount: Double,
    val avgBuyPriceUsd: Double,
    val isFiat: Boolean
)
