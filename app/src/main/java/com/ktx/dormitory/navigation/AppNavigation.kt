package com.ktx.dormitory.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ktx.dormitory.presentation.features.access.AccessHistoryScreen
import com.ktx.dormitory.presentation.features.face.FaceDetectionScreen
import com.ktx.dormitory.presentation.features.face.FaceRegistrationScreen
import com.ktx.dormitory.presentation.features.face.FaceVerificationScreen
import com.ktx.dormitory.presentation.features.auth.ChangePasswordScreen
import com.ktx.dormitory.presentation.features.auth.LoginScreen
import com.ktx.dormitory.presentation.features.auth.LoginViewModel
import com.ktx.dormitory.presentation.features.auth.SplashScreen
import com.ktx.dormitory.presentation.features.payment.PaymentScreen
import com.ktx.dormitory.presentation.features.payment.PaymentHistoryScreen
import com.ktx.dormitory.presentation.features.payment.PaymentHistoryViewModel
import com.ktx.dormitory.presentation.features.home.HomeScreen
import com.ktx.dormitory.presentation.features.profile.ProfileScreen
import com.ktx.dormitory.presentation.features.room.RoomScreen
import com.ktx.dormitory.presentation.features.room.RoomViewModel
import com.ktx.dormitory.presentation.features.application.ApplicationStatusScreen
import com.ktx.dormitory.presentation.features.application.ApplicationViewModel

sealed class Screen(val route: String, val title: String) {
    data object Splash : Screen("splash", "Chào mừng")
    data object Login : Screen("login", "Đăng nhập")
    data object StudentHome : Screen("student_home", "Trang chủ")
    data object AccessHistory : Screen("access_history", "Lịch sử ra vào")
    data object FaceRegistration : Screen("face_registration", "Đăng ký khuôn mặt")
    data object Profile : Screen("profile", "Hồ sơ cá nhân")
    data object Payment : Screen("payment", "Thanh toán")
    data object RoomInfo : Screen("room_info", "Thông tin phòng")
    data object ApplicationStatus : Screen("app_status", "Trạng thái đơn")
    data object PaymentHistory : Screen("payment_history", "Lịch sử thanh toán")
    data object ChangePassword : Screen("change_password", "Đổi mật khẩu")
}

@Composable
fun RoleGuard(
    loginViewModel: LoginViewModel,
    content: @Composable () -> Unit
) {
    val loginState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val userData = loginState.userData
    
    when {
        userData == null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Đang xác thực quyền...", style = MaterialTheme.typography.labelSmall)
                    
                    // Thêm nút thoát nếu bị kẹt ở đây quá lâu
                    TextButton(onClick = { 
                        loginViewModel.logout { 
                            // Quay về màn hình login
                        }
                    }) {
                        Text("Hủy và quay lại Đăng nhập", color = MaterialTheme.colorScheme.primary)
                    }
                }
            }
        }
        userData.role?.uppercase() == "STUDENT" || userData.role?.uppercase() == "USER" -> {
            content()
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Ứng dụng này chỉ dành cho Sinh viên")
            }
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController) {
    val loginViewModel: LoginViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController, loginViewModel)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController, loginViewModel)
        }

        composable(Screen.ChangePassword.route) {
            ChangePasswordScreen(navController)
        }

        // --- STUDENT ROUTES ---

        composable(Screen.StudentHome.route) {
            RoleGuard(loginViewModel) {
                HomeScreen(navController)
            }
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController)
        }

        composable(Screen.RoomInfo.route) {
            val roomViewModel: RoomViewModel = hiltViewModel()
            RoomScreen(navController, roomViewModel)
        }

        composable(Screen.ApplicationStatus.route) {
            val applicationViewModel: ApplicationViewModel = hiltViewModel()
            ApplicationStatusScreen(navController, applicationViewModel)
        }

        composable(Screen.PaymentHistory.route) {
            val paymentHistoryViewModel: PaymentHistoryViewModel = hiltViewModel()
            PaymentHistoryScreen(navController, paymentHistoryViewModel)
        }

        composable(Screen.Payment.route) { PaymentScreen(navController) }
        
        composable(Screen.AccessHistory.route) {
            AccessHistoryScreen(navController = navController)
        }
        
        composable(Screen.FaceRegistration.route) {
            FaceRegistrationScreen(navController, loginViewModel)
        }
    }
}
