package com.ktx.dormitory.presentation.features.student

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ktx.dormitory.presentation.components.LoadingView
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: StudentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    // Xử lý chọn ảnh từ gallery
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val file = uriToFile(context, it)
            file?.let { f -> viewModel.uploadAvatar(f.absolutePath) }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Hồ sơ cá nhân", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            LoadingView()
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar Section
                Box(contentAlignment = Alignment.BottomEnd) {
                    AsyncImage(
                        model = uiState.profile?.avatarUrl ?: "https://ui-avatars.com/api/?name=${uiState.profile?.fullName ?: "User"}&background=random",
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = { imagePickerLauncher.launch("image/*") },
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    
                    if (uiState.isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(120.dp),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    }
                }

                Text(
                    text = uiState.profile?.fullName ?: "Chưa cập nhật",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "MSSV: ${uiState.profile?.studentCode ?: "---"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Info Cards
                InfoSection("Thông tin cơ bản") {
                    InfoRow(Icons.Default.Badge, "CCCD", uiState.profile?.citizenId ?: "---")
                    InfoRow(Icons.Default.Person, "Giới tính", if (uiState.profile?.gender == "MALE") "Nam" else "Nữ")
                    InfoRow(Icons.Default.Cake, "Ngày sinh", uiState.profile?.birthDate ?: "---")
                }

                InfoSection("Học vấn") {
                    InfoRow(Icons.Default.School, "Khoa", uiState.profile?.faculty ?: "---")
                    InfoRow(Icons.Default.Class, "Khóa", uiState.profile?.course ?: "---")
                }

                InfoSection("Liên hệ") {
                    InfoRow(Icons.Default.Phone, "Điện thoại", uiState.profile?.phone ?: "---")
                    InfoRow(Icons.Default.Email, "Email", uiState.profile?.email ?: "---")
                    InfoRow(Icons.Default.LocationOn, "Địa chỉ", uiState.profile?.permanentAddress ?: "---")
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = { /* TODO: Mở màn hình Edit */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("CẬP NHẬT THÔNG TIN")
                }
            }
        }
    }
}

@Composable
fun InfoSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        Spacer(Modifier.width(12.dp))
        Text(label, modifier = Modifier.width(100.dp), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
    }
}

private fun uriToFile(context: android.content.Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "upload_avatar_${System.currentTimeMillis()}.jpg")
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        file
    } catch (e: Exception) {
        null
    }
}
