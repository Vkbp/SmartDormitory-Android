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
import androidx.compose.ui.platform.testTag
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
    val formState by viewModel.formState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var expanded by remember { mutableStateOf(false) }
    var showConfirmDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.submitSuccess) {
        if (uiState.submitSuccess) {
            snackbarHostState.showSnackbar("Gửi yêu cầu thành công!")
            viewModel.clearStatus()
        }
    }

    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("Gửi yêu cầu?") },
            text = { Text("Bạn có chắc chắn muốn gửi yêu cầu này tới ban quản lý KTX không?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.submitRequest()
                        showConfirmDialog = false
                    },
                    modifier = Modifier.testTag("request_confirm_dialog_confirm")
                ) { Text("Gửi ngay") }
            },
            dismissButton = {
                TextButton(
                    onClick = { showConfirmDialog = false },
                    modifier = Modifier.testTag("request_confirm_dialog_cancel")
                ) { Text("Hủy") }
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
                .fillMaxSize()
                .testTag("request_screen_column"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                RequestForm(
                    formState = formState,
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

            if (uiState.requests.isEmpty()) {
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp).testTag("request_empty_view"), contentAlignment = Alignment.Center) {
                        Text("Bạn chưa gửi yêu cầu nào", color = Color.Gray)
                    }
                }
            } else {
                items(uiState.requests) { request ->
                    MyRequestCard(request)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestForm(
    formState: RequestFormState,
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
            onExpandedChange = onExpandedChange,
            modifier = Modifier.testTag("request_type_dropdown")
        ) {
            OutlinedTextField(
                value = formState.type.displayName,
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
                        },
                        modifier = Modifier.testTag("request_type_item_${type.name}")
                    )
                }
            }
        }

        OutlinedTextField(
            value = formState.content,
            onValueChange = onContentChange,
            label = { Text("Nội dung chi tiết") },
            modifier = Modifier.fillMaxWidth().height(120.dp).testTag("request_content_field"),
            isError = formState.error != null,
            supportingText = { formState.error?.let { Text(it) } }
        )

        Button(
            onClick = onSubmit,
            modifier = Modifier.fillMaxWidth().testTag("request_submit_button"),
            enabled = !formState.isLoading,
            shape = RoundedCornerShape(8.dp)
        ) {
            if (formState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp).testTag("request_loading_indicator"), color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
            } else {
                Text("GỬI YÊU CẦU")
            }
        }
    }
}

@Composable
fun MyRequestCard(request: DormRequest) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("request_card_${request.id}"),
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
