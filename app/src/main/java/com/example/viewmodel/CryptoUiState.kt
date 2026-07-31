package com.example.viewmodel

import com.example.data.HoldingEntity
import com.example.data.TickerResponse
import com.example.data.TransactionEntity

data class CryptoUiState(
    val holdings: List<HoldingEntity> = emptyList(),
    val transactions: List<TransactionEntity> = emptyList(),
    val tickers: List<TickerResponse> = emptyList(),
    val topMovers: List<TickerResponse> = emptyList(),
    val totalNetWorth: Double = 0.0,
    val dailyPnl: Double = 0.0,
    val selectedAsset: String? = null,
    val tradeState: TradeState = TradeState.Idle,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isDarkMode: Boolean = false
)

sealed interface TradeState {
    object Idle : TradeState
    object Processing : TradeState
    data class Success(val message: String) : TradeState
    data class Error(val message: String) : TradeState
}
