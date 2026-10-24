package com.boyidapu.homestock.ui.history

import android.app.DatePickerDialog
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boyidapu.homestock.core.dates.DateUtils
import com.boyidapu.homestock.core.money.MoneyUtils
import com.boyidapu.homestock.core.quantity.QuantityUtils
import com.boyidapu.homestock.domain.model.InventoryTransaction
import com.boyidapu.homestock.domain.model.TransactionType
import com.boyidapu.homestock.domain.model.UnitType
import com.boyidapu.homestock.ui.theme.CleanWhite
import com.boyidapu.homestock.ui.theme.DeepTeal
import com.boyidapu.homestock.ui.theme.ForestCharcoal
import com.boyidapu.homestock.ui.theme.MutedSageGray
import com.boyidapu.homestock.ui.theme.SoftSage
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductHistoryScreen(
    viewModel: ProductHistoryViewModel,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var selectedTransactionForDetails by remember { mutableStateOf<InventoryTransaction?>(null) }
    var selectedTransactionForEdit by remember { mutableStateOf<InventoryTransaction?>(null) }
    var transactionToDelete by remember { mutableStateOf<InventoryTransaction?>(null) }

    val product = uiState.product

    LaunchedEffect(Unit) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is ProductHistoryEvent.ShowMessage -> {
                    snackbarHostState.showSnackbar(event.message)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Product History",
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
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (product != null) {
                // Header Summary Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = CleanWhite),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = product.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepTeal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Category: ${product.category.displayName} • ${product.dimension.displayName}",
                            fontSize = 12.sp,
                            color = MutedSageGray
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Current Stock", fontSize = 11.sp, color = MutedSageGray)
                                Text(
                                    text = uiState.formattedCurrentStock,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Total Spending", fontSize = 11.sp, color = MutedSageGray)
                                Text(
                                    text = uiState.formattedTotalSpending,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal
                                )
                            }
                        }
                    }
                }

                // Date-Grouped Transaction List
                if (uiState.dateGroupedTransactions.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No transactions recorded yet.\nTap '+' on the product row to Add or Use.",
                            fontSize = 14.sp,
                            color = MutedSageGray,
                            textAlign = TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        uiState.dateGroupedTransactions.forEach { group ->
                            item(key = "header_${group.epochDay}") {
                                Text(
                                    text = group.dateLabel,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DeepTeal,
                                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                                )
                            }

                            items(
                                items = group.transactions,
                                key = { it.id }
                            ) { tx ->
                                TransactionItemCard(
                                    transaction = tx,
                                    onClick = { selectedTransactionForDetails = tx }
                                )
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            }
        }
    }

    // Transaction Details Dialog
    if (selectedTransactionForDetails != null) {
        val tx = selectedTransactionForDetails!!
        val auditsFlow = viewModel.getAuditsForTransaction(tx.id)
        val audits by auditsFlow.collectAsState(initial = emptyList())
        val canDelete = viewModel.canDeleteTransaction(tx)

        AlertDialog(
            onDismissRequest = { selectedTransactionForDetails = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (tx.type == TransactionType.ADD) Icons.Default.ShoppingCart else Icons.Default.Done,
                        contentDescription = tx.type.name,
                        tint = DeepTeal
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (tx.type == TransactionType.ADD) "Purchase Details" else "Usage Details",
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    val qtyFormatted = QuantityUtils.formatDisplayQuantity(tx.baseQuantity, tx.entryUnit)
                    Text("Quantity: $qtyFormatted", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)

                    if (tx.type == TransactionType.ADD && tx.amountPaidPaise != null) {
                        val amountFormatted = MoneyUtils.formatPaiseToRupees(tx.amountPaidPaise)
                        Text("Amount Paid: $amountFormatted", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
                    }

                    val dateText = DateUtils.formatEpochDay(tx.transactionEpochDay)
                    Text("Date: $dateText", fontSize = 14.sp, color = ForestCharcoal)

                    if (audits.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Card(
                            colors = CardDefaults.cardColors(containerColor = SoftSage),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text("Edit Audit History (${audits.size}):", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepTeal)
                                audits.forEach { audit ->
                                    val oldQty = QuantityUtils.formatDisplayQuantity(audit.oldBaseQuantity, audit.oldEntryUnit)
                                    val newQty = QuantityUtils.formatDisplayQuantity(audit.newBaseQuantity, audit.newEntryUnit)
                                    Text("• Changed $oldQty → $newQty", fontSize = 11.sp, color = ForestCharcoal)
                                }
                            }
                        }
                    }

                    if (!canDelete) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Deletion window expired (available 30 mins after recording).",
                            fontSize = 11.sp,
                            color = MutedSageGray
                        )
                    }
                }
            },
            confirmButton = {
                Row {
                    if (canDelete) {
                        TextButton(
                            onClick = {
                                val targetTx = tx
                                selectedTransactionForDetails = null
                                transactionToDelete = targetTx
                            }
                        ) {
                            Text("Delete", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                        }
                    }

                    TextButton(
                        onClick = {
                            val targetTx = tx
                            selectedTransactionForDetails = null
                            selectedTransactionForEdit = targetTx
                        }
                    ) {
                        Text("Edit", color = DeepTeal, fontWeight = FontWeight.Bold)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedTransactionForDetails = null }) {
                    Text("Close", color = MutedSageGray)
                }
            }
        )
    }

    // Edit Transaction Dialog
    if (selectedTransactionForEdit != null) {
        val tx = selectedTransactionForEdit!!
        val product = uiState.product
        if (product != null) {
            EditTransactionDialog(
                transaction = tx,
                product = product,
                onDismiss = { selectedTransactionForEdit = null },
                onConfirm = { qInput, unit, amtInput, date, onError ->
                    viewModel.editTransaction(tx, qInput, unit, amtInput, date) { success, errMsg ->
                        if (success) {
                            selectedTransactionForEdit = null
                        } else {
                            onError(errMsg ?: "Failed to edit transaction.")
                        }
                    }
                }
            )
        }
    }

    // Delete Confirmation Dialog
    if (transactionToDelete != null) {
        val tx = transactionToDelete!!
        AlertDialog(
            onDismissRequest = { transactionToDelete = null },
            title = { Text("Delete Transaction", fontWeight = FontWeight.Bold, color = DeepTeal) },
            text = { Text("Are you sure you want to delete this transaction? This action cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val targetTx = tx
                        transactionToDelete = null
                        viewModel.deleteTransaction(targetTx) { _, _ -> }
                    }
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { transactionToDelete = null }) {
                    Text("Cancel", color = MutedSageGray)
                }
            }
        )
    }
}

