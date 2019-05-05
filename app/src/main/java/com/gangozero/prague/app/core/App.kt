package com.gangozero.prague.app.core

import android.app.Application
import android.content.Intent
import com.gangozero.prague.app.bledevice.BleConnection
import com.gangozero.prague.app.bledevice.BleDeviceListener
import com.gangozero.prague.app.bledevice.BleDeviceService
import com.gangozero.prague.app.core.location.LocationManager
import com.gangozero.prague.app.core.location.LocationService
import com.gangozero.prague.app.profiles.Profile
import com.gangozero.prague.app.profiles.ProfilesService
import com.gangozero.prague.app.profiles.usecases.SubmitGrade

class App : Application() {

    val profilesService = ProfilesService()
    val locationManager = LocationManager(this)
    var selectedProfile: Profile? = null

    var bleDeviceListener: BleDeviceListener? = null
    var bleConnection: BleConnection? = null

    override fun onCreate() {
        super.onCreate()
        startForegroundService(Intent(this, BleDeviceService::class.java))
        startForegroundService(Intent(this, LocationService::class.java))
    }

    fun createSubmitGrade(): SubmitGrade {
        return SubmitGrade(profilesService, locationManager) { selectedProfile }
    }


}