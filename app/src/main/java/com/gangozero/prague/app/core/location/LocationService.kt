package com.gangozero.prague.app.core.location

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.gangozero.prague.app.R
import com.gangozero.prague.app.core.App
import com.gangozero.prague.app.core.NotificationUtils

class LocationService : Service() {

    override fun onCreate() {
        super.onCreate()

        NotificationUtils.startForegroundNotification(
                "loc-service",
                "Location-channel",
                2000,
                R.string.notification_loc_title,
                R.string.notification_loc_message,
                this
        )

        (applicationContext as App).locationManager.init()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}