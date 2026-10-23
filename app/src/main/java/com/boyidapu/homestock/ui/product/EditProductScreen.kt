package com.boyidapu.homestock.ui.product

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boyidapu.homestock.domain.model.MeasurementDimension
import com.boyidapu.homestock.ui.theme.CleanWhite
import com.boyidapu.homestock.ui.theme.DeepTeal
import com.boyidapu.homestock.ui.theme.ForestCharcoal
import com.boyidapu.homestock.ui.theme.MutedSageGray
import com.boyidapu.homestock.ui.theme.SoftSage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProductScreen(
    viewModel: EditProductViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val product = uiState.product

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is EditProductEvent.SaveSuccess -> onBack()
                is EditProductEvent.ShowError -> { /* handled via UI state */ }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Product",
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
                            text = "Category: ${product.category.displayName}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MutedSageGray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        OutlinedTextField(
                            value = uiState.nameInput,
                            onValueChange = { viewModel.onNameInputChanged(it) },
                            label = { Text("Product Name") },
                            isError = uiState.errorMessage != null,
                            singleLine = true,
                            supportingText = {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    if (uiState.errorMessage != null) {
                                        Text(
                                            text = uiState.errorMessage!!,
                                            color = MaterialTheme.colorScheme.error,
                                            fontSize = 12.sp
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.width(1.dp))
                                    }
                                    Text(
                                        text = "${uiState.nameInput.length}/${CreateProductViewModel.MAX_PRODUCT_NAME_LENGTH}",
                                        fontSize = 12.sp,
                                        color = MutedSageGray
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepTeal,
                                focusedLabelColor = DeepTeal,
                                cursorColor = DeepTeal
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Measurement Type",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ForestCharcoal
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            MeasurementDimension.entries.forEach { dimension ->
                                val isSelected = uiState.selectedDimension == dimension
                                val isEnabled = !uiState.hasTransactions

                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.onDimensionSelected(dimension) },
                                    enabled = isEnabled,
                                    label = { Text(dimension.displayName) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = DeepTeal,
                                        selectedLabelColor = CleanWhite,
                                        containerColor = SoftSage
                                    ),
                                    modifier = Modifier.padding(end = 8.dp)
                                )
                            }
                        }

                        if (uiState.hasTransactions) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Measurement type cannot be changed after transactions exist.",
                                fontSize = 12.sp,
                                color = MutedSageGray
                            )
                        }

                        Spacer(modifier = Modifier.height(28.dp))

                        Button(
                            onClick = { viewModel.saveProduct() },
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
                                    text = "Save Changes",
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
