package com.bagas.pinjam100.presentation.features.installment

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bagas.pinjam100.domain.model.installment.InstallmentStatus
import com.bagas.pinjam100.domain.model.installment.LoanInstallment
import com.bagas.pinjam100.presentation.viewmodel.installment.LoanInstallmentViewModel
import com.bagas.pinjam100.ui.theme.PrimaryBlue
import com.bagas.pinjam100.ui.theme.Success
import com.bagas.pinjam100.ui.theme.TextDark
import com.bagas.pinjam100.ui.theme.Warning
import java.math.BigDecimal
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanInstallmentBillListScreen(
    customerId: String,
    viewModel: LoanInstallmentViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var selectedInstallment by remember {
        mutableStateOf<LoanInstallment?>(null)
    }

    var selectedFilter by remember {
        mutableStateOf(InstallmentFilter.ALL)
    }

    LaunchedEffect(customerId) {
        viewModel.getByCustomerId(customerId)
    }

    LaunchedEffect(uiState.paymentSuccess) {
        if (uiState.paymentSuccess) {
            selectedInstallment = null
            viewModel.clearPaymentSuccess()

            // Reload data setelah pembayaran berhasil
            viewModel.getByCustomerId(customerId)
        }
    }

    /*
     * ViewModel sudah memastikan bahwa semua data di sini:
     * - status = UNPAID
     * - due date <= hari ini + 30 hari
     *
     * Jadi PAID tidak akan pernah ditampilkan.
     */
    val unpaidInstallments = uiState.installments

    val overdueInstallments = unpaidInstallments.filter {
        it.isOverdue()
    }

    val filteredInstallments = when (selectedFilter) {
        InstallmentFilter.ALL -> unpaidInstallments

        InstallmentFilter.OVERDUE -> overdueInstallments
    }

    val totalOverdue = overdueInstallments.fold(
        BigDecimal.ZERO
    ) { total, installment ->
        total + (
                installment.installmentAmount -
                        installment.paidAmount
                )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tagihan Angsuran",
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

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    MaterialTheme.colorScheme.background
                )
                .padding(paddingValues)
        ) {

            when {

                /*
                 * Loading hanya ditampilkan ketika
                 * belum memiliki data sama sekali.
                 */
                uiState.isLoading &&
                        uiState.installments.isEmpty() -> {

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.installments.isEmpty() -> {

                    EmptyBillState(
                        modifier = Modifier.fillMaxSize()
                    )
                }

                else -> {

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            horizontal = 16.dp,
                            vertical = 18.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(
                            12.dp
                        )
                    ) {

                        /*
                         * Ringkasan tunggakan.
                         */
                        if (overdueInstallments.isNotEmpty()) {
                            item {
                                OverdueBillCard(
                                    total = totalOverdue,
                                    count = overdueInstallments.size
                                )
                            }
                        }

                        /*
                         * Filter.
                         */
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement =
                                    Arrangement.spacedBy(8.dp)
                            ) {

                                InstallmentFilter.values()
                                    .forEach { filter ->

                                        FilterChip(
                                            selected =
                                                selectedFilter == filter,

                                            onClick = {
                                                selectedFilter = filter
                                            },

                                            label = {
                                                Text(filter.label)
                                            },

                                            colors =
                                                FilterChipDefaults
                                                    .filterChipColors(
                                                        selectedContainerColor =
                                                            MaterialTheme
                                                                .colorScheme
                                                                .primary,

                                                        selectedLabelColor =
                                                            Color.White,

                                                        selectedLeadingIconColor =
                                                            Color.White
                                                    )
                                        )
                                    }
                            }
                        }

                        /*
                         * Tidak ada data untuk filter tertentu.
                         */
                        if (filteredInstallments.isEmpty()) {

                            item {
                                EmptyFilteredBillState(
                                    filter = selectedFilter
                                )
                            }

                        } else {

                            items(
                                items = filteredInstallments,
                                key = {
                                    it.id
                                }
                            ) { installment ->

                                InstallmentBillCard(
                                    installment = installment,
                                    onClick = {
                                        selectedInstallment =
                                            installment
                                    }
                                )
                            }
                        }
                    }
                }
            }

            /*
             * Error message.
             */
            uiState.errorMessage?.let { message ->

                Text(
                    text = message,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }

    /*
     * Detail tagihan.
     */
    selectedInstallment?.let { installment ->

        InstallmentBillDialog(
            installment = installment,
            isPaying = uiState.isPaying,

            onDismiss = {
                if (!uiState.isPaying) {
                    selectedInstallment = null
                }
            },

            onPay = {

                viewModel.pay(
                    id = installment.id,
                    customerId = customerId
                )
            }
        )
    }
}

