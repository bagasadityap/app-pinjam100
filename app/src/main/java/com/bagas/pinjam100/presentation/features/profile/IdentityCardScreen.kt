package com.bagas.pinjam100.presentation.features.profile

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview as CameraPreview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import com.bagas.pinjam100.presentation.viewmodel.document.DocumentViewModel
import com.bagas.pinjam100.ui.theme.Pinjam100Theme
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@Composable
fun IdentityCardScreen(
    customerId: String,
    modifier: Modifier = Modifier,
    documentViewModel: DocumentViewModel = hiltViewModel(),
    onBack: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val documentState by documentViewModel.uiState.collectAsState()

    var imageCapture by remember {
        mutableStateOf<ImageCapture?>(null)
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {}

    val hasCameraPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.CAMERA
    ) == PackageManager.PERMISSION_GRANTED

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    LaunchedEffect(documentState.successMessage) {
        if (documentState.successMessage != null) {
            documentViewModel.clearSuccess()
            onNext()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        val capture = imageCapture ?: return@Button

                        val photoFile = File(
                            context.cacheDir,
                            "ktp_${System.currentTimeMillis()}.jpg"
                        )

                        val outputOptions =
                            ImageCapture.OutputFileOptions.Builder(photoFile).build()

                        capture.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {

                                override fun onImageSaved(
                                    outputFileResults: ImageCapture.OutputFileResults
                                ) {
                                    val filePart = MultipartBody.Part.createFormData(
                                        "file",
                                        photoFile.name,
                                        photoFile.asRequestBody(
                                            "image/jpeg".toMediaType()
                                        )
                                    )

                                    val type = "KTP".toRequestBody(
                                        "text/plain".toMediaType()
                                    )

                                    val customerIdBody = customerId.toRequestBody(
                                        "text/plain".toMediaType()
                                    )

                                    documentViewModel.upload(
                                        file = filePart,
                                        type = type,
                                        customerId = customerIdBody
                                    )
                                }

                                override fun onError(
                                    exception: ImageCaptureException
                                ) {
                                    exception.printStackTrace()
                                }
                            }
                        )
                    },
                    enabled = hasCameraPermission &&
                            imageCapture != null &&
                            !documentState.isUploading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (documentState.isUploading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            text = "Mengunggah...",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            text = "Ambil Foto KTP",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header & Step Progress
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ScreenHeader(
                        title = "Foto KTP",
                        step = "Langkah 2 dari 5",
                        onBack = onBack
                    )

                    StepProgress(
                        currentStep = 2,
                        totalSteps = 5
                    )
                }
            }

            // Subtitle Description
            item {
                Text(
                    text = "Ambil foto KTP Anda untuk verifikasi identitas.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Camera Preview Container
            item {
                if (hasCameraPermission) {
                    IdentityCardCameraPreview(
                        lifecycleOwner = lifecycleOwner,
                        onImageCaptureReady = {
                            imageCapture = it
                        }
                    )
                } else {
                    CameraPermissionPlaceholder()
                }
            }

            // Guide Text
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Posisikan KTP dengan benar",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Pastikan seluruh bagian KTP terlihat jelas dan tidak terpotong.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // KTP Guide Card
            item {
                IdentityCardGuide()
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun ScreenHeader(
    title: String,
    step: String,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Kembali",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = step,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StepProgress(
    currentStep: Int,
    totalSteps: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalSteps) { index ->
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(
                        color = if (index < currentStep) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }
    }
}

@Composable
private fun IdentityCardCameraPreview(
    lifecycleOwner: LifecycleOwner,
    onImageCaptureReady: (ImageCapture) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(20.dp))
    ) {
        AndroidView(
            factory = { context ->
                PreviewView(context).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER

                    val cameraProviderFuture =
                        ProcessCameraProvider.getInstance(context)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()

                        val preview = CameraPreview.Builder()
                            .build()
                            .also {
                                it.surfaceProvider = surfaceProvider
                            }

                        val imageCapture = ImageCapture.Builder()
                            .setCaptureMode(
                                ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY
                            )
                            .build()

                        val cameraSelector =
                            CameraSelector.DEFAULT_BACK_CAMERA

                        cameraProvider.unbindAll()

                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageCapture
                        )

                        onImageCaptureReady(imageCapture)
                    }, ContextCompat.getMainExecutor(context))
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(0.88f)
                .height(180.dp)
                .border(
                    width = 3.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(14.dp)
                )
        )

        Text(
            text = "Letakkan KTP di dalam kotak",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun CameraPermissionPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFF111827)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.CameraAlt,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Izin kamera diperlukan",
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )
        }
    }
}

@Composable
private fun IdentityCardGuide() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IdentityCardGuideItem(
                icon = Icons.Filled.CheckCircle,
                text = "Seluruh bagian KTP terlihat"
            )

            IdentityCardGuideItem(
                icon = Icons.Filled.LightMode,
                text = "Pastikan pencahayaan cukup"
            )

            IdentityCardGuideItem(
                icon = Icons.Filled.Badge,
                text = "Pastikan tulisan pada KTP dapat dibaca"
            )
        }
    }
}

@Composable
private fun IdentityCardGuideItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IdentityCardPhotoScreenPreview() {
    Pinjam100Theme {
        IdentityCardScreen(
            customerId = "00000000-0000-0000-0000-000000000000"
        )
    }
}