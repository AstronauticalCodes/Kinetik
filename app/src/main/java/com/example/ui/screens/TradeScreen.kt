package com.example.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AssetBadge
import com.example.ui.components.Keypad
import com.example.ui.components.SwapSlider
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

  var inputAmountText by remember { mutableStateOf("0.5") }
  var payAsset by remember { mutableStateOf("BTC") }
  var receiveAsset by remember { mutableStateOf("USDT") }
  var payDropdownExpanded by remember { mutableStateOf(false) }
  var receiveDropdownExpanded by remember { mutableStateOf(false) }
  var showOrderSheet by remember { mutableStateOf(false) }
  var tradeErrorMessage by remember { mutableStateOf<String?>(null) }

  val availableAssets = listOf("USD", "BTC", "ETH", "SOL", "BNB", "USDT", "USDC")

  val payHolding = uiState.holdings.find { it.assetSymbol.uppercase(Locale.ROOT) == payAsset.uppercase(Locale.ROOT) }
  val receiveHolding = uiState.holdings.find { it.assetSymbol.uppercase(Locale.ROOT) == receiveAsset.uppercase(Locale.ROOT) }

  val payBalance = payHolding?.amount ?: 1.8420
  val receiveBalance = receiveHolding?.amount ?: 4218.40

  val priceMap = uiState.tickers.associate {
    val clean = it.symbol.replace("USDT", "")
    clean.uppercase(Locale.ROOT) to (it.lastPrice.toDoubleOrNull() ?: 0.0)
  }

  fun getAssetPrice(symbol: String): Double {
    val upper = symbol.uppercase(Locale.ROOT)
    if (upper == "USD" || upper == "USDC" || upper == "USDT") return 1.0
    val p = priceMap[upper]
    if (p != null && p > 0.0) return p
    return when (upper) {
      "BTC" -> 67420.00
      "ETH" -> 3512.80
      "SOL" -> 184.20
      "BNB" -> 578.40
      else -> 1.0
    }
  }

  val payPrice = getAssetPrice(payAsset)
  val receivePrice = getAssetPrice(receiveAsset)

  val inputAmount = inputAmountText.toDoubleOrNull() ?: 0.0
  val outputAmount = if (receivePrice > 0.0) (inputAmount * payPrice) / receivePrice else 0.0

  val rateText = if (receivePrice > 0.0) {
    val rate = payPrice / receivePrice
    "1 $payAsset ≈ ${String.format(Locale.US, "%.2f", rate)} $receiveAsset"
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

  fun executeSwap() {
    if (inputAmount <= 0.0) {
      tradeErrorMessage = "Trade amount must be greater than zero"
      Toast.makeText(context, tradeErrorMessage, Toast.LENGTH_SHORT).show()
    } else if (inputAmount > payBalance) {
      tradeErrorMessage = "Insufficient balance for $payAsset"
      Toast.makeText(context, tradeErrorMessage, Toast.LENGTH_SHORT).show()
    } else {
      coroutineScope.launch {
        val result = viewModel.executeTrade(payAsset, receiveAsset, inputAmount)
        if (result.isSuccess) {
          Toast.makeText(context, "Swap executed successfully!", Toast.LENGTH_SHORT).show()
          inputAmountText = "0"
          tradeErrorMessage = null
        } else {
          tradeErrorMessage = result.exceptionOrNull()?.message ?: "Swap failed"
          Toast.makeText(context, tradeErrorMessage, Toast.LENGTH_SHORT).show()
        }
      }
    }
  }

  Scaffold(
    modifier = modifier.fillMaxSize(),
    containerColor = Color(0xFFFAFAFA),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Swap",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
          )
        },
        actions = {
          IconButton(onClick = { }) {
            Icon(
              imageVector = Icons.Default.Tune,
              contentDescription = "Tune",
              tint = Color(0xFF0F172A)
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = Color.Transparent
        )
      )
    }
  ) { innerPadding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .verticalScroll(rememberScrollState())
        .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
      Box(modifier = Modifier.fillMaxWidth()) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .background(Color.White)
              .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
              .padding(16.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "You Pay",
                  fontSize = 13.sp,
                  color = Color(0xFF64748B)
                )
                Text(
                  text = "Balance: ${String.format(Locale.US, "%.4f", payBalance)} $payAsset",
                  fontSize = 13.sp,
                  color = Color(0xFF64748B)
                )
              }
              Spacer(modifier = Modifier.height(12.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = inputAmountText,
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0F172A)
                )

                Box {
                  Row(
                    modifier = Modifier
                      .clip(CircleShape)
                      .background(Color(0xFFF0F3FA))
                      .clickable { payDropdownExpanded = true }
                      .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    AssetBadge(symbol = payAsset, size = 24.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = payAsset,
                      fontSize = 15.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                      imageVector = Icons.Default.ExpandMore,
                      contentDescription = null,
                      tint = Color(0xFF64748B),
                      modifier = Modifier.size(18.dp)
                    )
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
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = "≈ $${String.format(Locale.US, "%,.2f", inputAmount * payPrice)}",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
              )
            }
          }

          Box(
            modifier = Modifier
              .fillMaxWidth()
              .clip(RoundedCornerShape(20.dp))
              .background(Color.White)
              .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(20.dp))
              .padding(16.dp)
          ) {
            Column {
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = "You Receive",
                  fontSize = 13.sp,
                  color = Color(0xFF64748B)
                )
                Text(
                  text = "Balance: ${String.format(Locale.US, "%,.2f", receiveBalance)} $receiveAsset",
                  fontSize = 13.sp,
                  color = Color(0xFF64748B)
                )
              }
              Spacer(modifier = Modifier.height(12.dp))
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Text(
                  text = String.format(Locale.US, "%,.2f", outputAmount),
                  fontSize = 32.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFF0F172A)
                )

                Box {
                  Row(
                    modifier = Modifier
                      .clip(CircleShape)
                      .background(Color(0xFFF0F3FA))
                      .clickable { receiveDropdownExpanded = true }
                      .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                  ) {
                    AssetBadge(symbol = receiveAsset, size = 24.dp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                      text = receiveAsset,
                      fontSize = 15.sp,
                      fontWeight = FontWeight.Bold,
                      color = Color(0xFF0F172A)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                      imageVector = Icons.Default.ExpandMore,
                      contentDescription = null,
                      tint = Color(0xFF64748B),
                      modifier = Modifier.size(18.dp)
                    )
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
              }
              Spacer(modifier = Modifier.height(6.dp))
              Text(
                text = rateText,
                fontSize = 12.sp,
                color = Color(0xFF64748B)
              )
            }
          }
        }

        Box(
          modifier = Modifier
            .align(Alignment.Center)
            .size(44.dp)
            .clip(CircleShape)
            .background(Color(0xFF1E60F9))
            .clickable {
              val temp = payAsset
              payAsset = receiveAsset
              receiveAsset = temp
            },
          contentAlignment = Alignment.Center
        ) {
          Icon(
            imageVector = Icons.Default.SwapVert,
            contentDescription = "Swap Direction",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Box(
        modifier = Modifier
          .fillMaxWidth()
          .clip(RoundedCornerShape(16.dp))
          .background(Color(0xFFF8FAFC))
          .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
          .padding(16.dp)
      ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Price Impact", fontSize = 13.sp, color = Color(0xFF64748B))
            Text(text = "< 0.01%", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
          }
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Network Fee", fontSize = 13.sp, color = Color(0xFF64748B))
            Text(text = "~$2.40", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
          }
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Text(text = "Route", fontSize = 13.sp, color = Color(0xFF64748B))
            Text(text = "$payAsset → $receiveAsset (1 hop)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E60F9))
          }
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      Keypad(
        onKeyClick = { handleKeypadInput(it) },
        onBackspaceClick = { handleKeypadInput("DEL") }
      )

      Spacer(modifier = Modifier.height(20.dp))

      SwapSlider(
        text = "Slide to Confirm Swap",
        onConfirm = { executeSwap() },
        enabled = inputAmount > 0.0
      )

      Spacer(modifier = Modifier.height(16.dp))
    }

    if (showOrderSheet) {
      OrderConfirmationSheet(
        sheetState = sheetState,
        onDismissRequest = {
          showOrderSheet = false
          tradeErrorMessage = null
        },
        onConfirm = {
          executeSwap()
          showOrderSheet = false
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
