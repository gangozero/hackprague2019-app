package com.gangozero.prague.app.bledevice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.gangozero.prague.app.core.NotificationUtils

class BleDeviceService : Service() {

    override fun onCreate() {
        super.onCreate()

        NotificationUtils.startForegroundNotification(
                "Prague2019",
                "BLE Device Service",
                1000,
                this
        )

        BleConnection(this).init()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}