package com.novsu.core_ble_communication

import android.bluetooth.BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE
import android.content.Context
import com.novsu.core_ble_communication_api.model.BleColor
import com.novsu.core_ble_communication_api.model.LedMode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import no.nordicsemi.android.ble.ktx.asFlow
import no.nordicsemi.android.ble.ktx.stateAsFlow
import no.nordicsemi.android.ble.ktx.suspend

class BleDataController(context: Context) : LampBluetoothManager(context) {

    val modeFlow: MutableSharedFlow<LedMode> = MutableSharedFlow()
    private val scope = CoroutineScope(Dispatchers.IO)

    init {
        scope.launch {
            stateAsFlow().collect {
                if (it.isReady) {
                    observe()
                }
            }
        }
    }

    private suspend fun observe() {
        try {
            enableNotifications(modeCharacteristic).suspend()
        } catch (e: Exception) {
            errorFlow.value = e
        }
        scope.launch {
            launch {
                try {
                    setNotificationCallback(modeCharacteristic).asFlow().collect { data ->
                        val bytes = data.value
                        bytes?.let { modeFlow.emit(LedMode.valueOf(String(it))) }
                    }
                } catch (e: Exception) {
                    errorFlow.value = e
                }

            }
        }
    }


    suspend fun writeMode(
        mode: LedMode,
        speed: Int = 1000,
        c1: BleColor? = null,
        c2: BleColor? = null,
        c3: BleColor? = null,
        c4: BleColor? = null,
    ) {
        var str = "m:${mode.name},s:${speed}"
        if(c1 != null && c2 != null && c3 != null && c4 != null){
            str +=",c1:${c1.R}-${c1.G}-${c1.B},c2:${c2.R}-${c2.G}-${c2.B},c3:${c3.R}-${c3.G}-${c3.B},c4:${c4.R}-${c4.G}-${c4.B}"
        }
        try {
            writeCharacteristic(
                modeCharacteristic,
                str.toByteArray(),
                WRITE_TYPE_NO_RESPONSE
            ).suspend()
        } catch (e: Exception) {
            errorFlow.value = e
        }

    }

    suspend fun writeTonic(value: Int) {
        try {
            writeCharacteristic(
                tonicCharacteristic,
                value.toString().toByteArray(),
                WRITE_TYPE_NO_RESPONSE
            ).suspend()
        } catch (e: Exception) {
            errorFlow.value = e
        }
    }

    suspend fun writeColor(color: BleColor) {
        try {
            val colorStr = "${color.R}-${color.G}-${color.B}"
            writeCharacteristic(
                colorCharacteristic,
                colorStr.toByteArray(),
                WRITE_TYPE_NO_RESPONSE
            ).suspend()
        } catch (e: Exception) {
            errorFlow.value = e
        }
    }


}