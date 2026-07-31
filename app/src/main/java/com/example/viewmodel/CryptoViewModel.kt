package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.CryptoRepository
import com.example.data.HoldingEntity
import com.example.data.RetrofitClient
import com.example.data.TickerResponse
import com.example.ui.components.Candle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import org.json.JSONArray

public data class CryptoState(
    val tickers: List<TickerResponse> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

public class CryptoViewModel(
    private val repository: CryptoRepository? = null
) : ViewModel() {
    private val _state = MutableStateFlow(CryptoState())
    public val state: StateFlow<CryptoState> = _state.asStateFlow()

    private val _uiState = MutableStateFlow(CryptoUiState())
    public val uiState: StateFlow<CryptoUiState> = _uiState.asStateFlow()

    private val _candles = MutableStateFlow<Map<String, List<Candle>>>(emptyMap())
    public val candles: StateFlow<Map<String, List<Candle>>> = _candles.asStateFlow()

    public val topAssets: List<String> = listOf("BTCUSDT", "ETHUSDT", "SOLUSDT", "BNBUSDT", "LINKUSDT", "TAOUSDT", "AVAXUSDT", "DOGEUSDT")
    private val symbolsQuery: String = "[\"" + topAssets.joinToString("\",\"") + "\"]"

    init {
        startStreaming()
        topAssets.forEach { symbol ->
            fetchKlines(symbol, "1d", 7) 
        }
        observeRepository()
    }

    private fun observeRepository() {
        if (repository == null) return
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
            combine(
                repository.allHoldingsFlow,
                repository.allTransactionsFlow,
                _state
            ) { holdings, transactions, legacyState ->
                val priceMap = legacyState.tickers.associate {
                    val cleanSymbol = it.symbol.replace("USDT", "")
                    cleanSymbol to (it.lastPrice.toDoubleOrNull() ?: 0.0)
                }
                val netWorth = repository.calculateNetWorth(holdings, priceMap)
                val sortedMovers = repository.getTopMovers(legacyState.tickers)

                val dailyPnl = holdings.sumOf { holding ->
                    if (holding.isFiat) 0.0
                    else {
                        val ticker = legacyState.tickers.find { 
                            it.symbol == "${holding.assetSymbol}USDT" || it.symbol == holding.assetSymbol 
                        }
                        val change = ticker?.priceChange?.toDoubleOrNull() ?: 0.0
                        holding.amount * change
                    }
                }

                _uiState.value.copy(
                    holdings = holdings,
                    transactions = transactions,
                    tickers = legacyState.tickers,
                    topMovers = sortedMovers,
                    totalNetWorth = netWorth,
                    dailyPnl = dailyPnl,
                    isLoading = legacyState.isLoading,
                    error = legacyState.error
                )
            }.collect { newState ->
                _uiState.value = newState
            }
        }
    }

    suspend fun executeTrade(fromAsset: String, toAsset: String, amount: Double): Result<Unit> {
        val repo = repository ?: return Result.failure(IllegalStateException("Repository not configured"))
        val priceMap = _state.value.tickers.associate {
            val cleanSymbol = it.symbol.replace("USDT", "")
            cleanSymbol to (it.lastPrice.toDoubleOrNull() ?: 0.0)
        }
        val result = repo.executeTrade(fromAsset, toAsset, amount, priceMap)
        if (result.isSuccess) {
            _uiState.value = _uiState.value.copy(tradeState = TradeState.Success("Trade executed successfully"))
        } else {
            _uiState.value = _uiState.value.copy(tradeState = TradeState.Error(result.exceptionOrNull()?.message ?: "Trade failed"))
        }
        return result
    }

    private fun startStreaming(): Unit {
        viewModelScope.launch {
            while (true) {
                try {
                    val data = RetrofitClient.api.getTickers(symbolsQuery)
                    _state.value = _state.value.copy(tickers = data, isLoading = false, error = null)
                } catch (e: Exception) {
                    _state.value = _state.value.copy(error = e.message, isLoading = false)
                }
                delay(5000) 
            }
        }
    }

    public fun fetchKlines(symbol: String, interval: String = "1h", limit: Int = 24): Unit {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getKlines(symbol, interval, limit)
                val jsonStr = response.string()
                val jsonArray = JSONArray(jsonStr)
                val candleList = mutableListOf<Candle>()
                for (i in 0 until jsonArray.length()) {
                    val item = jsonArray.getJSONArray(i)
                    val open = item.getString(1).toFloat()
                    val high = item.getString(2).toFloat()
                    val low = item.getString(3).toFloat()
                    val close = item.getString(4).toFloat()
                    candleList.add(Candle(open, close, high, low))
                }
                _candles.value = _candles.value.toMutableMap().apply {
                    put(symbol, candleList)
                }
            } catch (e: Exception) {
                
            }
        }
    }
}

class CryptoViewModelFactory(private val context: android.content.Context) : androidx.lifecycle.ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
        val db = com.example.data.AppDatabase.getDatabase(context.applicationContext)
        val repo = com.example.data.CryptoRepository(db.holdingDao(), db.transactionDao())
        return CryptoViewModel(repo) as T
    }
}
