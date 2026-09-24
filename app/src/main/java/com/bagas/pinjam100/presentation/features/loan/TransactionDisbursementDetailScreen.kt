package com.bagas.pinjam100.presentation.features.loan

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
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bagas.pinjam100.domain.model.disbursement.Disbursement
import com.bagas.pinjam100.presentation.viewmodel.disbursement.DisbursementViewModel
import com.bagas.pinjam100.ui.theme.Pinjam100Theme
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val PrimaryBlue = Color(0xFF0E209C)
private val Success = Color(0xFF16A34A)
private val TextDark = Color(0xFF1A1819)
private val FormShape = RoundedCornerShape(14.dp)

@Composable
fun TransactionDisbursementDetailScreen(
    onBack: () -> Unit = {},
    viewModel: DisbursementViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val disbursement = uiState.disbursement

    when {
        uiState.isLoading -> {
            LoadingScreen()
        }

        uiState.errorMessage != null -> {
            ErrorScreen(
                message = uiState.errorMessage ?: "Gagal mengambil data pencairan",
                onBack = onBack
            )
        }

        disbursement != null -> {
            DisbursementDetailContent(
                disbursement = disbursement,
                onBack = onBack
            )
        }

        else -> {
            ErrorScreen(
                message = "Data pencairan tidak ditemukan",
                onBack = onBack
            )
        }
    }
}

@Composable
private fun DisbursementDetailContent(
    disbursement: Disbursement,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            TransactionDetailHeader(
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                DisbursementAmountCard(
                    amount = disbursement.disbursementAmount
                )

                TransactionInformationCard(
                    disbursement = disbursement
                )

                LoanInformationCard(
                    disbursement = disbursement
                )
            }
        }
    }
}

@Composable
private fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            color = PrimaryBlue
        )
    }
}

@Composable
private fun ErrorScreen(
    message: String,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            TransactionDetailHeader(
                onBack = onBack
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
                text = "Informasi pencairan pinjaman",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DisbursementAmountCard(
    amount: Long
) {
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
                text = "Pencairan Pinjaman",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "+ ${formatCurrency(amount)}",
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

                Spacer(
                    modifier = Modifier.width(3.dp)
                )

                Text(
                    text = "Berhasil",
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
    disbursement: Disbursement
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
                value = disbursement.id
            )

            DetailItem(
                label = "Tanggal Transaksi",
                value = formatDateTime(disbursement.createdDate)
            )

            DetailItem(
                label = "Jenis Transaksi",
                value = "Pencairan Pinjaman"
            )

            DetailItem(
                label = "Status",
                value = "Berhasil",
                valueColor = Success
            )
        }
    }
}

@Composable
private fun LoanInformationCard(
    disbursement: Disbursement
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
                text = "Informasi Pinjaman",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            DetailItem(
                label = "Jumlah Pencairan",
                value = formatCurrency(
                    disbursement.disbursementAmount
                )
            )

            DetailItem(
                label = "ID Pengajuan",
                value = disbursement.loanApplicationId
            )

            DetailItem(
                label = "Rekening Tujuan",
                value = disbursement.rekening.noRekening
            )

            DetailItem(
                label = "Nama Pemilik Rekening",
                value = disbursement.rekening.accountHolder
            )

            DetailItem(
                label = "Keterangan",
                value = "Dana pinjaman berhasil dicairkan ke rekening tujuan."
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

private fun formatCurrency(
    amount: Long
): String {
    return "Rp%,d"
        .format(amount)
        .replace(",", ".")
}

private fun formatDateTime(
    value: String
): String {
    return try {
        val dateTime = LocalDateTime.parse(value)

        val formatter = DateTimeFormatter.ofPattern(
            "dd MMMM yyyy, HH:mm",
            Locale("id", "ID")
        )

        dateTime.format(formatter)
    } catch (_: Exception) {
        value
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionDisbursementDetailScreenPreview() {
    Pinjam100Theme {
        TransactionDisbursementDetailScreen()
    }
}