package com.ktx.dormitory.presentation.face.screen

import android.util.Size
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.google.mlkit.vision.face.Face
import com.ktx.dormitory.ai.core.FaceAnalyzer
import com.ktx.dormitory.presentation.face.viewmodel.FaceViewModel
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceDetectionScreen(
    navController: NavController,
    viewModel: FaceViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var detectedFaces by remember { mutableStateOf<List<Face>>(emptyList()) }
    var imageSize by remember { mutableStateOf(Size(0, 0)) }

    DisposableEffect(Unit) {
        onDispose { cameraExecutor.shutdown() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Face Detection", fontWeight = FontWeight.Bold) },
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
                                it.setAnalyzer(cameraExecutor, FaceAnalyzer(viewModel) { faces, width, height, _ ->
                                    detectedFaces = faces
                                    imageSize = Size(width, height)
                                })
                            }
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_FRONT_CAMERA, preview, imageAnalysis)
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )

            Canvas(modifier = Modifier.fillMaxSize()) {
                if (imageSize.width > 0 && imageSize.height > 0) {
                    val scaleX = size.width / imageSize.height
                    val scaleY = size.height / imageSize.width
                    detectedFaces.forEach { face ->
                        val rect = face.boundingBox
                        drawRect(
                            color = Color.Cyan,
                            topLeft = androidx.compose.ui.geometry.Offset(
                                x = size.width - (rect.right * scaleX),
                                y = rect.top * scaleY
                            ),
                            size = androidx.compose.ui.geometry.Size(
                                width = rect.width() * scaleX,
                                height = rect.height() * scaleY
                            ),
                            style = Stroke(width = 4f)
                        )
                    }
                }
            }
        }
    }
}
