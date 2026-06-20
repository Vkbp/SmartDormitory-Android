package com.ktx.dormitory.presentation.features.access

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOff
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ktx.dormitory.presentation.components.BottomNavBar
import com.ktx.dormitory.core.utils.ShowBiometricPrompt

fun openAppSettings(context: android.content.Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccessScreen(
    navController: NavController,
    viewModel: AccessViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiMessage by viewModel.uiMessage.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    LaunchedEffect(uiMessage) {
        uiMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearMessage()
        }
    }

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ra vào phòng") }) },
        bottomBar = { BottomNavBar(navController) },
        modifier = Modifier.testTag("access_screen")
    ) { innerPadding ->
        if (hasCameraPermission) {
            AccessCameraContent(innerPadding, viewModel, isLoading)
        } else {
            Box(
                modifier = Modifier.fillMaxSize().padding(innerPadding).testTag("access_permission_view"),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
                    Icon(Icons.Default.FlashOff, contentDescription = null, modifier = Modifier.size(64.dp), tint = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Chưa được cấp quyền Camera", style = MaterialTheme.typography.titleMedium)
                    Text("Cần quyền Camera để quét mã QR mở cửa.", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline)
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(onClick = { openAppSettings(context) }, modifier = Modifier.testTag("access_open_settings_button")) {
                        Text("MỞ CÀI ĐẶT")
                    }
                }
            }
        }
    }
}

@Composable
fun AccessCameraContent(
    innerPadding: PaddingValues,
    viewModel: AccessViewModel,
    isLoading: Boolean
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var showBiometricDialog by remember { mutableStateOf(false) }
    
    var isFlashOn by remember { mutableStateOf(false) }
    var cameraInstance by remember { mutableStateOf<Camera?>(null) }

    Column(
        modifier = Modifier.fillMaxSize().padding(innerPadding).testTag("access_camera_content")
    ) {
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val executor = ContextCompat.getMainExecutor(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(executor, QrCodeAnalyzer { qrContent ->
                                    viewModel.verifyQrCode(qrContent) {}
                                })
                            }

                        try {
                            cameraProvider.unbindAll()
                            cameraInstance = cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_BACK_CAMERA,
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, executor)
                    previewView
                },
                modifier = Modifier.fillMaxSize().testTag("access_camera_preview")
            )

            IconButton(
                onClick = {
                    isFlashOn = !isFlashOn
                    cameraInstance?.cameraControl?.enableTorch(isFlashOn)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    .testTag("access_toggle_flash")
            ) {
                Icon(
                    imageVector = if (isFlashOn) Icons.Default.FlashOn else Icons.Default.FlashOff,
                    contentDescription = "Toggle Flash",
                    tint = if (isFlashOn) Color.Yellow else Color.White
                )
            }
            
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize().testTag("access_loading_overlay"), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Quét mã QR tại cửa phòng để ra vào", style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    FilledTonalButton(
                        onClick = { showBiometricDialog = true },
                        enabled = !isLoading,
                        modifier = Modifier.testTag("access_biometric_button")
                    ) {
                        Text("DÙNG VÂN TAY")
                    }
                }
            }
        }
    }

    if (showBiometricDialog) {
        ShowBiometricPrompt(
            title = "Xác thực ra vào",
            subtitle = "Sử dụng vân tay để mở cửa",
            onSuccess = {
                showBiometricDialog = false
                viewModel.verifyQrCode("BIOMETRIC_BYPASS") {
                    Toast.makeText(context, "Cửa đã mở!", Toast.LENGTH_SHORT).show()
                }
            },
            onError = { showBiometricDialog = false }
        )
    }
}
