package com.ktx.dormitory.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ktx.dormitory.presentation.features.access.AccessHistoryScreen
import com.ktx.dormitory.presentation.features.access.AccessScreen
import com.ktx.dormitory.presentation.features.access.FaceDetectionScreen
import com.ktx.dormitory.presentation.features.access.FaceRegistrationScreen
import com.ktx.dormitory.presentation.features.access.FaceVerificationScreen
import com.ktx.dormitory.presentation.features.auth.ChangePasswordScreen
import com.ktx.dormitory.presentation.features.auth.LoginScreen
import com.ktx.dormitory.presentation.features.auth.LoginViewModel
import com.ktx.dormitory.presentation.features.auth.SplashScreen
import com.ktx.dormitory.presentation.features.notification.NotificationScreen
import com.ktx.dormitory.presentation.features.payment.PaymentScreen
import com.ktx.dormitory.presentation.features.request.RequestScreen
import com.ktx.dormitory.presentation.features.staff.StaffApprovalScreen
import com.ktx.dormitory.HomeScreen
import com.ktx.dormitory.presentation.features.student.ProfileScreen
import com.ktx.dormitory.presentation.features.student.QuickExtendScreen
import com.ktx.dormitory.presentation.features.student.RoomScreen
import com.ktx.dormitory.presentation.features.student.ApplicationStatusScreen
import com.ktx.dormitory.presentation.features.student.PaymentHistoryScreen
import com.ktx.dormitory.presentation.features.student.StudentViewModel
import com.ktx.dormitory.presentation.components.PlaceholderScreen

sealed class Screen(val route: String, val title: String) {
    data object Splash : Screen("splash", "Chào mừng")
    data object Login : Screen("login", "Đăng nhập")
    data object StudentHome : Screen("student_home", "Trang chủ")
    data object StaffHome : Screen("staff_home", "Dashboard Nhân viên")
    data object AdminHome : Screen("admin_home", "Quản trị viên")
    data object Access : Screen("access", "Quét mã ra vào")
    data object AccessHistory : Screen("access_history", "Lịch sử ra vào")
    data object FaceDetection : Screen("face_detection", "Nhận diện khuôn mặt")
    data object FaceRegistration : Screen("face_registration", "Đăng ký khuôn mặt")
    data object FaceVerification : Screen("face_verification", "Xác thực AI")
    data object Notifications : Screen("notifications", "Thông báo")
    data object Profile : Screen("profile", "Hồ sơ cá nhân")
    data object QuickExtend : Screen("quick_extend", "Gia hạn lưu trú")
    data object Payment : Screen("payment", "Thanh toán")
    data object Request : Screen("request", "Gửi yêu cầu")
    data object RoomInfo : Screen("room_info", "Thông tin phòng")
    data object ApplicationStatus : Screen("app_status", "Trạng thái đơn")
    data object PaymentHistory : Screen("payment_history", "Lịch sử thanh toán")
    data object StaffApproval : Screen("staff_approval", "Duyệt yêu cầu")
    data object StaffRoomManage : Screen("staff_room_manage", "Quản lý phòng")
    data object StaffWaterElectric : Screen("staff_water_electric", "Điện nước")
    data object AdminUsers : Screen("admin_users", "Quản lý người dùng")
    data object AdminStats : Screen("admin_stats", "Thống kê hệ thống")
    data object AdminSettings : Screen("admin_settings", "Cài đặt hệ thống")
    data object ChangePassword : Screen("change_password", "Đổi mật khẩu")
}

@Composable
fun RoleGuard(
    requiredRoles: List<String>,
    loginViewModel: LoginViewModel,
    content: @Composable () -> Unit
) {
    val userData by loginViewModel.userData.collectAsState()
    
    // Thêm log để bạn kiểm tra trong Logcat
    /* LaunchedEffect(userData) {
        android.util.Log.d("ROLE_GUARD", "Dữ liệu User hiện tại: Role=${userData?.role}, Name=${userData?.fullName}")
    } */

    when {
        userData == null -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Đang xác thực quyền...",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
        requiredRoles.any { it.equals(userData?.role, ignoreCase = true) } -> {
            content()
        }
        else -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Bạn không có quyền truy cập chức năng này")
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

        // --- PROTECTED ROUTES ---

        composable(Screen.StudentHome.route) {
            RoleGuard(listOf("USER", "STUDENT"), loginViewModel) {
                HomeScreen(navController, loginViewModel)
            }
        }

        composable(Screen.StaffHome.route) {
            RoleGuard(listOf("STAFF"), loginViewModel) {
                HomeScreen(navController, loginViewModel)
            }
        }

        composable(Screen.AdminHome.route) {
            RoleGuard(listOf("ADMIN"), loginViewModel) {
                HomeScreen(navController, loginViewModel)
            }
        }

        composable(Screen.Profile.route) {
            ProfileScreen(navController, loginViewModel)
        }

        composable(Screen.RoomInfo.route) {
            val studentViewModel: StudentViewModel = hiltViewModel()
            RoomScreen(navController, studentViewModel)
        }

        composable(Screen.ApplicationStatus.route) {
            val studentViewModel: StudentViewModel = hiltViewModel()
            ApplicationStatusScreen(navController, studentViewModel)
        }

        composable(Screen.PaymentHistory.route) {
            val studentViewModel: StudentViewModel = hiltViewModel()
            PaymentHistoryScreen(navController, studentViewModel)
        }

        composable(Screen.Notifications.route) { NotificationScreen(navController) }
        composable(Screen.QuickExtend.route) { QuickExtendScreen(navController) }
        composable(Screen.Payment.route) { PaymentScreen(navController) }
        composable(Screen.Request.route) { RequestScreen(navController) }
        composable(Screen.Access.route) { AccessScreen(navController) }
        composable(Screen.AccessHistory.route) { AccessHistoryScreen(navController) }
        
        composable(Screen.FaceDetection.route) {
            FaceDetectionScreen(navController)
        }
        
        composable(Screen.FaceRegistration.route) {
            FaceRegistrationScreen(navController, loginViewModel)
        }

        composable(Screen.FaceVerification.route) {
            FaceVerificationScreen(navController)
        }
        
        // Màn hình duyệt chỉ dành cho Staff/Admin
        composable(Screen.StaffApproval.route) {
            RoleGuard(listOf("STAFF", "ADMIN"), loginViewModel) {
                StaffApprovalScreen(navController)
            }
        }

        composable(Screen.StaffRoomManage.route) {
            PlaceholderScreen(navController, "Quản lý phòng")
        }

        composable(Screen.StaffWaterElectric.route) {
            PlaceholderScreen(navController, "Chỉ số Điện nước")
        }

        composable(Screen.AdminUsers.route) {
            PlaceholderScreen(navController, "Quản lý người dùng")
        }

        composable(Screen.AdminStats.route) {
            PlaceholderScreen(navController, "Thống kê hệ thống")
        }

        composable(Screen.AdminSettings.route) {
            PlaceholderScreen(navController, "Cài đặt hệ thống")
        }
    }
}
