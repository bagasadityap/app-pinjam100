package com.bagas.pinjam100.presentation.features.loan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Pending
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bagas.pinjam100.domain.model.loanapplication.LoanApplication
import com.bagas.pinjam100.presentation.components.PullToRefreshContainer
import com.bagas.pinjam100.presentation.viewmodel.loanapplication.LoanApplicationViewModel
import com.bagas.pinjam100.ui.theme.Pinjam100Theme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class LoanStatus(
    val label: String,
    val icon: ImageVector
) {
    PENGAJUAN(
        label = "Pengajuan",
        icon = Icons.Filled.Pending
    ),
    BERJALAN(
        label = "Sedang Berjalan",
        icon = Icons.Filled.PlayCircle
    ),
    LUNAS(
        label = "Lunas",
        icon = Icons.Filled.CheckCircle
    ),
    DITOLAK(
        label = "Ditolak",
        icon = Icons.Filled.Close
    ),
    DIBATALKAN(
        label = "Dibatalkan",
        icon = Icons.Filled.Close
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ApplicationListScreen(
    customerId: String,
    modifier: Modifier = Modifier,
    viewModel: LoanApplicationViewModel = hiltViewModel(),
    onApplicationClick: (String) -> Unit = {},
    onLoanApplicationClick: () -> Unit = {},
    onInstallmentClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    var selectedStatus by remember {
        mutableStateOf<LoanStatus?>(null)
    }

    var selectedDate by remember {
        mutableStateOf<Long?>(null)
    }

    var showDatePicker by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(customerId) {
        viewModel.getByCustomer(customerId)
    }

    val filteredApplications = uiState.loanApplications.filter { application ->
        val status = application.status.toLoanStatus()

        val statusMatch =
            selectedStatus == null || status == selectedStatus

        val dateMatch =
            selectedDate == null ||
                    application.createdDate.toDateMillis()
                        ?.let { isSameDate(it, selectedDate!!) } == true

        statusMatch && dateMatch
    }

    PullToRefreshContainer(
        isRefreshing = uiState.isLoading,
        onRefresh = {
            viewModel.getByCustomer(customerId)
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 20.dp
                    ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Riwayat Pinjaman",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        onClick = onLoanApplicationClick,
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(
                                        color = Color.White.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(21.dp)
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = "Ajukan pinjaman",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.White.copy(alpha = 0.75f)
                                )
                            }

                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        onClick = onInstallmentClick,
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 1.dp
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(12.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(21.dp)
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(start = 10.dp),
                                verticalArrangement = Arrangement.spacedBy(3.dp)
                            ) {
                                Text(
                                    text = "Lihat tagihan",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                        alpha = 0.75f
                                    )
                                )
                            }

                            Icon(
                                imageVector = Icons.Filled.ChevronRight,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedDate != null,
                        onClick = {
                            showDatePicker = true
                        },
                        label = {
                            Text(
                                text = selectedDate?.let {
                                    formatDateDisplay(it)
                                } ?: "Tanggal"
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.CalendarToday,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    )

                    FilterChip(
                        selected = selectedStatus != null,
                        onClick = {
                            selectedStatus = when (selectedStatus) {
                                null -> LoanStatus.PENGAJUAN
                                LoanStatus.PENGAJUAN -> LoanStatus.BERJALAN
                                LoanStatus.BERJALAN -> LoanStatus.LUNAS
                                LoanStatus.LUNAS -> LoanStatus.DITOLAK
                                LoanStatus.DITOLAK -> LoanStatus.DIBATALKAN
                                LoanStatus.DIBATALKAN -> null
                            }
                        },
                        label = {
                            Text(
                                text = selectedStatus?.label ?: "Status"
                            )
                        },
                        leadingIcon = {
                            selectedStatus?.let {
                                Icon(
                                    imageVector = it.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = selectedStatus?.containerColor()
                                ?: MaterialTheme.colorScheme.surface,
                            selectedLabelColor = selectedStatus?.contentColor()
                                ?: MaterialTheme.colorScheme.onSurface,
                            selectedLeadingIconColor = selectedStatus?.contentColor()
                                ?: MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
            }

            uiState.errorMessage?.let { error ->
                Text(
                    text = error,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 12.dp
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 20.dp,
                    bottom = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(
                    items = filteredApplications,
                    key = { it.id }
                ) { application ->
                    LoanApplicationItem(
                        application = application,
                        onClick = {
                            onApplicationClick(application.id)
                        }
                    )
                }

                if (filteredApplications.isEmpty() && !uiState.isLoading) {
                    item {
                        Text(
                            text = "Belum ada riwayat pinjaman.",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate
        )

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDate = datePickerState.selectedDateMillis
                        showDatePicker = false
                    }
                ) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                    }
                ) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }
}

@Composable
private fun LoanApplicationItem(
    application: LoanApplication,
    onClick: () -> Unit
) {
    val status = application.status.toLoanStatus()

    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(
                            color = status.containerColor(),
                            shape = RoundedCornerShape(13.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.AccountBalanceWallet,
                        contentDescription = null,
                        tint = status.contentColor(),
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = application.applicationId,
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = application.createdDate.toDisplayDate(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "Jumlah pinjaman",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = formatRupiah(
                            application.loanAmount.toLong()
                        ),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "Tenor",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "${application.tenorMonths} bulan",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${formatInstallment(application)} / bulan",
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                LoanStatusBadge(status)
            }
        }
    }
}

@Composable
private fun LoanStatusBadge(
    status: LoanStatus
) {
    val color = status.contentColor()

    Row(
        modifier = Modifier
            .background(
                color = status.containerColor(),
                shape = RoundedCornerShape(10.dp)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 6.dp
            ),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = status.icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(15.dp)
        )

        Text(
            text = status.label,
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = color
        )
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

private fun String.toLoanStatus(): LoanStatus {
    return when (uppercase()) {
        "DISBURSED" -> LoanStatus.BERJALAN
        "DONE" -> LoanStatus.LUNAS
        "REJECTED" -> LoanStatus.DITOLAK
        "CANCELLED" -> LoanStatus.DIBATALKAN
        else -> LoanStatus.PENGAJUAN
    }
}

fun formatInstallment(
    application: LoanApplication
): String {
    val totalDays = application.tenorMonths * 30
    val interest = (
            application.loanAmount.toDouble() *
                    application.interestRate.toDouble() / 100.0 *
                    totalDays
            ).toLong()

    val totalPayment =
        application.loanAmount.toLong() + interest

    val installment =
        if (application.tenorMonths > 0) {
            totalPayment / application.tenorMonths
        } else {
            0L
        }

    return formatRupiah(installment)
}

private fun formatRupiah(
    amount: Long
): String {
    return NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .format(amount)
        .replace(",00", "")
}

private fun formatDateDisplay(
    millis: Long
): String {
    return SimpleDateFormat(
        "dd MMM yyyy",
        Locale("id", "ID")
    ).format(Date(millis))
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

private fun isSameDate(
    firstMillis: Long,
    secondMillis: Long
): Boolean {
    val formatter = SimpleDateFormat(
        "yyyyMMdd",
        Locale.US
    )

    return formatter.format(Date(firstMillis)) ==
            formatter.format(Date(secondMillis))
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ApplicationListScreenPreview() {
    Pinjam100Theme {
        ApplicationListScreen(
            customerId = "preview"
        )
    }
}