package com.ktx.dormitory.presentation.features.auth

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ktx.dormitory.navigation.Screen
import com.ktx.dormitory.core.utils.checkBiometricSupport
import com.ktx.dormitory.core.utils.ShowBiometricPrompt
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(navController: NavController,
                loginViewModel: LoginViewModel
                ) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uiState by loginViewModel.uiState.collectAsState()

    var mssv by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isBiometricAvailable by remember { mutableStateOf(false) }
    var showBiometricDialog by remember { mutableStateOf(false) }
    var showForgotDialog by remember { mutableStateOf(false) }
    var emailForgot by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        isBiometricAvailable = checkBiometricSupport(context)
    }

    if (showBiometricDialog) {
        ShowBiometricPrompt(
            title = "Đăng nhập vân tay",
            subtitle = "Xác thực để truy cập hệ thống",
            onSuccess = {
                showBiometricDialog = false
                loginViewModel.loginWithBiometric(
                    onSuccess = { role ->
                        val destination = when (role.uppercase()) {
                            "USER", "STUDENT" -> Screen.StudentHome.route
                            "STAFF" -> Screen.StaffHome.route
                            "ADMIN" -> Screen.AdminHome.route
                            else -> Screen.StudentHome.route
                        }
                        // ISSUE 2: Xử lý Back Stack an toàn cho Biometric Login
                        navController.navigate(destination) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    onError = { error ->
                        scope.launch { snackbarHostState.showSnackbar(error) }
                    }
                )
            },
            onError = { showBiometricDialog = false }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Smart Dormitory",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = mssv,
                onValueChange = { mssv = it },
                label = { Text("MSSV hoặc Email") },
                modifier = Modifier.fillMaxWidth().testTag("login_mssv_field"),
                isError = uiState.mssvError != null,
                supportingText = {
                    uiState.mssvError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mật khẩu") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth().testTag("login_password_field"),
                isError = uiState.passwordError != null,
                supportingText = {
                    uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    loginViewModel.performLogin(
                        username = mssv,
                        password = password,
                        onSuccess = { role ->
                            val userRole = role.uppercase()
                            val destination = when (userRole) {
                                "STAFF" -> Screen.StaffHome.route
                                "ADMIN" -> Screen.AdminHome.route
                                else -> Screen.StudentHome.route
                            }
                            // ISSUE 2: Xử lý Back Stack an toàn cho Manual Login
                            navController.navigate(destination) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                                launchSingleTop = true
                            }
                        },
                        onError = { error ->
                            scope.launch {
                                snackbarHostState.showSnackbar(error)
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxWidth().testTag("login_button"),
                enabled = !uiState.isLoading
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp).testTag("login_loading"),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("ĐĂNG NHẬP")
                }
            }

            TextButton(onClick = { showForgotDialog = true }) {
                Text("Quên mật khẩu?")
            }

            if (showForgotDialog) {
                AlertDialog(
                    onDismissRequest = { showForgotDialog = false },
                    title = { Text("Khôi phục mật khẩu") },
                    text = {
                        OutlinedTextField(
                            value = emailForgot,
                            onValueChange = { emailForgot = it },
                            label = { Text("Nhập Email sinh viên") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            loginViewModel.forgotPassword(emailForgot,
                                onSuccess = {
                                    showForgotDialog = false
                                    Toast.makeText(context, "Mật khẩu mới đã gửi vào Email!", Toast.LENGTH_LONG).show()
                                },
                                onError = { 
                                    scope.launch { snackbarHostState.showSnackbar(it) }
                                }
                            )
                        }) { Text("Gửi") }
                    }
                )
            }

            if (isBiometricAvailable) {
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedButton(
                    onClick = { showBiometricDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("🔐 Đăng nhập bằng Vân tay")
                }
            }

            Spacer(modifier = Modifier.height(48.dp))
            Text(
                text = "Ứng dụng dành cho sinh viên đã được duyệt nội trú",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
