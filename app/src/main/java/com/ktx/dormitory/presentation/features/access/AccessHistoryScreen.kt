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

/**
 * AccessHistoryScreen - Màn hình xem lịch sử ra vào nâng cao.
 * Tự động lấy studentId từ profile thông qua ViewModel.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AccessHistoryScreen(
    navController: NavController,
    viewModel: AccessViewModel = hiltViewModel()
) {
    val logs by viewModel.accessHistory.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    // Group by date part of eventTimestamp (e.g. "2025-01-15")
    val groupedLogs = remember(logs) {
        logs.groupBy {
            (it.eventTimestamp ?: "").substringBefore("T").ifBlank { "Không xác định" }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchAccessHistory()
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
                                AccessHistoryLogItem(log)
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
fun AccessHistoryLogItem(log: AccessLog) {
    val isGranted = log.decision?.uppercase() == "GRANTED"
    val statusColor = if (isGranted) Color(0xFF4CAF50) else MaterialTheme.colorScheme.error
    val icon: ImageVector = when {
        log.method?.contains("QR", true) == true -> Icons.Default.QrCode
        log.method?.contains("FACE", true) == true -> Icons.Default.Face
        else -> Icons.Default.Nfc
    }

    val timeStr = (log.eventTimestamp ?: "").substringAfter("T", "")
        .substringBefore(".", "")

    val location = when {
        !log.gateId.isNullOrBlank() -> "Cổng ${log.gateId}"
        !log.buildingId.isNullOrBlank() -> "Tòa ${log.buildingId}"
        else -> "Ngoại vi KTX"
    }

    ListItem(
        headlineContent = {
            Text(
                text = location,
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$timeStr • ${log.method ?: "Thẻ"}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                if (!isGranted && !log.denialReason.isNullOrBlank()) {
                    Text(
                        text = "Lý do: ${log.denialReason}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        },
        trailingContent = {
            Column(horizontalAlignment = Alignment.End) {
                Icon(
                    imageVector = if (isGranted) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = null,
                    tint = statusColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = if (isGranted) "Thành công" else "Bị từ chối",
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
