package com.example.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
public data class TickerResponse(
    @Json(name = "symbol") val symbol: String,
    @Json(name = "priceChange") val priceChange: String,
    @Json(name = "priceChangePercent") val priceChangePercent: String,
    @Json(name = "lastPrice") val lastPrice: String,
    @Json(name = "highPrice") val highPrice: String = "0.0",
    @Json(name = "lowPrice") val lowPrice: String = "0.0",
    @Json(name = "volume") val volume: String = "0.0"
)

