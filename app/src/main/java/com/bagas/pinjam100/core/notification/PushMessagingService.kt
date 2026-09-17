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
        Log.d(PushMessagingService::class.java.simpleName, "BISMILLAHHHHH")
        subscribeToTopics()
    }

    // Re-subscribe to topics when the FCM token changes.
    override fun onRegistered(installationId: String) {
        super.onRegistered(installationId)
        Log.d(PushMessagingService::class.java.simpleName, installationId)
    }

    // Handle incoming FCM messages and display notifications.
    override fun onMessageReceived(message: RemoteMessage) {
        val data = message.data

        val title = message.notification?.title ?: data[KEY_TITLE] ?: return
        val body = message.notification?.body.orEmpty()

        notifier.show(
            AppNotification(
                title = title,
                body = body,
                channel = NotificationChannelType.fromEvent(data[KEY_CHANNEL]),
                deepLink = data[KEY_DEEP_LINK]
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