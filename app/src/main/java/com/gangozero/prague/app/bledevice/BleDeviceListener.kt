package com.gangozero.prague.app.bledevice

interface BleDeviceListener {
    fun onConnected()
    fun onDisconnected()
    fun onScanning()
    fun onRestartDeviceRequired()
    fun onLike(like: Boolean)
}