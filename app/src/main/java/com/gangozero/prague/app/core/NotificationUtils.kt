package com.gangozero.prague.app.core

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.annotation.RequiresApi
import com.gangozero.prague.app.R

object NotificationUtils {

    fun startForegroundNotification(
            channelId: String,
            channelName: String,
            notificationId: Int,
            title:Int,
            message:Int,
            context: Service
    ) {

        val channelId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(channelId, channelName, context)
        } else {
            ""
        }

        val pendingIntent: PendingIntent =
                Intent(context, MainActivity::class.java).let { notificationIntent ->
                    PendingIntent.getActivity(context, 0, notificationIntent, 0)
                }

        val notification: Notification = Notification.Builder(context, channelId)
                .setContentTitle(context.getText(title))
                .setContentText(context.getText(message))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(false)
                .setContentIntent(pendingIntent)
                .build()

        context.startForeground(notificationId, notification)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(channelId: String, channelName: String, context: Context): String {
        val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE)
        chan.lightColor = Color.BLUE
        chan.lockscreenVisibility = Notification.VISIBILITY_PRIVATE
        val service = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        service.createNotificationChannel(chan)
        return channelId
    }
}