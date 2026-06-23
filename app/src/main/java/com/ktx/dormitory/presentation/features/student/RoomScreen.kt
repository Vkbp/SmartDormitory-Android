package com.ktx.dormitory.presentation.features.student

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ktx.dormitory.presentation.components.LoadingView
import com.ktx.dormitory.presentation.components.ErrorView

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomScreen(navController: NavController, viewModel: RoomViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông tin chỗ ở", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            val room = uiState.roomInfo
            val error = uiState.error
            when {
                uiState.isLoading -> LoadingView()
                error != null -> ErrorView(
                    message = error,
                    onRetry = { viewModel.loadRoomInfo() }
                )
                room != null -> {
                    Column(modifier = Modifier.padding(16.dp)) {
                        RoomDetailCard("Tòa nhà", room.building, Icons.Default.Business)
                        RoomDetailCard("Tầng", room.floor?.toString(), Icons.Default.Layers)
                        RoomDetailCard("Số phòng", room.roomCode, Icons.Default.MeetingRoom)
                        RoomDetailCard("Vị trí giường", room.bedCode, Icons.Default.Bed)
                        RoomDetailCard("Trạng thái", room.status, Icons.Default.Info)
                    }
                }
                else -> {
                    if (!uiState.isLoading) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Chưa có thông tin phòng")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoomDetailCard(label: String, value: String?, icon: ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(16.dp))
            Column {
                Text(label, style = MaterialTheme.typography.labelMedium)
                Text(
                    text = value ?: "Không xác định",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
