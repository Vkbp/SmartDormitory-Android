package com.ktx.dormitory.presentation.features.profile

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.Logout
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
import com.ktx.dormitory.navigation.Screen
import com.ktx.dormitory.presentation.components.LoadingView
import com.ktx.dormitory.presentation.features.auth.LoginViewModel
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = hiltViewModel(),
    loginViewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    
    var isEditing by remember { mutableStateOf(false) }
    
    var editName by remember { mutableStateOf("") }
    var editPhone by remember { mutableStateOf("") }
    var editEmail by remember { mutableStateOf("") }

    LaunchedEffect(uiState.profile) {
        uiState.profile?.let {
            editName = it.fullName ?: ""
            editPhone = it.phone ?: ""
            editEmail = it.email ?: ""
        }
    }

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
                title = { 
                    Text(
                        text = if (isEditing) "Chỉnh sửa hồ sơ" else "Hồ sơ cá nhân", 
                        fontWeight = FontWeight.Bold 
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { 
                        if (isEditing) isEditing = false else navController.popBackStack() 
                    }) {
                        Icon(
                            if (isEditing) Icons.Default.Close else Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (isEditing) {
                        IconButton(onClick = {
                            viewModel.updateProfile(editName, editPhone, editEmail) {
                                isEditing = false
                                Toast.makeText(context, "Đã cập nhật thông tin!", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(Icons.Default.Check, contentDescription = "Lưu", tint = MaterialTheme.colorScheme.primary)
                        }
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

                if (isEditing) {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Họ tên") },
                        modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                        singleLine = true
                    )
                } else {
                    Text(
                        text = uiState.profile?.fullName ?: "Chưa cập nhật",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }
                
                Text(
                    text = "MSSV: ${uiState.profile?.studentCode ?: "---"}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                Spacer(modifier = Modifier.height(24.dp))

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
                    if (isEditing) {
                        EditableInfoRow(Icons.Default.Phone, "Điện thoại", editPhone) { editPhone = it }
                        EditableInfoRow(Icons.Default.Email, "Email", editEmail) { editEmail = it }
                    } else {
                        InfoRow(Icons.Default.Phone, "Điện thoại", uiState.profile?.phone ?: "---")
                        InfoRow(Icons.Default.Email, "Email", uiState.profile?.email ?: "---")
                    }
                    InfoRow(Icons.Default.LocationOn, "Địa chỉ", uiState.profile?.permanentAddress ?: "---")
                }
                
                Spacer(modifier = Modifier.height(24.dp))

                if (!isEditing) {
                    Button(
                        onClick = { isEditing = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("CẬP NHẬT THÔNG TIN")
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = { navController.navigate(Screen.ChangePassword.route) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("ĐỔI MẬT KHẨU")
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = {
                            loginViewModel.logout {
                                navController.navigate(Screen.Login.route) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("ĐĂNG XUẤT")
                    }
                } else {
                    Button(
                        onClick = {
                            viewModel.updateProfile(editName, editPhone, editEmail) {
                                isEditing = false
                                Toast.makeText(context, "Đã cập nhật thông tin!", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("LƯU THÔNG TIN")
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    TextButton(
                        onClick = { isEditing = false },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("HỦY BỎ", color = Color.Gray)
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
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

@Composable
fun EditableInfoRow(icon: ImageVector, label: String, value: String, onValueChange: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.width(12.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            textStyle = MaterialTheme.typography.bodyMedium,
            singleLine = true
        )
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
