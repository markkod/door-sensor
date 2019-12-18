package ut.ee.door_sensor

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class MessagingService : FirebaseMessagingService() {

    private val TAG = "MessagingService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val intent = Intent("INTENT_FILTER")

        // For data message types.
        if (remoteMessage.data.isNotEmpty()) {
            val notificationText = buildNotificationText(remoteMessage.data)
            sendInfoToActivity(intent, notificationText)
        }

        // For notification types.
        if (remoteMessage.notification != null) {
            sendInfoToActivity(intent, remoteMessage.notification!!.body!!)
        }
    }

    private fun sendInfoToActivity(intent: Intent, data: String) {
        intent.putExtra("notification_text", data)
        LocalBroadcastManager
            .getInstance(applicationContext)
            .sendBroadcast(intent)
    }

    private fun buildNotificationText(data: Map<String, String>): String {
        Log.i(TAG, "Received data: $data")
        val doorState = data["doorState"]
        val doorName = data["doorName"]
        return "Door: $doorName is now ${getStateText(doorState)}"
    }

    private fun getStateText(state: String?): String {
        return if (state == "true") {
            "open"
        } else "closed"
    }
}