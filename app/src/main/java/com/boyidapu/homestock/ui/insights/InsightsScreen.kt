package com.boyidapu.homestock.ui.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boyidapu.homestock.core.money.MoneyUtils
import com.boyidapu.homestock.ui.theme.CleanWhite
import com.boyidapu.homestock.ui.theme.DeepTeal
import com.boyidapu.homestock.ui.theme.ForestCharcoal
import com.boyidapu.homestock.ui.theme.MutedSageGray
import com.boyidapu.homestock.ui.theme.SoftSage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    viewModel: InsightsViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

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
                .padding(20.dp)
        ) {
            // Selected Filter Pill
            Box(
                modifier = Modifier
                    .background(SoftSage, shape = RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "Filter: This Month",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepTeal
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Summary Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "This Month's Spending",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestCharcoal
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = uiState.dateRangeText,
                        fontSize = 13.sp,
                        color = MutedSageGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    val formattedAmount = MoneyUtils.formatPaiseToFormattedAmount(uiState.totalSpendingPaise)
                    Text(
                        text = formattedAmount,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )

                    if (uiState.totalSpendingPaise == 0L) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "No purchases recorded for this period",
                            fontSize = 13.sp,
                            color = MutedSageGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Phase 06 Notice Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Detailed date range filters (Last 7 days, Last 3 months, Custom range) and spending graphs will be available in Phase 06.",
                        fontSize = 13.sp,
                        color = MutedSageGray
                    )
                }
            }
        }
    }
}
