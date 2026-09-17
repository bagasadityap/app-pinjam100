package com.bagas.pinjam100

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bagas.pinjam100.core.notification.AppNotification
import com.bagas.pinjam100.core.notification.AppNotifier
import com.bagas.pinjam100.presentation.navigation.AppNavHost
import com.bagas.pinjam100.presentation.viewmodel.auth.AuthViewModel
import com.bagas.pinjam100.ui.theme.Pinjam100Theme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()
    private val notificationPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
    @Inject
    lateinit var notifier: AppNotifier
    private fun requestNotificationPermission() {
        val granted = Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pinjam100Theme {
                val authState by authViewModel.uiState.collectAsStateWithLifecycle()
                var navReady by remember { mutableStateOf(false) }

                LaunchedEffect(authState.isRestoringSession) {
                    if (!authState.isRestoringSession) {
                        navReady = true

                        notifier.show(
                            AppNotification(
                                title = "SUDAH LOGIN CUY",
                                body = "MANTAP SEKALI"
                            )
                        )
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
    }
}

@Composable
private fun SessionRestoringIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}