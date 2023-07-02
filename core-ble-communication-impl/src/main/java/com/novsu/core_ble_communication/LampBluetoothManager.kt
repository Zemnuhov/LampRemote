package com.novsu.core_ble_communication

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.Context
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.BleManager
import no.nordicsemi.android.ble.ktx.state
import no.nordicsemi.android.ble.ktx.suspend

open class LampBluetoothManager(context: Context) : BleManager(context) {

    private var paramsService: BluetoothGattService? = null
    protected var modeCharacteristic: BluetoothGattCharacteristic? = null
    protected var tonicCharacteristic: BluetoothGattCharacteristic? = null

    private var colorService: BluetoothGattService? = null
    protected var colorCharacteristic: BluetoothGattCharacteristic? = null


    protected val errorFlow = MutableStateFlow(Exception())

    init {
        CoroutineScope(Dispatchers.IO).launch{
            errorFlow.collect{
                Log.e("BluetoothException",it.message.toString())
                if(it.message != null && !state.isReady){

                }
            }
        }

    }

    private fun initParamsService(gatt: BluetoothGatt): Boolean{
        try {
            paramsService = gatt.getService(LampUUID.paramsServiceUUID)
            modeCharacteristic = paramsService?.getCharacteristic(LampUUID.modeCharacteristicUUID)
            tonicCharacteristic = paramsService?.getCharacteristic(LampUUID.tonicCharacteristicUUID)
        }catch (e: Exception){
            errorFlow.value = e
        }
        return modeCharacteristic != null
    }

    private fun initColorService(gatt: BluetoothGatt): Boolean{
        try {
            colorService = gatt.getService(LampUUID.colorServiceUUID)
            colorCharacteristic = colorService?.getCharacteristic(LampUUID.colorRCharacteristicUUID)
        }catch (e: Exception){
            errorFlow.value = e
        }
        return colorCharacteristic != null
    }


    override fun isRequiredServiceSupported(gatt: BluetoothGatt): Boolean {
       return initColorService(gatt) && initParamsService(gatt)
    }

    override fun initialize() {
        try {
            requestMtu(512).enqueue()
        } catch (e: Exception) {
            errorFlow.value = e
        }
    }

    override fun onServicesInvalidated() {
        colorService = null
        colorCharacteristic = null
        paramsService = null
        modeCharacteristic = null
        tonicCharacteristic = null
    }

    suspend fun connectToDevice(device: BluetoothDevice) {
        try {
            connect(device)
                .retry(4, 300)
                .useAutoConnect(true)
                .timeout(15000)
                .suspend()
        }catch (e:Exception){
            errorFlow.value = e
        }

    }
}