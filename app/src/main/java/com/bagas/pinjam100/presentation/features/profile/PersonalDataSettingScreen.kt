package com.bagas.pinjam100.presentation.features.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.bagas.pinjam100.domain.model.rekening.Rekening
import com.bagas.pinjam100.presentation.viewmodel.customer.CustomerDetailUIState
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataSettingScreen(
    uiState: CustomerDetailUIState,
    onBack: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Data Pribadi",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // 1. STATE LOADING
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                // 2. STATE ERROR
                uiState.errorMessage != null -> {
                    Text(
                        text = "Gagal memuat data:\n${uiState.errorMessage}",
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                }

                // 3. STATE SUCCESS (DATA LOADED)
                uiState.customerDetail != null -> {
                    val detail = uiState.customerDetail

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 12.dp)
                    ) {

                        SettingSection(title = "Informasi Pribadi") {
                            ReadOnlyField("Tanggal Lahir", formatDate(detail.birthDate))
                            ReadOnlyField("Tempat Lahir", detail.placeOfBirth)
                            ReadOnlyField("Jenis Kelamin", detail.gender, showDivider = false)
                        }

                        SettingSection(title = "Alamat") {
                            ReadOnlyField("Alamat", detail.address)
                            ReadOnlyField("Provinsi", detail.province)
                            ReadOnlyField("Kota / Kabupaten", detail.city)
                            ReadOnlyField("Kecamatan", detail.district)
                            ReadOnlyField("Kelurahan / Desa", detail.village)
                            ReadOnlyField("Kode Pos", detail.postalCode, showDivider = false)
                        }

                        SettingSection(title = "Pekerjaan") {
                            ReadOnlyField("Jenis Pekerjaan", detail.employmentType)
                            ReadOnlyField("Nama Perusahaan", detail.companyName)
                            ReadOnlyField("Posisi / Jabatan", detail.position)
                            ReadOnlyField("Penghasilan Bulanan", formatCurrency(detail.monthlyIncome))
                            ReadOnlyField("Tanggal Mulai Bekerja", formatDate(detail.startDate))
                            ReadOnlyField("Alamat Perusahaan", detail.companyAddress)
                            ReadOnlyField("Nomor Telepon Perusahaan", detail.companyPhone, showDivider = false)
                        }

                        SettingSection(title = "Rekening Bank") {
                            if (detail.rekenings.isEmpty()) {
                                Text(
                                    text = "Belum ada rekening yang ditambahkan.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(16.dp)
                                )
                            } else {
                                detail.rekenings.forEachIndexed { index, rekening ->
                                    val isLast = index == detail.rekenings.size - 1
                                    ReadOnlyRekening(
                                        rekening = rekening,
                                        isLastItem = isLast
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.size(32.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(start = 4.dp, bottom = 6.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 1.dp
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                content()
            }
        }
    }
}

@Composable
private fun ReadOnlyField(
    label: String,
    value: String?,
    showDivider: Boolean = true
) {
    val displayValue = if (value.isNullOrBlank()) "-" else value

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.size(4.dp))

        Text(
            text = displayValue,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.size(12.dp))

        if (showDivider) {
            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
private fun ReadOnlyRekening(
    rekening: Rekening,
    isLastItem: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        ReadOnlyField(label = "Nama Bank", value = rekening.namaBank)
        ReadOnlyField(label = "Nomor Rekening", value = rekening.noRekening)
        ReadOnlyField(label = "Nama Pemilik Rekening", value = rekening.accountHolder, showDivider = !isLastItem)
    }
}

private fun formatCurrency(amount: Long?): String {
    if (amount == null) return "-"
    return "Rp " + String.format("%,d", amount).replace(",", ".")
}

private fun formatDate(dateStr: String?): String {
    if (dateStr.isNullOrBlank()) return "-"
    return try {
        val localDate = LocalDate.parse(dateStr.take(10))
        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID"))
        localDate.format(formatter)
    } catch (e: Exception) {
        dateStr
    }
}