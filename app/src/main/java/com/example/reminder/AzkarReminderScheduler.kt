package com.example.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object AzkarReminderScheduler {
    const val PREFS = "nurul_azkar_prefs"
    const val ACTION_REMINDER = "com.example.action.AZKAR_REMINDER"
    const val EXTRA_KIND = "reminder_kind"
    const val MORNING = "morning"
    const val EVENING = "evening"

    private fun requestCode(kind: String) = if (kind == MORNING) 7101 else 7102

    fun schedule(context: Context, kind: String, hour: Int, minute: Int) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AzkarReminderReceiver::class.java).apply {
            action = ACTION_REMINDER
            putExtra(EXTRA_KIND, kind)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode(kind), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val next = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour.coerceIn(0, 23))
            set(Calendar.MINUTE, minute.coerceIn(0, 59))
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) add(Calendar.DAY_OF_YEAR, 1)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, next.timeInMillis, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, next.timeInMillis, pendingIntent)
        }
    }

    fun cancel(context: Context, kind: String) {
        val intent = Intent(context, AzkarReminderReceiver::class.java).apply {
            action = ACTION_REMINDER
            putExtra(EXTRA_KIND, kind)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context, requestCode(kind), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}
