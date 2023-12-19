package com.ppb.travelanywhere.services.notification

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.ppb.travelanywhere.R
import com.ppb.travelanywhere.TravelAnywhereApps

class NotifReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val title = intent?.getStringExtra("TITLE")
        val msg = intent?.getStringExtra("MESSAGE")
        val app = context?.applicationContext as TravelAnywhereApps
        if (msg != null && title != null) {
            app.showNotification(title, msg)
        }

    }
}