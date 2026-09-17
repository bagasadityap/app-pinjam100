package com.bagas.pinjam100.core.notification

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.core.app.NotificationManagerCompat
import com.bagas.pinjam100.R

enum class NotificationChannelType(
    val id: String,
    @param:StringRes val nameRes: Int,
    val importance: Int,
    val channelId: String = id,
    @param:RawRes val soundRes: Int? = null,
) {
    GENERAL(
        id = "general",
        nameRes = R.string.notification_channel_general,
        importance = NotificationManagerCompat.IMPORTANCE_DEFAULT,
    ),
    VERIFICATION(
        id = "verification",
        nameRes = R.string.notification_channel_verification,
        importance = NotificationManagerCompat.IMPORTANCE_HIGH,
    ),
    DISBURSEMENT(
        id = "disbursement",
        nameRes = R.string.notification_channel_disbursement,
        importance = NotificationManagerCompat.IMPORTANCE_HIGH,
    ),
    INSTALLMENT(
        id = "installment",
        nameRes = R.string.notification_channel_installment,
        importance = NotificationManagerCompat.IMPORTANCE_HIGH,
    ),
    PROMO(
        id = "promo",
        nameRes = R.string.notification_channel_promo,
        importance = NotificationManagerCompat.IMPORTANCE_DEFAULT,
        channelId = "promo_sound_v1",
    );

    companion object {
        fun fromId(id: String?): NotificationChannelType =
            entries.firstOrNull { it.id == id } ?: GENERAL

        fun fromEvent(event: String?): NotificationChannelType =
            when (event) {
                "verification" -> VERIFICATION
                "disbursement" -> DISBURSEMENT
                "installment" -> INSTALLMENT
                "promo" -> PROMO
                else -> GENERAL
            }
    }
}