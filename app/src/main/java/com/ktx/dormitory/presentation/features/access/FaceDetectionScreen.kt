package com.ktx.dormitory.presentation.features.access

import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.google.mlkit.vision.face.Face
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceDetectionScreen(navController: NavController) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    
    // State để lưu danh sách khuôn mặt và kích thước preview
    var detectedFaces by remember { mutableStateOf<List<Face>>(emptyList()) }
    var imageSize by remember { mutableStateOf(Size(0, 0)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Face Detection", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            // 1. Camera Preview
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = androidx.camera.lifecycle.ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalysis = ImageAnalysis.Builder()
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor, FaceAnalyzer { faces, width, height, _ ->
                                    detectedFaces = faces
                                    imageSize = Size(width, height)
                                })
                            }

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                CameraSelector.DEFAULT_FRONT_CAMERA, // Dùng camera trước cho AI Face
                                preview,
                                imageAnalysis
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            // 2. Bounding Box Overlay (Vẽ khung mặt)
            FaceBoundingBoxOverlay(detectedFaces, imageSize)
            
            // 3. Status Text
            Column(
                modifier = Modifier.align(androidx.compose.ui.Alignment.BottomCenter).padding(32.dp),
                horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
            ) {
                Surface(
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (detectedFaces.isNotEmpty()) "Phát hiện ${detectedFaces.size} khuôn mặt" else "Đang tìm khuôn mặt...",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FaceBoundingBoxOverlay(faces: List<Face>, imageSize: Size) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (imageSize.width == 0 || imageSize.height == 0) return@Canvas

        val scaleX = size.width / imageSize.height // Vì ảnh Camera xoay 90 độ
        val scaleY = size.height / imageSize.width

        faces.forEach { face ->
            val boundingBox = face.boundingBox
            
            // Map tọa độ từ ImageAnalysis sang Screen (Cần lưu ý gương - Mirroring cho camera trước)
            val left = size.width - (boundingBox.bottom * scaleX)
            val top = boundingBox.left * scaleY
            val right = size.width - (boundingBox.top * scaleX)
            val bottom = boundingBox.right * scaleY

            drawRect(
                color = Color.Cyan,
                topLeft = Offset(left, top),
                size = androidx.compose.ui.geometry.Size(right - left, bottom - top),
                style = Stroke(width = 3.dp.toPx())
            )
        }
    }
}
