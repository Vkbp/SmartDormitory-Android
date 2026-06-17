package com.ktx.dormitory.presentation.features.student

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.ktx.dormitory.navigation.Screen
import com.ktx.dormitory.presentation.components.BottomNavBar
import com.ktx.dormitory.presentation.components.LoadingView
import com.ktx.dormitory.presentation.components.ErrorView
import com.ktx.dormitory.domain.model.UserProfile
import com.ktx.dormitory.presentation.features.auth.LoginViewModel
import com.ktx.dormitory.core.utils.DateTimeUtils
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    studentViewModel: StudentViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by studentViewModel.uiState.collectAsState()
    val userData by loginViewModel.userData.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    // Trạng thái chỉnh sửa trực tiếp
    var isEditMode by remember { mutableStateOf(false) }
    var editedName by remember { mutableStateOf("") }
    var editedPhone by remember { mutableStateOf("") }
    var editedEmail by remember { mutableStateOf("") }

    // Cập nhật giá trị ban đầu khi có profile hoặc userData
    LaunchedEffect(uiState.profile, userData) {
        uiState.profile?.let {
            editedName = it.fullName ?: userData?.fullName ?: ""
            editedPhone = it.phone ?: ""
            editedEmail = it.email ?: ""
        } ?: userData?.let {
            if (editedName.isEmpty()) editedName = it.fullName ?: ""
        }
    }

    // Launcher chọn ảnh
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val file = File(context.cacheDir, "avatar_upload.jpg")
            context.contentResolver.openInputStream(it)?.use { input ->
                FileOutputStream(file).use { output ->
                    input.copyTo(output)
                }
            }
            studentViewModel.uploadAvatar(file.absolutePath)
        }
    }

    if (uiState.uploadSuccess) {
        LaunchedEffect(Unit) {
            studentViewModel.clearUploadStatus()
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Đăng xuất") },
            text = { Text("Bạn có chắc chắn muốn đăng xuất khỏi hệ thống?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        loginViewModel.logout {
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) { Text("Đăng xuất") }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Hủy") }
            }
        )
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
                actions = {
                    if (isEditMode) {
                        TextButton(onClick = { 
                            isEditMode = false 
                            // Reset về dữ liệu cũ nếu hủy
                            uiState.profile?.let {
                                editedName = it.fullName ?: ""
                                editedPhone = it.phone ?: ""
                                editedEmail = it.email ?: ""
                            }
                        }) {
                            Text("Hủy")
                        }
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            val profile = uiState.profile
            val error = uiState.error
            when {
                uiState.isLoading -> LoadingView()
                error != null -> ErrorView(
                    message = error,
                    onRetry = { studentViewModel.loadAllData() }
                )
                profile != null -> {
                    ProfileContent(
                        profile = profile,
                        isUploading = uiState.isUploading,
                        isEditMode = isEditMode,
                        editedName = editedName,
                        editedPhone = editedPhone,
                        editedEmail = editedEmail,
                        defaultName = userData?.fullName ?: "Người dùng",
                        onNameChange = { editedName = it },
                        onPhoneChange = { editedPhone = it },
                        onEmailChange = { editedEmail = it },
                        onLogout = { showLogoutDialog = true },
                        onToggleEdit = {
                            if (isEditMode) {
                                studentViewModel.updateProfile(editedName, editedPhone, editedEmail) {
                                    isEditMode = false
                                }
                            } else {
                                isEditMode = true
                            }
                        },
                        onPickImage = { launcher.launch("image/*") },
                        onChangePassword = { navController.navigate(Screen.ChangePassword.route) }
                    )
                }
                else -> {
                    if (!uiState.isLoading) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("Không tìm thấy dữ liệu hồ sơ")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileContent(
    profile: UserProfile,
    isUploading: Boolean,
    isEditMode: Boolean,
    editedName: String,
    editedPhone: String,
    editedEmail: String,
    defaultName: String,
    onNameChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onLogout: () -> Unit,
    onToggleEdit: () -> Unit,
    onPickImage: () -> Unit,
    onChangePassword: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ... (Avatar code remains same)
        // Avatar Section
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .clickable { if (!isUploading) onPickImage() }
        ) {
            if (profile.avatarUrl != null) {
                AsyncImage(
                    model = profile.avatarUrl,
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            if (isUploading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.secondary
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.padding(6.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isEditMode) {
            OutlinedTextField(
                value = editedName,
                onValueChange = onNameChange,
                label = { Text("Họ và tên") },
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Text(
                text = profile.fullName ?: defaultName,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        }
        
        Text(
            text = "@${profile.username ?: "user"}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        SuggestionChip(
            onClick = { },
            label = { Text(profile.role ?: "USER") },
            colors = SuggestionChipDefaults.suggestionChipColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Chi tiết hồ sơ theo Spec
        ProfileInfoGroup(title = "Thông tin định danh") {
            ProfileInfoItem("Mã sinh viên", profile.studentCode, Icons.Default.Badge)
            ProfileInfoItem("Số CCCD", profile.cccd, Icons.Default.CreditCard)
        }

        ProfileInfoGroup(title = "Thông tin học tập") {
            ProfileInfoItem("Khoa", profile.faculty, Icons.Default.School)
            ProfileInfoItem("Khóa", profile.course, Icons.Default.CalendarToday)
        }

        ProfileInfoGroup(title = "Thông tin liên hệ") {
            if (isEditMode) {
                OutlinedTextField(
                    value = editedEmail,
                    onValueChange = onEmailChange,
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    leadingIcon = { Icon(Icons.Default.Email, null) }
                )
                OutlinedTextField(
                    value = editedPhone,
                    onValueChange = onPhoneChange,
                    label = { Text("Số điện thoại") },
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    leadingIcon = { Icon(Icons.Default.Phone, null) }
                )
            } else {
                ProfileInfoItem("Email", profile.email, Icons.Default.Email)
                ProfileInfoItem("Số điện thoại", profile.phone, Icons.Default.Phone)
            }
            ProfileInfoItem("Địa chỉ", profile.address, Icons.Default.Home)
        }

        ProfileInfoGroup(title = "Thông tin cá nhân") {
            ProfileInfoItem("Giới tính", profile.gender, Icons.Default.Person)
            ProfileInfoItem(
                label = "Ngày sinh",
                value = DateTimeUtils.formatIsoDate(profile.birthDate),
                icon = Icons.Default.Cake
            )
        }

        ProfileInfoGroup(title = "Bảo mật & Tài khoản") {
            ProfileInfoItem(
                label = "Mật khẩu",
                value = "********",
                icon = Icons.Default.Lock,
                onClick = if (!isEditMode) onChangePassword else null
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onToggleEdit,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isEditMode) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(if (isEditMode) "LƯU THÔNG TIN" else "CẬP NHẬT THÔNG TIN", color = Color.White)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            shape = MaterialTheme.shapes.medium
        ) {
            Text("ĐĂNG XUẤT", color = Color.White)
        }
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileInfoGroup(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp, bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                content()
            }
        }
    }
}

@Composable
fun ProfileInfoItem(
    label: String,
    value: String?,
    icon: ImageVector,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = onClick != null) { onClick?.invoke() }
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            icon,
            null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
            Text(
                text = if (value.isNullOrBlank()) "Chưa cập nhật" else value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold
            )
        }
        if (onClick != null) {
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
