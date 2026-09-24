package com.bagas.pinjam100.presentation.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bagas.pinjam100.domain.model.transaction.TransactionHistory
import com.bagas.pinjam100.domain.model.transaction.TransactionType
import com.bagas.pinjam100.presentation.viewmodel.transaction.TransactionHistoryViewModel
import com.bagas.pinjam100.ui.theme.Pinjam100Theme
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    customerId: String,
    onDisbursementClick: (String) -> Unit,
    onInstallmentClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TransactionHistoryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedType by remember { mutableStateOf("Semua") }
    var startDate by remember { mutableStateOf<Long?>(null) }
    var endDate by remember { mutableStateOf<Long?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectingStartDate by remember { mutableStateOf(true) }

    val dateFormatter = remember {
        SimpleDateFormat("dd MMM yyyy", Locale("id", "ID"))
    }

    LaunchedEffect(customerId) {
        viewModel.getByCustomerId(customerId)
    }

    val filteredTransactions = uiState.transactions.filter { transaction ->
        val typeMatch = when (selectedType) {
            "Pencairan" -> transaction.type == TransactionType.DISBURSEMENT
            "Pembayaran" -> transaction.type == TransactionType.INSTALLMENT_PAYMENT
            else -> true
        }

        val transactionTime = parseTransactionDate(transaction.date)

        val startMatch = startDate?.let {
            transactionTime >= startOfDay(it)
        } ?: true

        val endMatch = endDate?.let {
            transactionTime <= endOfDay(it)
        } ?: true

        typeMatch && startMatch && endMatch
    }

    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = {
            viewModel.getByCustomerId(customerId)
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .statusBarsPadding()
                .navigationBarsPadding()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Riwayat Transaksi",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Lihat semua aktivitas keuangan Anda.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    HistoryFilterChip(
                        selected = selectedType == "Semua",
                        text = "Semua",
                        onClick = {
                            selectedType = "Semua"
                        }
                    )

                    HistoryFilterChip(
                        selected = selectedType == "Pencairan",
                        text = "Pencairan",
                        onClick = {
                            selectedType = "Pencairan"
                        }
                    )

                    HistoryFilterChip(
                        selected = selectedType == "Pembayaran",
                        text = "Pembayaran",
                        onClick = {
                            selectedType = "Pembayaran"
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 1.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 14.dp,
                                vertical = 8.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DateRange,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = when {
                                    startDate != null && endDate != null ->
                                        "Rentang tanggal"

                                    startDate != null ->
                                        "Tanggal mulai"

                                    else ->
                                        "Tanggal transaksi"
                                },
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Text(
                                text = when {
                                    startDate != null && endDate != null ->
                                        "${dateFormatter.format(Date(startDate!!))} - ${
                                            dateFormatter.format(Date(endDate!!))
                                        }"

                                    startDate != null ->
                                        dateFormatter.format(Date(startDate!!))

                                    else ->
                                        "Pilih tanggal mulai"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        if (startDate != null || endDate != null) {
                            TextButton(
                                onClick = {
                                    startDate = null
                                    endDate = null
                                }
                            ) {
                                Text("Reset")
                            }
                        }

                        Button(
                            onClick = {
                                selectingStartDate = true
                                showDatePicker = true
                            },
                            shape = RoundedCornerShape(12.dp),
                            contentPadding = PaddingValues(
                                horizontal = 14.dp
                            ),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Pilih")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage!!,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 4.dp
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
                    bottom = 20.dp
                ),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(
                    items = filteredTransactions,
                    key = { it.id }
                ) { transaction ->
                    TransactionItem(
                        transaction = transaction,
                        onClick = {
                            when (transaction.type) {
                                TransactionType.DISBURSEMENT -> {
                                    onDisbursementClick(transaction.id)
                                }

                                TransactionType.INSTALLMENT_PAYMENT -> {
                                    onInstallmentClick(transaction.id)
                                }
                            }
                        }
                    )
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = if (selectingStartDate) {
                startDate
            } else {
                endDate
            }
        )

        DatePickerDialog(
            onDismissRequest = {
                showDatePicker = false
            },
            colors = androidx.compose.material3.DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.background
            ),
            modifier = Modifier.padding(vertical = 12.dp),
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { selected ->
                            if (selectingStartDate) {
                                startDate = selected

                                if (endDate != null && selected > endDate!!) {
                                    endDate = null
                                }

                                selectingStartDate = false
                            } else {
                                endDate = selected
                                showDatePicker = false
                            }
                        }
                    }
                ) {
                    Text(
                        if (selectingStartDate) {
                            "Pilih Tanggal Mulai"
                        } else {
                            "Pilih Tanggal Berakhir"
                        }
                    )
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
            Column {
                Surface(
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(
                            horizontal = 24.dp,
                            vertical = 12.dp
                        )
                    ) {
                        Text(
                            text = if (selectingStartDate) {
                                "Tanggal Mulai"
                            } else {
                                "Tanggal Berakhir"
                            },
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = if (selectingStartDate) {
                                "Pilih tanggal awal periode transaksi."
                            } else {
                                "Pilih tanggal akhir periode transaksi."
                            },
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }

                DatePicker(
                    state = datePickerState,
                    title = null,
                    headline = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        val headlineText = if (selectedMillis != null) {
                            dateFormatter.format(Date(selectedMillis))
                        } else {
                            "Pilih Tanggal"
                        }

                        Text(
                            text = headlineText,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            modifier = Modifier.padding(
                                start = 24.dp,
                                top = 12.dp,
                                bottom = 4.dp
                            )
                        )
                    },
                    colors = androidx.compose.material3.DatePickerDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        headlineContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}

@Composable
private fun HistoryFilterChip(
    selected: Boolean,
    text: String,
    onClick: () -> Unit
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = {
            Text(
                text = text,
                fontWeight = if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.Medium
                }
            )
        },
        shape = RoundedCornerShape(12.dp),
        colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurface
        ),
        border = androidx.compose.material3.FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outlineVariant,
            selectedBorderColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun TransactionItem(
    transaction: TransactionHistory,
    onClick: () -> Unit
) {
    val isIncome = transaction.type == TransactionType.DISBURSEMENT

    val title = when (transaction.type) {
        TransactionType.DISBURSEMENT -> "Pencairan Pinjaman"
        TransactionType.INSTALLMENT_PAYMENT -> "Pembayaran Angsuran"
    }

    val description = "Lorem ipsum dolor sit amet"

    val amount = "${if (isIncome) "+" else "-"} ${formatRupiah(transaction.amount)}"

    val date = formatTransactionDate(transaction.date)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 14.dp,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(42.dp),
                shape = CircleShape,
                color = if (isIncome) {
                    Color(0xFFE8F7ED)
                } else {
                    MaterialTheme.colorScheme.primaryContainer
                }
            ) {
                Icon(
                    imageVector = if (isIncome) {
                        Icons.Filled.ArrowDownward
                    } else {
                        Icons.Filled.Payments
                    },
                    contentDescription = null,
                    modifier = Modifier.padding(10.dp),
                    tint = if (isIncome) {
                        Color(0xFF16A34A)
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }

            Spacer(modifier = Modifier.size(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    softWrap = false
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = date,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.size(8.dp))

            Column(
                modifier = Modifier
                    .align(Alignment.Bottom)
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = amount,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (isIncome) {
                        Color(0xFF16A34A)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

private fun parseTransactionDate(value: String): Date {
    return try {
        SimpleDateFormat(
            "yyyy-MM-dd'T'HH:mm:ss",
            Locale.US
        ).parse(value) ?: Date(0)
    } catch (_: Exception) {
        try {
            SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
                Locale.US
            ).parse(value) ?: Date(0)
        } catch (_: Exception) {
            Date(0)
        }
    }
}

private fun startOfDay(value: Long): Date {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = value
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }

    return calendar.time
}

private fun endOfDay(value: Long): Date {
    val calendar = Calendar.getInstance().apply {
        timeInMillis = value
        set(Calendar.HOUR_OF_DAY, 23)
        set(Calendar.MINUTE, 59)
        set(Calendar.SECOND, 59)
        set(Calendar.MILLISECOND, 999)
    }

    return calendar.time
}

private fun formatTransactionDate(value: String): String {
    return try {
        val date = parseTransactionDate(value)

        SimpleDateFormat(
            "dd MMMM yyyy, HH:mm",
            Locale("id", "ID")
        ).format(date)
    } catch (_: Exception) {
        value
    }
}

private fun formatRupiah(amount: BigDecimal): String {
    val formatter = java.text.NumberFormat.getNumberInstance(
        Locale("id", "ID")
    ).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }

    return "Rp${formatter.format(amount)}"
}
