package com.boyidapu.homestock.ui.transaction

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boyidapu.homestock.core.dates.DateUtils
import com.boyidapu.homestock.domain.model.UnitType
import com.boyidapu.homestock.ui.theme.CleanWhite
import com.boyidapu.homestock.ui.theme.DeepTeal
import com.boyidapu.homestock.ui.theme.ForestCharcoal
import com.boyidapu.homestock.ui.theme.MutedSageGray
import com.boyidapu.homestock.ui.theme.SoftSage
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UseTransactionScreen(
    viewModel: UseTransactionViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val product = uiState.product

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UseTransactionEvent.SaveSuccess -> onBack()
                is UseTransactionEvent.ShowError -> {}
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Record Usage (Use)",
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
                .statusBarsPadding()
                .imePadding()
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            if (product != null) {
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
                            text = product.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepTeal
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Category: ${product.category.displayName} • ${product.dimension.displayName}",
                            fontSize = 13.sp,
                            color = MutedSageGray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Current Available Stock Banner
                        val isZeroStock = uiState.stockBaseQuantity <= 0L
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isZeroStock) MaterialTheme.colorScheme.errorContainer else SoftSage
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = if (isZeroStock) "No stock available" else "Available Stock",
                                    fontSize = 12.sp,
                                    color = if (isZeroStock) MaterialTheme.colorScheme.onErrorContainer else MutedSageGray
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = if (isZeroStock) "Current stock is 0. Record a purchase (Add) first." else uiState.formattedCurrentStock,
                                    fontSize = if (isZeroStock) 14.sp else 22.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isZeroStock) MaterialTheme.colorScheme.onErrorContainer else DeepTeal
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Quantity Input
                        OutlinedTextField(
                            value = uiState.quantityInput,
                            onValueChange = { viewModel.onQuantityInputChanged(it) },
                            label = { Text("Quantity to Use") },
                            placeholder = { Text("e.g. 1 or 0.5") },
                            isError = uiState.quantityError != null,
                            enabled = !isZeroStock,
                            singleLine = true,
                            supportingText = {
                                if (uiState.quantityError != null) {
                                    Text(
                                        text = uiState.quantityError!!,
                                        color = MaterialTheme.colorScheme.error,
                                        fontSize = 12.sp
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepTeal,
                                focusedLabelColor = DeepTeal,
                                cursorColor = DeepTeal
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Quantity Unit Selection
                        Text(
                            text = "Unit",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ForestCharcoal
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        val availableUnits = UnitType.forDimension(product.dimension)
                        Row(modifier = Modifier.fillMaxWidth()) {
                            availableUnits.forEach { unit ->
                                FilterChip(
                                    selected = uiState.selectedUnit == unit,
                                    onClick = { viewModel.onUnitSelected(unit) },
                                    enabled = !isZeroStock,
                                    label = { Text("${unit.symbol} (${unit.displayName})") },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DeepTeal,
                                        selectedLabelColor = CleanWhite,
                                        containerColor = SoftSage
                                    ),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // Usage Date Selector
                        Text(
                            text = "Usage Date",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ForestCharcoal
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        val dateFormatted = DateUtils.formatEpochDay(uiState.usageDate.toEpochDay())
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SoftSage),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(enabled = !isZeroStock) {
                                    val current = uiState.usageDate
                                    val picker = DatePickerDialog(
                                        context,
                                        { _, year, month, dayOfMonth ->
                                            val selected = LocalDate.of(year, month + 1, dayOfMonth)
                                            viewModel.onDateSelected(selected)
                                        },
                                        current.year,
                                        current.monthValue - 1,
                                        current.dayOfMonth
                                    )
                                    picker.datePicker.maxDate = System.currentTimeMillis()
                                    picker.show()
                                }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = dateFormatted,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal
                                )

                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select Date",
                                    tint = DeepTeal
                                )
                            }
                        }

                        if (uiState.dateError != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.dateError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        // Submit Button
                        Button(
                            onClick = { viewModel.saveUseTransaction() },
                            enabled = uiState.canSave,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepTeal,
                                contentColor = CleanWhite,
                                disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                                disabledContentColor = MutedSageGray
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                        ) {
                            if (uiState.isSaving) {
                                CircularProgressIndicator(
                                    color = CleanWhite,
                                    modifier = Modifier.size(24.dp)
                                )
                            } else {
                                Text(
                                    text = "Record Usage",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
