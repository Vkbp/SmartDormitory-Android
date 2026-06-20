package com.ktx.dormitory

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ktx.dormitory.navigation.Screen
import com.ktx.dormitory.presentation.components.BottomNavBar
import com.ktx.dormitory.presentation.features.auth.LoginViewModel
import com.ktx.dormitory.presentation.features.student.RoomViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    roomViewModel: RoomViewModel = androidx.hilt.navigation.compose.hiltViewModel()
) {
    val loginState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val userData = loginState.userData
    val roomState by roomViewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(userData) {
        if (userData == null) {
            loginViewModel.fetchCurrentUser()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Smart Dormitory", fontWeight = FontWeight.Black) },
                actions = {
                    IconButton(onClick = { navController.navigate("notifications") }) {
                        Icon(Icons.Default.Notifications, contentDescription = "Notifications")
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController, loginViewModel) } // TRUYỀN loginViewModel VÀO ĐÂY
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            val isStudent = userData?.role?.uppercase() == "STUDENT" || userData?.role?.uppercase() == "USER"
            
            WelcomeSection(
                name = userData?.fullName ?: "Người dùng",
                room = if (isStudent) (roomState.roomInfo?.roomCode ?: "...") else null, // ẨN PHÒNG NẾU LÀ ADMIN
                onRoomClick = { if (isStudent) navController.navigate(Screen.RoomInfo.route) }
            )

            Column(modifier = Modifier.padding(16.dp)) {
                when (userData?.role?.uppercase()) {
                    "USER", "STUDENT" -> StudentDashboard(navController)
                    "STAFF" -> StaffDashboard(navController)
                    "ADMIN" -> AdminDashboard(navController)
                    else -> {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WelcomeSection(name: String, room: String?, onRoomClick: () -> Unit) {
    val gradient = Brush.verticalGradient(
        colors = listOf(MaterialTheme.colorScheme.primaryContainer, Color.Transparent)
    )
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(gradient)
            .padding(24.dp)
            .padding(top = 16.dp) // Thêm padding để tránh chữ bị sát TopAppBar
    ) {
        Column {
            Text(
                text = "Xin chào,",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = name,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            // CHỈ HIỆN PHÒNG NẾU DỮ LIỆU CÓ (SINH VIÊN)
            if (room != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Surface(
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(12.dp),
                    onClick = onRoomClick
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Phòng $room",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StudentDashboard(navController: NavController) {
    DashboardGrid(
        title = "Tiện ích sinh viên",
        items = listOf(
            DashboardItem("Xác thực AI", Icons.Default.Face, Screen.FaceVerification.route),
            DashboardItem("Đăng ký AI", Icons.Default.PersonAdd, Screen.FaceRegistration.route),
            DashboardItem("Ra vào QR", Icons.Default.QrCode, Screen.Access.route),
            DashboardItem("Thanh toán", Icons.Default.Payments, Screen.Payment.route),
            DashboardItem("Lịch sử GD", Icons.AutoMirrored.Filled.ReceiptLong, Screen.PaymentHistory.route),
            DashboardItem("Tiến độ đơn", Icons.Default.Timeline, Screen.ApplicationStatus.route),
            DashboardItem("Gửi yêu cầu", Icons.AutoMirrored.Filled.Send, Screen.Request.route),
            DashboardItem("Lịch sử vào", Icons.Default.History, Screen.AccessHistory.route)
        ),
        navController = navController
    )
}

@Composable
fun StaffDashboard(navController: NavController) {
    DashboardGrid(
        title = "Quản lý nhân viên",
        items = listOf(
            DashboardItem("Duyệt yêu cầu", Icons.Default.AssignmentTurnedIn, Screen.StaffApproval.route),
            DashboardItem("Quản lý phòng", Icons.Default.MeetingRoom, Screen.StaffRoomManage.route),
            DashboardItem("Gửi thông báo", Icons.Default.Campaign, Screen.Notifications.route),
            DashboardItem("Điện nước", Icons.Default.WaterDrop, Screen.StaffWaterElectric.route)
        ),
        navController = navController
    )
}

@Composable
fun AdminDashboard(navController: NavController) {
    DashboardGrid(
        title = "Hệ thống Admin",
        items = listOf(
            DashboardItem("Người dùng", Icons.Default.People, Screen.AdminUsers.route),
            DashboardItem("Thống kê", Icons.Default.Analytics, Screen.AdminStats.route),
            DashboardItem("Cài đặt", Icons.Default.Settings, Screen.AdminSettings.route)
        ),
        navController = navController
    )
}

data class DashboardItem(val title: String, val icon: ImageVector, val route: String)

@Composable
fun DashboardGrid(title: String, items: List<DashboardItem>, navController: NavController) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // SỬA ĐỔI: Sử dụng Column kết hợp Row thay vì Grid với height cố định 
        // để đảm bảo hiển thị đầy đủ tất cả các mục trên mọi màn hình.
        items.chunked(2).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                rowItems.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        FeatureCard(item.title, item.icon) {
                            if (item.route.isNotEmpty()) navController.navigate(item.route)
                        }
                    }
                }
                // Nếu hàng chỉ có 1 mục, thêm 1 Box trống để giữ bố cục
                if (rowItems.size == 1) {
                    Box(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun FeatureCard(title: String, icon: ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = title, tint = MaterialTheme.colorScheme.primary)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}
