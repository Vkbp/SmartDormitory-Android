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
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Chỉ hiển thị BottomNav cho Sinh viên
    val items = listOf(
        BottomNavItem(Screen.StudentHome, Icons.Default.Home, "Trang chủ"),
        BottomNavItem(Screen.RoomInfo, Icons.Default.MeetingRoom, "Phòng"),
        BottomNavItem(Screen.Payment, Icons.Default.Payments, "Thanh toán"),
        //BottomNavItem(Screen.AccessHistory, Icons.Default.History, "Lịch sử"),
        BottomNavItem(Screen.Profile, Icons.Default.Person, "Hồ sơ")
    )

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