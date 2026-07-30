package com.example.data

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

public interface BinanceApi {
    @GET("api/v3/ticker/24hr")
    public suspend fun getTickers(@Query("symbols") symbols: String): List<TickerResponse>

    @GET("api/v3/klines")
    public suspend fun getKlines(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String = "1h",
        @Query("limit") limit: Int = 24
    ): ResponseBody
}
