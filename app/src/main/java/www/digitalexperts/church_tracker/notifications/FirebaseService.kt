package www.digitalexperts.church_tracker.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_DEFAULT
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import www.digitalexperts.church_tracker.index
import www.digitalexperts.church_traker.R
import kotlin.random.Random

private const val CHANNEL_ID="mychannel"

class FirebaseService:FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        val intent=Intent(this, index::class.java)
        val notificationmanager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID=Random.nextInt()
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val myicon = BitmapFactory.decodeResource(resources, R.drawable.dove)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationchannnel(notificationmanager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent=PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT)
        val notification=NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(message.notification?.title)
            .setContentText(message.notification?.body)
            .setSmallIcon(R.drawable.dove)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message.notification?.body)
                    .setBigContentTitle(message.notification?.title)
            )
            .setLargeIcon(myicon)
            .build()

        notificationmanager.notify(notificationID, notification)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private  fun createNotificationchannnel(notificationManager: NotificationManager){
        val channelName="ChannelName"
        val channel=NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_DEFAULT).apply {
            description="Repentance and Holiness"
            enableLights(true)
        }
        notificationManager.createNotificationChannel(channel)
    }
}