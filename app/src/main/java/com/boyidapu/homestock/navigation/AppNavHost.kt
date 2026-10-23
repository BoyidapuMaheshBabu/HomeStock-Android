package com.boyidapu.homestock.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.boyidapu.homestock.R
import com.boyidapu.homestock.ui.theme.DeepTeal
import com.boyidapu.homestock.ui.theme.ForestCharcoal
import com.boyidapu.homestock.ui.theme.MutedSageGray

@Composable
fun HomeStockAppNavHost(
    modifier: Modifier = Modifier
) {
    val backStack = rememberNavBackStack(Screen.Home)
    val entries = entryProvider<NavKey> {
        entry<Screen.Home> {
            FoundationHomeScreen()
        }
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entries,
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun FoundationHomeScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = DeepTeal
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.telugu_app_name),
                fontSize = 24.sp,
                fontWeight = FontWeight.SemiBold,
                color = DeepTeal
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(id = R.string.telugu_app_caption),
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                color = MutedSageGray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.app_tagline),
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = ForestCharcoal
            )
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = stringResource(id = R.string.status_phase_01),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = DeepTeal
            )
        }
    }
}
