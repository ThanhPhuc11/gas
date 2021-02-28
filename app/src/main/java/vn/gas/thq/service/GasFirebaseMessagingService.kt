package vn.gas.thq.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import vn.gas.thq.MainActivity
import vn.hongha.ga.R

class GasFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        showNotification(applicationContext, p0)
    }

    private fun showNotification(context: Context, remoteMessage: RemoteMessage) {

        // notify id
        //DateFormat df = new SimpleDateFormat(DateUtil.DATE_FORMAT_3, Locale.getDefault());
        //Date now = DateUtil.getJapanCalendar().getTime();
        //String reportDate = df.format(now);

        // notify channel
        val channelId = ""

        // notify sound
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        // notify notification
        val title = remoteMessage.notification?.title
        val body = remoteMessage.notification?.body

        // notify data
        val data = remoteMessage.data
        val type = data["notificationType"]

        // intent, prepare data
        val intent = Intent(context, MainActivity::class.java)
        intent.action = System.currentTimeMillis().toString()
        val bundle = Bundle()
        bundle.putString("notificationType", type)
        intent.putExtras(bundle)
        val resultPendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(resultPendingIntent)
        notificationBuilder.setVibrate(longArrayOf(100, 100))
        notificationBuilder.setLights(Color.GREEN, 10000, 10000)
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                resources.getString(R.string.app_name),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.setShowBadge(false)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(123 /* ID of notification */, notificationBuilder.build())
    }
}