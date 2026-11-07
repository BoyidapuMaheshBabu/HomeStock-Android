package com.boyidapu.homestock.ui.about

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.boyidapu.homestock.R
import com.boyidapu.homestock.ui.theme.CleanWhite
import com.boyidapu.homestock.ui.theme.DeepTeal
import com.boyidapu.homestock.ui.theme.ForestCharcoal
import com.boyidapu.homestock.ui.theme.MutedSageGray
import com.boyidapu.homestock.ui.theme.SoftSage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val versionName = rememberAppVersion(context)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "About IntiVentory",
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1. Product Identity Header (Branded Header)
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = Modifier
                            .size(88.dp)
                            .background(DeepTeal, shape = RoundedCornerShape(22.dp))
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = stringResource(id = R.string.app_name),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepTeal,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = stringResource(id = R.string.telugu_app_name),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DeepTeal,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = stringResource(id = R.string.telugu_app_caption),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = ForestCharcoal,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .background(SoftSage, shape = RoundedCornerShape(12.dp))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "Version $versionName",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepTeal
                        )
                    }
                }
            }

            // 2. About the Application
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "About the Application",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestCharcoal
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "IntiVentory is an offline-first household inventory and spending application that helps households manage products, record purchases, track product usage, monitor available stock and understand household purchase spending.",
                        fontSize = 14.sp,
                        color = ForestCharcoal,
                        lineHeight = 20.sp
                    )
                }
            }

            // 3. Core Capabilities (Four Compact Highlights)
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Core Capabilities",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestCharcoal
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        CapabilityHighlightItem(
                            icon = Icons.Default.Info,
                            title = "Inventory Management",
                            description = "Manage household products across fixed categories."
                        )

                        CapabilityHighlightItem(
                            icon = Icons.Default.ShoppingCart,
                            title = "Purchase Tracking",
                            description = "Record purchased quantities and actual amounts paid."
                        )

                        CapabilityHighlightItem(
                            icon = Icons.Default.DateRange,
                            title = "Product History",
                            description = "Review and correct recorded purchase and usage transactions."
                        )

                        CapabilityHighlightItem(
                            icon = Icons.Default.CheckCircle,
                            title = "Spending Insights",
                            description = "Understand household purchase spending through date filters and analytics."
                        )
                    }
                }
            }

            // 4. Application Details (Label / Value rows)
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Application Details",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestCharcoal
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow(label = "Platform", value = "Android")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftSage.copy(alpha = 0.5f))
                    DetailRow(label = "Application type", value = "Native Android")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftSage.copy(alpha = 0.5f))
                    DetailRow(label = "Architecture", value = "Offline-first")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftSage.copy(alpha = 0.5f))
                    DetailRow(label = "Language", value = "Kotlin")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftSage.copy(alpha = 0.5f))
                    DetailRow(label = "UI Toolkit", value = "Jetpack Compose & Material 3")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftSage.copy(alpha = 0.5f))
                    DetailRow(label = "Local Database", value = "Room / SQLite")
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = SoftSage.copy(alpha = 0.5f))
                    DetailRow(label = "Preferences", value = "Preferences DataStore")
                }
            }

            // 5. Developer Information
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Developer Information",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = ForestCharcoal
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DetailRow(label = "Developer", value = "Boyidapu Mahesh Babu")

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Contact Email",
                        fontSize = 13.sp,
                        color = MutedSageGray
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    val email = "maheshbabuboyidapu@gmail.com"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { launchEmailIntent(context, email) },
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = DeepTeal,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = email,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepTeal,
                            textDecoration = TextDecoration.Underline
                        )
                    }
                }
            }

            // 6. Source Code Link (GitHub Logo Icon + Repository Link)
            Card(
                colors = CardDefaults.cardColors(containerColor = CleanWhite),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Source Code Repository",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ForestCharcoal
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val githubUrl = "https://github.com/BoyidapuMaheshBabu/IntiVentory-Android"
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { launchWebIntent(context, githubUrl) }
                            .semantics {
                                contentDescription = "View source code on GitHub"
                            }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_github),
                            contentDescription = "View source code on GitHub",
                            tint = DeepTeal,
                            modifier = Modifier.size(22.dp)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "IntiVentory-Android",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepTeal
                            )
                            Text(
                                text = "BoyidapuMaheshBabu/IntiVentory-Android",
                                fontSize = 12.sp,
                                color = MutedSageGray
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "View source code on GitHub",
                            tint = DeepTeal,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // 7. Privacy Information
            Card(
                colors = CardDefaults.cardColors(containerColor = SoftSage),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            tint = DeepTeal,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Privacy & Data Storage",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DeepTeal
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "IntiVentory is designed for offline use. Household inventory and transaction records are stored locally on the device. An account or cloud connection is not required.",
                        fontSize = 13.sp,
                        color = ForestCharcoal,
                        lineHeight = 19.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CapabilityHighlightItem(
    icon: ImageVector,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SoftSage.copy(alpha = 0.5f), shape = RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(DeepTeal, shape = RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = CleanWhite,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = DeepTeal
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                fontSize = 12.sp,
                color = ForestCharcoal
            )
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 13.sp,
            color = MutedSageGray
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = ForestCharcoal
        )
    }
}

private fun rememberAppVersion(context: Context): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(
                context.packageName,
                PackageManager.PackageInfoFlags.of(0)
            ).versionName ?: "1.0.0"
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
        }
    } catch (_: Exception) {
        "1.0.0"
    }
}

private fun launchEmailIntent(context: Context, email: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:$email")
        putExtra(Intent.EXTRA_SUBJECT, "IntiVentory App Inquiry")
    }
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "No compatible email application found.", Toast.LENGTH_SHORT).show()
    } catch (_: Exception) {
        Toast.makeText(context, "Unable to launch email app.", Toast.LENGTH_SHORT).show()
    }
}

private fun launchWebIntent(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    try {
        context.startActivity(intent)
    } catch (_: ActivityNotFoundException) {
        Toast.makeText(context, "Unable to open web browser.", Toast.LENGTH_SHORT).show()
    } catch (_: Exception) {
        Toast.makeText(context, "Unable to open link.", Toast.LENGTH_SHORT).show()
    }
}
