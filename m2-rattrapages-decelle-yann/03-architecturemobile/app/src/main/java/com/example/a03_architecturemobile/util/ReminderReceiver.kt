// package com.example.a03_architecturemobile.util

// import android.app.NotificationChannel
// import android.app.NotificationManager
// import android.app.PendingIntent
// import android.content.BroadcastReceiver
// import android.content.Context
// import android.content.Intent
// import android.os.Build
// import androidx.core.app.NotificationCompat
// import com.example.a03_architecturemobile.R

// class ReminderReceiver : BroadcastReceiver() {
//     override fun onReceive(context: Context, intent: Intent) {
//         val title = intent.getStringExtra("title") ?: "Reminder"
//         val description = intent.getStringExtra("description") ?: ""
//         val notificationId = intent.getIntExtra("notificationId", 0)

//         val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//         val channelId = "reminder_channel"
//         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//             val channel = NotificationChannel(channelId, "Reminders", NotificationManager.IMPORTANCE_HIGH)
//             notificationManager.createNotificationChannel(channel)
//         }
//         val notification = NotificationCompat.Builder(context, channelId)
//             .setContentTitle(title)
//             .setContentText(description)
//             .setSmallIcon(R.drawable.ic_notification)
//             .setAutoCancel(true)
//             .build()
//         notificationManager.notify(notificationId, notification)
//     }
// }
