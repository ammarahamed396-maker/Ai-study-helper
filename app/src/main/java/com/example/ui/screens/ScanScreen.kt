package com.example.ui.screens

import android.Manifest
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScanScreen(
    onBack: () -> Unit,
    onAnalysisComplete: (Long) -> Unit,
    viewModel: com.example.viewmodel.LensViewModel
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(permission = Manifest.permission.CAMERA)
    val coroutineScope = rememberCoroutineScope()

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }
    var isAnalyzing by remember { mutableStateOf(false) }
    var customQuery by remember { mutableStateOf("") }
    var showManualInput by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            isAnalyzing = true
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val bitmap = BitmapFactory.decodeStream(inputStream)
                coroutineScope.launch(Dispatchers.IO) {
                    val saved = viewModel.analyzeAndSave(bitmap, null)
                    withContext(Dispatchers.Main) {
                        isAnalyzing = false
                        onAnalysisComplete(saved.id)
                    }
                }
            } catch (e: Exception) {
                isAnalyzing = false
            }
        }
    }

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(Color.Black)) {
        if (cameraPermissionState.status.isGranted) {
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        imageCapture = ImageCapture.Builder().build()
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner, cameraSelector, preview, imageCapture
                            )
                        } catch (e: Exception) {
                            // ignore
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Camera permission required", color = Color.White)
            }
        }

        // Overlay & Viewfinder
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                }
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color.Black.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = "Point. Scan. Understand.",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(
                    onClick = { showManualInput = true },
                    modifier = Modifier.background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "Manual Query", tint = Color.White)
                }
            }

            // Center Scanning Frame Animation Hint
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .border(2.dp, Color.White.copy(alpha = 0.6f), RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (isAnalyzing) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.Black.copy(alpha = 0.8f)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                            Text("Analyzing Object with AI...", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Bottom Controls
            Row(
                modifier = Modifier.fillMaxWidth().navigationBarsPadding(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.PhotoLibrary, contentDescription = "Gallery", tint = Color.White)
                }

                // Shutter Button
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                        .clickable {
                            isAnalyzing = true
                            imageCapture?.let { capture ->
                                val photoFile = File(context.cacheDir, "scan_${System.currentTimeMillis()}.jpg")
                                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
                                capture.takePicture(
                                    outputOptions,
                                    ContextCompat.getMainExecutor(context),
                                    object : ImageCapture.OnImageSavedCallback {
                                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                            val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath)
                                            coroutineScope.launch(Dispatchers.IO) {
                                                val saved = viewModel.analyzeAndSave(bitmap, null)
                                                withContext(Dispatchers.Main) {
                                                    isAnalyzing = false
                                                    onAnalysisComplete(saved.id)
                                                }
                                            }
                                        }
                                        override fun onError(exception: ImageCaptureException) {
                                            isAnalyzing = false
                                        }
                                    }
                                )
                            } ?: run {
                                coroutineScope.launch(Dispatchers.IO) {
                                    val saved = viewModel.analyzeAndSave(null, "Car Engine")
                                    withContext(Dispatchers.Main) {
                                        isAnalyzing = false
                                        onAnalysisComplete(saved.id)
                                    }
                                }
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(68.dp)
                            .clip(CircleShape)
                            .border(2.dp, Color.Black, CircleShape)
                    )
                }

                IconButton(
                    onClick = {
                        isAnalyzing = true
                        coroutineScope.launch(Dispatchers.IO) {
                            val saved = viewModel.analyzeAndSave(null, "Internal Combustion Engine")
                            withContext(Dispatchers.Main) {
                                isAnalyzing = false
                                onAnalysisComplete(saved.id)
                            }
                        }
                    },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(Icons.Default.FlashOn, contentDescription = "Quick Engine Scan", tint = Color.White)
                }
            }
        }

        if (showManualInput) {
            AlertDialog(
                onDismissRequest = { showManualInput = false },
                title = { Text("What do you want to discover?") },
                text = {
                    OutlinedTextField(
                        value = customQuery,
                        onValueChange = { customQuery = it },
                        placeholder = { Text("e.g. Mango Tree, Laptop, Turbine") },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        showManualInput = false
                        if (customQuery.isNotBlank()) {
                            isAnalyzing = true
                            coroutineScope.launch(Dispatchers.IO) {
                                val saved = viewModel.analyzeAndSave(null, customQuery)
                                withContext(Dispatchers.Main) {
                                    isAnalyzing = false
                                    onAnalysisComplete(saved.id)
                                }
                            }
                        }
                    }) {
                        Text("Analyze")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showManualInput = false }) { Text("Cancel") }
                }
            )
        }
    }
}
