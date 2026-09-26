package com.bagas.pinjam100.presentation.features.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.bagas.pinjam100.R
import com.bagas.pinjam100.domain.model.auth.AuthUser
import com.bagas.pinjam100.domain.model.limit.Limit
import com.bagas.pinjam100.domain.model.transaction.TransactionHistory
import com.bagas.pinjam100.domain.model.transaction.TransactionType
import com.bagas.pinjam100.presentation.components.PullToRefreshContainer
import com.bagas.pinjam100.presentation.navigation.HelpRoute
import com.bagas.pinjam100.presentation.navigation.LoanApplicationRoute
import com.bagas.pinjam100.presentation.navigation.LoanInstallmentListRoute
import com.bagas.pinjam100.presentation.navigation.LoanSimulationRoute
import com.bagas.pinjam100.presentation.navigation.NotificationRoute
import com.bagas.pinjam100.presentation.navigation.TransactionRoute
import com.bagas.pinjam100.presentation.viewmodel.customer.CustomerViewModel
import com.bagas.pinjam100.presentation.viewmodel.limit.LimitUIState
import com.bagas.pinjam100.presentation.viewmodel.transaction.TransactionHistoryViewModel
import com.bagas.pinjam100.ui.theme.SecondaryYellow
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Locale

private data class Promo(
    val title: String,
    val image: Int
)

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    user: AuthUser?,
    limitState: LimitUIState,
    onRefresh: () -> Unit,
    customerViewModel: CustomerViewModel = hiltViewModel(),
    transactionViewModel: TransactionHistoryViewModel = hiltViewModel()
) {
    val transactionState by transactionViewModel.uiState.collectAsStateWithLifecycle()
    val detailUiState by customerViewModel.detailUiState.collectAsStateWithLifecycle()

    LaunchedEffect(user?.id) {
        user?.id?.let { customerId ->
            customerViewModel.getDetailById(customerId)
            transactionViewModel.getByCustomerId(customerId)
        }
    }

    val customerDetail = detailUiState.customerDetail

    val promos = listOf(
        Promo(
            title = "Promo Pinjaman Spesial",
            image = R.drawable.bunga_spesial
        ),
        Promo(
            title = "Limit Lebih Besar",
            image = R.drawable.limit_besar
        )
    )

    PullToRefreshContainer(
        isRefreshing = limitState.isLoading || detailUiState.isLoading,
        onRefresh = {
            onRefresh()
            user?.id?.let { customerId ->
                customerViewModel.getDetailById(customerId)
                transactionViewModel.getByCustomerId(customerId)
            }
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
                    .background(MaterialTheme.colorScheme.background)
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = 20.dp,
                    end = 20.dp,
                    top = 5.dp,
                    bottom = 28.dp
                ),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    HomeHeader(
                        name = customerDetail?.fullName ?: user?.fullName ?: "Pengguna"
                    )
                }

                item {
                    if (customerDetail?.verificationStatus?.equals("REJECTED", ignoreCase = true) == true) {
                        RejectedCard()
                    } else {
                        when {
                            limitState.isLoading -> {
                                LoadingLimitCard()
                            }

                            limitState.limit != null -> {
                                LimitCard(
                                    limit = limitState.limit
                                )
                            }

                            else -> {
                                VerificationCard()
                            }
                        }
                    }
                }

                item {
                    ServiceSection(
                        onLoanClick = {
                            navController.navigate(LoanApplicationRoute)
                        },
                        onSimulationClick = {
                            navController.navigate(LoanSimulationRoute)
                        },
                        onInstallmentCLick = {
                            navController.navigate(LoanInstallmentListRoute)
                        },
                        onHelpClick = {
                            navController.navigate(HelpRoute)
                        }
                    )
                }

                item {
                    PromoSection(promos)
                }

                item {
                    TransactionHistorySection(
                        transactions = transactionState.transactions,
                        onClick = {
                            navController.navigate(TransactionRoute)
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun HomeHeader(
    name: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Pinjam100",
                modifier = Modifier.height(25.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selamat datang,",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = name,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun LoadingLimitCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Memuat informasi limit",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = "Mohon tunggu sebentar...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.72f
                )
            )

            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(10.dp)),
                color = SecondaryYellow,
                trackColor = MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.16f
                )
            )
        }
    }
}

@Composable
private fun LimitCard(
    limit: Limit
) {
    val progress = if (limit.creditLimit > 0) {
        (limit.availableLimit.toFloat() / limit.creditLimit.toFloat())
            .coerceIn(0f, 1f)
    } else {
        0f
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Unspecified
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)
                        )
                    )
                )
                .padding(22.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(140.dp)
                    .offset(x = 180.dp, y = (-40).dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.06f))
            )

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(30.dp),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "Limit Pinjaman",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Rp${limit.availableLimit.toRupiah()}",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 28.sp
                            ),
                            color = MaterialTheme.colorScheme.onPrimary,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Visible
                        )
                    }

                    Image(
                        painter = painterResource(R.drawable.illustration_hand_coin),
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp, 60.dp)
                            .offset(x = 6.dp, y = (-4).dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tersedia untuk digunakan",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = SecondaryYellow
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    color = SecondaryYellow,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )

                Spacer(modifier = Modifier.height(18.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.Black.copy(alpha = 0.12f)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text(
                                text = "Limit awal",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "Rp${limit.creditLimit.toRupiah()}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.End,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "Sisa Tersedia",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                            Text(
                                text = "Rp${limit.availableLimit.toRupiah()}",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = SecondaryYellow
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun VerificationCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            MaterialTheme.colorScheme.onPrimary.copy(
                                alpha = 0.12f
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.History,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "Akun sedang diverifikasi",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onPrimary
                    )

                    Text(
                        text = "Data Anda sedang kami periksa",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimary.copy(
                            alpha = 0.72f
                        )
                    )
                }
            }

            LinearProgressIndicator(
                progress = { 0.7f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(7.dp)
                    .clip(RoundedCornerShape(10.dp)),
                color = SecondaryYellow,
                trackColor = MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.16f
                )
            )

            Text(
                text = "Setelah verifikasi selesai, limit pinjaman Anda akan tersedia di sini.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimary.copy(
                    alpha = 0.78f
                )
            )
        }
    }
}

