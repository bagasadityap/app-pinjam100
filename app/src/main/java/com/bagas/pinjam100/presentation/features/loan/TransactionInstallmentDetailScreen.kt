package com.bagas.pinjam100.presentation.features.loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ReceiptLong
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bagas.pinjam100.domain.model.installment.InstallmentStatus
import com.bagas.pinjam100.presentation.viewmodel.installment.LoanInstallmentViewModel
import com.bagas.pinjam100.ui.theme.Pinjam100Theme

private val PrimaryBlue = Color(0xFF0E209C)
private val Success = Color(0xFF16A34A)
private val TextDark = Color(0xFF1A1819)
private val FormShape = RoundedCornerShape(14.dp)

@Composable
fun TransactionInstallmentDetailScreen(
    installmentId: String,
    onBack: () -> Unit = {},
    viewModel: LoanInstallmentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(installmentId) {
        viewModel.getById(installmentId)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        when {
            uiState.isLoading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .navigationBarsPadding(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        color = PrimaryBlue
                    )
                }
            }

            uiState.selectedInstallment != null -> {
                val installment = uiState.selectedInstallment!!

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    item {
                        TransactionDetailHeader(
                            onBack = onBack
                        )
                    }

                    item {
                        InstallmentPaymentAmountCard(
                            installment = installment
                        )
                    }

                    item {
                        TransactionInformationCard(
                            installment = installment
                        )
                    }

                    item {
                        InstallmentInformationCard(
                            installment = installment
                        )
                    }

                    item {
                        Spacer(
                            modifier = Modifier.height(6.dp)
                        )
                    }
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .navigationBarsPadding()
                        .padding(horizontal = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = uiState.errorMessage
                            ?: "Data angsuran tidak ditemukan",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionDetailHeader(
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Kembali"
            )
        }

        Column(
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Text(
                text = "Detail Transaksi",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Informasi pembayaran angsuran",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun InstallmentPaymentAmountCard(
    installment: com.bagas.pinjam100.domain.model.installment.LoanInstallment
) {
    val isPaid = installment.status == InstallmentStatus.PAID

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = FormShape,
        colors = CardDefaults.cardColors(
            containerColor = PrimaryBlue
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.ReceiptLong,
                contentDescription = null,
                tint = Color.White
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Pembayaran Angsuran",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = formatRupiah(installment.paidAmount),
                style = MaterialTheme.typography.headlineSmall,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color.White
                )

                Text(
                    text = if (isPaid) "Berhasil" else installment.status.name,
                    modifier = Modifier.padding(start = 5.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TransactionInformationCard(
    installment: com.bagas.pinjam100.domain.model.installment.LoanInstallment
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = FormShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Informasi Transaksi",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            DetailItem(
                label = "Nomor Transaksi",
                value = installment.id
            )

            DetailItem(
                label = "Tanggal Transaksi",
                value = installment.paidDate ?: "-"
            )

            DetailItem(
                label = "Jenis Transaksi",
                value = "Pembayaran Angsuran"
            )

            DetailItem(
                label = "Status",
                value = if (installment.status == InstallmentStatus.PAID) {
                    "Berhasil"
                } else {
                    installment.status.name
                },
                valueColor = if (installment.status == InstallmentStatus.PAID) {
                    Success
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@Composable
private fun InstallmentInformationCard(
    installment: com.bagas.pinjam100.domain.model.installment.LoanInstallment
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = FormShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Informasi Angsuran",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            DetailItem(
                label = "Nomor Pinjaman",
                value = installment.loanApplicationId
            )

            DetailItem(
                label = "Angsuran Ke",
                value = "${installment.installmentSequence}"
            )

            DetailItem(
                label = "Jumlah Angsuran",
                value = formatRupiah(installment.installmentAmount)
            )

            DetailItem(
                label = "Jumlah Dibayar",
                value = formatRupiah(installment.paidAmount)
            )

            DetailItem(
                label = "Jatuh Tempo",
                value = installment.dueDate
            )

            DetailItem(
                label = "Status",
                value = installment.status.name
            )
        }
    }
}

@Composable
private fun DetailItem(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextDark,
            fontWeight = FontWeight.Medium
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = valueColor
        )
    }
}

private fun formatRupiah(amount: Double): String {
    return "Rp${"%,.0f".format(amount).replace(',', '.')}"
}

@Preview(showBackground = true)
@Composable
private fun TransactionInstallmentDetailScreenPreview() {
    Pinjam100Theme {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text("Transaction Installment Detail")
        }
    }
}