<div align="center">

# Kinetik

### A Modern Cryptocurrency Trading & Portfolio Tracker for Android

[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.0-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-Material%203-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![API](https://img.shields.io/badge/Min%20SDK-24-brightgreen)](https://developer.android.com/about/versions/nougat)

**Kinetik** is a sleek, dark-themed crypto wallet & trading interface built with Jetpack Compose and powered by live Binance market data. Track your portfolio in real time, execute trades with balance validation, and explore the market — all from a beautifully crafted mobile experience.

---

## Screenshots

<div align="center">

<!-- Replace these with actual screenshot paths -->
| Dashboard | Markets | Asset Detail |
|:-:|:-:|:-:|
| <img src="https://github.com/user-attachments/assets/ab9b2c7f-f556-4124-8b08-b4285766e7b4" width="250"/> | <img src="https://github.com/user-attachments/assets/cfa7d0a2-7e57-49ea-8ab1-60c6c6ebd957" width="250"/> | <img src="https://github.com/user-attachments/assets/86c4edca-8007-405b-abf6-479623876181" width="250"/> |

| Portfolio | Trade |
|:-:|:-:|
| <img src="https://github.com/user-attachments/assets/197faf6f-4201-4d3a-8422-f466d2ca3254" width="250"/> | <img src="https://github.com/user-attachments/assets/cb9558af-4d31-4ca2-bfb6-8e116982dc73" width="250"/> |

</div>

---

## Features

- **Live Market Data** — Real-time price streaming from the Binance API with 5-second refresh intervals
- **Dynamic Portfolio** — Total net worth calculated on the fly from local holdings × live prices
- **Interactive Trading** — Buy, sell, and swap crypto with a built-in keypad and live fiat conversion
- **Balance Validation** — Trades are enforced against actual holdings; insufficient balances are rejected
- **Top Movers** — Dashboard cards dynamically ranked by 24h percentage change from live tickers
- **Candlestick & Sparkline Charts** — Custom-drawn Compose Canvas charts for asset price visualization
- **Transaction History** — Every trade is logged to a local Room database with full receipt details
- **Search & Discovery** — Browse and search across tracked assets with shared element transitions
- **Dark Theme** — A premium, dark-first design language built with Material 3

---

## Architecture

Kinetik follows a clean **MVVM (Model-View-ViewModel)** architecture with a unidirectional data flow:

```
┌─────────────────────────────────────────────────────────┐
│                      UI Layer                           │
│                                                         │
│  Screens (Compose)  ◄──  ViewModel  ◄──  UiState        │
│  • DashboardScreen       CryptoViewModel   CryptoUiState│
│  • MarketsScreen         (StateFlow)                    │
│  • PortfolioScreen                                      │
│  • TradeScreen                                          │
│  • AssetDetailScreen                                    │
│  • TransactionHistoryScreen                             │
│  • ProfileScreen / SearchScreen / ...                   │
└──────────────────────┬──────────────────────────────────┘
                       │
                       ▼
┌─────────────────────────────────────────────────────────┐
│                    Domain Layer                          │
│                                                         │
│  CryptoRepository                                       │
│  • calculateNetWorth(holdings, livePrices)               │
│  • getTopMovers(tickers)                                │
│  • executeTrade(from, to, amount, prices)               │
│  • seedInitialDataIfEmpty()                             │
└──────────┬─────────────────────────┬────────────────────┘
           │                         │
           ▼                         ▼
┌────────────────────┐   ┌────────────────────────────────┐
│   Local Database   │   │        Network Layer           │
│                    │   │                                │
│  Room (SQLite)     │   │  Retrofit + Moshi              │
│  • HoldingEntity   │   │  • BinanceApi                  │
│  • TransactionEntity│  │    GET /api/v3/ticker/24hr     │
│  • HoldingDao      │   │    GET /api/v3/klines          │
│  • TransactionDao  │   │  • OkHttp + LoggingInterceptor │
│  • AppDatabase     │   │                                │
└────────────────────┘   └────────────────────────────────┘
```

### Key Design Decisions

| Decision | Rationale |
|----------|-----------|
| **Room for local persistence** | Holdings and transaction history survive app restarts; enables offline portfolio viewing |
| **Polling over WebSocket** | Simpler implementation with 5s refresh; sufficient for a portfolio tracker without order-book depth |
| **Single ViewModel** | All screens share `CryptoViewModel` for consistent state; `CryptoUiState` combines holdings, tickers, and trade state into one reactive flow |
| **`combine` for derived state** | Net worth and PnL are recomputed reactively whenever holdings _or_ prices change |
| **Canvas-based charts** | Custom `Sparkline` and `CandlestickChart` composables avoid heavy charting library dependencies |

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| **Language** | Kotlin 2.0 |
| **UI** | Jetpack Compose + Material 3 |
| **Navigation** | Compose Navigation with shared element transitions |
| **State Management** | StateFlow, ViewModel, `combine` |
| **Local Database** | Room (SQLite) with KSP code generation |
| **Networking** | Retrofit 2, OkHttp, Moshi |
| **Market Data** | Binance Public REST API |
| **Image Loading** | Coil Compose |
| **Testing** | JUnit, Robolectric, Compose UI Test, Roborazzi |
| **Build** | Gradle (Kotlin DSL), Version Catalogs |

---

## Project Structure

```
app/src/main/java/com/example/
├── MainActivity.kt                  # Entry point, ViewModel + NavHost setup
├── data/
│   ├── AppDatabase.kt               # Room database singleton with auto-seeding
│   ├── BinanceApi.kt                # Retrofit API interface for Binance
│   ├── CryptoRepository.kt          # Business logic: trades, net worth, top movers
│   ├── HoldingDao.kt                # Room DAO for portfolio holdings
│   ├── HoldingEntity.kt             # Room entity: asset symbol, amount, avg price
│   ├── RetrofitClient.kt            # Retrofit/OkHttp client configuration
│   ├── TickerResponse.kt            # Moshi model for 24hr ticker data
│   ├── TransactionDao.kt            # Room DAO for transaction history
│   └── TransactionEntity.kt         # Room entity: trade logs with timestamps
├── ui/
│   ├── components/
│   │   ├── CandlestickChart.kt      # Custom Canvas candlestick chart
│   │   └── Sparkline.kt             # Custom Canvas sparkline chart
│   ├── navigation/
│   │   ├── AppNavGraph.kt           # All routes and navigation logic
│   │   └── BottomNavigationBar.kt   # Bottom tab bar
│   ├── screens/
│   │   ├── AssetDetailScreen.kt     # Asset detail with live 24H stats + chart
│   │   ├── BottomSheetComponents.kt # Order confirmation sheet
│   │   ├── DashboardScreen.kt       # Home: portfolio summary + top movers
│   │   ├── DepositScreen.kt         # Fiat deposit flow
│   │   ├── MarketsScreen.kt         # Full market listing with live prices
│   │   ├── PortfolioScreen.kt       # Holdings breakdown with live valuations
│   │   ├── ProfileScreen.kt         # User profile and settings
│   │   ├── SearchScreen.kt          # Asset search with filtering
│   │   ├── SecuritySettingsScreen.kt# Security preferences
│   │   ├── TradeScreen.kt           # Buy/Sell/Swap with keypad + validation
│   │   ├── TransactionHistoryScreen.kt # Trade history from Room DB
│   │   └── TransactionReceiptScreen.kt # Individual trade receipt
│   └── theme/
│       ├── Color.kt                 # Color palette
│       ├── Theme.kt                 # Material 3 dark theme
│       └── Type.kt                  # Typography scale
└── viewmodel/
    ├── CryptoUiState.kt             # Combined UI state data class
    └── CryptoViewModel.kt           # Central ViewModel + Factory
```

---

## Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (Ladybug or later recommended)
- Android SDK 24+ (Android 7.0 Nougat)
- An Android emulator or physical device

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/AstronauticalCodes/Kinetik.git
   cd Kinetik
   ```

2. **Open in Android Studio**
   - Select **File → Open** and choose the project directory
   - Allow Gradle to sync and resolve dependencies

3. **Run the app**
   - Select a device/emulator and hit **Run ▶**
   - The app will auto-seed your portfolio with $50,000 USD and sample crypto holdings on first launch

> [!NOTE]
> No API keys are required for market data. Kinetik uses Binance's public REST API which does not require authentication.

---

## License

This project is for educational and demonstration purposes.

---

<div align="center">

**Built with ❤️ using Jetpack Compose**

</div>
