package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.RetrofitClient
import com.example.data.TickerResponse
import com.example.ui.components.Candle
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONArray

public data class CryptoState(
    val tickers: List<TickerResponse> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

public class CryptoViewModel : ViewModel() {
    private val _state = MutableStateFlow(CryptoState())
    public val state: StateFlow<CryptoState> = _state.asStateFlow()

    private val _candles = MutableStateFlow<Map<String, List<Candle>>>(emptyMap())
    public val candles: StateFlow<Map<String, List<Candle>>> = _candles.asStateFlow()

    public val topAssets: List<String> = listOf("BTCUSDT", "ETHUSDT", "SOLUSDT", "BNBUSDT", "LINKUSDT", "TAOUSDT", "AVAXUSDT", "DOGEUSDT")
    private val symbolsQuery: String = "[\"" + topAssets.joinToString("\",\"") + "\"]"

    init {
        startStreaming()
        topAssets.forEach { symbol ->
            fetchKlines(symbol, "1d", 7) // 7 days of daily candles for sparklines
        }
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
                delay(5000) // update every 5 seconds
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
                // handle error silently
            }
        }
    }
}
