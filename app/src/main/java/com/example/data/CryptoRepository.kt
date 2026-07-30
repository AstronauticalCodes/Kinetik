package com.example.data

import kotlinx.coroutines.flow.Flow
import java.util.Locale

class CryptoRepository(
    private val holdingDao: HoldingDao,
    private val transactionDao: TransactionDao
) {
    val allHoldingsFlow: Flow<List<HoldingEntity>> = holdingDao.getAllHoldingsFlow()
    val allTransactionsFlow: Flow<List<TransactionEntity>> = transactionDao.getAllTransactionsFlow()

    suspend fun seedInitialDataIfEmpty() {
        val existing = holdingDao.getAllHoldings()
        if (existing.isEmpty()) {
            val initialHoldings = listOf(
                HoldingEntity(
                    assetSymbol = "USD",
                    name = "US Dollar",
                    amount = 50000.0,
                    avgBuyPriceUsd = 1.0,
                    isFiat = true
                ),
                HoldingEntity(
                    assetSymbol = "BTC",
                    name = "Bitcoin",
                    amount = 0.5,
                    avgBuyPriceUsd = 65000.0,
                    isFiat = false
                ),
                HoldingEntity(
                    assetSymbol = "ETH",
                    name = "Ethereum",
                    amount = 4.0,
                    avgBuyPriceUsd = 3500.0,
                    isFiat = false
                ),
                HoldingEntity(
                    assetSymbol = "SOL",
                    name = "Solana",
                    amount = 25.0,
                    avgBuyPriceUsd = 150.0,
                    isFiat = false
                )
            )
            holdingDao.insertHoldings(initialHoldings)
        }
    }

    suspend fun getAllHoldings(): List<HoldingEntity> = holdingDao.getAllHoldings()

    suspend fun getAllTransactions(): List<TransactionEntity> = transactionDao.getAllTransactions()

    fun calculateNetWorth(holdings: List<HoldingEntity>, prices: Map<String, Double>): Double {
        return holdings.sumOf { holding ->
            if (holding.isFiat) {
                holding.amount
            } else {
                val currentPrice = prices[holding.assetSymbol] ?: prices["${holding.assetSymbol}USDT"] ?: holding.avgBuyPriceUsd
                holding.amount * currentPrice
            }
        }
    }

    fun getTopMovers(tickers: List<TickerResponse>): List<TickerResponse> {
        return tickers.sortedByDescending { ticker ->
            ticker.priceChangePercent.toDoubleOrNull() ?: 0.0
        }
    }

    suspend fun executeTrade(
        fromAsset: String,
        toAsset: String,
        amount: Double,
        currentPrices: Map<String, Double>
    ): Result<Unit> {
        if (amount <= 0.0) {
            return Result.failure(IllegalArgumentException("Trade amount must be greater than zero"))
        }

        val fromHolding = holdingDao.getHoldingBySymbol(fromAsset)
            ?: return Result.failure(IllegalArgumentException("Unknown source asset: $fromAsset"))

        if (fromHolding.amount < amount) {
            return Result.failure(IllegalStateException("Insufficient balance for $fromAsset: available ${fromHolding.amount}, required $amount"))
        }

        val fromPrice = getPriceForAsset(fromAsset, currentPrices)
        val toPrice = getPriceForAsset(toAsset, currentPrices)

        if (fromPrice <= 0.0 || toPrice <= 0.0) {
            return Result.failure(IllegalArgumentException("Invalid or missing market price for trade pair $fromAsset -> $toAsset"))
        }

        val toAmount = (amount * fromPrice) / toPrice
        val tradeType = when {
            fromAsset.uppercase(Locale.ROOT) == "USD" -> "BUY"
            toAsset.uppercase(Locale.ROOT) == "USD" -> "SELL"
            else -> "SWAP"
        }

        // Deduct from source asset
        val newFromAmount = fromHolding.amount - amount
        holdingDao.insertHolding(fromHolding.copy(amount = newFromAmount))

        // Add to destination asset
        val toHolding = holdingDao.getHoldingBySymbol(toAsset)
        if (toHolding != null) {
            val newToAmount = toHolding.amount + toAmount
            holdingDao.insertHolding(toHolding.copy(amount = newToAmount))
        } else {
            val newHolding = HoldingEntity(
                assetSymbol = toAsset,
                name = getNameForAsset(toAsset),
                amount = toAmount,
                avgBuyPriceUsd = toPrice,
                isFiat = (toAsset.uppercase(Locale.ROOT) == "USD")
            )
            holdingDao.insertHolding(newHolding)
        }

        // Record transaction log
        val transaction = TransactionEntity(
            timestamp = System.currentTimeMillis(),
            type = tradeType,
            fromAsset = fromAsset,
            toAsset = toAsset,
            fromAmount = amount,
            toAmount = toAmount,
            priceUsd = if (fromAsset == "USD") toPrice else fromPrice,
            status = "COMPLETED"
        )
        transactionDao.insertTransaction(transaction)

        return Result.success(Unit)
    }

    private fun getPriceForAsset(symbol: String, prices: Map<String, Double>): Double {
        if (symbol.uppercase(Locale.ROOT) == "USD") return 1.0
        return prices[symbol] ?: prices["${symbol}USDT"] ?: 0.0
    }

    private fun getNameForAsset(symbol: String): String {
        return when (symbol.uppercase(Locale.ROOT)) {
            "USD" -> "US Dollar"
            "BTC" -> "Bitcoin"
            "ETH" -> "Ethereum"
            "SOL" -> "Solana"
            "BNB" -> "Binance Coin"
            "DOGE" -> "Dogecoin"
            "AVAX" -> "Avalanche"
            "LINK" -> "Chainlink"
            "TAO" -> "Bittensor"
            else -> symbol
        }
    }
}
