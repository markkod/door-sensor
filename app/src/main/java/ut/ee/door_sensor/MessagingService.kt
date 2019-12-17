package ut.ee.door_sensor

import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager


class MessagingService : FirebaseMessagingService() {

    private val TAG = "MessagingService"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.from!!)

        //when the protocol changes
        //val data = remoteMessage.data
        //val myCustomKey = data["my_custom_key"]
        //...

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: " + remoteMessage.data)

            // if notifications_enabled
                    // display notification
            // else
                // do nothing
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body!!)
            val intent = Intent("INTENT_FILTER")
            intent.putExtra("notification_text", remoteMessage.notification!!.body!!)
            LocalBroadcastManager
                .getInstance(applicationContext)
                .sendBroadcast(intent)
            // if notifications_enabled
                // display notification
            // else
                // do nothing
        }
    }
}