package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface HoldingDao {
    @Query("SELECT * FROM holdings")
    fun getAllHoldingsFlow(): Flow<List<HoldingEntity>>

    @Query("SELECT * FROM holdings")
    suspend fun getAllHoldings(): List<HoldingEntity>

    @Query("SELECT * FROM holdings WHERE assetSymbol = :symbol LIMIT 1")
    suspend fun getHoldingBySymbol(symbol: String): HoldingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHolding(holding: HoldingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHoldings(holdings: List<HoldingEntity>)

    @Update
    suspend fun updateHolding(holding: HoldingEntity)

    @Query("DELETE FROM holdings WHERE assetSymbol = :symbol")
    suspend fun deleteHoldingBySymbol(symbol: String)
}
