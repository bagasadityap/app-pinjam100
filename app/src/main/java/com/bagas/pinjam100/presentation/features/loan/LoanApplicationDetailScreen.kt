package com.bagas.pinjam100.presentation.features.loan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanApplicationDetailScreen(
    application: LoanApplication,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Pinjaman",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(
                    horizontal = 16.dp,
                    vertical = 18.dp
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InvoiceCard(application)

            CustomerCard(application)

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = "Dokumen ini merupakan ringkasan pengajuan pinjaman.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 4.dp,
                        vertical = 4.dp
                    ),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )
        }
    }
}

@Composable
private fun InvoiceCard(
    application: LoanApplication
) {
    val status = application.status.toLoanStatus()

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            InvoiceHeader(application)

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                InvoiceStatus(status)

                InvoiceDate(application)

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                InvoiceSectionTitle(
                    title = "Rincian Pinjaman"
                )

                InvoiceLine(
                    label = "Jumlah pinjaman",
                    value = formatRupiah(application.loanAmount)
                )

                InvoiceLine(
                    label = "Tenor",
                    value = "${application.tenorMonths} bulan"
                )

                InvoiceLine(
                    label = "Bunga",
                    value = "${formatDecimal(application.interestRate)}% / hari"
                )

                InvoiceLine(
                    label = "Tujuan",
                    value = application.purpose?.ifBlank {
                        "-"
                    } ?: "-"
                )

                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outlineVariant
                )

                InvoiceTotal(application)
            }
        }
    }
}

@Composable
private fun InvoiceHeader(
    application: LoanApplication
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = "PINJAM100",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "RINGKASAN PENGAJUAN",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Box(
            modifier = Modifier
                .size(46.dp)
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(13.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.AccountBalanceWallet,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 20.dp,
                end = 20.dp,
                bottom = 20.dp
            ),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = "Nomor Pengajuan",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = application.applicationId,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun InvoiceStatus(
    status: LoanStatus
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Status Pengajuan",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = status.label,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = status.contentColor()
            )
        }

        Surface(
            shape = RoundedCornerShape(10.dp),
            color = status.containerColor()
        ) {
            Row(
                modifier = Modifier.padding(
                    horizontal = 11.dp,
                    vertical = 7.dp
                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = status.icon,
                    contentDescription = null,
                    tint = status.contentColor(),
                    modifier = Modifier.size(16.dp)
                )

                Text(
                    text = status.label,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = status.contentColor()
                )
            }
        }
    }
}

@Composable
private fun InvoiceDate(
    application: LoanApplication
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Tanggal Pengajuan",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = application.createdDate.toDisplayDate(),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Icon(
            imageVector = Icons.Default.CalendarToday,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InvoiceSectionTitle(
    title: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall.copy(
            fontWeight = FontWeight.Bold
        ),
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun InvoiceLine(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(
            modifier = Modifier.size(16.dp)
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

@Composable
private fun InvoiceTotal(
    application: LoanApplication
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Nilai Pinjaman",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = formatRupiah(application.loanAmount),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tenor",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = "${application.tenorMonths} bulan",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "Angsuran / bulan",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.75f)
                    )

                    Text(
                        text = formatInstallment(application),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )
                }

                Icon(
                    imageVector = Icons.Default.AccountBalanceWallet,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    }
}

@Composable
private fun CustomerCard(
    application: LoanApplication
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(11.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = "Informasi Pemohon",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            CustomerRow(
                label = "Nama",
                value = application.customer.fullName
            )

            CustomerRow(
                label = "Nomor Customer",
                value = application.customer.customerNumber
            )

            CustomerRow(
                label = "Nomor Telepon",
                value = application.customer.phoneNumber
            )

            CustomerRow(
                label = "Email",
                value = application.customer.email.ifBlank {
                    "-"
                }
            )
        }
    }
}

@Composable
private fun CustomerRow(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

private fun String.toLoanStatus(): LoanStatus {
    return when (uppercase()) {
        "DISBURSED" -> LoanStatus.BERJALAN
        "DONE" -> LoanStatus.LUNAS
        "REJECTED" -> LoanStatus.DITOLAK
        "CANCELLED" -> LoanStatus.DIBATALKAN
        else -> LoanStatus.PENGAJUAN
    }
}

@Composable
private fun LoanStatus.containerColor(): Color {
    return when (this) {
        LoanStatus.PENGAJUAN ->
            Color(0xFFFFF7D6)

        LoanStatus.BERJALAN ->
            MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)

        LoanStatus.LUNAS ->
            Color(0xFFDCFCE7)

        LoanStatus.DITOLAK ->
            Color(0xFFFEE2E2)

        LoanStatus.DIBATALKAN ->
            MaterialTheme.colorScheme.surfaceVariant
    }
}

@Composable
private fun LoanStatus.contentColor(): Color {
    return when (this) {
        LoanStatus.PENGAJUAN ->
            Color(0xFF9A7B00)

        LoanStatus.BERJALAN ->
            MaterialTheme.colorScheme.primary

        LoanStatus.LUNAS ->
            Color(0xFF15803D)

        LoanStatus.DITOLAK ->
            MaterialTheme.colorScheme.error

        LoanStatus.DIBATALKAN ->
            MaterialTheme.colorScheme.onSurfaceVariant
    }
}

private fun formatRupiah(
    amount: BigDecimal
): String {
    return NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .apply {
            maximumFractionDigits = 0
            minimumFractionDigits = 0
        }
        .format(amount)
        .replace("Rp", "Rp ")
}

private fun formatDecimal(
    value: BigDecimal
): String {
    return value
        .stripTrailingZeros()
        .toPlainString()
}

private fun String.toDateMillis(): Long? {
    return runCatching {
        SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
            Locale.US
        ).parse(this)?.time
    }.getOrNull()
}

private fun String.toDisplayDate(): String {
    return toDateMillis()?.let {
        SimpleDateFormat(
            "d MMMM yyyy",
            Locale("id", "ID")
        ).format(Date(it))
    } ?: this
}