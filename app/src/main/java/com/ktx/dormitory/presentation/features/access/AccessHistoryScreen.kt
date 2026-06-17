package com.ktx.dormitory.presentation.features.access

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ktx.dormitory.domain.model.AccessLog
import com.ktx.dormitory.presentation.components.EmptyView
import com.ktx.dormitory.presentation.components.ErrorView
import com.ktx.dormitory.presentation.components.LoadingView

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AccessHistoryScreen(
    navController: NavController,
    viewModel: AccessViewModel = hiltViewModel()
) {
    val logs by viewModel.accessHistory.collectAsStateWithLifecycle()
    val isLoading = viewModel.isLoading
    val error by viewModel.error.collectAsStateWithLifecycle()

    val groupedLogs = remember(logs) {
        logs.groupBy { (it.timestamp ?: "").split(" ").firstOrNull() ?: "Khác" }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lịch sử ra vào", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.fetchAccessHistory() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Tải lại")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            when {
                isLoading && logs.isEmpty() -> LoadingView()
                error != null && logs.isEmpty() -> ErrorView(
                    message = error ?: "Không thể tải lịch sử",
                    onRetry = { viewModel.fetchAccessHistory() }
                )
                logs.isEmpty() -> EmptyHistoryState()
                else -> {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        groupedLogs.forEach { (date, logsInDate) ->
                            stickyHeader {
                                DateHeader(date)
                            }
                            items(logsInDate) { log ->
                                AccessLogItem(log)
                                HorizontalDivider(
                                    modifier = Modifier.padding(horizontal = 16.dp),
                                    thickness = 0.5.dp,
                                    color = MaterialTheme.colorScheme.outlineVariant
                                )
                            }
                        }
                        item { Spacer(modifier = Modifier.height(32.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun DateHeader(date: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = date,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun AccessLogItem(log: AccessLog) {
    val statusColor = if (log.isSuccess) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
    val icon: ImageVector = if (log.method?.contains("QR", true) == true) Icons.Default.QrCode else Icons.Default.Nfc

    ListItem(
        headlineContent = {
            Text(
                text = log.location ?: "Ngoại vi KTX",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${(log.timestamp ?: "").split(" ").lastOrNull() ?: ""} • ${log.method ?: "Thẻ"}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                Icon(
                    imageVector = if (log.isSuccess) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = if (log.isSuccess) "Thành công" else "Bị từ chối",
                    style = MaterialTheme.typography.labelSmall,
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
            }
        },
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}

@Composable
fun EmptyHistoryState() {
    EmptyView(
        message = "Chưa có dữ liệu ra vào",
        icon = Icons.Default.History
    )
}
