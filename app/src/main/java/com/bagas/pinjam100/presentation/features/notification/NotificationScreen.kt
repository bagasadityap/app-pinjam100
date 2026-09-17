package com.bagas.pinjam100.presentation.features.notification

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bagas.pinjam100.ui.theme.Pinjam100Theme

private enum class NotificationType {
    PROMOTION,
    OVERDUE
}

private data class NotificationItem(
    val title: String,
    val description: String,
    val date: String,
    val type: NotificationType,
    val amount: String? = null
)

@Composable
fun NotificationScreen(modifier: Modifier = Modifier) {
    val notifications = listOf(
        NotificationItem(
            title = "Angsuran Anda Menunggak",
            description = "Segera lakukan pembayaran untuk menghindari biaya keterlambatan.",
            date = "Hari ini, 09:15",
            type = NotificationType.OVERDUE,
            amount = "Rp525.000"
        ),
        NotificationItem(
            title = "Promo Pinjaman Spesial",
            description = "Dapatkan kesempatan memperoleh penawaran pinjaman dengan limit lebih besar.",
            date = "08 September 2026",
            type = NotificationType.PROMOTION
        ),
        NotificationItem(
            title = "Cashback Pembayaran Angsuran",
            description = "Bayar angsuran tepat waktu dan nikmati kesempatan mendapatkan cashback.",
            date = "05 September 2026",
            type = NotificationType.PROMOTION
        ),
        NotificationItem(
            title = "Promo Khusus Pengguna Baru Pinjam100",
            description = "Nikmati berbagai penawaran menarik untuk kebutuhan finansial Anda.",
            date = "01 September 2026",
            type = NotificationType.PROMOTION
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Notifikasi",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Informasi terbaru untuk Anda.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(notifications) { notification ->
                NotificationItemCard(
                    notification
                )
            }
        }
    }
}

@Composable
private fun NotificationItemCard(notification: com.bagas.pinjam100.presentation.features.notification.NotificationItem) {
    val isOverdue = notification.type == NotificationType.OVERDUE

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = if (isOverdue) {
            Color(0xFFFFF5F5)
        } else {
            MaterialTheme.colorScheme.surface
        },
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Surface(
                    modifier = Modifier.size(46.dp),
                    shape = CircleShape,
                    color = if (isOverdue) {
                        Color(0xFFFFE5E5)
                    } else {
                        MaterialTheme.colorScheme.primaryContainer
                    }
                ) {
                    Icon(
                        imageVector = if (isOverdue) {
                            Icons.Filled.Warning
                        } else {
                            Icons.Filled.Campaign
                        },
                        contentDescription = null,
                        modifier = Modifier.padding(11.dp),
                        tint = if (isOverdue) {
                            Color(0xFFDC2626)
                        } else {
                            MaterialTheme.colorScheme.primary
                        }
                    )
                }

                Spacer(modifier = Modifier.size(14.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = notification.title,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = notification.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = notification.date,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (notification.amount != null) {
                Spacer(modifier = Modifier.height(14.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFE5E5)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total angsuran",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = notification.amount,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color(0xFFDC2626)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Bayar Sekarang",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun NotificationScreenPreview() {
    Pinjam100Theme {
        NotificationScreen()
    }
}