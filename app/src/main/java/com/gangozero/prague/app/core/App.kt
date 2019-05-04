package com.gangozero.prague.app.core

import android.app.Application
import android.content.Intent
import com.gangozero.prague.app.bledevice.BleDeviceService

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startForegroundService(Intent(this, BleDeviceService::class.java))
    }
}