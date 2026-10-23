package com.boyidapu.homestock.ui.home

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boyidapu.homestock.core.money.MoneyUtils
import com.boyidapu.homestock.domain.model.FixedCategory
import com.boyidapu.homestock.ui.theme.CleanWhite
import com.boyidapu.homestock.ui.theme.DeepTeal
import com.boyidapu.homestock.ui.theme.ForestCharcoal
import com.boyidapu.homestock.ui.theme.MutedSageGray
import com.boyidapu.homestock.ui.theme.SoftSage

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onNavigateToCategory: (String) -> Unit,
    onNavigateToInsights: () -> Unit,
    onNavigateToSettings: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        // Top row: Greeting and Settings Gear
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = uiState.greeting,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepTeal
                )
            }

            IconButton(
                onClick = onNavigateToSettings,
                modifier = Modifier
                    .background(CleanWhite, CircleShape)
                    .size(44.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    tint = DeepTeal
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // This Month Spending Card
        Card(
            colors = CardDefaults.cardColors(containerColor = CleanWhite),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onNavigateToInsights() }
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "This month spending",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ForestCharcoal
                    )

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "View Insights",
                        tint = MutedSageGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = uiState.thisMonthDateRange,
                    fontSize = 13.sp,
                    color = MutedSageGray
                )

                Spacer(modifier = Modifier.height(14.dp))

                val formattedSpending = MoneyUtils.formatPaiseToFormattedAmount(uiState.thisMonthSpendingPaise)
                Text(
                    text = formattedSpending,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepTeal
                )

                if (uiState.thisMonthSpendingPaise == 0L) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "No purchases recorded this month",
                        fontSize = 13.sp,
                        color = MutedSageGray
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Three Fixed Category Hero Cards
        Text(
            text = "Categories",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ForestCharcoal
        )

        Spacer(modifier = Modifier.height(14.dp))

        CategoryHeroCard(
            category = FixedCategory.FOOD_KITCHEN,
            icon = Icons.Default.ShoppingCart,
            subtitle = "Rice, Dal, Flour, Oil, Spices...",
            onClick = { onNavigateToCategory(FixedCategory.FOOD_KITCHEN.id) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CategoryHeroCard(
            category = FixedCategory.HOME_CARE_CLEANING,
            icon = Icons.Default.Home,
            subtitle = "Cleaner, Liquid, Detergent, Disinfectant...",
            onClick = { onNavigateToCategory(FixedCategory.HOME_CARE_CLEANING.id) }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CategoryHeroCard(
            category = FixedCategory.PERSONAL_CARE_COSMETICS,
            icon = Icons.Default.Face,
            subtitle = "Soap, Shampoo, Toothpaste, Hair Oil, Cream...",
            onClick = { onNavigateToCategory(FixedCategory.PERSONAL_CARE_COSMETICS.id) }
        )
    }
}

@Composable
private fun CategoryHeroCard(
    category: FixedCategory,
    icon: ImageVector,
    subtitle: String,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(SoftSage, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = category.displayName,
                    tint = DeepTeal,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = category.displayName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestCharcoal
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = subtitle,
                    fontSize = 13.sp,
                    color = MutedSageGray
                )
            }
        }
    }
}
