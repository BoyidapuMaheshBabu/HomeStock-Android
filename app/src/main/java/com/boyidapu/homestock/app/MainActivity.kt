package com.boyidapu.homestock.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.boyidapu.homestock.navigation.HomeStockAppNavHost
import com.boyidapu.homestock.ui.theme.IntiVentoryTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IntiVentoryTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    HomeStockAppNavHost()
                }
            }
        }
    }
}
