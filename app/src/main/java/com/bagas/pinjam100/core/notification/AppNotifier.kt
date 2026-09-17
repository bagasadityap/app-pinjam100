package com.bagas.pinjam100.core.notification

import com.bagas.pinjam100.core.notification.AppNotification

interface AppNotifier {
    fun show(notification: AppNotification): Int

    fun cancel(id: Int)
}
