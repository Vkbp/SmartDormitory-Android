package com.ktx.dormitory.presentation.features.access

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ktx.dormitory.domain.model.AccessLog
import com.ktx.dormitory.presentation.components.BottomNavBar

/**
 * AccessHistoryScreen - Màn hình xem lịch sử ra vào của sinh viên.
 *
 * Lưu ý: QR Verify đã bị loại bỏ vì Backend KHÔNG hỗ trợ API này cho Sinh viên.
 * Backend chỉ có /api/v1/access/history/student/{id} (VIEW_ACCESS_HISTORY permission).
 *
 * @param studentId UUID của sinh viên đang đăng nhập.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessScreen(
    navController: NavController,
    studentId: String,
    viewModel: AccessViewModel = hiltViewModel()
) {
    val history by viewModel.accessHistory.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchAccessHistory(studentId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Lịch sử ra vào") }) },
        bottomBar = { BottomNavBar(navController) },
        modifier = Modifier.testTag("access_screen")
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center).padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Không thể tải dữ liệu", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(error ?: "", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.fetchAccessHistory(studentId) }) {
                            Text("Thử lại")
                        }
                    }
                }
                history.isEmpty() -> {
                    Text(
                        "Chưa có lịch sử ra vào",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().testTag("access_history_list"),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(history) { log ->
                            AccessLogItem(log)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AccessLogItem(log: AccessLog) {
    val isGranted = log.decision?.uppercase() == "GRANTED"
    Card(
        modifier = Modifier.fillMaxWidth().testTag("access_log_item_${log.id}"),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isGranted) Icons.Default.CheckCircle else Icons.Default.Cancel,
                contentDescription = log.decision,
                tint = if (isGranted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (isGranted) "Vào cổng thành công" else "Bị từ chối: ${log.denialReason ?: "Không rõ lý do"}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Phương thức: ${log.method ?: "N/A"} | ${log.eventTimestamp ?: ""}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
