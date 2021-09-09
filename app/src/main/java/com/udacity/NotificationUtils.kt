package com.udacity

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

// Notification ID.
private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(downloadMap: MutableMap<String, String>,
                                         applicationContext: Context) {

    Log.d("Post-Download", "urlKey: ${downloadMap["urlKey"]} Url: ${downloadMap["url"]}, Filename: ${downloadMap["filename"]}, Status: ${downloadMap["status"]}")
    val messageBody = downloadMap["filename"] + " download is finished!"

    // 1. Create intent
    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    contentIntent.putExtra("urlKey", downloadMap["urlKey"])
    contentIntent.putExtra("Status", downloadMap["status"])

    // 2. Create pending intent
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    // Get an instance of NotificationCompat.Builder
    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.notification_channel_id)
    )
        .setSmallIcon(R.drawable.cooked_egg)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(messageBody)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    // notify() is a method of NotificationManager
    notify(NOTIFICATION_ID, builder.build())
}