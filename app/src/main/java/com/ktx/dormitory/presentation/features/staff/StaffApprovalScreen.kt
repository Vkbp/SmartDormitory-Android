package com.ktx.dormitory.presentation.features.staff

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffApprovalScreen(
    navController: NavController,
    viewModel: StaffApprovalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearError()
        }
    }

    Scaffold(
        topBar = { 
            TopAppBar(
                title = { Text("Duyệt yêu cầu") },
                actions = {
                    IconButton(onClick = { viewModel.fetchRequests() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Tải lại")
                    }
                }
            ) 
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (uiState.isLoading && uiState.requests.isEmpty()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.requests.isEmpty()) {
                Text("Không có yêu cầu nào cần duyệt", modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.requests) { request ->
                        ApprovalCard(
                            request = request,
                            onApprove = { viewModel.updateStatus(request.id, RequestStatus.APPROVED) },
                            onReject = { viewModel.updateStatus(request.id, RequestStatus.REJECTED) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ApprovalCard(request: DormRequest, onApprove: () -> Unit, onReject: () -> Unit) {
    var showApproveDialog by remember { mutableStateOf(false) }
    var showRejectDialog by remember { mutableStateOf(false) }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(request.studentName ?: "Sinh viên", fontWeight = FontWeight.Bold)
                Text(request.type?.displayName ?: "Yêu cầu", color = MaterialTheme.colorScheme.primary)
            }
            Text("MSSV: ${request.studentId ?: "N/A"} • ${request.createdAt ?: ""}", style = MaterialTheme.typography.bodySmall)
            HorizontalDivider(Modifier.padding(vertical = 8.dp))
            Text(request.content ?: "(Không có nội dung)")

            if (request.status == RequestStatus.PENDING) {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = { showRejectDialog = true }, colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)) {
                        Text("TỪ CHỐI")
                    }
                    Button(onClick = { showApproveDialog = true }) {
                        Text("DUYỆT")
                    }
                }
            } else {
                Text(
                    text = if (request.status == RequestStatus.APPROVED) "ĐÃ DUYỆT" else "ĐÃ TỪ CHỐI",
                    modifier = Modifier.padding(top = 12.dp).align(Alignment.End),
                    color = if (request.status == RequestStatus.APPROVED) Color(0xFF4CAF50) else Color.Red,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }

    if (showApproveDialog) {
        AlertDialog(
            onDismissRequest = { showApproveDialog = false },
            title = { Text("Duyệt yêu cầu") },
            text = { Text("Bạn có chắc chắn muốn duyệt yêu cầu của sinh viên ${request.studentName}?") },
            confirmButton = { Button(onClick = { onApprove(); showApproveDialog = false }) { Text("Đồng ý") } },
            dismissButton = { TextButton(onClick = { showApproveDialog = false }) { Text("Hủy") } }
        )
    }

    if (showRejectDialog) {
        AlertDialog(
            onDismissRequest = { showRejectDialog = false },
            title = { Text("Từ chối yêu cầu") },
            text = { Text("Hành động này sẽ từ chối yêu cầu của sinh viên ${request.studentName}. Bạn có chắc chắn không?") },
            confirmButton = { 
                Button(
                    onClick = { onReject(); showRejectDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Từ chối") } 
            },
            dismissButton = { TextButton(onClick = { showRejectDialog = false }) { Text("Hủy") } }
        )
    }
}