@Composable
private fun OverdueBillCard(
    total: BigDecimal,
    count: Int
) {
    val errorColor = MaterialTheme.colorScheme.error

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(
            width = 1.dp,
            color = errorColor.copy(alpha = 0.12f)
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(13.dp))
                        .background(
                            errorColor.copy(alpha = 0.08f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = errorColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(
                    modifier = Modifier.width(12.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Total Tagihan Menunggak",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    Text(
                        text = "Segera lakukan pembayaran",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Total yang harus dibayar",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.Gray
                )

                Text(
                    text = formatRupiah(total),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = errorColor
                )
            }

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(
                                color = errorColor,
                                shape = CircleShape
                            )
                    )

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Text(
                        text = "$count tagihan belum dibayar",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = TextDark
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = errorColor.copy(alpha = 0.08f)
                ) {
                    Text(
                        text = "Perlu dibayar",
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = errorColor
                    )
                }
            }
        }
    }
}

@Composable
private fun InstallmentBillCard(
    installment: LoanInstallment,
    onClick: () -> Unit
) {
    /*
     * PAID tidak seharusnya pernah sampai ke sini.
     */
    val isPaid =
        installment.status == InstallmentStatus.PAID

    val isOverdue =
        installment.isOverdue()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            ),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor =
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement =
                Arrangement.spacedBy(14.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .background(
                            color =
                                MaterialTheme
                                    .colorScheme
                                    .primaryContainer,
                            shape =
                                RoundedCornerShape(12.dp)
                        ),
                    contentAlignment =
                        Alignment.Center
                ) {

                    Icon(
                        imageVector =
                            Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint =
                            MaterialTheme
                                .colorScheme
                                .primary
                    )
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp)
                ) {

                    Text(
                        text =
                            installment.installmentNumber,
                        style =
                            MaterialTheme
                                .typography
                                .titleSmall,
                        fontWeight =
                            FontWeight.Bold,
                        maxLines = 1,
                        overflow =
                            TextOverflow.Ellipsis
                    )

                    Text(
                        text =
                            "Jatuh tempo ${installment.dueDate.toDisplayDate()}",
                        style =
                            MaterialTheme
                                .typography
                                .bodySmall,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant
                    )
                }

                InstallmentStatusBadge(
                    installment = installment,
                    isOverdue = isOverdue
                )
            }

            HorizontalDivider(
                color =
                    MaterialTheme
                        .colorScheme
                        .outlineVariant
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                    Arrangement.SpaceBetween,
                verticalAlignment =
                    Alignment.Bottom
            ) {

                Column {

                    Text(
                        text = "Jumlah Tagihan",
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,
                        color =
                            MaterialTheme
                                .colorScheme
                                .onSurfaceVariant
                    )

                    Spacer(
                        modifier = Modifier.height(3.dp)
                    )

                    Text(
                        text =
                            formatRupiah(
                                installment.installmentAmount
                            ),
                        style =
                            MaterialTheme
                                .typography
                                .titleMedium,
                        fontWeight =
                            FontWeight.Bold
                    )
                }

                /*
                 * Secara normal blok PAID ini tidak akan
                 * pernah digunakan karena ViewModel sudah
                 * melakukan filter UNPAID.
                 */
                if (isPaid) {

                    Icon(
                        imageVector =
                            Icons.Default.CheckCircle,
                        contentDescription =
                            "Sudah dibayar",
                        tint = Success
                    )

                } else {

                    Text(
                        text = "Lihat detail",
                        style =
                            MaterialTheme
                                .typography
                                .labelMedium,
                        color =
                            MaterialTheme
                                .colorScheme
                                .primary,
                        fontWeight =
                            FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun InstallmentStatusBadge(
    installment: LoanInstallment,
    isOverdue: Boolean
) {

    val containerColor = when {

        installment.status ==
                InstallmentStatus.PAID ->

            MaterialTheme
                .colorScheme
                .primaryContainer

        isOverdue ->

            MaterialTheme
                .colorScheme
                .errorContainer

        else ->

            MaterialTheme
                .colorScheme
                .surfaceVariant
    }

    val contentColor = when {

        installment.status ==
                InstallmentStatus.PAID ->

            MaterialTheme
                .colorScheme
                .primary

        isOverdue ->

            MaterialTheme
                .colorScheme
                .error

        else ->

            MaterialTheme
                .colorScheme
                .onSurfaceVariant
    }

    Box(
        modifier = Modifier
            .background(
                color = containerColor,
                shape =
                    RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = 9.dp,
                vertical = 5.dp
            )
    ) {

        Text(
            text = when {

                installment.status ==
                        InstallmentStatus.PAID ->

                    "Lunas"

                isOverdue ->

                    "Menunggak"

                else ->

                    "Belum Bayar"
            },

            style =
                MaterialTheme
                    .typography
                    .labelSmall,

            color = contentColor,

            fontWeight =
                FontWeight.Bold
        )
    }
}

@Composable
private fun InstallmentBillDialog(
    installment: LoanInstallment,
    isPaying: Boolean,
    onDismiss: () -> Unit,
    onPay: () -> Unit
) {

    /*
     * Karena hanya UNPAID yang ditampilkan,
     * isPaid seharusnya selalu false.
     */
    val isPaid =
        installment.status == InstallmentStatus.PAID

    val isOverdue =
        installment.isOverdue()

    AlertDialog(
        onDismissRequest = onDismiss,

        confirmButton = {},

        title = {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(
                                RoundedCornerShape(12.dp)
                            )
                            .background(
                                PrimaryBlue.copy(
                                    alpha = 0.1f
                                )
                            ),
                        contentAlignment =
                            Alignment.Center
                    ) {

                        Icon(
                            imageVector =
                                Icons.Default.ReceiptLong,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier =
                                Modifier.size(23.dp)
                        )
                    }

                    Spacer(
                        modifier =
                            Modifier.width(12.dp)
                    )

                    Column(
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        Text(
                            text =
                                installment.installmentNumber,
                            fontSize = 14.sp,
                            fontWeight =
                                FontWeight.Bold,
                            color = TextDark,
                            maxLines = 1,
                            overflow =
                                TextOverflow.Ellipsis
                        )
                    }
                }

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                HorizontalDivider(
                    color = Color.LightGray
                )
            }
        },

        text = {

            Column(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                InvoiceItem(
                    label = "Jatuh tempo",
                    value =
                        installment.dueDate
                            .toDisplayDate()
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                InvoiceItem(
                    label = "Angsuran",
                    value =
                        "${installment.installmentSequence} dari 24"
                )

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Column {

                    Text(
                        text = "Jumlah tagihan",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Spacer(
                        modifier =
                            Modifier.height(2.dp)
                    )

                    Text(
                        text =
                            formatRupiah(
                                installment.installmentAmount
                            ),
                        fontSize = 20.sp,
                        fontWeight =
                            FontWeight.Bold,
                        color = TextDark
                    )
                }

                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )

                Row(
                    modifier =
                        Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.SpaceBetween,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Text(
                        text = "Status",
                        fontSize = 13.sp,
                        color = Color.Gray
                    )

                    Surface(
                        shape =
                            RoundedCornerShape(8.dp),
                        color = when {

                            isPaid ->
                                Success.copy(
                                    alpha = 0.1f
                                )

                            isOverdue ->
                                MaterialTheme
                                    .colorScheme
                                    .errorContainer

                            else ->
                                Warning.copy(
                                    alpha = 0.15f
                                )
                        }
                    ) {

                        Text(
                            text = when {

                                isPaid ->
                                    "LUNAS"

                                isOverdue ->
                                    "MENUNGGAK"

                                else ->
                                    "BELUM BAYAR"
                            },

                            modifier =
                                Modifier.padding(
                                    horizontal = 10.dp,
                                    vertical = 6.dp
                                ),

                            fontSize = 11.sp,

                            fontWeight =
                                FontWeight.Bold,

                            color = when {

                                isPaid ->
                                    Success

                                isOverdue ->
                                    MaterialTheme
                                        .colorScheme
                                        .error

                                else ->
                                    PrimaryBlue
                            }
                        )
                    }
                }
            }
        },

        dismissButton = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 4.dp
                    ),
                horizontalArrangement =
                    Arrangement.spacedBy(8.dp)
            ) {

                OutlinedButton(
                    onClick = onDismiss,
                    enabled = !isPaying,
                    modifier =
                        Modifier.weight(1f)
                ) {

                    Text("Tutup")
                }

                if (!isPaid) {

                    Button(
                        onClick = onPay,
                        enabled = !isPaying,
                        modifier =
                            Modifier.weight(1f)
                    ) {

                        if (isPaying) {

                            CircularProgressIndicator(
                                modifier =
                                    Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )

                        } else {

                            Text("Bayar")
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun InvoiceItem(
    label: String,
    value: String
) {

    Column {

        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(
            modifier =
                Modifier.height(2.dp)
        )

        Text(
            text = value,
            fontSize = 15.sp,
            fontWeight =
                FontWeight.SemiBold,
            color = TextDark
        )
    }
}

@Composable
private fun EmptyBillState(
    modifier: Modifier = Modifier
) {

    Box(
        modifier = modifier,
        contentAlignment =
            Alignment.Center
    ) {

        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally,
            verticalArrangement =
                Arrangement.spacedBy(8.dp)
        ) {

            Icon(
                imageVector =
                    Icons.Default.ReceiptLong,
                contentDescription = null,
                modifier =
                    Modifier.size(48.dp),
                tint =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )

            Text(
                text = "Belum ada tagihan",
                style =
                    MaterialTheme
                        .typography
                        .titleMedium,
                fontWeight =
                    FontWeight.SemiBold
            )

            Text(
                text =
                    "Tagihan angsuran Anda akan muncul di sini.",
                style =
                    MaterialTheme
                        .typography
                        .bodySmall,
                color =
                    MaterialTheme
                        .colorScheme
                        .onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyFilteredBillState(
    filter: InstallmentFilter
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                vertical = 48.dp
            ),
        contentAlignment =
            Alignment.Center
    ) {

        Text(
            text = when (filter) {

                InstallmentFilter.ALL ->
                    "Belum ada tagihan"

                InstallmentFilter.OVERDUE ->
                    "Tidak ada tagihan menunggak"
            },

            style =
                MaterialTheme
                    .typography
                    .bodyMedium,

            color =
                MaterialTheme
                    .colorScheme
                    .onSurfaceVariant
        )
    }
}

private enum class InstallmentFilter(
    val label: String
) {
    ALL("Semua"),
    OVERDUE("Menunggak")
}

private fun LoanInstallment.isOverdue(): Boolean {

    if (status == InstallmentStatus.PAID) {
        return false
    }

    return try {

        val parsedDueDate =
            LocalDate.parse(dueDate)

        parsedDueDate.isBefore(
            LocalDate.now()
        )

    } catch (_: Exception) {

        false
    }
}

private fun formatRupiah(
    amount: BigDecimal
): String {

    val formatter =
        NumberFormat.getCurrencyInstance(
            Locale("id", "ID")
        ).apply {

            maximumFractionDigits = 0
            minimumFractionDigits = 0
        }

    return formatter
        .format(amount)
        .replace("Rp", "Rp ")
        .trim()
}

private fun String.toDisplayDate(): String {

    return try {

        val input =
            java.text.SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            )

        val output =
            java.text.SimpleDateFormat(
                "d MMMM yyyy",
                Locale("id", "ID")
            )

        output.format(
            input.parse(this)
                ?: return this
        )

    } catch (_: Exception) {

        this
    }
}