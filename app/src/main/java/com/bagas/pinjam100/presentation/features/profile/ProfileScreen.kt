package com.bagas.pinjam100.presentation.features.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bagas.pinjam100.presentation.components.PullToRefreshContainer
import com.bagas.pinjam100.presentation.viewmodel.auth.AuthViewModel
import com.bagas.pinjam100.presentation.viewmodel.customer.CustomerViewModel
import com.bagas.pinjam100.ui.theme.Pinjam100Theme

private data class ProfileMenu(
    val icon: ImageVector,
    val title: String,
    val description: String,
    val onClick: () -> Unit = {}
)

private data class ProfileSection(
    val sectionTitle: String,
    val items: List<ProfileMenu>
)

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    authViewModel: AuthViewModel = hiltViewModel(),
    customerViewModel: CustomerViewModel = hiltViewModel(),
    onRefresh: () -> Unit = {},
    onPersonalDataSetting: () -> Unit = {},
    onLogout: () -> Unit = {},
    onChangePassword: () -> Unit = {},
    onHelp: () -> Unit = {},
    onAbout: () -> Unit = {}
) {
    val authState by authViewModel.uiState.collectAsState()
    val detailUiState by customerViewModel.detailUiState.collectAsState()

    LaunchedEffect(authState.user?.id) {
        authState.user?.id?.let { customerId ->
            customerViewModel.getDetailById(customerId)
        }
    }

    var showLogoutDialog by remember {
        mutableStateOf(false)
    }

    val accountSection = ProfileSection(
        sectionTitle = "Akun & Keamanan",
        items = listOf(
            ProfileMenu(
                icon = Icons.Filled.Person,
                title = "Data Diri",
                description = "Kelola informasi pribadi",
                onClick = onPersonalDataSetting
            ),
            ProfileMenu(
                icon = Icons.Filled.Lock,
                title = "Ubah Password",
                description = "Ubah password akun Anda",
                onClick = onChangePassword
            )
        )
    )

    val preferencesSection = ProfileSection(
        sectionTitle = "Preferensi & Bantuan",
        items = listOf(
            ProfileMenu(
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                title = "Pusat Bantuan",
                description = "FAQ dan Customer Service",
                onClick = onHelp
            ),
            ProfileMenu(
                icon = Icons.Filled.Info,
                title = "Tentang Pinjam100",
                description = "Versi aplikasi dan kebijakan",
                onClick = onAbout
            )
        )
    )

    val menuSections = listOf(
        accountSection,
        preferencesSection
    )

    val customerDetail = detailUiState.customerDetail

    PullToRefreshContainer(
        isRefreshing = detailUiState.isLoading || authState.isSubmitting,
        onRefresh = {
            authState.user?.id?.let { customerId ->
                customerViewModel.getDetailById(customerId)
            }
            onRefresh()
        },
        modifier = modifier
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = MaterialTheme.colorScheme.background
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Text(
                            text = "Profil Saya",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp
                            ),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                item {
                    ProfileHeaderCard(
                        fullName = customerDetail?.fullName ?: authState.user?.fullName ?: "-",
                        email = customerDetail?.email ?: authState.user?.email ?: "-",
                        phoneNumber = customerDetail?.phoneNumber ?: authState.user?.phoneNumber ?: "-",
                        verificationStatus = customerDetail?.verificationStatus
                    )
                }

                items(menuSections) { section ->
                    ProfileSectionGroup(
                        section = section
                    )
                }

                item {
                    LogoutSectionItem(
                        onClick = {
                            showLogoutDialog = true
                        }
                    )

                    Spacer(
                        modifier = Modifier.size(8.dp)
                    )
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = {
                showLogoutDialog = false
            },
            title = {
                Text(
                    text = "Keluar Akun?",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            },
            text = {
                Text(
                    text = "Apakah Anda yakin ingin keluar dari akun Pinjam100 di perangkat ini?",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        authViewModel.logout()
                        onLogout()
                    }
                ) {
                    Text(
                        text = "Keluar",
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                    }
                ) {
                    Text("Batal")
                }
            }
        )
    }
}

