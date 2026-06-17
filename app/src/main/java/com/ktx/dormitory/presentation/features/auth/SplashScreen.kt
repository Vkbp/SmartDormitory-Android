package com.ktx.dormitory.presentation.features.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.ktx.dormitory.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    loginViewModel: LoginViewModel // Sử dụng Shared ViewModel truyền từ AppNavigation
) {
    // Cờ để đảm bảo chỉ điều hướng 1 lần duy nhất
    var hasNavigated by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1500) // Hiện logo 1.5 giây

        loginViewModel.checkAuthStatus { role ->
            // CƠ CHẾ TỰ DỌN DẸP: 
            // Nếu có lỗi (role == null), App sẽ tự động hiểu là Token hỏng 
            // và chuyển thẳng về Login mà không cần bạn phải xóa Storage.
            if (!hasNavigated) {
                hasNavigated = true
                if (role != null) {
                    val destination = when (role.uppercase()) {
                        "USER", "STUDENT" -> "student_home"
                        "STAFF" -> "staff_home"
                        "ADMIN" -> "admin_home"
                        else -> "student_home"
                    }
                    navController.navigate(destination) {
                        popUpTo("splash") { inclusive = true }
                    }
                } else {
                    // Nếu Token hỏng hoặc chưa đăng nhập -> Về Login ngay
                    navController.navigate("login") {
                        popUpTo("splash") { inclusive = true }
                    }
                }
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