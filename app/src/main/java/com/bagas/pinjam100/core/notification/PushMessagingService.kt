package com.bagas.pinjam100.core.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "PushMessagingService"

@AndroidEntryPoint
class PushMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var notifier: AppNotifier

    // Initialize required FCM topic subscriptions.
    override fun onCreate() {
        super.onCreate()
        subscribeToTopics()
    }

    // Re-subscribe to topics when the FCM token changes.
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "FCM token: $token")
        subscribeToTopics()
    }

    // Handle incoming FCM messages and display notifications.
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val data = message.data

        Log.d(TAG, "FCM message received: $data")
        Log.d(TAG, "FCM from: ${message.from}")

        val title = data[KEY_TITLE] ?: return
        val body = data[KEY_BODY] ?: return
        val channel = data[KEY_CHANNEL]
        val deeplink = data[KEY_DEEP_LINK]

        Log.d(TAG, "Notification title: $title")
        Log.d(TAG, "Notification body: $body")
        Log.d(TAG, "Notification channel: $channel")
        Log.d(TAG, "Notification deeplink: $deeplink")

        notifier.show(
            AppNotification(
                title = title,
                body = body,
                channel = NotificationChannelType.fromEvent(channel),
                deepLink = deeplink
            )
        )
    }

    // Subscribe the device to all required notification topics.
    private fun subscribeToTopics() {
        listOf(
            TOPIC_COMMON,
            TOPIC_VERIFICATION,
            TOPIC_DISBURSEMENT,
            TOPIC_INSTALLMENT
        ).forEach { topic ->
            FirebaseMessaging.getInstance()
                .subscribeToTopic(topic)
                .addOnSuccessListener {
                    Log.d(TAG, "Subscribed to topic: $topic")
                }
                .addOnFailureListener { exception ->
                    Log.e(TAG, "Failed to subscribe to topic: $topic", exception)
                }
        }
    }

    private companion object {
        const val TOPIC_COMMON = "common"
        const val TOPIC_VERIFICATION = "verification"
        const val TOPIC_DISBURSEMENT = "disbursement"
        const val TOPIC_INSTALLMENT = "installment"
        const val KEY_TITLE = "title"
        const val KEY_BODY = "body"
        const val KEY_CHANNEL = "channel"
        const val KEY_DEEP_LINK = "deeplink"
    }
}