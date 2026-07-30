package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.DoubleArrow
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.theme.DataLarge
import com.example.ui.theme.DataMedium
import com.example.ui.theme.md_theme_dark_onSurface
import com.example.ui.theme.md_theme_dark_onSurfaceVariant
import com.example.ui.theme.md_theme_dark_primaryContainer
import com.example.ui.theme.primary_fixed_dim
import com.example.ui.theme.secondary_fixed_dim
import com.example.ui.theme.surface_container_high
import com.example.ui.theme.surface_container_highest
import com.example.ui.theme.surface_container_low
import com.example.viewmodel.CryptoViewModel
import com.example.viewmodel.CryptoViewModelFactory
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
public fun TradeScreen(
  modifier: Modifier = Modifier,
  onNavigateToSearch: () -> Unit = {},
  viewModel: CryptoViewModel = viewModel(factory = CryptoViewModelFactory(LocalContext.current))
): Unit {
  val context = LocalContext.current
  val coroutineScope = rememberCoroutineScope()
  val uiState by viewModel.uiState.collectAsState()
  val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

  var inputAmountText by remember { mutableStateOf("0") }
  var payAsset by remember { mutableStateOf("USD") }
  var receiveAsset by remember { mutableStateOf("BTC") }
  var payDropdownExpanded by remember { mutableStateOf(false) }
  var receiveDropdownExpanded by remember { mutableStateOf(false) }
  var showOrderSheet by remember { mutableStateOf(false) }
  var tradeErrorMessage by remember { mutableStateOf<String?>(null) }

  val availableAssets = listOf("USD", "BTC", "ETH", "SOL", "BNB", "USDC", "DOGE", "AVAX", "LINK", "TAO")

  val payHolding = uiState.holdings.find { it.assetSymbol.uppercase(Locale.ROOT) == payAsset.uppercase(Locale.ROOT) }
  val receiveHolding = uiState.holdings.find { it.assetSymbol.uppercase(Locale.ROOT) == receiveAsset.uppercase(Locale.ROOT) }

  val payBalance = payHolding?.amount ?: 0.0
  val receiveBalance = receiveHolding?.amount ?: 0.0

  val priceMap = uiState.tickers.associate {
    val clean = it.symbol.replace("USDT", "")
    clean.uppercase(Locale.ROOT) to (it.lastPrice.toDoubleOrNull() ?: 0.0)
  }

  fun getAssetPrice(symbol: String): Double {
    val upper = symbol.uppercase(Locale.ROOT)
    if (upper == "USD" || upper == "USDC" || upper == "USDT") return 1.0
    return priceMap[upper] ?: 0.0
  }

  val payPrice = getAssetPrice(payAsset)
  val receivePrice = getAssetPrice(receiveAsset)

  val inputAmount = inputAmountText.toDoubleOrNull() ?: 0.0
  val outputAmount = if (receivePrice > 0.0) (inputAmount * payPrice) / receivePrice else 0.0

  val rateText = if (receivePrice > 0.0) {
    val rate = payPrice / receivePrice
    "1 $payAsset ≈ ${String.format(Locale.US, "%.4f", rate)} $receiveAsset"
  } else ""

  fun handleKeypadInput(key: String) {
    when (key) {
      "DEL" -> {
        inputAmountText = if (inputAmountText.length > 1) inputAmountText.dropLast(1) else "0"
      }
      "." -> {
        if (!inputAmountText.contains(".")) {
          inputAmountText += "."
        }
      }
      else -> {
        inputAmountText = if (inputAmountText == "0") key else inputAmountText + key
      }
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(fraction = 1.0f),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "TRADE",
            style = MaterialTheme.typography.headlineMedium,
            color = md_theme_dark_primaryContainer,
            letterSpacing = 2.sp
          )
        },
        actions = {
          IconButton(onClick = onNavigateToSearch) {
            Icon(
              imageVector = Icons.Filled.Search,
              contentDescription = "Search",
              tint = md_theme_dark_onSurfaceVariant
            )
          }
          Box(
            modifier = Modifier
              .padding(end = 16.dp)
              .size(size = 32.dp)
              .clip(shape = CircleShape)
              .background(color = primary_fixed_dim),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = Icons.Filled.Person,
              contentDescription = "User",
              tint = Color(color = 0xFF001F24),
              modifier = Modifier.size(size = 18.dp)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent
        )
      )
    },
    containerColor = Color.Transparent
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize(fraction = 1.0f)
        .padding(paddingValues = innerPadding)
        .padding(horizontal = 16.dp)
    ) {
      Spacer(modifier = Modifier.height(height = 16.dp))
      
      // Pay From Box
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = surface_container_low, shape = RoundedCornerShape(size = 16.dp))
          .padding(all = 16.dp)
      ) {
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween) {
          Text(text = "Pay From", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
          Text(
            text = "Bal: ${String.format(Locale.US, "%.4f", payBalance)} $payAsset",
            style = DataMedium,
            color = primary_fixed_dim
          )
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box {
            Row(
              modifier = Modifier
                .background(color = surface_container_high, shape = RoundedCornerShape(size = 8.dp))
                .clickable { payDropdownExpanded = true }
                .padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(imageVector = Icons.Filled.CurrencyBitcoin, contentDescription = null, tint = Color.White, modifier = Modifier.size(size = 16.dp))
              Spacer(modifier = Modifier.width(width = 4.dp))
              Text(text = payAsset, style = MaterialTheme.typography.headlineMedium, color = md_theme_dark_onSurface)
              Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = null, tint = md_theme_dark_onSurfaceVariant)
            }
            DropdownMenu(
              expanded = payDropdownExpanded,
              onDismissRequest = { payDropdownExpanded = false }
            ) {
              availableAssets.forEach { asset ->
                DropdownMenuItem(
                  text = { Text(asset) },
                  onClick = {
                    payAsset = asset
                    payDropdownExpanded = false
                  }
                )
              }
            }
          }
          Column(horizontalAlignment = Alignment.End) {
            Text(text = inputAmountText, style = DataLarge.copy(fontSize = 40.sp), color = primary_fixed_dim)
            val usdVal = inputAmount * payPrice
            Text(text = "≈ $${String.format(Locale.US, "%.2f", usdVal)}", style = DataMedium, color = md_theme_dark_onSurfaceVariant)
          }
        }
      }
      
      // Swap Icon Toggle Button
      Box(
        modifier = Modifier.fillMaxWidth(fraction = 1.0f).height(height = 0.dp),
        contentAlignment = Alignment.Center
      ) {
         Box(
           modifier = Modifier
             .size(size = 40.dp)
             .background(color = surface_container_highest, shape = CircleShape)
             .clickable {
               val temp = payAsset
               payAsset = receiveAsset
               receiveAsset = temp
             },
           contentAlignment = Alignment.Center
         ) {
           Icon(imageVector = Icons.Filled.SwapVert, contentDescription = "Swap Assets", tint = primary_fixed_dim)
         }
      }

      // Receive To Box
      Column(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .background(color = surface_container_low, shape = RoundedCornerShape(size = 16.dp))
          .padding(all = 16.dp)
      ) {
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.SpaceBetween) {
          Text(text = "Receive To", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
          Text(
            text = "Bal: ${String.format(Locale.US, "%.4f", receiveBalance)} $receiveAsset",
            style = DataMedium,
            color = secondary_fixed_dim
          )
        }
        Spacer(modifier = Modifier.height(height = 8.dp))
        Row(
          modifier = Modifier.fillMaxWidth(fraction = 1.0f),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Box {
            Row(
              modifier = Modifier
                .background(color = surface_container_high, shape = RoundedCornerShape(size = 8.dp))
                .clickable { receiveDropdownExpanded = true }
                .padding(horizontal = 8.dp, vertical = 4.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(imageVector = Icons.Filled.AttachMoney, contentDescription = null, tint = Color.White, modifier = Modifier.size(size = 16.dp))
              Spacer(modifier = Modifier.width(width = 4.dp))
              Text(text = receiveAsset, style = MaterialTheme.typography.headlineMedium, color = md_theme_dark_onSurface)
              Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = null, tint = md_theme_dark_onSurfaceVariant)
            }
            DropdownMenu(
              expanded = receiveDropdownExpanded,
              onDismissRequest = { receiveDropdownExpanded = false }
            ) {
              availableAssets.forEach { asset ->
                DropdownMenuItem(
                  text = { Text(asset) },
                  onClick = {
                    receiveAsset = asset
                    receiveDropdownExpanded = false
                  }
                )
              }
            }
          }
          Column(horizontalAlignment = Alignment.End) {
            Text(text = String.format(Locale.US, "%.4f", outputAmount), style = DataLarge.copy(fontSize = 40.sp), color = secondary_fixed_dim)
            Text(text = rateText, style = DataMedium, color = md_theme_dark_onSurfaceVariant)
          }
        }
      }

      Spacer(modifier = Modifier.height(height = 16.dp))
      
      Row(
        modifier = Modifier.fillMaxWidth(fraction = 1.0f),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(imageVector = Icons.Filled.Tune, contentDescription = null, tint = md_theme_dark_onSurfaceVariant, modifier = Modifier.size(size = 18.dp))
          Spacer(modifier = Modifier.width(width = 4.dp))
          Text(text = "Slippage Tolerance", style = MaterialTheme.typography.labelSmall, color = md_theme_dark_onSurfaceVariant)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          Box(modifier = Modifier.background(color = surface_container_highest, shape = RoundedCornerShape(size = 4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) { Text(text = "0.1%", style = DataMedium) }
          Box(modifier = Modifier.background(color = primary_fixed_dim, shape = RoundedCornerShape(size = 4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) { Text(text = "0.5%", style = DataMedium, color = Color(color = 0xFF001F24)) }
          Box(modifier = Modifier.background(color = surface_container_highest, shape = RoundedCornerShape(size = 4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) { Text(text = "1.0%", style = DataMedium) }
        }
      }

      Spacer(modifier = Modifier.weight(weight = 1.0f))

      // Keypad Input Buttons
      Column(
        modifier = Modifier.fillMaxWidth(fraction = 1.0f),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
      ) {
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          KeypadButton(text = "1", modifier = Modifier.weight(1f)) { handleKeypadInput("1") }
          KeypadButton(text = "2", modifier = Modifier.weight(1f)) { handleKeypadInput("2") }
          KeypadButton(text = "3", modifier = Modifier.weight(1f)) { handleKeypadInput("3") }
        }
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          KeypadButton(text = "4", modifier = Modifier.weight(1f)) { handleKeypadInput("4") }
          KeypadButton(text = "5", modifier = Modifier.weight(1f)) { handleKeypadInput("5") }
          KeypadButton(text = "6", modifier = Modifier.weight(1f)) { handleKeypadInput("6") }
        }
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          KeypadButton(text = "7", modifier = Modifier.weight(1f)) { handleKeypadInput("7") }
          KeypadButton(text = "8", modifier = Modifier.weight(1f)) { handleKeypadInput("8") }
          KeypadButton(text = "9", modifier = Modifier.weight(1f)) { handleKeypadInput("9") }
        }
        Row(modifier = Modifier.fillMaxWidth(fraction = 1.0f), horizontalArrangement = Arrangement.spacedBy(space = 8.dp)) {
          KeypadButton(text = ".", modifier = Modifier.weight(1f)) { handleKeypadInput(".") }
          KeypadButton(text = "0", modifier = Modifier.weight(1f)) { handleKeypadInput("0") }
          KeypadButton(text = "DEL", modifier = Modifier.weight(1f)) { handleKeypadInput("DEL") }
        }
      }

      Spacer(modifier = Modifier.height(height = 16.dp))

      // Swipe to Swap / Swap Action Button
      Box(
        modifier = Modifier
          .fillMaxWidth(fraction = 1.0f)
          .height(height = 56.dp)
          .background(color = primary_fixed_dim, shape = RoundedCornerShape(size = 28.dp))
          .clickable {
            tradeErrorMessage = null
            showOrderSheet = true
          },
        contentAlignment = Alignment.Center
      ) {
        Text(
          text = "SWAP $payAsset FOR $receiveAsset",
          style = MaterialTheme.typography.labelLarge.copy(fontWeight = androidx.compose.ui.text.font.FontWeight.Bold),
          color = Color(0xFF001F24)
        )
      }
      Spacer(modifier = Modifier.height(height = 24.dp))
    }

    if (showOrderSheet) {
      OrderConfirmationSheet(
        sheetState = sheetState,
        onDismissRequest = {
          showOrderSheet = false
          tradeErrorMessage = null
        },
        onConfirm = {
          if (inputAmount <= 0.0) {
            tradeErrorMessage = "Trade amount must be greater than zero"
          } else if (inputAmount > payBalance) {
            tradeErrorMessage = "Insufficient balance for $payAsset: available ${String.format(Locale.US, "%.4f", payBalance)}, required $inputAmountText"
          } else {
            coroutineScope.launch {
              val result = viewModel.executeTrade(payAsset, receiveAsset, inputAmount)
              if (result.isSuccess) {
                Toast.makeText(context, "Trade executed successfully!", Toast.LENGTH_SHORT).show()
                showOrderSheet = false
                inputAmountText = "0"
                tradeErrorMessage = null
              } else {
                tradeErrorMessage = result.exceptionOrNull()?.message ?: "Trade failed"
              }
            }
          }
        },
        payAsset = payAsset,
        receiveAsset = receiveAsset,
        payAmount = inputAmountText,
        receiveAmount = String.format(Locale.US, "%.4f", outputAmount),
        rate = rateText,
        total = "$${String.format(Locale.US, "%.2f", inputAmount * payPrice)}",
        errorMessage = tradeErrorMessage
      )
    }
  }
}

@Composable
public fun KeypadButton(
  text: String,
  modifier: Modifier = Modifier,
  onClick: () -> Unit = {}
): Unit {
  Box(
    modifier = modifier
      .height(height = 56.dp)
      .background(color = surface_container_low, shape = RoundedCornerShape(size = 12.dp))
      .clickable { onClick() },
    contentAlignment = Alignment.Center
  ) {
    Text(
      text = text,
      style = DataLarge.copy(fontSize = 24.sp),
      color = md_theme_dark_onSurface
    )
  }
}