@Composable
private fun RejectedCard() {
    val redContainerColor = Color(0xFFDC2626)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = redContainerColor
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White.copy(alpha = 0.15f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Cancel,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(3.dp)
                ) {
                    Text(
                        text = "Pengajuan Ditolak",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color.White
                    )

                    Text(
                        text = "Mohon maaf, pengajuan Anda ditolak",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            Text(
                text = "Data Anda belum memenuhi kriteria verifikasi kami. Silakan hubungi layanan bantuan untuk informasi lebih lanjut.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.9f)
            )
        }
    }
}

@Composable
private fun ServiceSection(
    onLoanClick: () -> Unit,
    onSimulationClick: () -> Unit,
    onInstallmentCLick: () -> Unit,
    onHelpClick: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text(
            text = "Layanan",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ServiceCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.CreditCard,
                title = "Pengajuan",
                description = "Ajukan pinjaman",
                highlighted = true,
                onClick = onLoanClick
            )

            ServiceCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Calculate,
                title = "Simulasi",
                description = "Hitung cicilan",
                onClick = onSimulationClick
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ServiceCard(
                modifier = Modifier.weight(1f),
                icon = Icons.Filled.Payments,
                title = "Pembayaran",
                description = "Bayar tagihan",
                onClick = onInstallmentCLick
            )

            ServiceCard(
                modifier = Modifier.weight(1f),
                icon = Icons.AutoMirrored.Filled.HelpOutline,
                title = "Bantuan",
                description = "Butuh bantuan?",
                onClick = onHelpClick
            )
        }
    }
}

@Composable
private fun ServiceCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    description: String,
    highlighted: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (highlighted) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (highlighted) 0.dp else 1.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        if (highlighted) {
                            MaterialTheme.colorScheme.onPrimary.copy(
                                alpha = 0.14f
                            )
                        } else {
                            MaterialTheme.colorScheme.primaryContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (highlighted) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    modifier = Modifier.size(21.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = if (highlighted) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (highlighted) {
                        MaterialTheme.colorScheme.onPrimary.copy(
                            alpha = 0.72f
                        )
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Composable
private fun PromoSection(
    promos: List<Promo>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Promo & Penawaran",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(end = 4.dp)
        ) {
            items(promos) { promo ->
                PromoCard(promo)
            }
        }
    }
}

@Composable
private fun PromoCard(
    promo: Promo
) {
    Card(
        modifier = Modifier.width(260.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            ) {
                Image(
                    painter = painterResource(promo.image),
                    contentDescription = promo.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = promo.title,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = when (promo.title) {
                        "Promo Pinjaman Spesial" -> "Bunga spesial mulai 0,1% per hari."
                        "Limit Lebih Besar" -> "Kesempatan limit pinjaman lebih besar."
                        else -> "Dapatkan penawaran menarik dari Pinjam100."
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
private fun TransactionHistorySection(
    transactions: List<TransactionHistory>,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Aktivitas Terakhir",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Lihat Semua",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp
        ) {
            if (transactions.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp, horizontal = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Belum ada transaksi",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                Column(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    transactions.take(3).forEach { transaction ->
                        val isIncome = transaction.type == TransactionType.DISBURSEMENT
                        TransactionHistoryItem(
                            title = if (isIncome) "Pencairan Pinjaman" else "Pembayaran Angsuran",
                            description = if (isIncome) "Pinjaman berhasil dicairkan" else "Pembayaran angsuran berhasil",
                            amount = "${if (isIncome) "+" else "-"} ${formatRupiah(transaction.amount)}",
                            positive = isIncome
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionHistoryItem(
    title: String,
    description: String,
    amount: String,
    positive: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (positive) {
                        Color(0xFFDCFCE7)
                    } else {
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (positive) {
                    Icons.Filled.AccountBalanceWallet
                } else {
                    Icons.Filled.Payments
                },
                contentDescription = null,
                tint = if (positive) {
                    Color(0xFF15803D)
                } else {
                    MaterialTheme.colorScheme.primary
                },
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold
                ),
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = amount,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold
            ),
            color = if (positive) {
                Color(0xFF16A34A)
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            maxLines = 1,
            softWrap = false
        )
    }
}

private fun Long.toRupiah(): String {
    return NumberFormat
        .getNumberInstance(Locale("id", "ID"))
        .format(this)
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