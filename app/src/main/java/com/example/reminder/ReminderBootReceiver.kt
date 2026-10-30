package com.example.reminder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ReminderBootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return
        val prefs = context.getSharedPreferences(AzkarReminderScheduler.PREFS, Context.MODE_PRIVATE)
        listOf(AzkarReminderScheduler.MORNING, AzkarReminderScheduler.EVENING).forEach { kind ->
            if (prefs.getBoolean("reminder_${kind}_enabled", false)) {
                val defaultHour = if (kind == AzkarReminderScheduler.MORNING) 7 else 18
                val hour = prefs.getInt("reminder_${kind}_hour", defaultHour)
                val minute = prefs.getInt("reminder_${kind}_minute", 0)
                AzkarReminderScheduler.schedule(context, kind, hour, minute)
            }
        }
    }
}
