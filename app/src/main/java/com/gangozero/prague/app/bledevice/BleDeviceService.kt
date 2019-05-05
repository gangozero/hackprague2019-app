package com.gangozero.prague.app.bledevice

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.gangozero.prague.app.R
import com.gangozero.prague.app.core.App
import com.gangozero.prague.app.core.NotificationUtils

class BleDeviceService : Service() {

    override fun onCreate() {
        super.onCreate()

        NotificationUtils.startForegroundNotification(
                "Prague2019",
                "BLE Device Service",
                1000,
                R.string.notification_ble_title,
                R.string.notification_ble_message,
                this
        )

        val bleConnection = BleConnection(this)
        (applicationContext as App).bleConnection = bleConnection
        bleConnection.init()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}