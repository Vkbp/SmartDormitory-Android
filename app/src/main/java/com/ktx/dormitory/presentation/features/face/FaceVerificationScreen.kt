package com.ktx.dormitory.presentation.features.face

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ktx.dormitory.ai.core.FaceAnalyzer
import com.ktx.dormitory.ai.core.FaceNetModel
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
    val result by viewModel.verificationResult.collectAsStateWithLifecycle()
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val faceNetModel = remember { FaceNetModel(context) }
    var detectedFaces by remember { mutableStateOf<List<Face>>(emptyList()) }
    var imageSize by remember { mutableStateOf(Size(0, 0)) }

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

    DisposableEffect(Unit) {
        onDispose { 
            cameraExecutor.shutdown()
            faceNetModel.close()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Xác thực khuôn mặt", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
            if (hasCameraPermission) {
                AndroidView(
                    factory = { ctx ->
                        val previewView = PreviewView(ctx)
                        val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                        cameraProviderFuture.addListener({
                            val cameraProvider = cameraProviderFuture.get()
                            
                            val resolutionSelector = ResolutionSelector.Builder()
                                .setResolutionStrategy(
                                    ResolutionStrategy(
                                        Size(1280, 720),
                                        ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER
                                    )
                                )
                                .build()

                            val preview = Preview.Builder()
                                .setResolutionSelector(resolutionSelector)
                                .build().also {
                                it.setSurfaceProvider(previewView.surfaceProvider)
                            }
                            
                            val imageAnalysis = ImageAnalysis.Builder()
                                .setResolutionSelector(resolutionSelector)
                                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                                .build()
                                .also {
                                    it.setAnalyzer(cameraExecutor, FaceAnalyzer(viewModel) { faces, width, height, bitmap ->
                                        detectedFaces = faces
                                        imageSize = Size(width, height)
                                        val face = faces.firstOrNull()
                                        if (face != null && bitmap != null) {
                                            try {
                                                val croppedFace = bitmap.cropFace(face.boundingBox)
                                                val embedding = faceNetModel.getFaceEmbedding(croppedFace)
                                                if (embedding.isNotEmpty()) {
                                                    viewModel.verifyFace(embedding)
                                                }
                                                if (croppedFace != bitmap) croppedFace.recycle()
                                            } catch (e: Exception) { }
                                        }
                                        bitmap?.recycle()
                                    })
                                }
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, CameraSelector.DEFAULT_FRONT_CAMERA, preview, imageAnalysis)
                        }, ContextCompat.getMainExecutor(ctx))
                        previewView
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Cần quyền Camera để xác thực khuôn mặt", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { permissionLauncher.launch(Manifest.permission.CAMERA) }) {
                        Text("CẤP QUYỀN CAMERA")
                    }
                }
            }

            Canvas(modifier = Modifier.fillMaxSize()) {
                if (imageSize.width > 0 && imageSize.height > 0) {
                    val scaleX = size.width / imageSize.height
                    val scaleY = size.height / imageSize.width
                    detectedFaces.forEach { face ->
                        val rect = face.boundingBox
                        drawRect(
                            color = Color.Green,
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

            Column(
                modifier = Modifier.align(Alignment.BottomCenter).padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                result?.let {
                    val color = if (it.isMatched) Color(0xFF4CAF50) else Color.Red
                    Surface(color = color.copy(alpha = 0.9f), shape = RoundedCornerShape(12.dp)) {
                        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = if (it.isMatched) "KHỚP 😄" else "KHÔNG KHỚP ❌", color = Color.White, fontWeight = FontWeight.Bold)
                            if (it.isMatched) {
                                Text("Sinh viên: ${it.studentName}", color = Color.White)
                                Text("Độ tin cậy: ${it.confidence}%", color = Color.White)
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Button(onClick = { viewModel.resetStatus() }) { Text("QUÉT LẠI") }
                } ?: run {
                    Surface(color = Color.Black.copy(alpha = 0.6f), shape = RoundedCornerShape(8.dp)) {
                        Text(text = "Đang chờ nhận diện...", color = Color.White, modifier = Modifier.padding(12.dp))
                    }
                }
            }
        }
    }
}
