package com.ktx.dormitory.presentation.features.auth

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ktx.dormitory.R
import com.ktx.dormitory.navigation.Screen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    loginViewModel: LoginViewModel // Sử dụng Shared ViewModel truyền từ AppNavigation
) {
    // Cờ để đảm bảo chỉ điều hướng 1 lần duy nhất
    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        Log.d("SPLASH_DEBUG", "1. Màn hình Splash bắt đầu chạy")
        delay(1500) // Hiện logo tối thiểu 1.5 giây

        Log.d("SPLASH_DEBUG", "2. Bắt đầu gọi checkAuthStatus...")
        loginViewModel.checkAuthStatus { role ->
            Log.d("SPLASH_DEBUG", "3. Nhận kết quả checkAuthStatus: role = $role")
            
            if (!hasNavigated) {
                hasNavigated = true
                if (role != null) {
                    Log.d("SPLASH_DEBUG", "4. Điều hướng sang: ${Screen.StudentHome.route}")
                    navController.navigate(Screen.StudentHome.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                } else {
                    Log.d("SPLASH_DEBUG", "4. Điều hướng sang: ${Screen.Login.route}")
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            } else {
                Log.w("SPLASH_DEBUG", "Cảnh báo: Đã điều hướng rồi, bỏ qua callback này.")
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = R.drawable.logo_stu),
                contentDescription = "Logo",
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "SMART DORMITORY",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
