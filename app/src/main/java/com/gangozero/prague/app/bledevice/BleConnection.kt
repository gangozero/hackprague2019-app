package com.gangozero.prague.app.bledevice

import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import java.util.*

class BleConnection(val context: Context) {

    val BUTTON_SERVICE = UUID.fromString("e95d9882-251d-470a-a062-fa1922dfa9a8")
    val BUTTON_SERVICE_CHAR_A = UUID.fromString("e95dda90-251d-470a-a062-fa1922dfa9a8")
    val BUTTON_SERVICE_CHAR_B = UUID.fromString("e95dda91-251d-470a-a062-fa1922dfa9a8")

    val CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID = convertFromInteger(0x2902)

    var connected = false
    var subToA = false
    var subToB = false
    var callback: ScanCallback? = null

    fun init(){
        startBluetooth()
        callback = newCallback()
        BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner.startScan(callback)
    }

    private fun startBluetooth() {
        connected = false
        subToA = false
        subToB = false
    }

    private fun newCallback(): ScanCallback {
        return object : ScanCallback() {

            override fun onScanFailed(errorCode: Int) {

            }

            override fun onScanResult(callbackType: Int, result: ScanResult) {

                if (!connected && result.device.address == "D8:D1:A4:56:5B:D0") {
                    connected = true

                    Log.d("gatt", "found device: $result")
                    connect(result.device)
                }
            }

            override fun onBatchScanResults(results: MutableList<ScanResult>?) {

            }
        }
    }

    private fun connect(it: BluetoothDevice) {

        it.connectGatt(context, true, object : BluetoothGattCallback() {
            override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {


                if (newState == BluetoothProfile.STATE_CONNECTED) {

                    Log.d("gatt", "gatt-connected")

                    gatt!!.discoverServices()

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    Log.d("gatt", "gatt-disconnected")
                    startBluetooth()
                } else if (newState == BluetoothProfile.STATE_CONNECTING) {
                    Log.d("gatt", "gatt-connectnig...")
                } else if (newState == BluetoothProfile.STATE_DISCONNECTING) {
                    Log.d("gatt", "gatt-disconnecting...")
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {

                gatt.services.forEach { service ->
                    if (service.uuid == BUTTON_SERVICE && !subToA) {
                        subToA = true

                        Log.d("gatt", "sub to A")
//                        Log.d("gatt", "onServicesDiscovered found service: $status")
                        val setCharResultA = setCharacteristicNotification(BUTTON_SERVICE, BUTTON_SERVICE_CHAR_A, gatt, true)
//                        Log.d("gatt", "set char result - a: $setCharResultA")
                    }
                }
            }

            override fun onDescriptorWrite(gatt: BluetoothGatt, descriptor: BluetoothGattDescriptor?, status: Int) {

                Log.d("gatt", "onDescriptorWrite: $status")

                if (!subToB) {
                    subToB = true
                    Log.d("gatt", "sub to B")
                    val setCharResultB = setCharacteristicNotification(BUTTON_SERVICE, BUTTON_SERVICE_CHAR_B, gatt, true)
                    Log.d("gatt", "set char result - b: $setCharResultB")
                }
            }

            override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
//                Log.d("gatt", "on char changed")

                val b: Byte = 1

                if (characteristic != null && characteristic.value.size == 1 && characteristic.value[0] == b) {
                    val uuid = characteristic.uuid

                    if (uuid == BUTTON_SERVICE_CHAR_A) {
                        Log.d("gatt", "dislike")
                    } else if (uuid == BUTTON_SERVICE_CHAR_B) {
                        Log.d("gatt", "like")
                    }
                }
            }

            // Result of a characteristic read operation
            override fun onCharacteristicRead(
                    gatt: BluetoothGatt,
                    characteristic: BluetoothGattCharacteristic,
                    status: Int
            ) {

                Log.d("gatt", "success: onCharacteristicRead: $status")
            }

            override fun onDescriptorRead(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
                Log.d("gatt", "onDescriptorRead: $status")
            }

            override fun onCharacteristicWrite(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?, status: Int) {
                Log.d("gatt", "onCharacteristicWrite: $status")
            }

            override fun onReliableWriteCompleted(gatt: BluetoothGatt?, status: Int) {
                Log.d("gatt", "onReliableWriteCompleted: $status")
            }
        })
    }

    fun setCharacteristicNotification(service: UUID,
                                      characteristicUuid: UUID,
                                      gatt: BluetoothGatt,
                                      enable: Boolean
    ): Boolean {
        val characteristic = gatt.getService(service).getCharacteristic(characteristicUuid)
        gatt.setCharacteristicNotification(characteristic, enable)
        val descriptor = characteristic.getDescriptor(CHARACTERISTIC_UPDATE_NOTIFICATION_DESCRIPTOR_UUID)
        descriptor.value = if (enable) BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE else byteArrayOf(0x00, 0x00)
        return gatt.writeDescriptor(descriptor)
    }

    fun convertFromInteger(i: Int): UUID {
        val MSB = 0x0000000000001000L
        val LSB = -0x7fffff7fa064cb05L
        val value = (i and -0x1).toLong()
        return UUID(MSB or (value shl 32), LSB)
    }

}