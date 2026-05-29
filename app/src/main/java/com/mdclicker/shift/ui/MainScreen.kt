package com.mdclicker.shift.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider //  أضف هذا السطر
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mdclicker.shift.data.AutomationLog
import com.mdclicker.shift.data.BookedShift
import com.mdclicker.shift.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onNavigateToSettings: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val logs by viewModel.logs.collectAsState(initial = emptyList())
    val todayShifts by viewModel.todayShifts.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("MD Clicker Shift", fontWeight = FontWeight.Bold)
                        Text(
                            text = if (uiState.automationRunning) "🟢 يعمل..." else "🔴 متوقف",
                            fontSize = 12.sp,
                            color = if (uiState.automationRunning) Green500 else Color.Gray
                        )
                    }
                },
                actions = {
                    // Settings button
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Filled.Settings, contentDescription = "الإعدادات")
                    }
                    // Logout
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Filled.Logout, contentDescription = "تسجيل الخروج")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Indigo900,
                    titleContentColor = TextOnPrimary,
                    actionIconContentColor = TextOnPrimary
                )
            )
        },
        bottomBar = {
            if (uiState.automationRunning) {
                Surface(
                    color = Green500,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "✓ Automation Active - Scanning for shifts...",
                            color = TextOnPrimary,
                            fontWeight = FontWeight.Medium,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Status & Controls Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Indigo900.copy(alpha = 0.05f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        // Start Button
                        Button(
                            onClick = { viewModel.startAutomation() },
                            enabled = !uiState.automationRunning,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Green500,
                                contentColor = TextOnPrimary
                            ),
                            modifier = Modifier.weight(1f).padding(end = 4.dp)
                        ) {
                            Icon(Icons.Filled.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("بدء", fontWeight = FontWeight.Bold)
                        }

                        // Stop Button
                        Button(
                            onClick = { viewModel.stopAutomation() },
                            enabled = uiState.automationRunning,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Red500,
                                contentColor = TextOnPrimary
                            ),
                            modifier = Modifier.weight(1f).padding(start = 4.dp)
                        ) {
                            Icon(Icons.Filled.Stop, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("إيقاف", fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // License & Device Info
                    Surface(
                        color = Indigo800.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            InfoRow("LicenseGate ID", uiState.licenseGateId, Amber500)
                            InfoRow("الجهاز", uiState.deviceId.take(12) + "...", Indigo400)
                            InfoRow("البائع", uiState.sellerCode.ifEmpty { "---" }, Indigo400)
                            InfoRow("النوبات اليوم", "${todayShifts.size}", Green500)
                        }
                    }
                }
            }

            // Today's Booked Shifts
            if (todayShifts.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Green500.copy(alpha = 0.05f)
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "نوبات اليوم المحجوزة",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Green500
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        todayShifts.forEach { shift ->
                            ShiftRow(shift)
                            if (shift != todayShifts.last()) {
                                Divider(modifier = Modifier.padding(vertical = 4.dp))
                            }
                        }
                    }
                }
            }

            // Logs Section
            Text(
                text = "سجل التشغيل",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                items(logs) { log ->
                    LogRow(log)
                }
                if (logs.isEmpty()) {
                    item {
                        Text(
                            text = "لا توجد أحداث بعد. ابدأ التشغيل الآلي لرؤية السجل.",
                            modifier = Modifier.padding(16.dp),
                            color = Color.Gray,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
        Text(text = value, fontSize = 12.sp, color = valueColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun ShiftRow(shift: BookedShift) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(Green500)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = shift.shiftLabel, fontSize = 13.sp, fontWeight = FontWeight.Medium)
        }
        Text(
            text = "${shift.startTime} - ${shift.endTime}",
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
private fun LogRow(log: AutomationLog) {
    Surface(
        color = when (log.level) {
            "ERROR" -> Red500.copy(alpha = 0.05f)
            "WARN" -> Amber500.copy(alpha = 0.05f)
            else -> Color.Transparent
        },
        shape = RoundedCornerShape(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = when (log.level) {
                    "ERROR" -> "🔴"
                    "WARN" -> "🟡"
                    else -> "🟢"
                },
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = log.message,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Default
                )
                Text(
                    text = log.timestamp.toString(),
                    fontSize = 9.sp,
                    color = Color.Gray
                )
            }
        }
    }
}
