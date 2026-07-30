# Project: Kinetik Android Application

## Architecture
- **Language & Runtime**: Kotlin 2.2.10, Android SDK target 34 / min 26, Jetpack Compose, Coroutines.
- **Data & Persistence**: Room 2.7.0 (KSP) storing local holdings (`HoldingEntity`), transaction log (`TransactionEntity`). Pre-populated with $50,000 USD fiat + sample BTC, ETH, SOL holdings & history.
- **Networking**: Retrofit 2.12.0 + Moshi 1.15.2 fetching live ticker stats from Binance API (`https://api.binance.com/api/v3/ticker/24hr`).
- **UI & Navigation**: Jetpack Compose single-activity architecture, `CryptoViewModel` driving reactive UI state via StateFlow.
- **Git Commit Discipline**: Logical git commits performed by workers after completing each subtask/milestone.

## Code Layout
- `app/src/main/java/com/example/data/`: `BinanceApi.kt`, `RetrofitClient.kt`, `TickerResponse.kt`, Room DB (`AppDatabase.kt`, entities, DAOs), repository (`CryptoRepository.kt`).
- `app/src/main/java/com/example/viewmodel/`: `CryptoViewModel.kt`.
- `app/src/main/java/com/example/ui/screens/`: `DashboardScreen.kt`, `AssetDetailScreen.kt`, `TradeScreen.kt`, `PortfolioScreen.kt`, `TransactionHistoryScreen.kt`, `BottomSheetComponents.kt`, `ProfileScreen.kt`, `DepositScreen.kt`.
- `app/src/main/java/com/example/ui/components/`: Navigation & reusable UI components.
- `app/src/test/java/com/example/`: Unit & UI test suite.

## Milestones
| # | Name | Scope | Dependencies | Status |
|---|------|-------|-------------|--------|
| E2E | E2E Testing Suite | Requirements-driven test suite (Tiers 1-4) & infrastructure setup | None | IN_PROGRESS |
| 1 | Room DB & Holdings Persistence | Room database, DAOs, pre-population ($50k fiat + sample holdings), repository layer, git commit | E2E | PLANNED |
| 2 | Live API Integration & Dynamic Balances | Extend TickerResponse (24H High/Low/Vol), dynamic portfolio balance calculation, Top Movers sorting by priceChangePercent | M1 | PLANNED |
| 3 | Trade Screen, Validation & Interactivity | Live fiat conversion, trade keypad/swipe interactivity, balance validation, Room transaction logging, UI clickability audit | M2 | PLANNED |
| Final | E2E Test Pass & Adversarial Hardening | 100% E2E test pass (Tiers 1-4) + Tier 5 white-box coverage hardening | M3 | PLANNED |

## Interface Contracts
### Data Layer (Room + Retrofit) -> ViewModel -> UI
- `HoldingEntity`: `assetSymbol: String (PK)`, `name: String`, `amount: Double`, `avgBuyPriceUsd: Double`, `isFiat: Boolean`.
- `TransactionEntity`: `id: Long (PK, autoGenerate)`, `timestamp: Long`, `type: String ("BUY", "SELL", "SWAP")`, `fromAsset: String`, `toAsset: String`, `fromAmount: Double`, `toAmount: Double`, `priceUsd: Double`, `status: String`.
- `CryptoViewModel`:
  - `uiState: StateFlow<CryptoUiState>` (holdings, totalNetWorth, dailyPnl, topMovers, 24hStats, selectedAsset, tradeState).
  - `executeTrade(fromAsset: String, toAsset: String, amount: Double): Result<Unit>` (validates balance, updates holdings in Room DB, logs transaction).

## Build & Test Verification Commands
- `gradle test` or `./gradlew test`
- `gradle compileDebugKotlin` or `./gradlew compileDebugKotlin`
- `gradle assembleDebug` or `./gradlew assembleDebug`
