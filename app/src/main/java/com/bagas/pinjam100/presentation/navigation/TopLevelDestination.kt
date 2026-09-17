package com.bagas.pinjam100.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.ui.graphics.vector.ImageVector

enum class TopLevelDestination(
    val route: AppRoute,
    val labelRes: String,
    val iconRes: ImageVector,
) {
    HOME(
        HomeGraph,
        "Beranda",
        Icons.Outlined.Home
    ),
    TRANSACTION(
        TransactionGraph,
        "Transaksi",
        Icons.Filled.History
    ),
    LOAN(
        LoanGraph,
        "Pinjaman",
        Icons.Filled.AccountBalanceWallet
    ),
    HELP(
        HelpGraph,
        "Bantuan",
        Icons.AutoMirrored.Filled.HelpOutline
    ),
    PROFILE(
        ProfileGraph,
        "Profile",
        Icons.Outlined.Person
    ),
}