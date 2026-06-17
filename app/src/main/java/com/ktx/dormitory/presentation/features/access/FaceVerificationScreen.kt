package com.ktx.dormitory.presentation.features.access

import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ktx.dormitory.core.utils.cropFace
import com.google.mlkit.vision.face.Face
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceVerificationScreen(
    navController: NavController,
    viewModel: FaceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val result by viewModel.verificationResult.collectAsState()
    
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val faceNetModel = remember { FaceNetModel(context) }
    
    var detectedFaces by remember { mutableStateOf<List<Face>>(emptyList()) }
    var imageSize by remember { mutableStateOf(Size(0, 0)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Face Verification", fontWeight = FontWeight.Bold) },
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
                                it.setAnalyzer(cameraExecutor, FaceAnalyzer { faces, width, height, bitmap ->
                                    detectedFaces = faces
                                    imageSize = Size(width, height)
                                    
                                    // Tự động xác thực nếu thấy khuôn mặt
                                    val face = faces.firstOrNull()
                                    if (face != null && bitmap != null) {
                                        try {
                                            val croppedFace = bitmap.cropFace(face.boundingBox)
                                            val embedding = faceNetModel.getFaceEmbedding(croppedFace)
                                            if (embedding.isNotEmpty()) {
                                                viewModel.verifyFace(embedding)
                                            }
                                        } catch (e: Exception) { e.printStackTrace() }
                                    }
                                })
                            }
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_FRONT_CAMERA, preview, imageAnalysis)
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // Vẽ khung mặt
            FaceBoundingBoxOverlay(detectedFaces, imageSize)

            // Hiển thị kết quả
            Column(
                modifier = Modifier.align(Alignment.BottomCenter).padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                result?.let {
                    val color = if (it.isMatched) Color(0xFF4CAF50) else Color.Red
                    Surface(
                        color = color.copy(alpha = 0.9f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = if (it.isMatched) "MATCHED 😄🔥" else "NOT RECOGNIZED 😄❌",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleLarge
                            )
                            if (it.isMatched) {
                                Text("Sinh viên: ${it.studentName}", color = Color.White)
                                Text("Confidence: ${it.confidence}%", color = Color.White)
                            }
                        }
                    }
                    
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.resetStatus() }) {
                        Text("QUÉT LẠI")
                    }
                } ?: run {
                    Surface(
                        color = Color.Black.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Vui lòng đưa khuôn mặt vào khung hình",
                            color = Color.White,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }
        }
    }
}
