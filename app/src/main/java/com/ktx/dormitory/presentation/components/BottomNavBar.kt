package com.ktx.dormitory.presentation.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination // THÊM DÒNG NÀY
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ktx.dormitory.navigation.Screen

import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ktx.dormitory.presentation.features.auth.LoginViewModel

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

@Composable
fun BottomNavBar(
    navController: NavController,
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val loginState by loginViewModel.uiState.collectAsStateWithLifecycle()
    val userData = loginState.userData
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Tự động thay đổi danh sách icon dựa trên Role
    val items = when (userData?.role?.uppercase()) {
        "ADMIN" -> listOf(
            BottomNavItem(Screen.AdminHome, Icons.Default.AdminPanelSettings, "Admin"),
            BottomNavItem(Screen.AdminUsers, Icons.Default.People, "Users"),
            BottomNavItem(Screen.AdminStats, Icons.Default.Analytics, "Stats"),
            BottomNavItem(Screen.Profile, Icons.Default.Person, "Hồ sơ")
        )
        "STAFF" -> listOf(
            BottomNavItem(Screen.StaffHome, Icons.Default.Dashboard, "Staff"),
            BottomNavItem(Screen.StaffRoomManage, Icons.Default.MeetingRoom, "Rooms"),
            BottomNavItem(Screen.StaffApproval, Icons.Default.FactCheck, "Approve"),
            BottomNavItem(Screen.Profile, Icons.Default.Person, "Hồ sơ")
        )
        else -> listOf(
            BottomNavItem(Screen.StudentHome, Icons.Default.Home, "Trang chủ"),
            BottomNavItem(Screen.Access, Icons.Default.Key, "Ra vào"),
            BottomNavItem(Screen.Payment, Icons.Default.Payments, "Thanh toán"),
            BottomNavItem(Screen.Notifications, Icons.Default.Notifications, "Thông báo"),
            BottomNavItem(Screen.Profile, Icons.Default.Person, "Hồ sơ")
        )
    }

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}