package com.bagas.pinjam100.presentation.features.loan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bagas.pinjam100.ui.theme.Pinjam100Theme

private val PrimaryBlue = Color(0xFF0E209C)
private val Success = Color(0xFF16A34A)
private val TextDark = Color(0xFF1A1819)
private val FormShape = RoundedCornerShape(14.dp)

@Composable
fun TransactionInstallmentDetailScreen(
    onBack: () -> Unit = {}
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
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
                InstallmentPaymentAmountCard()
            }

            item {
                TransactionInformationCard()
            }

            item {
                InstallmentInformationCard()
            }

            item {
                PaymentInformationCard()
            }

            item {
                Spacer(
                    modifier = Modifier.height(6.dp)
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
            .padding(top = 8.dp),
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
private fun InstallmentPaymentAmountCard() {
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
                text = "Rp875.000",
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
                    modifier = Modifier.height(0.dp)
                )

                Text(
                    text = "Berhasil",
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
private fun TransactionInformationCard() {
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
                value = "TRX-20260909-0002"
            )

            DetailItem(
                label = "Tanggal Transaksi",
                value = "09 September 2026, 14:30"
            )

            DetailItem(
                label = "Jenis Transaksi",
                value = "Pembayaran Angsuran"
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
private fun InstallmentInformationCard() {
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
                value = "LN-20260909-0001"
            )

            DetailItem(
                label = "Angsuran Ke",
                value = "1 dari 12"
            )

            DetailItem(
                label = "Pokok",
                value = "Rp750.000"
            )

            DetailItem(
                label = "Bunga",
                value = "Rp125.000"
            )

            DetailItem(
                label = "Total Angsuran",
                value = "Rp875.000"
            )

            DetailItem(
                label = "Jatuh Tempo",
                value = "10 September 2026"
            )
        }
    }
}

@Composable
private fun PaymentInformationCard() {
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
                text = "Informasi Pembayaran",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            DetailItem(
                label = "Metode Pembayaran",
                value = "Virtual Account"
            )

            DetailItem(
                label = "Bank",
                value = "BCA"
            )

            DetailItem(
                label = "Nomor Virtual Account",
                value = "1234567890123456"
            )

            DetailItem(
                label = "Keterangan",
                value = "Pembayaran angsuran berhasil diterima."
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

@Preview(showBackground = true)
@Composable
private fun TransactionInstallmentDetailScreenPreview() {
    Pinjam100Theme {
        TransactionInstallmentDetailScreen()
    }
}