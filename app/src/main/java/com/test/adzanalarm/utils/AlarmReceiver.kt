package com.test.adzanalarm.utils

import android.app.Notification
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.test.adzanalarm.MainActivity
import com.test.adzanalarm.R
import java.util.*

class AlarmReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val titleIntent = intent.getStringExtra("title")
        val timestamp = intent.getLongExtra("timestamp", 0)
        var notificationManager: NotificationManagerCompat
        val mNotificationId: Int = 1
        val notifyIntent = Intent(context, MainActivity::class.java)
        var title = "Sudah Masuk Waktu "+titleIntent
        val message = "Solat dulu yuk"

        notifyIntent.putExtra("title", title)
        notifyIntent.putExtra("message", message)
        notifyIntent.putExtra("notification", true)
        notifyIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timestamp

        val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val res = context.resources
        val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            var mNotification = NotificationCompat.Builder(context, NotificationService.CHANNEL_ID)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_masjid)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentTitle(title)
                .setContentText(message).build()
            notificationManager = NotificationManagerCompat.from(context)//getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // mNotificationId is a unique int for each notification that you must define
            notificationManager.notify(mNotificationId, mNotification)
        } else {

            var mNotification = NotificationCompat.Builder(context)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_masjid)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle(title)
                .setSound(uri)
                .setContentText(message).build()
            notificationManager = NotificationManagerCompat.from(context)//getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            // mNotificationId is a unique int for each notification that you must define
            notificationManager.notify(mNotificationId, mNotification)

        }
    }

}