@Composable
private fun ProfileHeaderCard(
    fullName: String,
    email: String,
    phoneNumber: String,
    verificationStatus: Any? = null
) {
    val statusString = verificationStatus?.toString()

    val (badgeBgColor, badgeContentColor, badgeIcon, badgeText) = when {
        statusString.equals("VERIFIED", ignoreCase = true) -> StatusBadgeConfig(
            bgColor = Color(0xFF16A34A).copy(alpha = 0.08f),
            contentColor = Color(0xFF16A34A),
            icon = Icons.Filled.VerifiedUser,
            text = "Akun Terverifikasi"
        )
        statusString.equals("REJECTED", ignoreCase = true) -> StatusBadgeConfig(
            bgColor = MaterialTheme.colorScheme.error.copy(alpha = 0.08f),
            contentColor = MaterialTheme.colorScheme.error,
            icon = Icons.Filled.Cancel,
            text = "Verifikasi Ditolak"
        )
        else -> StatusBadgeConfig(
            bgColor = Color(0xFFD97706).copy(alpha = 0.08f),
            contentColor = Color(0xFFD97706),
            icon = Icons.Filled.History,
            text = "Menunggu Verifikasi"
        )
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 4.dp),
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
                .padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.tertiary
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = fullName
                            .trim()
                            .firstOrNull()
                            ?.uppercase()
                            ?: "?",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }

                Spacer(
                    modifier = Modifier.size(16.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        text = phoneNumber,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            Spacer(
                modifier = Modifier.size(16.dp)
            )

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant.copy(
                    alpha = 0.4f
                )
            )

            Spacer(
                modifier = Modifier.size(12.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(badgeBgColor)
                    .padding(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = badgeIcon,
                    contentDescription = null,
                    tint = badgeContentColor,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(
                    modifier = Modifier.size(8.dp)
                )

                Text(
                    text = badgeText,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = badgeContentColor
                )
            }
        }
    }
}

private data class StatusBadgeConfig(
    val bgColor: Color,
    val contentColor: Color,
    val icon: ImageVector,
    val text: String
)

@Composable
private fun ProfileSectionGroup(
    section: ProfileSection
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 8.dp
            )
    ) {
        Text(
            text = section.sectionTitle,
            style = MaterialTheme.typography.labelLarge.copy(
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.padding(
                start = 4.dp,
                bottom = 6.dp
            )
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
            Column {
                section.items.forEachIndexed { index, menu ->
                    ProfileMenuItemRow(
                        menu = menu
                    )

                    if (index < section.items.size - 1) {
                        HorizontalDivider(
                            modifier = Modifier.padding(
                                horizontal = 16.dp
                            ),
                            color = MaterialTheme.colorScheme.outlineVariant.copy(
                                alpha = 0.3f
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuItemRow(
    menu: ProfileMenu
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                menu.onClick()
            }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(
                    MaterialTheme.colorScheme.primaryContainer.copy(
                        alpha = 0.5f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = menu.icon,
                contentDescription = menu.title,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(
            modifier = Modifier.size(14.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = menu.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = menu.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Icon(
            imageVector = Icons.Filled.ChevronRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun LogoutSectionItem(
    onClick: () -> Unit
) {
    val mutedRedBackground = MaterialTheme.colorScheme.error.copy(
        alpha = 0.08f
    )

    val mutedRedBorder = MaterialTheme.colorScheme.error.copy(
        alpha = 0.2f
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 20.dp,
                vertical = 6.dp
            )
            .border(
                width = 1.dp,
                color = mutedRedBorder,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = mutedRedBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(
                        MaterialTheme.colorScheme.error.copy(
                            alpha = 0.15f
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Logout,
                    contentDescription = "Keluar",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(
                modifier = Modifier.size(14.dp)
            )

            Text(
                text = "Keluar dari Akun",
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.error
            )

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error.copy(
                    alpha = 0.7f
                ),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
private fun ProfileScreenPreview() {
    Pinjam100Theme {
        ProfileScreen()
    }
}