package com.gangozero.prague.app.core.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.android.gms.location.*

class LocationManager(
        private val context: Context
) {

    var locClient: FusedLocationProviderClient? = null
    var lastLocation: Location? = null
    var listener: ((loc: Location) -> Unit)? = null

    fun init() {

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        if (locClient != null) {
            return
        }

        initLocationRequest()
    }

    fun addListener(listener: (loc: Location) -> Unit) {
        this.listener = listener
    }

    fun removeListener() {
        this.listener = null
    }

    @SuppressLint("MissingPermission")
    private fun initLocationRequest() {

        locClient = LocationServices.getFusedLocationProviderClient(context)
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Update UI with location data
                    // ...
                }
                if (locationResult.locations.isNotEmpty()) {
                    lastLocation = locationResult.locations[0]
                    Log.d("loc", locationResult.locations[0].toString())

                    listener?.invoke(lastLocation!!)
                }
            }
        }
        val locationRequest = LocationRequest()
        locationRequest.interval = 1000
        locClient!!.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
}