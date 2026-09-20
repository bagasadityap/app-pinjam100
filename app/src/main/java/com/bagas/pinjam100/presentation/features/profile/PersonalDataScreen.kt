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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
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
import com.bagas.pinjam100.domain.model.wilayah.Wilayah
import com.bagas.pinjam100.presentation.viewmodel.customer.CustomerOnboardingUIState
import com.bagas.pinjam100.presentation.viewmodel.wilayah.WilayahUIState
import com.bagas.pinjam100.ui.theme.Pinjam100Theme
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalDataScreen(
    onboardingState: CustomerOnboardingUIState = CustomerOnboardingUIState(),
    wilayahState: WilayahUIState = WilayahUIState(),
    onNationalIdChanged: (String) -> Unit = {},
    onBirthDateChanged: (String) -> Unit = {},
    onPlaceOfBirthChanged: (String) -> Unit = {},
    onGenderChanged: (String) -> Unit = {},
    onAddressChanged: (String) -> Unit = {},
    onPostalCodeChanged: (String) -> Unit = {},
    onProvinceSelected: (Wilayah) -> Unit = {},
    onRegencySelected: (Wilayah) -> Unit = {},
    onDistrictSelected: (Wilayah) -> Unit = {},
    onVillageSelected: (Wilayah) -> Unit = {},
    onBack: () -> Unit = {},
    onNext: () -> Unit = {}
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showGenderDialog by remember { mutableStateOf(false) }
    var showProvinceDialog by remember { mutableStateOf(false) }
    var showRegencyDialog by remember { mutableStateOf(false) }
    var showDistrictDialog by remember { mutableStateOf(false) }
    var showVillageDialog by remember { mutableStateOf(false) }

    val maxAllowedTimestamp = remember {
        Calendar.getInstance().apply {
            add(Calendar.YEAR, -18)
            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }.timeInMillis
    }

    val datePickerState = rememberDatePickerState(
        selectableDates = object : androidx.compose.material3.SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                return utcTimeMillis <= maxAllowedTimestamp
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onBirthDateChanged(
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

    if (showGenderDialog) {
        SelectionDialog(
            title = "Jenis Kelamin",
            items = listOf("Laki-laki", "Perempuan"),
            onDismiss = { showGenderDialog = false },
            onSelected = {
                onGenderChanged(it)
                showGenderDialog = false
            }
        )
    }

    if (showProvinceDialog) {
        WilayahSelectionDialog(
            title = "Pilih Provinsi",
            items = wilayahState.provinces,
            isLoading = wilayahState.isLoadingProvinces,
            onDismiss = { showProvinceDialog = false },
            onSelected = {
                showProvinceDialog = false
                onProvinceSelected(it)
            }
        )
    }

    if (showRegencyDialog) {
        WilayahSelectionDialog(
            title = "Pilih Kota/Kabupaten",
            items = wilayahState.regencies,
            isLoading = wilayahState.isLoadingRegencies,
            onDismiss = { showRegencyDialog = false },
            onSelected = {
                showRegencyDialog = false
                onRegencySelected(it)
            }
        )
    }

    if (showDistrictDialog) {
        WilayahSelectionDialog(
            title = "Pilih Kecamatan",
            items = wilayahState.districts,
            isLoading = wilayahState.isLoadingDistricts,
            onDismiss = { showDistrictDialog = false },
            onSelected = {
                showDistrictDialog = false
                onDistrictSelected(it)
            }
        )
    }

    if (showVillageDialog) {
        WilayahSelectionDialog(
            title = "Pilih Kelurahan/Desa",
            items = wilayahState.villages,
            isLoading = wilayahState.isLoadingVillages,
            onDismiss = { showVillageDialog = false },
            onSelected = {
                showVillageDialog = false
                onVillageSelected(it)
            }
        )
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
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
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
                                text = "Data Pribadi",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            )
                            Text(
                                text = "Langkah 1 dari 5",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    StepProgress(currentStep = 1, totalSteps = 5)
                }
            }

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
                            text = "Identitas Diri",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        FormSelectionField(
                            label = "Tanggal Lahir",
                            value = onboardingState.birthDate,
                            placeholder = "Pilih tanggal lahir",
                            leadingIcon = Icons.Default.CalendarToday,
                            onClick = { showDatePicker = true }
                        )

                        FormTextField(
                            label = "Tempat Lahir",
                            value = onboardingState.placeOfBirth,
                            placeholder = "Masukkan tempat lahir",
                            leadingIcon = Icons.Default.LocationOn,
                            onValueChange = onPlaceOfBirthChanged
                        )

                        FormSelectionField(
                            label = "Jenis Kelamin",
                            value = onboardingState.gender,
                            placeholder = "Pilih jenis kelamin",
                            leadingIcon = Icons.Default.Person,
                            onClick = { showGenderDialog = true }
                        )
                    }
                }
            }

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
                            text = "Alamat Domisili",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        FormTextField(
                            label = "Alamat Lengkap",
                            value = onboardingState.address,
                            placeholder = "Masukkan jalan, no. rumah, RT/RW",
                            leadingIcon = Icons.Default.Home,
                            onValueChange = onAddressChanged,
                            minLines = 3,
                            maxLines = 4
                        )

                        FormSelectionField(
                            label = "Provinsi",
                            value = wilayahState.selectedProvince?.name.orEmpty(),
                            placeholder = if (wilayahState.isLoadingProvinces) "Memuat provinsi..." else "Pilih provinsi",
                            enabled = !wilayahState.isLoadingProvinces,
                            leadingIcon = Icons.Default.LocationOn,
                            onClick = { showProvinceDialog = true }
                        )

                        FormSelectionField(
                            label = "Kota/Kabupaten",
                            value = wilayahState.selectedRegency?.name.orEmpty(),
                            placeholder = if (wilayahState.isLoadingRegencies) "Memuat kota/kabupaten..." else "Pilih kota/kabupaten",
                            enabled = wilayahState.selectedProvince != null && !wilayahState.isLoadingRegencies,
                            leadingIcon = Icons.Default.LocationOn,
                            onClick = { showRegencyDialog = true }
                        )

                        FormSelectionField(
                            label = "Kecamatan",
                            value = wilayahState.selectedDistrict?.name.orEmpty(),
                            placeholder = if (wilayahState.isLoadingDistricts) "Memuat kecamatan..." else "Pilih kecamatan",
                            enabled = wilayahState.selectedRegency != null && !wilayahState.isLoadingDistricts,
                            leadingIcon = Icons.Default.LocationOn,
                            onClick = { showDistrictDialog = true }
                        )

                        FormSelectionField(
                            label = "Kelurahan/Desa",
                            value = wilayahState.selectedVillage?.name.orEmpty(),
                            placeholder = if (wilayahState.isLoadingVillages) "Memuat kelurahan/desa..." else "Pilih kelurahan/desa",
                            enabled = wilayahState.selectedDistrict != null && !wilayahState.isLoadingVillages,
                            leadingIcon = Icons.Default.LocationOn,
                            onClick = { showVillageDialog = true }
                        )

                        FormTextField(
                            label = "Kode Pos",
                            value = onboardingState.postalCode,
                            placeholder = "Masukkan kode pos",
                            leadingIcon = Icons.Default.LocationOn,
                            onValueChange = {
                                if (it.length <= 5 && it.all(Char::isDigit)) {
                                    onPostalCodeChanged(it)
                                }
                            }
                        )
                    }
                }
            }

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

@Composable
private fun WilayahSelectionDialog(
    title: String,
    items: List<Wilayah>,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onSelected: (Wilayah) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredItems by remember(items, searchQuery) {
        derivedStateOf {
            if (searchQuery.isBlank()) {
                items
            } else {
                items.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
            }
        }
    }

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
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Cari...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Memuat data...",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    filteredItems.isEmpty() -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(150.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (searchQuery.isBlank()) "Data tidak tersedia" else "Data tidak ditemukan",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp)
                        ) {
                            items(
                                items = filteredItems,
                                key = { it.code }
                            ) { wilayah ->
                                Text(
                                    text = wilayah.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onSelected(wilayah) }
                                        .padding(horizontal = 8.dp, vertical = 12.dp),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {}
    )
}

@Preview(showBackground = true)
@Composable
private fun PersonalDataScreenPreview() {
    Pinjam100Theme {
        PersonalDataScreen()
    }
}