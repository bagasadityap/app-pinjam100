package com.bagas.pinjam100.presentation.features.loan

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bagas.pinjam100.presentation.components.PullToRefreshContainer
import com.bagas.pinjam100.presentation.viewmodel.limit.LimitUIState
import com.bagas.pinjam100.presentation.viewmodel.loanapplication.LoanApplicationViewModel
import com.bagas.pinjam100.ui.theme.Pinjam100Theme
import com.bagas.pinjam100.ui.theme.SecondaryYellow

private const val MIN_LOAN_AMOUNT = 500_000L
private const val MAX_LOAN_AMOUNT = 35_000_000L
private const val DAILY_INTEREST_RATE = 0.001

@Composable
fun LoanApplicationScreen(
    customerId: String,
    limitState: LimitUIState,
    onRefresh: () -> Unit,
    viewModel: LoanApplicationViewModel = hiltViewModel(),
    onSuccess: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var loanAmount by remember { mutableStateOf("500.000") }
    var selectedTenor by remember { mutableIntStateOf(6) }
    var purpose by remember { mutableStateOf("") }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    val tenors = listOf(3, 6, 9, 12, 18, 24)

    val amount = loanAmount
        .replace(".", "")
        .replace(",", "")
        .toLongOrNull()

    val availableLimit = limitState.limit?.availableLimit

    val hasLimit = availableLimit != null

    val isAmountValid = amount != null &&
            amount >= MIN_LOAN_AMOUNT &&
            amount <= MAX_LOAN_AMOUNT &&
            (availableLimit == null || amount <= availableLimit)

    val isPurposeValid = purpose.isNotBlank()

    PullToRefreshContainer(
        isRefreshing = limitState.isLoading,
        onRefresh = onRefresh,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 20.dp,
                bottom = 28.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Pengajuan Pinjaman",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Ajukan pinjaman sesuai kebutuhan Anda.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                AvailableLimitCard(
                    limitState = limitState
                )
            }

            item {
                LoanAmountCard(
                    loanAmount = loanAmount,
                    amount = amount,
                    availableLimit = availableLimit,
                    onLoanAmountChange = {
                        loanAmount = formatInputRupiah(it)
                    }
                )
            }

            item {
                TenorCard(
                    tenors = tenors,
                    selectedTenor = selectedTenor,
                    onTenorSelected = {
                        selectedTenor = it
                    }
                )
            }

            item {
                PurposeCard(
                    purpose = purpose,
                    onPurposeSelected = {
                        purpose = it
                    }
                )
            }

            item {
                LoanSummaryCard(
                    loanAmount = amount ?: 0L,
                    tenor = selectedTenor
                )
            }

            item {
                Button(
                    onClick = {
                        if (
                            !hasLimit ||
                            !isAmountValid ||
                            !isPurposeValid ||
                            amount == null
                        ) {
                            return@Button
                        }

                        showConfirmationDialog = true
                    },
                    enabled = hasLimit &&
                            isAmountValid &&
                            isPurposeValid &&
                            !uiState.isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (hasLimit) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.secondary
                        }
                    )
                ) {
                    Text(
                        text = when {
                            uiState.isSubmitting -> "Mengajukan..."
                            hasLimit -> "Lanjutkan Pengajuan"
                            else -> "Menunggu Verifikasi"
                        },
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    if (hasLimit && !uiState.isSubmitting) {
                        Spacer(modifier = Modifier.size(8.dp))

                        Icon(
                            imageVector = Icons.Filled.ChevronRight,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            item {
                uiState.errorMessage?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }

            item {
                Text(
                    text = if (hasLimit) {
                        "Pengajuan akan diproses setelah data dan dokumen yang diperlukan dilengkapi."
                    } else {
                        "Pengajuan pinjaman dapat dilakukan setelah limit Anda diverifikasi."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }

    if (showConfirmationDialog) {
        AlertDialog(
            onDismissRequest = {
                showConfirmationDialog = false
            },
            containerColor = Color.White,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            title = {
                Text(
                    text = "Konfirmasi Pengajuan",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Pastikan data pengajuan pinjaman Anda sudah benar."
                    )

                    Text(
                        text = "Jumlah pinjaman: Rp${formatRupiah(amount ?: 0L)}",
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Tenor: $selectedTenor bulan",
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Tujuan: $purpose",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                        showSuccessDialog = true
                        onRefresh()

                        viewModel.create(
                            customerId = customerId,
                            loanAmount = amount ?: 0L,
                            tenorMonths = selectedTenor,
                            purpose = purpose,
                            onSuccess = onSuccess
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Ajukan")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        showConfirmationDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Batal")
                }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
            },
            containerColor = Color.White,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFF4CAF50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(32.dp)
                        )
                    }

                    Text(
                        text = "Pengajuan pinjaman berhasil dilakukan",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            },
            text = {
                Text(
                    text = "Mohon tunggu persetujuan dari tim kami",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("OK")
                }
            }
        )
    }
}

@Composable
private fun AvailableLimitCard(
    limitState: LimitUIState
) {
    val limit = limitState.limit

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(13.dp))
                    .background(Color.White.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(23.dp)
                )
            }

            Spacer(modifier = Modifier.size(14.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Text(
                    text = "Limit tersedia",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f)
                )

                Text(
                    text = if (limit != null) {
                        "Rp${formatRupiah(limit.availableLimit)}"
                    } else {
                        "-"
                    },
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color.White
                )

                if (limit == null) {
                    Text(
                        text = "Menunggu verifikasi",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

@Composable
private fun LoanAmountCard(
    loanAmount: String,
    amount: Long?,
    availableLimit: Long?,
    onLoanAmountChange: (String) -> Unit
) {
    val isInvalid = amount != null && (
            amount < MIN_LOAN_AMOUNT ||
                    amount > MAX_LOAN_AMOUNT ||
                    (availableLimit != null && amount > availableLimit)
            )

    val borderColor = if (isInvalid) {
        MaterialTheme.colorScheme.error
    } else {
        MaterialTheme.colorScheme.outline
    }

    val errorMessage = when {
        amount != null && amount < MIN_LOAN_AMOUNT ->
            "Minimum pinjaman Rp500.000"

        amount != null && amount > MAX_LOAN_AMOUNT ->
            "Maksimum pinjaman Rp35.000.000"

        availableLimit != null &&
                amount != null &&
                amount > availableLimit ->
            "Jumlah pinjaman melebihi limit tersedia"

        else -> null
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Jumlah pinjaman",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            OutlinedTextField(
                value = loanAmount,
                onValueChange = onLoanAmountChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = isInvalid,
                prefix = {
                    Text(
                        text = "Rp ",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(14.dp),
                supportingText = if (errorMessage != null) {
                    {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                } else {
                    null
                },
                colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (isInvalid) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    unfocusedBorderColor = borderColor,
                    errorBorderColor = MaterialTheme.colorScheme.error,
                    errorCursorColor = MaterialTheme.colorScheme.error,
                    errorPrefixColor = MaterialTheme.colorScheme.error
                )
            )

            if (!isInvalid) {
                Text(
                    text = "Minimal Rp500.000",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TenorCard(
    tenors: List<Int>,
    selectedTenor: Int,
    onTenorSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Tenor pinjaman",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            tenors.chunked(3).forEach { rowTenors ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowTenors.forEach { tenor ->
                        Card(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                onTenorSelected(tenor)
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (selectedTenor == tenor) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    MaterialTheme.colorScheme.primaryContainer
                                }
                            ),
                            elevation = CardDefaults.cardElevation(
                                defaultElevation = 0.dp
                            )
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 13.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "$tenor bulan",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.SemiBold
                                    ),
                                    color = if (selectedTenor == tenor) {
                                        Color.White
                                    } else {
                                        MaterialTheme.colorScheme.primary
                                    }
                                )
                            }
                        }
                    }

                    if (rowTenors.size < 3) {
                        repeat(3 - rowTenors.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PurposeCard(
    purpose: String,
    onPurposeSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val purposes = listOf(
        "Kebutuhan pribadi",
        "Modal usaha",
        "Pendidikan",
        "Renovasi rumah",
        "Kesehatan",
        "Kebutuhan lainnya"
    )

    val isInvalid = purpose.isBlank()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Tujuan pinjaman",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Box {
                OutlinedTextField(
                    value = purpose,
                    onValueChange = {},
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    singleLine = true,
                    isError = false,
                    label = {
                        Text("Pilih tujuan pinjaman")
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Filled.ArrowDropDown,
                            contentDescription = null
                        )
                    },
                    shape = RoundedCornerShape(14.dp)
                )

                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            expanded = true
                        }
                )
            }

            if (isInvalid) {
                Text(
                    text = "Tujuan pinjaman wajib dipilih",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

    if (expanded) {
        SelectionDialog(
            title = "Tujuan pinjaman",
            items = purposes,
            onDismiss = {
                expanded = false
            },
            onSelected = {
                onPurposeSelected(it)
                expanded = false
            }
        )
    }
}

@Composable
private fun SelectionDialog(
    title: String,
    items: List<String>,
    onDismiss: () -> Unit,
    onSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        title = {
            Text(
                text = title,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                items.forEach { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onSelected(item)
                            }
                            .padding(
                                horizontal = 8.dp,
                                vertical = 12.dp
                            ),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        },
        confirmButton = {}
    )
}

@Composable
private fun LoanSummaryCard(
    loanAmount: Long,
    tenor: Int
) {
    val totalDays = tenor * 30
    val interest = (loanAmount * DAILY_INTEREST_RATE * totalDays).toLong()
    val totalPayment = loanAmount + interest
    val monthlyPayment = if (tenor > 0) {
        totalPayment / tenor
    } else {
        0L
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
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
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(50))
                        .background(SecondaryYellow)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "Ringkasan pinjaman",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            SummaryRow(
                label = "Jumlah pinjaman",
                value = "Rp${formatRupiah(loanAmount)}"
            )

            SummaryRow(
                label = "Tenor",
                value = "$tenor bulan"
            )

            SummaryRow(
                label = "Bunga",
                value = "0,1% / hari"
            )

            SummaryRow(
                label = "Total bunga",
                value = "Rp${formatRupiah(interest)}"
            )

            SummaryRow(
                label = "Total pembayaran",
                value = "Rp${formatRupiah(totalPayment)}"
            )

            SummaryRow(
                label = "Estimasi angsuran",
                value = "Rp${formatRupiah(monthlyPayment)} / bulan"
            )
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun formatInputRupiah(value: String): String {
    val number = value
        .replace(".", "")
        .replace(",", "")
        .filter { it.isDigit() }
        .toLongOrNull()
        ?: return ""

    return formatRupiah(number)
}

private fun formatRupiah(value: Long): String {
    if (value == 0L) return "0"

    return value
        .toString()
        .reversed()
        .chunked(3)
        .joinToString(".")
        .reversed()
}

@Preview(showBackground = true)
@Composable
private fun LoanApplicationScreenPreview() {
    Pinjam100Theme {
        LoanApplicationScreen(
            customerId = "preview",
            limitState = LimitUIState(),
            onRefresh = {}
        )
    }
}