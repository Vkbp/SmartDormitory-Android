package com.ktx.dormitory.presentation.features.notification

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ktx.dormitory.domain.model.Notification
import com.ktx.dormitory.presentation.components.EmptyView
import com.ktx.dormitory.presentation.components.ErrorView
import com.ktx.dormitory.presentation.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationScreen(
    navController: NavController,
    viewModel: NotificationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Thông báo", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize().testTag("notification_screen_container")) {
            when {
                uiState.isLoading && uiState.notifications.isEmpty() -> Box(Modifier.testTag("notification_loading_view")) { LoadingView() }
                uiState.error != null && uiState.notifications.isEmpty() -> Box(Modifier.testTag("notification_error_view")) {
                    ErrorView(
                        message = uiState.error,
                        onRetry = { viewModel.loadNotifications() }
                    )
                }
                uiState.notifications.isEmpty() -> Box(Modifier.testTag("notification_empty_view")) {
                    EmptyView(
                        message = "Bạn không có thông báo nào",
                        icon = Icons.Default.Notifications
                    )
                }
                else -> {
                    NotificationList(
                        notifications = uiState.notifications,
                        onItemClick = { viewModel.markAsRead(it.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationList(
    notifications: List<Notification>,
    onItemClick: (Notification) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().testTag("notification_list")) {
        items(notifications) { notification ->
            NotificationItem(notification) { onItemClick(notification) }
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )
        }
    }
}

@Composable
fun NotificationItem(
    notification: Notification,
    onClick: () -> Unit
) {
    val backgroundColor = if (notification.isRead) Color.Transparent else MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(16.dp)
            .testTag("notification_item_${notification.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    if (notification.isRead) Color.Transparent else MaterialTheme.colorScheme.primary,
                    CircleShape
                )
                .testTag("notification_unread_indicator_${notification.id}")
        )
        
        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.title ?: "Thông báo",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = notification.message ?: "",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = notification.time ?: "",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}
