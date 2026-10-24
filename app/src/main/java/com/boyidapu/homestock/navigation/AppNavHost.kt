package com.boyidapu.homestock.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.boyidapu.homestock.app.AppContainer
import com.boyidapu.homestock.ui.archived.ArchivedProductsScreen
import com.boyidapu.homestock.ui.archived.ArchivedProductsViewModel
import com.boyidapu.homestock.ui.category.CategoryScreen
import com.boyidapu.homestock.ui.category.CategoryViewModel
import com.boyidapu.homestock.ui.history.ProductHistoryScreen
import com.boyidapu.homestock.ui.history.ProductHistoryViewModel
import com.boyidapu.homestock.ui.home.HomeScreen
import com.boyidapu.homestock.ui.home.HomeViewModel
import com.boyidapu.homestock.ui.insights.InsightsScreen
import com.boyidapu.homestock.ui.insights.InsightsViewModel
import com.boyidapu.homestock.ui.onboarding.WelcomeScreen
import com.boyidapu.homestock.ui.onboarding.WelcomeViewModel
import com.boyidapu.homestock.ui.product.CreateProductScreen
import com.boyidapu.homestock.ui.product.CreateProductViewModel
import com.boyidapu.homestock.ui.product.EditProductScreen
import com.boyidapu.homestock.ui.product.EditProductViewModel
import com.boyidapu.homestock.ui.settings.SettingsScreen
import com.boyidapu.homestock.ui.settings.SettingsViewModel
import com.boyidapu.homestock.ui.transaction.AddTransactionScreen
import com.boyidapu.homestock.ui.transaction.AddTransactionViewModel
import com.boyidapu.homestock.ui.transaction.UseTransactionScreen
import com.boyidapu.homestock.ui.transaction.UseTransactionViewModel

