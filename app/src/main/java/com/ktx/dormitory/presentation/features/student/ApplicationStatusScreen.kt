package com.ktx.dormitory.presentation.features.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ktx.dormitory.core.utils.DateTimeUtils
import com.ktx.dormitory.domain.model.AppStepStatus
import com.ktx.dormitory.domain.model.TimelineStep
import com.ktx.dormitory.presentation.components.LoadingView
import com.ktx.dormitory.presentation.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationStatusScreen(navController: NavController, viewModel: StudentViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tiến độ đăng ký", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            val app = uiState.application
            when {
                uiState.isLoading -> LoadingView()
                uiState.error != null -> ErrorView(
                    message = uiState.error ?: "Lỗi không xác định",
                    onRetry = { viewModel.loadAllData() }
                )
                app != null -> {
                    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
                        Card(modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Mã đơn: ${app.applicationCode ?: "N/A"}", fontWeight = FontWeight.Bold)
                                Text(
                                    "Ngày nộp: ${DateTimeUtils.formatIsoDate(app.submissionDate)}", 
                                    style = MaterialTheme.typography.bodySmall
                                )
                                Text("Trạng thái: ${app.status ?: "Đang xử lý"}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                            }
                        }

                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            itemsIndexed(app.timeline) { index, step ->
                                TimelineItem(
                                    step = step,
                                    isLast = index == app.timeline.size - 1
                                )
                            }
                        }
                    }
                }
                else -> {
                    if (!uiState.isLoading) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Không tìm thấy thông tin đơn đăng ký")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineItem(step: TimelineStep, isLast: Boolean) {
    val color = when(step.stepStatus) {
        AppStepStatus.COMPLETED -> MaterialTheme.colorScheme.primary
        AppStepStatus.CURRENT -> Color(0xFFF44336)
        AppStepStatus.WAITING, null -> Color.Gray
    }

    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier.size(24.dp).clip(CircleShape).background(color),
                contentAlignment = Alignment.Center
            ) {
                if (step.stepStatus == AppStepStatus.COMPLETED) {
                    Icon(Icons.Default.Check, null, modifier = Modifier.size(16.dp), tint = Color.White)
                }
            }
            if (!isLast) {
                Box(modifier = Modifier.width(2.dp).fillMaxHeight().background(Color.LightGray))
            }
        }
        
        Spacer(Modifier.width(16.dp))
        
        Column(modifier = Modifier.padding(bottom = 32.dp)) {
            Text(step.title ?: "Không có tiêu đề", fontWeight = FontWeight.Bold, color = color)
            Text(step.description ?: "", style = MaterialTheme.typography.bodyMedium)
            step.timestamp?.let {
                Text(
                    DateTimeUtils.formatIsoDateTime(it), 
                    style = MaterialTheme.typography.labelSmall, 
                    color = Color.Gray
                )
            }
        }
    }
}
