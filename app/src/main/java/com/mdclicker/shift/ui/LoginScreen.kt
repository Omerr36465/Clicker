package com.mdclicker.shift.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mdclicker.shift.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    onLoginSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var licenseKey by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var showLicenseSection by remember { mutableStateOf(false) }
    var isRegistering by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    var successMessage by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    val uiState by viewModel.uiState.collectAsState()

    // Auto-navigate if already logged in
    LaunchedEffect(uiState.isLoggedIn) {
        if (uiState.isLoggedIn) {
            onLoginSuccess()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Indigo900, Indigo800, Indigo700)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Icon / Logo area
            Icon(
                imageVector = Icons.Filled.Schedule,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = Amber500
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "MD Clicker Shift",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextOnPrimary
            )

            Text(
                text = "Booking Automation",
                fontSize = 16.sp,
                color = Indigo100
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Offline badge
            Surface(
                color = Amber500.copy(alpha = 0.2f),
                shape = MaterialTheme.shapes.small
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.OfflineBolt,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = Amber500
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Offline Mode | LicenseGate",
                        fontSize = 11.sp,
                        color = Amber500
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Error/Success Messages
            if (errorMessage.isNotEmpty()) {
                Surface(
                    color = Red500.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = errorMessage,
                        modifier = Modifier.padding(12.dp),
                        color = Red500,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (successMessage.isNotEmpty()) {
                Surface(
                    color = Green500.copy(alpha = 0.1f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = successMessage,
                        modifier = Modifier.padding(12.dp),
                        color = Green500,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Username / Email field
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    errorMessage = ""
                },
                label = { Text("اسم المستخدم / البريد الإلكتروني") },
                leadingIcon = {
                    Icon(Icons.Filled.Person, contentDescription = null, tint = Indigo400)
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Amber500,
                    focusedLabelColor = Amber500,
                    focusedLeadingIconColor = Amber500,
                    unfocusedTextColor = TextOnPrimary,
                    focusedTextColor = TextOnPrimary
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Password field
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    errorMessage = ""
                },
                label = { Text("كلمة المرور") },
                leadingIcon = {
                    Icon(Icons.Filled.Lock, contentDescription = null, tint = Indigo400)
                },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Filled.VisibilityOff else Icons.Filled.Visibility,
                            contentDescription = null,
                            tint = Indigo400
                        )
                    }
                },
                singleLine = true,
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        if (!showLicenseSection) {
                            viewModel.login(username, password) { success, msg ->
                                if (success) {
                                    successMessage = msg
                                    errorMessage = ""
                                } else {
                                    errorMessage = msg
                                }
                            }
                        }
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Amber500,
                    focusedLabelColor = Amber500,
                    focusedLeadingIconColor = Amber500,
                    unfocusedTextColor = TextOnPrimary,
                    focusedTextColor = TextOnPrimary
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            // License Gate section toggle
            TextButton(onClick = { showLicenseSection = !showLicenseSection }) {
                Icon(
                    Icons.Filled.Key,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp),
                    tint = Amber500
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    if (showLicenseSection) "إخفاء تفعيل LicenseGate" else "تفعيل via LicenseGate",
                    color = Amber500,
                    fontSize = 13.sp
                )
            }

            if (showLicenseSection) {
                OutlinedTextField(
                    value = licenseKey,
                    onValueChange = {
                        licenseKey = it
                        errorMessage = ""
                    },
                    label = { Text("LicenseGate ID أو مفتاح الترخيص") },
                    leadingIcon = {
                        Icon(Icons.Filled.VerifiedUser, contentDescription = null, tint = Indigo400)
                    },
                    placeholder = { Text("مثال: a26b9", color = Indigo400.copy(alpha = 0.5f)) },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Amber500,
                        focusedLabelColor = Amber500,
                        focusedLeadingIconColor = Amber500,
                        unfocusedTextColor = TextOnPrimary,
                        focusedTextColor = TextOnPrimary
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.activateWithLicenseGate(licenseKey) { success, msg ->
                            if (success) {
                                successMessage = msg
                                errorMessage = ""
                                licenseKey = ""
                            } else {
                                errorMessage = msg
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Amber600,
                        contentColor = TextPrimary
                    )
                ) {
                    Icon(Icons.Filled.VerifiedUser, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("تفعيل الترخيص")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Login / Register button
            Button(
                onClick = {
                    focusManager.clearFocus()
                    if (username.isNotBlank() && password.isNotBlank()) {
                        viewModel.login(username, password) { success, msg ->
                            if (success) {
                                successMessage = msg
                                errorMessage = ""
                            } else {
                                errorMessage = msg
                            }
                        }
                    } else {
                        errorMessage = "يرجى إدخال اسم المستخدم وكلمة المرور"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Amber600,
                    contentColor = TextPrimary
                )
            ) {
                Icon(Icons.Filled.Login, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isRegistering) "إنشاء حساب" else "تسجيل الدخول",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "LicenseGate User ID: ${uiState.licenseGateId}",
                fontSize = 12.sp,
                color = Indigo100.copy(alpha = 0.7f)
            )
        }
    }
}
