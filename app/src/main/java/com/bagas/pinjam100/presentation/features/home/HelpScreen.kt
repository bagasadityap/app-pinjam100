package com.bagas.pinjam100.presentation.features.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bagas.pinjam100.ui.theme.Pinjam100Theme

private data class FaqItem(
    val question: String,
    val answer: String
)

private data class FaqSection(
    val title: String,
    val items: List<FaqItem>
)

private val faqSections = listOf(
    FaqSection(
        title = "Pengajuan Pinjaman",
        items = listOf(
            FaqItem(
                "Bagaimana cara mengajukan pinjaman?",
                "Pilih menu Pengajuan pada aplikasi, kemudian lengkapi data pribadi, pekerjaan, rekening bank, dan kontak darurat. Setelah semua data diperiksa, lanjutkan untuk mengirim pengajuan pinjaman."
            ),
            FaqItem(
                "Apa saja syarat pengajuan pinjaman?",
                "Lengkapi data pribadi, identitas, informasi pekerjaan, rekening bank, dan kontak darurat sesuai dengan data yang diminta pada aplikasi."
            ),
            FaqItem(
                "Bagaimana cara mengetahui status pengajuan?",
                "Status pengajuan dapat dilihat melalui halaman pengajuan. Anda dapat melihat apakah pengajuan sedang diproses, disetujui, atau ditolak."
            ),
            FaqItem(
                "Berapa lama proses pengajuan?",
                "Waktu proses pengajuan dapat berbeda tergantung proses verifikasi dan pemeriksaan data yang dilakukan."
            )
        )
    ),
    FaqSection(
        title = "Pinjaman",
        items = listOf(
            FaqItem(
                "Bagaimana dana pinjaman dicairkan?",
                "Setelah pengajuan disetujui dan proses pencairan dikonfirmasi, dana pinjaman akan dicairkan ke rekening bank yang telah Anda daftarkan."
            ),
            FaqItem(
                "Bagaimana cara melihat pinjaman aktif?",
                "Informasi pinjaman aktif dapat dilihat melalui menu Pinjaman. Halaman tersebut menampilkan jumlah pinjaman, sisa pinjaman, dan informasi angsuran."
            ),
            FaqItem(
                "Bagaimana cara melihat jadwal angsuran?",
                "Jadwal angsuran dapat dilihat pada detail pinjaman aktif. Informasi tersebut menampilkan jumlah angsuran, tanggal jatuh tempo, dan status pembayaran."
            )
        )
    ),
    FaqSection(
        title = "Pembayaran",
        items = listOf(
            FaqItem(
                "Bagaimana cara membayar angsuran?",
                "Buka informasi pinjaman aktif untuk melihat angsuran yang harus dibayar. Pilih angsuran yang tersedia, kemudian ikuti langkah pembayaran menggunakan metode pembayaran yang tersedia."
            ),
            FaqItem(
                "Apa yang terjadi jika terlambat membayar?",
                "Keterlambatan pembayaran dapat menyebabkan kewajiban pembayaran menjadi tertunda dan dapat dikenakan ketentuan sesuai dengan perjanjian pinjaman yang berlaku."
            ),
            FaqItem(
                "Bagaimana cara melihat riwayat pembayaran?",
                "Riwayat pembayaran dapat dilihat melalui menu Riwayat Transaksi untuk melihat aktivitas pembayaran dan transaksi lainnya."
            )
        )
    ),
    FaqSection(
        title = "Akun",
        items = listOf(
            FaqItem(
                "Bagaimana cara mengubah data pribadi?",
                "Buka menu Profile kemudian pilih Data Diri. Anda dapat memperbarui informasi yang tersedia sesuai dengan ketentuan perubahan data."
            ),
            FaqItem(
                "Bagaimana cara mengubah password?",
                "Buka menu Profile, pilih Keamanan, kemudian pilih opsi untuk mengubah password akun Anda."
            ),
            FaqItem(
                "Bagaimana cara menghubungi Customer Service?",
                "Gunakan informasi bantuan yang tersedia pada aplikasi untuk menghubungi Customer Service Pinjam100 apabila membutuhkan bantuan lebih lanjut."
            )
        )
    )
)

@Composable
fun HelpScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Bantuan",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1819)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Temukan jawaban untuk pertanyaan umum mengenai layanan Pinjam100.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        faqSections.forEach { section ->
            item {
                FaqSectionView(
                    section
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun FaqSectionView(
    section: com.bagas.pinjam100.presentation.features.home.FaqSection
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = section.title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(
                start = 2.dp,
                bottom = 10.dp
            )
        )

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            color = Color.White
        ) {
            Column {
                section.items.forEachIndexed { index, item ->
                    FaqItemView(
                        item = item,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(
                                RoundedCornerShape(
                                    topStart = if (index == 0) 14.dp else 0.dp,
                                    topEnd = if (index == 0) 14.dp else 0.dp,
                                    bottomStart = if (index == section.items.lastIndex) 14.dp else 0.dp,
                                    bottomEnd = if (index == section.items.lastIndex) 14.dp else 0.dp
                                )
                            )
                    )

                    if (index < section.items.lastIndex) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FaqItemView(
    modifier: Modifier,
    item: com.bagas.pinjam100.presentation.features.home.FaqItem
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    expanded = !expanded
                }
                .padding(
                    horizontal = 14.dp,
                    vertical = 14.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.question,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1819)
            )

            Icon(
                imageVector = if (expanded) {
                    Icons.Default.KeyboardArrowUp
                } else {
                    Icons.Default.KeyboardArrowDown
                },
                contentDescription = null,
                tint = Color(0xFF1A1819)
            )
        }

        AnimatedVisibility(
            visible = expanded
        ) {
            Text(
                text = item.answer,
                modifier = Modifier.padding(
                    start = 14.dp,
                    end = 14.dp,
                    bottom = 14.dp
                ),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HelpScreenPreview() {
    Pinjam100Theme {
        HelpScreen()
    }
}