@Composable
private fun TransactionItemCard(
    transaction: InventoryTransaction,
    onClick: () -> Unit
) {
    val isAdd = transaction.type == TransactionType.ADD
    val formattedQty = QuantityUtils.formatDisplayQuantity(transaction.baseQuantity, transaction.entryUnit)

    Card(
        colors = CardDefaults.cardColors(containerColor = CleanWhite),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(if (isAdd) SoftSage else MaterialTheme.colorScheme.surfaceVariant, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isAdd) Icons.Default.ShoppingCart else Icons.Default.Done,
                    contentDescription = transaction.type.name,
                    tint = DeepTeal,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isAdd) "Purchase (Add)" else "Usage (Use)",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ForestCharcoal
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = DateUtils.formatEpochDay(transaction.transactionEpochDay),
                    fontSize = 12.sp,
                    color = MutedSageGray
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (isAdd) "+$formattedQty" else "-$formattedQty",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepTeal
                )

                if (isAdd && transaction.amountPaidPaise != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = MoneyUtils.formatPaiseToRupees(transaction.amountPaidPaise),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = ForestCharcoal
                    )
                }
            }
        }
    }
}

@Composable
private fun EditTransactionDialog(
    transaction: InventoryTransaction,
    product: com.boyidapu.homestock.domain.model.Product,
    onDismiss: () -> Unit,
    onConfirm: (quantityInput: String, unit: UnitType, amountInput: String, date: LocalDate, onError: (String) -> Unit) -> Unit
) {
    val context = LocalContext.current
    val initialQty = QuantityUtils.formatDisplayQuantity(transaction.baseQuantity, transaction.entryUnit)
        .replace(transaction.entryUnit.symbol, "").trim()
    val initialAmount = if (transaction.amountPaidPaise != null) {
        MoneyUtils.formatPaiseToRupees(transaction.amountPaidPaise).replace("₹", "").trim()
    } else ""

    var quantityInput by remember { mutableStateOf(initialQty) }
    var selectedUnit by remember { mutableStateOf(transaction.entryUnit) }
    var amountPaidInput by remember { mutableStateOf(initialAmount) }
    var transactionDate by remember { mutableStateOf(LocalDate.ofEpochDay(transaction.transactionEpochDay)) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (transaction.type == TransactionType.ADD) "Edit Purchase" else "Edit Usage", fontWeight = FontWeight.Bold, color = DeepTeal) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = quantityInput,
                    onValueChange = { quantityInput = it; errorMessage = null },
                    label = { Text("Quantity") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )

                val availableUnits = UnitType.forDimension(product.dimension)
                Row(modifier = Modifier.fillMaxWidth()) {
                    availableUnits.forEach { unit ->
                        FilterChip(
                            selected = selectedUnit == unit,
                            onClick = { selectedUnit = unit; errorMessage = null },
                            label = { Text(unit.symbol) },
                            modifier = Modifier.padding(end = 6.dp)
                        )
                    }
                }

                if (transaction.type == TransactionType.ADD) {
                    OutlinedTextField(
                        value = amountPaidInput,
                        onValueChange = { amountPaidInput = it; errorMessage = null },
                        label = { Text("Amount Paid (₹)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                val dateFormatted = DateUtils.formatEpochDay(transactionDate.toEpochDay())
                OutlinedButton(
                    onClick = {
                        val picker = DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                transactionDate = LocalDate.of(year, month + 1, dayOfMonth)
                                errorMessage = null
                            },
                            transactionDate.year,
                            transactionDate.monthValue - 1,
                            transactionDate.dayOfMonth
                        )
                        picker.datePicker.maxDate = System.currentTimeMillis()
                        picker.show()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Date: $dateFormatted")
                }

                if (errorMessage != null) {
                    Text(text = errorMessage!!, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(quantityInput, selectedUnit, amountPaidInput, transactionDate) { err ->
                        errorMessage = err
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal)
            ) {
                Text("Save Changes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = MutedSageGray)
            }
        }
    )
}
