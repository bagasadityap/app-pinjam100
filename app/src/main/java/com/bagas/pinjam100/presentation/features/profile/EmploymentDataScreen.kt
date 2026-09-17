package com.bagas.pinjam100.presentation.features.profile

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
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bagas.pinjam100.presentation.viewmodel.customer.CustomerOnboardingUIState
import com.bagas.pinjam100.ui.theme.Pinjam100Theme
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val EmploymentTypes = listOf(
    "Karyawan Swasta",
    "Pegawai Negeri",
    "Wiraswasta",
    "Profesional",
    "Pekerja Lepas",
    "Lainnya"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmploymentDataScreen(
    onboardingState: CustomerOnboardingUIState = CustomerOnboardingUIState(),
    onEmploymentTypeChanged: (String) -> Unit = {},
    onCompanyNameChanged: (String) -> Unit = {},
    onPositionChanged: (String) -> Unit = {},
    onMonthlyIncomeChanged: (String) -> Unit = {},
    onStartDateChanged: (String) -> Unit = {},
    onCompanyAddressChanged: (String) -> Unit = {},
    onCompanyPhoneChanged: (String) -> Unit = {},
    onBack: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    var showEmploymentTypeDialog by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()

    if (showEmploymentTypeDialog) {
        SelectionDialog(
            title = "Status Pekerjaan",
            items = EmploymentTypes,
            onDismiss = { showEmploymentTypeDialog = false },
            onSelected = {
                onEmploymentTypeChanged(it)
                showEmploymentTypeDialog = false
            }
        )
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onStartDateChanged(
                                SimpleDateFormat(
                                    "dd MMMM yyyy",
                                    Locale("id", "ID")
                                ).format(Date(millis))
                            )
                        }
                        showDatePicker = false
                    }
                ) {
                    Text(
                        text = "Pilih",
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(
                        text = "Batal",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    headlineContentColor = MaterialTheme.colorScheme.onSurface,
                    weekdayContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    subheadContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    yearContentColor = MaterialTheme.colorScheme.onSurface,
                    currentYearContentColor = MaterialTheme.colorScheme.primary,
                    selectedYearContainerColor = MaterialTheme.colorScheme.primary,
                    selectedYearContentColor = MaterialTheme.colorScheme.onPrimary,
                    dayContentColor = MaterialTheme.colorScheme.onSurface,
                    selectedDayContainerColor = MaterialTheme.colorScheme.primary,
                    selectedDayContentColor = MaterialTheme.colorScheme.onPrimary,
                    todayContentColor = MaterialTheme.colorScheme.primary,
                    todayDateBorderColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }

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
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Step Progress Indicator
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Data Pekerjaan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                        Text(
                            text = "Langkah 2 dari 5",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    StepProgress(currentStep = 2, totalSteps = 5)
                }
            }

            // Informasi Pekerjaan Section Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Informasi Pekerjaan",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        FormSelectionField(
                            label = "Status Pekerjaan",
                            value = onboardingState.employmentType,
                            placeholder = "Pilih status pekerjaan",
                            leadingIcon = Icons.Default.Work,
                            onClick = { showEmploymentTypeDialog = true }
                        )

                        FormTextField(
                            label = "Nama Perusahaan",
                            value = onboardingState.companyName,
                            placeholder = "Masukkan nama perusahaan",
                            leadingIcon = Icons.Default.Business,
                            onValueChange = onCompanyNameChanged
                        )

                        FormTextField(
                            label = "Jabatan",
                            value = onboardingState.position,
                            placeholder = "Masukkan jabatan",
                            leadingIcon = Icons.Default.Badge,
                            onValueChange = onPositionChanged
                        )

                        FormSelectionField(
                            label = "Mulai Bekerja",
                            value = onboardingState.startDate,
                            placeholder = "Pilih tanggal mulai bekerja",
                            leadingIcon = Icons.Default.CalendarToday,
                            onClick = { showDatePicker = true }
                        )
                    }
                }
            }

            // Penghasilan & Kontak Perusahaan Section Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Penghasilan & Kantor",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        FormTextField(
                            label = "Penghasilan Bulanan",
                            value = onboardingState.monthlyIncome,
                            placeholder = "Masukkan penghasilan bulanan",
                            leadingIcon = Icons.Default.MonetizationOn,
                            onValueChange = {
                                onMonthlyIncomeChanged(formatCurrency(it))
                            }
                        )

                        FormTextField(
                            label = "Alamat Perusahaan",
                            value = onboardingState.companyAddress,
                            placeholder = "Masukkan alamat lengkap kantor",
                            leadingIcon = Icons.Default.HomeWork,
                            onValueChange = onCompanyAddressChanged,
                            minLines = 3,
                            maxLines = 4
                        )

                        FormTextField(
                            label = "Nomor Telepon Perusahaan",
                            value = onboardingState.companyPhone,
                            placeholder = "Masukkan nomor telepon kantor",
                            leadingIcon = Icons.Default.Phone,
                            onValueChange = {
                                if (it.length <= 15 && it.all(Char::isDigit)) {
                                    onCompanyPhoneChanged(it)
                                }
                            }
                        )
                    }
                }
            }

            // Action Button
            item {
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Lanjutkan",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
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
private fun FormTextField(
    label: String,
    value: String,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    onValueChange: (String) -> Unit = {},
    minLines: Int = 1,
    maxLines: Int = 1
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = leadingIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            },
            minLines = minLines,
            maxLines = maxLines,
            shape = RoundedCornerShape(12.dp),
            singleLine = minLines == 1 && maxLines == 1,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
            )
        )
    }
}

@Composable
private fun FormSelectionField(
    label: String,
    value: String,
    placeholder: String,
    leadingIcon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.SemiBold
            ),
            color = MaterialTheme.colorScheme.onSurface
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = leadingIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                },
                readOnly = true,
                enabled = enabled,
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f),
                    disabledBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            )

            if (enabled) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { onClick() }
                )
            }
        }
    }
}

@Composable
private fun SelectionDialog(
    title: String,
    items: List<String>,
    onDismiss: () -> Unit,
    onSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        titleContentColor = MaterialTheme.colorScheme.onSurface,
        textContentColor = MaterialTheme.colorScheme.onSurface,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                items.forEach { item ->
                    Text(
                        text = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onSelected(item) }
                            .padding(horizontal = 8.dp, vertical = 12.dp),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {}
    )
}

private fun formatCurrency(value: String): String {
    val digits = value.filter(Char::isDigit)

    if (digits.isEmpty()) {
        return ""
    }

    return digits
        .toLongOrNull()
        ?.let {
            NumberFormat
                .getNumberInstance(Locale("id", "ID"))
                .format(it)
        }
        ?: ""
}

@Preview(showBackground = true)
@Composable
private fun EmploymentDataScreenPreview() {
    Pinjam100Theme {
        EmploymentDataScreen()
    }
}