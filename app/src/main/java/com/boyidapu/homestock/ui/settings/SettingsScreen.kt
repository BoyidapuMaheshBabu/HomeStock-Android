package com.boyidapu.homestock.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boyidapu.homestock.R
import com.boyidapu.homestock.ui.onboarding.WelcomeViewModel
import com.boyidapu.homestock.ui.theme.CleanWhite
import com.boyidapu.homestock.ui.theme.DeepTeal
import com.boyidapu.homestock.ui.theme.ForestCharcoal
import com.boyidapu.homestock.ui.theme.MutedSageGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit,
    onNavigateToArchivedProducts: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Settings",
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
            // Profile / User Name Card
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
                        text = "User Profile",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestCharcoal
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!uiState.isEditing) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Name",
                                    fontSize = 12.sp,
                                    color = MutedSageGray
                                )
                                Text(
                                    text = uiState.currentSavedName,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DeepTeal
                                )
                            }

                            IconButton(onClick = { viewModel.startEditing() }) {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = "Edit Name",
                                    tint = DeepTeal
                                )
                            }
                        }

                        if (uiState.successMessage != null) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = uiState.successMessage!!,
                                fontSize = 12.sp,
                                color = DeepTeal
                            )
                        }
                    } else {
                        OutlinedTextField(
                            value = uiState.nameInput,
                            onValueChange = { viewModel.onNameInputChanged(it) },
                            label = { Text("Your Name") },
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
                                        text = "${uiState.nameInput.length}/${WelcomeViewModel.MAX_USER_NAME_LENGTH}",
                                        fontSize = 12.sp,
                                        color = MutedSageGray
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Words,
                                imeAction = ImeAction.Done
                            ),
                            keyboardActions = KeyboardActions(
                                onDone = { viewModel.saveName() }
                            ),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = DeepTeal,
                                focusedLabelColor = DeepTeal,
                                cursorColor = DeepTeal
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            OutlinedButton(
                                onClick = { viewModel.cancelEditing() },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Cancel")
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Button(
                                onClick = { viewModel.saveName() },
                                colors = ButtonDefaults.buttonColors(containerColor = DeepTeal),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Save")
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Archived Products Entry Card
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateToArchivedProducts() }
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Archived Products",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestCharcoal
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "View and restore hidden or archived products",
                            fontSize = 13.sp,
                            color = MutedSageGray
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "View Archived Products",
                        tint = MutedSageGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // App Identity Card
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
                        text = stringResource(id = R.string.app_name),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal
                    )
                    Text(
                        text = stringResource(id = R.string.telugu_app_name),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepTeal
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.telugu_app_caption),
                        fontSize = 13.sp,
                        color = MutedSageGray
                    )
                }
            }
        }
    }
}
