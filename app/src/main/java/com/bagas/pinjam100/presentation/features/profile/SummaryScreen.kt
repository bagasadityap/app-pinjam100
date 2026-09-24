package com.bagas.pinjam100.presentation.features.profile

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bagas.pinjam100.domain.model.document.Document
import com.bagas.pinjam100.presentation.viewmodel.customer.CustomerOnboardingViewModel
import com.bagas.pinjam100.presentation.viewmodel.document.DocumentViewModel
import com.bagas.pinjam100.ui.theme.Pinjam100Theme

@Composable
fun SummaryScreen(
    customerId: String,
    modifier: Modifier = Modifier,
    onboardingViewModel: CustomerOnboardingViewModel = hiltViewModel(),
    documentViewModel: DocumentViewModel = hiltViewModel(),
    onProfileCompleted: () -> Unit = {},
    onBack: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val onboardingState by onboardingViewModel.uiState.collectAsState()
    val documentState by documentViewModel.uiState.collectAsState()

    val rekening = onboardingState.rekenings.firstOrNull()

    val personalData = listOf(
        "Tanggal Lahir" to onboardingState.birthDate,
        "Tempat Lahir" to onboardingState.placeOfBirth,
        "Jenis Kelamin" to onboardingState.gender,
        "Alamat" to onboardingState.address,
        "Provinsi" to onboardingState.province,
        "Kota/Kabupaten" to onboardingState.city,
        "Kecamatan" to onboardingState.district,
        "Kelurahan/Desa" to onboardingState.village,
        "Kode Pos" to onboardingState.postalCode
    )

    val employmentData = listOf(
        "Status Pekerjaan" to onboardingState.employmentType,
        "Nama Perusahaan" to onboardingState.companyName,
        "Jabatan" to onboardingState.position,
        "Penghasilan Bulanan" to onboardingState.monthlyIncome,
        "Mulai Bekerja" to onboardingState.startDate,
        "Alamat Perusahaan" to onboardingState.companyAddress,
        "Telepon Perusahaan" to onboardingState.companyPhone
    )

    val bankData = listOf(
        "Nama Bank" to (rekening?.namaBank ?: ""),
        "Nomor Rekening" to (rekening?.noRekening ?: ""),
        "Nama Pemilik Rekening" to (rekening?.accountHolder ?: "")
    )

    val documents = documentState.documents

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Button(
                    onClick = {
                        onboardingViewModel.submit(customerId) {
                            onProfileCompleted()
                            onSubmit()
                        }
                    },
                    enabled = !onboardingState.isSubmitting,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    if (onboardingState.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )

                        Spacer(modifier = Modifier.size(8.dp))

                        Text(
                            text = "Menyimpan...",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    } else {
                        Text(
                            text = "Simpan Data",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    SummaryHeader(
                        onBack = onBack
                    )

                    StepProgress(
                        currentStep = 5,
                        totalSteps = 5
                    )
                }
            }

            item {
                Text(
                    text = "Periksa kembali data Anda sebelum menyimpan.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            item {
                SummarySection(
                    title = "Data Pribadi",
                    icon = Icons.Filled.Person,
                    data = personalData
                )
            }

            item {
                SummarySection(
                    title = "Data Pekerjaan",
                    icon = Icons.Filled.Work,
                    data = employmentData
                )
            }

            item {
                SummarySection(
                    title = "Rekening Pencairan",
                    icon = Icons.Filled.Badge,
                    data = bankData
                )
            }

            item {
                DocumentSummary(
                    documents = documents
                )
            }

            onboardingState.errorMessage?.let { errorMessage ->
                item {
                    Text(
                        text = errorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun SummaryHeader(
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Kembali",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Review Data",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Text(
                text = "Langkah 5 dari 5",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun StepProgress(
    currentStep: Int,
    totalSteps: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        repeat(totalSteps) { index ->
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .background(
                        color = if (index < currentStep) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                        },
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }
    }
}

@Composable
private fun SummarySection(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    data: List<Pair<String, String>>
) {
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            data.forEach { (label, value) ->
                if (value.isNotBlank()) {
                    SummaryItem(
                        label = label,
                        value = value
                    )
                }
            }
        }
    }
}

@Composable
private fun DocumentSummary(
    documents: List<Document>
) {
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.size(10.dp))

                Text(
                    text = "Dokumen",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            DocumentSummaryItem(
                title = "Foto Selfie",
                exists = documents.any {
                    it.type == "SELFIE"
                }
            )

            DocumentSummaryItem(
                title = "Foto KTP",
                exists = documents.any {
                    it.type == "KTP"
                }
            )

            DocumentSummaryItem(
                title = "Foto Selfie + KTP",
                exists = documents.any {
                    it.type == "SELFIE_KTP"
                }
            )
        }
    }
}

@Composable
private fun DocumentSummaryItem(
    title: String,
    exists: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (exists) {
                Icons.Filled.CheckCircle
            } else {
                Icons.Filled.Description
            },
            contentDescription = null,
            tint = if (exists) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.size(10.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun SummaryItem(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(
            modifier = Modifier.height(2.dp)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun SummaryScreenPreview() {
    Pinjam100Theme {
        SummaryScreen(
            customerId = "00000000-0000-0000-0000-000000000000"
        )
    }
}