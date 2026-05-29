package com.mdclicker.shift.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider //  أضف هذا السطر
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mdclicker.shift.data.ShiftSettings
import com.mdclicker.shift.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val settings by viewModel.settings.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    var shift1Start by remember(settings) { mutableStateOf(settings.shift1Start) }
    var shift1End by remember(settings) { mutableStateOf(settings.shift1End) }
    var shift2Start by remember(settings) { mutableStateOf(settings.shift2Start) }
    var shift2End by remember(settings) { mutableStateOf(settings.shift2End) }
    var shift3Start by remember(settings) { mutableStateOf(settings.shift3Start) }
    var shift3End by remember(settings) { mutableStateOf(settings.shift3End) }
    var sellerCode by remember(uiState) { mutableStateOf(uiState.sellerCode) }
    var bookingDelay by remember(settings) { mutableStateOf(settings.bookingDelayMs.toString()) }
    var retryCount by remember(settings) { mutableStateOf(settings.retryCount.toString()) }
    var showSaved by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("الإعدادات", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "رجوع")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Indigo900,
                    titleContentColor = TextOnPrimary,
                    navigationIconContentColor = TextOnPrimary
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Offline mode indicator
            Surface(
                color = Amber500.copy(alpha = 0.1f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Filled.OfflineBolt,
                        contentDescription = null,
                        tint = Amber500,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "وضع عدم الاتصال - جميع البيانات مخزنة محلياً",
                        fontSize = 13.sp,
                        color = Amber500
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // LicenseGate Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Indigo900.copy(alpha = 0.05f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("LicenseGate", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "LicenseGate User ID: ${uiState.licenseGateId}",
                        fontSize = 14.sp,
                        color = Amber500
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Device ID: ${uiState.deviceId}",
                        fontSize = 12.sp,
                        color = Indigo400
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Shift Timing Settings
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Indigo900.copy(alpha = 0.05f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "مواعيد النوبات",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Shift 1
                    ShiftTimeRow("النوبة 1", shift1Start, shift1End,
                        onStartChange = { shift1Start = it },
                        onEndChange = { shift1End = it }
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Shift 2
                    ShiftTimeRow("النوبة 2", shift2Start, shift2End,
                        onStartChange = { shift2Start = it },
                        onEndChange = { shift2End = it }
                    )
                    Divider(modifier = Modifier.padding(vertical = 8.dp))

                    // Shift 3
                    ShiftTimeRow("النوبة 3", shift3Start, shift3End,
                        onStartChange = { shift3Start = it },
                        onEndChange = { shift3End = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Advanced Settings
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Indigo900.copy(alpha = 0.05f)
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "الإعدادات المتقدمة",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = sellerCode,
                        onValueChange = { sellerCode = it },
                        label = { Text("كود البائع (Seller Code)") },
                        leadingIcon = { Icon(Icons.Filled.Sell, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = bookingDelay,
                        onValueChange = { bookingDelay = it },
                        label = { Text("تأخير الحجز (بالملي ثانية)") },
                        leadingIcon = { Icon(Icons.Filled.Timer, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = retryCount,
                        onValueChange = { retryCount = it },
                        label = { Text("عدد محاولات إعادة المحاولة") },
                        leadingIcon = { Icon(Icons.Filled.Replay, contentDescription = null) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save Button
            Button(
                onClick = {
                    viewModel.saveSettings(
                        ShiftSettings(
                            shift1Start = shift1Start,
                            shift1End = shift1End,
                            shift2Start = shift2Start,
                            shift2End = shift2End,
                            shift3Start = shift3Start,
                            shift3End = shift3End,
                            bookingDelayMs = bookingDelay.toLongOrNull() ?: 2000,
                            retryCount = retryCount.toIntOrNull() ?: 3
                        )
                    )
                    viewModel.setSellerCode(sellerCode)
                    showSaved = true
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Amber600,
                    contentColor = TextPrimary
                )
            ) {
                Icon(Icons.Filled.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("حفظ الإعدادات", fontWeight = FontWeight.Bold)
            }

            if (showSaved) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "✓ تم حفظ الإعدادات بنجاح",
                    color = Green500,
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // App Info
            Surface(
                color = Indigo900.copy(alpha = 0.03f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text("معلومات التطبيق", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("الإصدار: 1.0.0", fontSize = 12.sp, color = Indigo400)
                    Text("الوضع: غير متصل (Offline)", fontSize = 12.sp, color = Amber500)
                    Text("التحقق: LicenseGate", fontSize = 12.sp, color = Green500)
                    Text("التخزين: محلي (Room DB)", fontSize = 12.sp, color = Indigo400)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ShiftTimeRow(
    label: String,
    startTime: String,
    endTime: String,
    onStartChange: (String) -> Unit,
    onEndChange: (String) -> Unit
) {
    Column {
        Text(text = label, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = startTime,
                onValueChange = onStartChange,
                label = { Text("البداية") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            OutlinedTextField(
                value = endTime,
                onValueChange = onEndChange,
                label = { Text("النهاية") },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}
