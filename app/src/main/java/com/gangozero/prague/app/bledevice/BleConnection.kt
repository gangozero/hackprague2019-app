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

    fun init() {
        startScan()
    }

    fun startScan() {
        Log.d("gatt", "start scan...")
        callback = Callback(context, this)
        BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner.startScan(callback)
    }

    fun onDisconnected() {
        if (callback != null) {
            BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner.stopScan(callback)
        }
        startScan()
    }

    fun submit(like: Boolean) {

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


}