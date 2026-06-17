package com.ktx.dormitory.presentation.features.access

import android.graphics.Bitmap
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ktx.dormitory.core.utils.cropFace
import com.ktx.dormitory.presentation.features.auth.LoginViewModel
import java.util.concurrent.Executors
import com.google.mlkit.vision.face.Face

// ISSUE 1: Đảm bảo tính nguyên tử của dữ liệu Frame
data class CaptureState(
    val bitmap: Bitmap? = null,
    val faces: List<Face> = emptyList()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceRegistrationScreen(
    navController: NavController,
    loginViewModel: LoginViewModel,
    faceViewModel: FaceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val userData by loginViewModel.uiState.collectAsState()
    val isRegistering by faceViewModel.isRegistering.collectAsState()
    val isSuccess by faceViewModel.registrationSuccess.collectAsState()
    val livenessState by faceViewModel.livenessState.collectAsState()
    val qualityState by faceViewModel.qualityState.collectAsState()
    
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    
    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    val faceNetModel = remember { FaceNetModel(context) }
    
    // Sử dụng CaptureState để tránh Race Condition
    var captureState by remember { mutableStateOf(CaptureState()) }

    LaunchedEffect(isSuccess) {
        if (isSuccess) {
            Toast.makeText(context, "Đăng ký khuôn mặt thành công!", Toast.LENGTH_SHORT).show()
            faceViewModel.resetStatus()
            navController.popBackStack()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Đăng ký khuôn mặt", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
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
                                it.setAnalyzer(cameraExecutor, FaceAnalyzer(faceViewModel) { faces, _, _, bitmap ->
                                    // Cập nhật nguyên tử (Atomic Update)
                                    captureState = CaptureState(bitmap, faces)
                                })
                            }
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_FRONT_CAMERA, preview, imageAnalysis)
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // UI Overlay cho Liveness và Chất lượng
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                // Hiển thị bước Liveness
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (qualityState.isGood) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = if (qualityState.isGood) "Bước: ${getStepName(livenessState.currentStep)}" else qualityState.message,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        if (qualityState.isGood) {
                            LinearProgressIndicator(
                                progress = { livenessState.progress },
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.align(Alignment.BottomCenter).padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val currentFrame = captureState
                        val face = currentFrame.faces.firstOrNull()
                        val bitmap = currentFrame.bitmap
                        val student = userData.userData
                        
                        if (face != null && bitmap != null && student != null) {
                            val croppedFace = bitmap.cropFace(face.boundingBox)
                            val embedding = faceNetModel.getFaceEmbedding(croppedFace)
                            
                            if (embedding.isNotEmpty()) {
                                faceViewModel.registerFace(
                                    student.username,
                                    student.fullName ?: "Người dùng",
                                    embedding
                                )
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = !isRegistering && livenessState.currentStep == LivenessStep.COMPLETED
                ) {
                    if (isRegistering) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(if (livenessState.currentStep == LivenessStep.COMPLETED) "LƯU KHUÔN MẶT" else "HOÀN THÀNH CÁC BƯỚC TRÊN")
                    }
                }
            }
        }
    }
}

fun getStepName(step: LivenessStep): String {
    return when (step) {
        LivenessStep.EYE_BLINK -> "Hãy nháy mắt"
        LivenessStep.TURN_LEFT -> "Hãy quay đầu sang trái"
        LivenessStep.TURN_RIGHT -> "Hãy quay đầu sang phải"
        LivenessStep.SMILE -> "Hãy mỉm cười"
        LivenessStep.COMPLETED -> "Xác thực người thật thành công"
    }
}
