package com.bagas.pinjam100

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bagas.pinjam100.presentation.navigation.AppNavHost
import com.bagas.pinjam100.presentation.viewmodel.auth.AuthViewModel
import com.bagas.pinjam100.security.RootDetector
import com.bagas.pinjam100.security.SecurityState
import com.bagas.pinjam100.ui.theme.Pinjam100Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {}

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

        val granted = checkSelfPermission(
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED

        if (!granted) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        requestNotificationPermission()

        val securityState = if (RootDetector.isRooted(this)) {
            SecurityState.Rooted
        } else {
            SecurityState.Secure
        }

        setContent {
            Pinjam100Theme {

                when (securityState) {

                    SecurityState.Checking -> {
                        SecurityCheckingIndicator()
                    }

                    SecurityState.Rooted -> {
                        RootDetectedScreen()
                    }

                    SecurityState.Secure -> {
                        SecureAppContent()
                    }
                }
            }
        }
    }

    @Composable
    private fun SecureAppContent() {

        val authState by authViewModel.uiState.collectAsStateWithLifecycle()

        var navReady by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(authState.isRestoringSession) {
            if (!authState.isRestoringSession) {
                navReady = true
            }
        }

        if (!navReady) {
            SessionRestoringIndicator()
        } else {
            AppNavHost(
                authState = authState
            )
        }
    }
}

@Composable
private fun SecurityCheckingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun SessionRestoringIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun RootDetectedScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Perangkat Tidak Didukung"
            )

            Text(
                text = "Aplikasi tidak dapat digunakan pada perangkat yang terdeteksi memiliki akses root."
            )
        }
    }
}