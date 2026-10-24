package com.boyidapu.homestock.ui.insights

import android.app.DatePickerDialog
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boyidapu.homestock.core.dates.DateUtils
import com.boyidapu.homestock.core.money.MoneyUtils
import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.domain.model.SpendingPeriod
import com.boyidapu.homestock.ui.theme.CleanWhite
import com.boyidapu.homestock.ui.theme.DeepTeal
import com.boyidapu.homestock.ui.theme.ForestCharcoal
import com.boyidapu.homestock.ui.theme.MutedSageGray
import com.boyidapu.homestock.ui.theme.SoftSage
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Spending Insights",
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = DeepTeal
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // 2. Global Date Filters (Horizontal Scrollable Row)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SpendingPeriod.entries.forEach { period ->
                    FilterChip(
                        selected = uiState.selectedPeriod == period,
                        onClick = { viewModel.selectPeriod(period) },
                        label = { Text(period.displayName) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = DeepTeal,
                            selectedLabelColor = CleanWhite,
                            containerColor = SoftSage,
                            labelColor = ForestCharcoal
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Custom Range Date Selector Card (when CUSTOM selected)
            if (uiState.selectedPeriod == SpendingPeriod.CUSTOM) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Select Custom Date Range",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestCharcoal
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            val startFormatted = DateUtils.formatEpochDay(uiState.customStart.toEpochDay())
                            OutlinedButton(
                                onClick = {
                                    val current = uiState.customStart
                                    val picker = DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            val selected = LocalDate.of(year, month + 1, dayOfMonth)
                                            viewModel.onCustomDatesSelected(selected, uiState.customEnd)
                                        },
                                        current.year,
                                        current.monthValue - 1,
                                        current.dayOfMonth
                                    )
                                    picker.datePicker.maxDate = System.currentTimeMillis()
                                    picker.show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "From Date",
                                        tint = DeepTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("From: $startFormatted", fontSize = 12.sp, color = DeepTeal)
                                }
                            }

                            val endFormatted = DateUtils.formatEpochDay(uiState.customEnd.toEpochDay())
                            OutlinedButton(
                                onClick = {
                                    val current = uiState.customEnd
                                    val picker = DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            val selected = LocalDate.of(year, month + 1, dayOfMonth)
                                            viewModel.onCustomDatesSelected(uiState.customStart, selected)
                                        },
                                        current.year,
                                        current.monthValue - 1,
                                        current.dayOfMonth
                                    )
                                    picker.datePicker.maxDate = System.currentTimeMillis()
                                    picker.show()
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "To Date",
                                        tint = DeepTeal,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("To: $endFormatted", fontSize = 12.sp, color = DeepTeal)
                                }
                            }
                        }

                        if (uiState.customRangeError != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = uiState.customRangeError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // 3. Spending Summary Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Total purchase spending",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ForestCharcoal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    val formattedAmount = MoneyUtils.formatPaiseToFormattedAmount(uiState.totalSpendingPaise)
                    Text(
                        text = formattedAmount,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = uiState.dateRangeText,
                        fontSize = 13.sp,
                        color = MutedSageGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Purchases", fontSize = 11.sp, color = MutedSageGray)
                            Text(
                                text = "${uiState.purchaseCount}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text("Average per purchase", fontSize = 11.sp, color = MutedSageGray)
                            Text(
                                text = uiState.formattedAveragePerPurchase,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal
                            )
                        }
                    }

                    if (uiState.purchaseCount == 0) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "No purchases recorded for this period.",
                            fontSize = 12.sp,
                            color = MutedSageGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 4. Spending Trend Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Spending trend",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestCharcoal
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val maxSpend = uiState.trendBuckets.maxOfOrNull { it.totalSpendingPaise } ?: 0L
                    if (maxSpend == 0L) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No purchase spending in this period to plot.",
                                fontSize = 13.sp,
                                color = MutedSageGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        // Bar Chart Layout
                        var selectedBucketIndex by remember { mutableStateOf<Int?>(null) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(140.dp)
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.Bottom
                        ) {
                            uiState.trendBuckets.forEachIndexed { index, bucket ->
                                val fraction = if (maxSpend > 0L) bucket.totalSpendingPaise.toFloat() / maxSpend.toFloat() else 0f
                                val animatedHeightFraction by animateFloatAsState(targetValue = fraction, label = "barHeight")
                                val isSelected = selectedBucketIndex == index

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .clickable { selectedBucketIndex = if (isSelected) null else index }
                                        .semantics {
                                            contentDescription = "${bucket.label}: ${MoneyUtils.formatPaiseToFormattedAmount(bucket.totalSpendingPaise)}"
                                        }
                                ) {
                                    if (isSelected || (bucket.totalSpendingPaise > 0L && uiState.trendBuckets.size <= 7)) {
                                        Text(
                                            text = MoneyUtils.formatPaiseToFormattedAmount(bucket.totalSpendingPaise),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = DeepTeal
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                    }

                                    Box(
                                        modifier = Modifier
                                            .width(28.dp)
                                            .height((100 * animatedHeightFraction).dp.coerceAtLeast(4.dp))
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                                            .background(if (isSelected) DeepTeal else SoftSage)
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = bucket.label,
                                        fontSize = 10.sp,
                                        color = MutedSageGray
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 5. Spending by Category Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Spending by category",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestCharcoal
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val allCategories = FixedCategory.entries
                    allCategories.forEach { category ->
                        val catSpending = uiState.categorySpendings.firstOrNull { it.category == category }
                        val amountPaise = catSpending?.totalSpendingPaise ?: 0L
                        val pct = catSpending?.percentageShare ?: 0f

                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = category.displayName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = ForestCharcoal
                                )

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = MoneyUtils.formatPaiseToFormattedAmount(amountPaise),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${String.format(Locale.getDefault(), "%.1f", pct)}%",
                                        fontSize = 12.sp,
                                        color = MutedSageGray
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            LinearProgressIndicator(
                                progress = { (pct / 100f).coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = DeepTeal,
                                trackColor = SoftSage
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 6. Top Spending Products Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Top spending products",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestCharcoal
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (uiState.topProductSpendings.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No positive-spending products in this period.",
                                fontSize = 13.sp,
                                color = MutedSageGray,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            uiState.topProductSpendings.forEachIndexed { rank, prod ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "#${rank + 1}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal,
                                        modifier = Modifier.width(28.dp)
                                    )

                                    Column(modifier = Modifier.weight(1f)) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(
                                                text = prod.productName,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = ForestCharcoal
                                            )

                                            if (prod.isArchived) {
                                                Spacer(modifier = Modifier.width(6.dp))
                                                Box(
                                                    modifier = Modifier
                                                        .background(SoftSage, shape = RoundedCornerShape(4.dp))
                                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                                ) {
                                                    Text(
                                                        text = "Archived",
                                                        fontSize = 10.sp,
                                                        color = DeepTeal,
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                }
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(2.dp))

                                        Text(
                                            text = prod.category.displayName,
                                            fontSize = 12.sp,
                                            color = MutedSageGray
                                        )
                                    }

                                    Text(
                                        text = MoneyUtils.formatPaiseToFormattedAmount(prod.totalSpendingPaise),
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DeepTeal
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
