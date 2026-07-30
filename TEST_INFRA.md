# E2E Testing Infrastructure & Suite Specification (TEST_INFRA.md)

## Overview
This document outlines the End-to-End (E2E) testing architecture, guidelines, tier breakdown, and execution commands for the **Kinetik** Android Application.

## Test Frameworks & Tools
- **Test Framework**: JUnit 4
- **Host-side Android Environment**: Robolectric (Android SDK 34/36, native graphics)
- **UI & Screenshot Verification**: Roborazzi / Compose Test Rule
- **Data Layer Persistence**: Room 2.7.0 in-memory database (`Room.inMemoryDatabaseBuilder`)
- **Asynchronous Execution**: Kotlin Coroutines (`runBlocking`, `StateFlow`, `Flow`)

---

## Test Hierarchy & Coverage Tiers

### Tier 1: Feature Coverage
- **Location**: `app/src/test/java/com/example/Tier1FeatureCoverageTest.kt`
- **Focus**: Core data structures, database seed initialization, parsing, and sorting logic.
- **Coverage Checklist**:
  1. Room DB Seed Validation: Verifies default pre-population of holdings (USD, BTC, ETH, SOL).
  2. $50,000 Fiat Balance Initialization: Ensures USD fiat balance initializes to $50,000.0 with `isFiat = true`.
  3. Dynamic Portfolio Calculation Logic: Tests net worth calculation across fiat and crypto holdings at current market prices.
  4. Top Movers Sorting: Verifies sorting tickers descending by `priceChangePercent.toDoubleOrNull()`.
  5. 24H High/Low Parsing: Validates parsing of `highPrice`, `lowPrice`, and `volume` fields from Binance API ticker responses.

### Tier 2: Boundary & Corner Cases
- **Location**: `app/src/test/java/com/example/Tier2BoundaryCornerCasesTest.kt`
- **Focus**: Error handling, validation rules, extreme values, and unknown inputs.
- **Coverage Checklist**:
  1. Insufficient Balance Trade Validation: Validates that trading amounts greater than available balance return failure without mutating DB.
  2. Zero & Negative Trade Amounts: Ensures non-positive amounts (0.0 or <0.0) trigger `IllegalArgumentException`.
  3. Max Balance Trades: Confirms trading 100% of available balance executes cleanly without underflow.
  4. Unknown Symbol Handling: Validates error responses when trading non-existent asset symbols or missing price feeds.

### Tier 3: Cross-Feature Interactions
- **Location**: `app/src/test/java/com/example/Tier3CrossFeatureInteractionsTest.kt`
- **Focus**: Cascade effects across DB updates, balance calculations, and logging.
- **Coverage Checklist**:
  1. Trade Execution Cascade: Trade execution -> Room holdings update -> dynamic portfolio net worth recalculation -> transaction log record creation (`TransactionEntity` inserted with `COMPLETED` status).

### Tier 4: Real-World Application Scenarios
- **Location**: `app/src/test/java/com/example/Tier4RealWorldScenariosTest.kt`
- **Focus**: Multi-step trade lifecycle simulation.
- **Coverage Checklist**:
  1. Multi-Trade Lifecycle Simulation:
     - Buy BTC with USD ($10,000 USD @ $50k/BTC).
     - Swap BTC for ETH (0.2 BTC -> 4.0 ETH @ BTC=$50k, ETH=$2.5k).
     - Price rally simulation (ETH price increases to $3,000).
     - Sell ETH for USD (4.0 ETH -> $12,000 USD).
     - Final assertions on end-to-end holdings (USD=$52,000, BTC=0.5, ETH=4.0, SOL=25.0) and chronologically ordered transaction log history.

---

## Execution Commands
To execute the complete unit and integration test suite:
```bash
./gradlew test
# or
./gradlew testDebugUnitTest
```
