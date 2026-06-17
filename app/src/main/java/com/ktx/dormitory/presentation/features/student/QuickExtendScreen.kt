package com.ktx.dormitory.presentation.features.student

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ktx.dormitory.domain.model.RequestType
import com.ktx.dormitory.presentation.features.request.RequestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuickExtendScreen(
    navController: NavController,
    viewModel: RequestViewModel = hiltViewModel()
) {
    var semesterCount by remember { mutableStateOf("1") }
    var note by remember { mutableStateOf("") }
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    // Theo dõi trạng thái thành công để quay lại màn hình trước
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Đã gửi yêu cầu gia hạn thành công!", Toast.LENGTH_SHORT).show()
            viewModel.resetSuccess()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gia hạn lưu trú", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                "Đăng ký tiếp tục nội trú tại KTX cho học kỳ tiếp theo.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = semesterCount,
                onValueChange = { if (it.all { char -> char.isDigit() }) semesterCount = it },
                label = { Text("Số học kỳ muốn gia hạn") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                prefix = { Text("Học kỳ: ") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = note,
                onValueChange = { note = it },
                label = { Text("Lý do / Ghi chú thêm") },
                placeholder = { Text("Ví dụ: Em muốn ở tiếp kỳ hè...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = 5
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (semesterCount.isBlank()) {
                        Toast.makeText(context, "Vui lòng nhập số học kỳ", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    val content = "Yêu cầu gia hạn $semesterCount học kỳ. Ghi chú: $note"
                    viewModel.onTypeChange(RequestType.EXTEND)
                    viewModel.onContentChange(content)
                    viewModel.submitRequest()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading,
                shape = MaterialTheme.shapes.medium
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("GỬI YÊU CẦU GIA HẠN")
                }
            }
        }
    }
}
