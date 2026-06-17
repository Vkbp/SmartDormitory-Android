package com.ktx.dormitory.presentation.features.request

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ktx.dormitory.core.utils.DateTimeUtils
import com.ktx.dormitory.domain.model.DormRequest
import com.ktx.dormitory.domain.model.RequestStatus
import com.ktx.dormitory.domain.model.RequestType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestScreen(navController: NavController, viewModel: RequestViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val myRequests by viewModel.myRequests.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            snackbarHostState.showSnackbar("Gửi yêu cầu thành công!")
            viewModel.resetSuccess()
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Gửi yêu cầu?") },
            text = { Text("Bạn có chắc chắn muốn gửi yêu cầu này tới ban quản lý KTX không?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.submitRequest()
                    showConfirmDialog = false
                }) { Text("Gửi ngay") }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) { Text("Hủy") }
            }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Gửi yêu cầu", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                RequestForm(
                    uiState = uiState,
                    expanded = expanded,
                    onExpandedChange = { expanded = it },
                    onTypeChange = { viewModel.onTypeChange(it) },
                    onContentChange = { viewModel.onContentChange(it) },
                    onSubmit = { showConfirmDialog = true }
                )
            }

            item {
                HorizontalDivider()
                Spacer(Modifier.height(16.dp))
                Text("Yêu cầu của tôi", style = MaterialTheme.typography.titleMedium)
            }

            if (myRequests.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("Bạn chưa gửi yêu cầu nào", color = Color.Gray)
                    }
                }
            } else {
                items(myRequests) { request ->
                    MyRequestCard(request)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestForm(
    uiState: RequestFormState,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onTypeChange: (RequestType) -> Unit,
    onContentChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Chọn loại yêu cầu", style = MaterialTheme.typography.titleSmall)

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = onExpandedChange
        ) {
            OutlinedTextField(
                value = uiState.type.displayName,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) }) {
                RequestType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.displayName) },
                        onClick = {
                            onTypeChange(type)
                            onExpandedChange(false)
                        }
                    )
                }
            }
        }

        OutlinedTextField(
            value = uiState.content,
            onValueChange = onContentChange,
            label = { Text("Nội dung chi tiết") },
            modifier = Modifier.fillMaxWidth().height(120.dp),
            isError = uiState.error != null,
            supportingText = { uiState.error?.let { Text(it) } }
        )

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth(),
            enabled = !uiState.isLoading,
            shape = RoundedCornerShape(8.dp)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
            } else {
                Text("GỬI YÊU CẦU")
            }
        }
    }
}

@Composable
fun MyRequestCard(request: DormRequest) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(request.type?.displayName ?: "Yêu cầu", fontWeight = FontWeight.Bold)
                StatusChip(request.status)
            }
            Text(DateTimeUtils.formatIsoDate(request.createdAt), style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Text(request.content ?: "", style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun StatusChip(status: RequestStatus) {
    val color = when (status) {
        RequestStatus.PENDING -> Color(0xFFFF9800)
        RequestStatus.APPROVED -> Color(0xFF4CAF50)
        RequestStatus.REJECTED -> Color(0xFFF44336)
    }
    Surface(
        color = color.copy(alpha = 0.1f),
        contentColor = color,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = when(status) {
                RequestStatus.PENDING -> "Chờ duyệt"
                RequestStatus.APPROVED -> "Đã duyệt"
                RequestStatus.REJECTED -> "Từ chối"
            },
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold
        )
    }
}
