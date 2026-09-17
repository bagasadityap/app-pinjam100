package com.bagas.pinjam100.presentation.features.simulation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bagas.pinjam100.ui.theme.Pinjam100Theme
import java.text.NumberFormat
import java.util.Locale

private const val MIN_LOAN = 1_000_000L
private const val MAX_LOAN = 50_000_000L

private val tenorOptions = listOf(3, 6, 9, 12, 18, 24)

@Composable
fun SimulationScreen(
    modifier: Modifier = Modifier,
    onApplyClick: () -> Unit = {}
) {
    var loanAmount by remember {
        mutableLongStateOf(12_000_000L)
    }

    var amountInput by remember {
        mutableStateOf("12.000.000")
    }

    var selectedTenor by remember {
        mutableIntStateOf(12)
    }

    val interestRate = 15L
    val adminRate = 1L

    val monthlyInterest = loanAmount * interestRate / 1000L
    val totalInterest = monthlyInterest * selectedTenor
    val adminFee = loanAmount * adminRate / 100L
    val totalPayment = loanAmount + totalInterest + adminFee
    val monthlyPayment = totalPayment / selectedTenor

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .statusBarsPadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Simulasi Pinjaman",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Hitung estimasi cicilan pinjamanmu sebelum mengajukan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                SimulationInputCard(
                    loanAmount = loanAmount,
                    amountInput = amountInput,
                    selectedTenor = selectedTenor,
                    onAmountInputChange = { input ->
                        val digits = input.filter { it.isDigit() }

                        if (digits.isEmpty()) {
                            amountInput = ""
                        } else {
                            val parsed = digits.toLongOrNull()

                            if (parsed != null) {
                                val value = parsed.coerceIn(
                                    MIN_LOAN,
                                    MAX_LOAN
                                )

                                loanAmount = value
                                amountInput = formatNumber(value)
                            }
                        }
                    },
                    onLoanAmountChange = { value ->
                        val amount = value
                            .toLong()
                            .coerceIn(MIN_LOAN, MAX_LOAN)

                        loanAmount = amount
                        amountInput = formatNumber(amount)
                    },
                    onTenorChange = {
                        selectedTenor = it
                    }
                )
            }

            item {
                SimulationResultCard(
                    loanAmount = loanAmount,
                    selectedTenor = selectedTenor,
                    monthlyPayment = monthlyPayment,
                    totalInterest = totalInterest,
                    adminFee = adminFee,
                    totalPayment = totalPayment
                )
            }

            item {
                Button(
                    onClick = onApplyClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Ajukan Pinjaman",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )

                    Spacer(modifier = Modifier.size(4.dp))

                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            item {
                Text(
                    text = "Hasil simulasi merupakan estimasi dan dapat berubah sesuai hasil penilaian pengajuan pinjaman.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            start = 4.dp,
                            end = 4.dp,
                            bottom = 20.dp
                        ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun SimulationInputCard(
    loanAmount: Long,
    amountInput: String,
    selectedTenor: Int,
    onAmountInputChange: (String) -> Unit,
    onLoanAmountChange: (Long) -> Unit,
    onTenorChange: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            MaterialTheme.colorScheme.primaryContainer
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Calculate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.size(12.dp))

                Column {
                    Text(
                        text = "Detail Pinjaman",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Atur nominal dan tenor",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Jumlah pinjaman",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = amountInput,
                onValueChange = onAmountInputChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                prefix = {
                    Text(
                        text = "Rp ",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                textStyle = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                shape = RoundedCornerShape(14.dp)
            )

            Text(
                text = "Masukkan nominal atau geser slider",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Slider(
                value = loanAmount.toFloat(),
                onValueChange = { value ->
                    val amount = value
                        .toLong()
                        .coerceIn(MIN_LOAN, MAX_LOAN)

                    onLoanAmountChange(amount)
                },
                valueRange = MIN_LOAN.toFloat()..MAX_LOAN.toFloat(),
                steps = 0,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                colors = SliderDefaults.colors(
                    thumbColor = Color.Gray,
                    activeTrackColor = MaterialTheme.colorScheme.primary,
                    inactiveTrackColor = MaterialTheme.colorScheme.primaryContainer,
                    activeTickColor = Color.Transparent,
                    inactiveTickColor = Color.Transparent
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Rp1 Juta",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Text(
                    text = "Rp50 Juta",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Tenor pinjaman",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tenorOptions.take(3).forEach { tenor ->
                        TenorItem(
                            tenor = tenor,
                            selected = selectedTenor == tenor,
                            onClick = {
                                onTenorChange(tenor)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    tenorOptions.drop(3).forEach { tenor ->
                        TenorItem(
                            tenor = tenor,
                            selected = selectedTenor == tenor,
                            onClick = {
                                onTenorChange(tenor)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TenorItem(
    tenor: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.primaryContainer.copy(
                        alpha = 0.35f
                    )
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$tenor Bulan",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = if (selected) {
                    FontWeight.Bold
                } else {
                    FontWeight.SemiBold
                },
                fontSize = 13.sp
            ),
            color = if (selected) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            maxLines = 1,
            softWrap = false
        )
    }
}

@Composable
private fun SimulationResultCard(
    loanAmount: Long,
    selectedTenor: Int,
    monthlyPayment: Long,
    totalInterest: Long,
    adminFee: Long,
    totalPayment: Long
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.secondary)
                )

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = "Estimasi Angsuran",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = formatRupiah(monthlyPayment),
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.primary,
                maxLines = 1,
                softWrap = false
            )

            Text(
                text = "per bulan × $selectedTenor bulan",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(
                        MaterialTheme.colorScheme.outlineVariant
                    )
            )

            SimulationRow(
                label = "Pokok pinjaman",
                value = formatRupiah(loanAmount)
            )

            SimulationRow(
                label = "Total bunga",
                value = formatRupiah(totalInterest)
            )

            SimulationRow(
                label = "Biaya admin",
                value = formatRupiah(adminFee)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        MaterialTheme.colorScheme.outlineVariant
                    )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total pembayaran",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = formatRupiah(totalPayment),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    softWrap = false
                )
            }
        }
    }
}

@Composable
private fun SimulationRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            softWrap = false
        )
    }
}

private fun formatNumber(value: Long): String {
    return NumberFormat
        .getNumberInstance(Locale("id", "ID"))
        .format(value)
}

private fun formatRupiah(value: Long): String {
    return "Rp " + formatNumber(value)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun SimulationScreenPreview() {
    Pinjam100Theme {
        SimulationScreen()
    }
}