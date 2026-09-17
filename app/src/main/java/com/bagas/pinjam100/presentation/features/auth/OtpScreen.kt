package com.bagas.pinjam100.presentation.features.auth

import androidx.compose.foundation.Image
import com.bagas.pinjam100.ui.theme.Pinjam100Theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bagas.pinjam100.R
import com.bagas.pinjam100.presentation.viewmodel.auth.AuthViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun OtpScreen(
    phoneNumber: String,
    onVerifySuccess: () -> Unit,
    onResendSuccess: () -> Unit = {}
) {
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var otp by remember { mutableStateOf("") }
    var remainingSeconds by remember { mutableIntStateOf(60) }

    LaunchedEffect(remainingSeconds) {
        if (remainingSeconds > 0) {
            delay(1000.milliseconds)
            remainingSeconds--
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "Pinjam100",
            modifier = Modifier.size(width = 150.dp, height = 60.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Verifikasi OTP",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Masukkan kode OTP yang telah kami kirimkan ke nomor",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = phoneNumber,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        val otpFocusRequester = remember {
            FocusRequester()
        }

        LaunchedEffect(Unit) {
            otpFocusRequester.requestFocus()
        }

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            BasicTextField(
                value = otp,
                onValueChange = { value ->
                    if (value.length <= 6 && value.all(Char::isDigit)) {
                        otp = value
                    }
                },
                modifier = Modifier
                    .size(1.dp)
                    .focusRequester(otpFocusRequester),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                cursorBrush = SolidColor(Color.Transparent)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                repeat(6) { index ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(58.dp)
                            .border(
                                width = 1.dp,
                                color = if (index == otp.length) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.outline
                                },
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = otp.getOrNull(index)?.toString() ?: "",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            if (remainingSeconds > 0) {
                Text(
                    text = "Kirim ulang dalam ${remainingSeconds}s",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                Text(
                    text = "Belum menerima kode?",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                viewModel.verifyOtp(
                    phoneNumber = phoneNumber,
                    otpCode = otp,
                    onSuccess = onVerifySuccess
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = otp.length == 6 && !uiState.isSubmitting,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = Color(0xFFB9BDD0),
                disabledContentColor = Color.White
            )
        ) {
            Text(
                text = if (uiState.isSubmitting) {
                    "MEMPROSES..."
                } else {
                    "VERIFIKASI"
                },
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        OutlinedButton(
            onClick = {
                viewModel.resendOtp(
                    phoneNumber = phoneNumber,
                    onSuccess = {
                        otp = ""
                        remainingSeconds = 60
                        onResendSuccess()
                    }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            enabled = remainingSeconds == 0 && !uiState.isSubmitting,
            shape = RoundedCornerShape(14.dp)
        ) {
            Text(
                text = "KIRIM ULANG OTP",
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OtpScreenPreview() {
    Pinjam100Theme {
        OtpScreen(
            phoneNumber = "6281234567890",
            onVerifySuccess = {},
            onResendSuccess = {}
        )
    }
}