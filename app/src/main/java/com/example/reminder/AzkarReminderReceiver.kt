package com.example.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AzkarReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val kind = intent.getStringExtra(AzkarReminderScheduler.EXTRA_KIND) ?: return
        val prefs = context.getSharedPreferences(AzkarReminderScheduler.PREFS, Context.MODE_PRIVATE)
        val enabled = prefs.getBoolean("reminder_${kind}_enabled", false)
        if (!enabled) return

        val hour = prefs.getInt("reminder_${kind}_hour", if (kind == AzkarReminderScheduler.MORNING) 7 else 18)
        val minute = prefs.getInt("reminder_${kind}_minute", 0)
        AzkarReminderScheduler.schedule(context, kind, hour, minute)

        val channelId = "azkar_reminders"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(
                NotificationChannel(channelId, "Azkar reminders", NotificationManager.IMPORTANCE_DEFAULT)
            )
        }

        val title = if (kind == AzkarReminderScheduler.MORNING) "የጠዋት አዝካር" else "የማታ አዝካር"
        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(context.applicationInfo.icon)
            .setContentTitle(title)
            .setContentText("የአዝካር ጊዜ ደርሷል። አዝካርህን አትርሳ።")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(if (kind == AzkarReminderScheduler.MORNING) 7101 else 7102, notification)
        } catch (_: SecurityException) {
            // Android 13+ notification permission may not have been granted.
        }
    }
}