@Composable
fun HomeStockAppNavHost(
    appContainer: AppContainer,
    modifier: Modifier = Modifier
) {
    val isOnboardingCompleted by appContainer.userRepository.isOnboardingCompleted.collectAsState(initial = null)

    // While loading onboarding status from DataStore, render clean background to prevent startup flash
    if (isOnboardingCompleted == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
        return
    }

    val initialScreen: NavKey = if (isOnboardingCompleted == true) Screen.Home else Screen.Welcome
    val backStack = rememberNavBackStack(initialScreen)

    val entries = entryProvider<NavKey> {
        entry<Screen.Welcome> {
            val welcomeViewModel: WelcomeViewModel = viewModel(
                factory = viewModelFactory {
                    initializer { WelcomeViewModel(appContainer.userRepository) }
                }
            )
            WelcomeScreen(
                viewModel = welcomeViewModel,
                onOnboardingComplete = {
                    backStack.clear()
                    backStack += Screen.Home
                }
            )
        }

        entry<Screen.Home> {
            val homeViewModel: HomeViewModel = viewModel(
                factory = viewModelFactory {
                    initializer {
                        HomeViewModel(
                            appContainer.userRepository,
                            appContainer.transactionRepository
                        )
                    }
                }
            )
            HomeScreen(
                viewModel = homeViewModel,
                onNavigateToCategory = { categoryId ->
                    backStack += Screen.CategoryProducts(categoryId)
                },
                onNavigateToInsights = {
                    backStack += Screen.Insights
                },
                onNavigateToSettings = {
                    backStack += Screen.Settings
                }
            )
        }

        entry<Screen.CategoryProducts> { key ->
            val categoryViewModel: CategoryViewModel = viewModel(
                key = "category_${key.categoryId}",
                factory = viewModelFactory {
                    initializer {
                        CategoryViewModel(
                            key.categoryId,
                            appContainer.productRepository
                        )
                    }
                }
            )
            CategoryScreen(
                viewModel = categoryViewModel,
                onBack = {
                    if (backStack.isNotEmpty()) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                },
                onAddProduct = { categoryId ->
                    backStack += Screen.CreateProduct(categoryId)
                },
                onEditProduct = { productId ->
                    backStack += Screen.EditProduct(productId)
                },
                onNavigateToHistory = { productId ->
                    backStack += Screen.ProductHistory(productId)
                },
                onNavigateToAddTransaction = { productId ->
                    backStack += Screen.AddTransaction(productId)
                },
                onNavigateToUseTransaction = { productId ->
                    backStack += Screen.UseTransaction(productId)
                }
            )
        }

        entry<Screen.ProductHistory> { key ->
            val historyViewModel: ProductHistoryViewModel = viewModel(
                key = "history_${key.productId}",
                factory = viewModelFactory {
                    initializer {
                        ProductHistoryViewModel(
                            key.productId,
                            appContainer.productRepository,
                            appContainer.transactionRepository
                        )
                    }
                }
            )
            LaunchedEffect(key.productId) {
                historyViewModel.loadHistoryData()
            }
            ProductHistoryScreen(
                viewModel = historyViewModel,
                onBack = {
                    if (backStack.isNotEmpty()) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }
            )
        }

        entry<Screen.CreateProduct> { key ->
            val createProductViewModel: CreateProductViewModel = viewModel(
                key = "create_product_${key.categoryId}",
                factory = viewModelFactory {
                    initializer {
                        CreateProductViewModel(
                            key.categoryId,
                            appContainer.productRepository
                        )
                    }
                }
            )
            CreateProductScreen(
                viewModel = createProductViewModel,
                onBack = {
                    if (backStack.isNotEmpty()) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }
            )
        }

        entry<Screen.EditProduct> { key ->
            val editProductViewModel: EditProductViewModel = viewModel(
                key = "edit_product_${key.productId}",
                factory = viewModelFactory {
                    initializer {
                        EditProductViewModel(
                            key.productId,
                            appContainer.productRepository
                        )
                    }
                }
            )
            LaunchedEffect(key.productId) {
                editProductViewModel.loadProduct()
            }
            EditProductScreen(
                viewModel = editProductViewModel,
                onBack = {
                    if (backStack.isNotEmpty()) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }
            )
        }

        entry<Screen.AddTransaction> { key ->
            val addViewModel: AddTransactionViewModel = viewModel(
                key = "add_tx_${key.productId}",
                factory = viewModelFactory {
                    initializer {
                        AddTransactionViewModel(
                            key.productId,
                            appContainer.productRepository,
                            appContainer.transactionRepository
                        )
                    }
                }
            )
            AddTransactionScreen(
                viewModel = addViewModel,
                onBack = {
                    if (backStack.isNotEmpty()) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }
            )
        }

        entry<Screen.UseTransaction> { key ->
            val useViewModel: UseTransactionViewModel = viewModel(
                key = "use_tx_${key.productId}",
                factory = viewModelFactory {
                    initializer {
                        UseTransactionViewModel(
                            key.productId,
                            appContainer.productRepository,
                            appContainer.transactionRepository
                        )
                    }
                }
            )
            UseTransactionScreen(
                viewModel = useViewModel,
                onBack = {
                    if (backStack.isNotEmpty()) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }
            )
        }

        entry<Screen.Insights> {
            val insightsViewModel: InsightsViewModel = viewModel(
                factory = viewModelFactory {
                    initializer { InsightsViewModel(appContainer.transactionRepository) }
                }
            )
            InsightsScreen(
                viewModel = insightsViewModel,
                onBack = {
                    if (backStack.isNotEmpty()) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }
            )
        }

        entry<Screen.Settings> {
            val settingsViewModel: SettingsViewModel = viewModel(
                factory = viewModelFactory {
                    initializer { SettingsViewModel(appContainer.userRepository) }
                }
            )
            SettingsScreen(
                viewModel = settingsViewModel,
                onBack = {
                    if (backStack.isNotEmpty()) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                },
                onNavigateToArchivedProducts = {
                    backStack += Screen.ArchivedProducts
                }
            )
        }

        entry<Screen.ArchivedProducts> {
            val archivedViewModel: ArchivedProductsViewModel = viewModel(
                factory = viewModelFactory {
                    initializer { ArchivedProductsViewModel(appContainer.productRepository) }
                }
            )
            ArchivedProductsScreen(
                viewModel = archivedViewModel,
                onBack = {
                    if (backStack.isNotEmpty()) {
                        backStack.removeAt(backStack.lastIndex)
                    }
                }
            )
        }
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entries,
        modifier = modifier.fillMaxSize()
    )
}
