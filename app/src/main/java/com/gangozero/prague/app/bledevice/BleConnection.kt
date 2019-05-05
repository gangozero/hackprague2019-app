package com.gangozero.prague.app.bledevice

import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.util.Log
import com.gangozero.prague.app.core.App
import com.gangozero.prague.app.core.Schedulers
import io.reactivex.Completable

class BleConnection(
        private val context: Context
) {

    var callback: Callback? = null
    var state = State.Scanning
    val app: App = context.applicationContext as App

    fun init() {
        startScan()
    }

    private fun startScan() {

        state = State.Scanning

        app.bleDeviceListener?.onScanning()

        Log.d("gatt", "start scan...")
        callback = Callback(context, this)
        BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner.startScan(callback)
    }

    fun onRestartRequired() {
        state = State.RestartRequired
        app.bleDeviceListener?.onRestartDeviceRequired()
    }

    fun onConnected() {
        state = State.Connected
        app.bleDeviceListener?.onConnected()
    }

    fun onDisconnected() {
        state = State.Disconnected
        app.bleDeviceListener?.onDisconnected()
        if (callback != null) {
            BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner.stopScan(callback)
        }
        startScan()
    }

    fun submit(like: Boolean) {

        app.bleDeviceListener?.onLike(like)

        val createSubmitGrade = (context.applicationContext as App).createSubmitGrade()
        val schedulers = Schedulers()

        Completable.fromAction { createSubmitGrade.get(like) }
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe({

                }, {
                    it.printStackTrace()
                })
    }

    enum class State {
        Scanning, Connected, Disconnected, RestartRequired
    }


}