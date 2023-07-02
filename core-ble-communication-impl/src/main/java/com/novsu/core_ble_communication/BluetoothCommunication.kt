package com.novsu.core_ble_communication

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import androidx.core.content.ContextCompat
import com.novsu.core_ble_communication.di.BluetoothCommunicationComponent
import com.novsu.core_ble_communication_api.BluetoothCommunicationApi
import com.novsu.core_ble_communication_api.model.BleColor
import com.novsu.core_ble_communication_api.model.ConnectionState
import com.novsu.core_ble_communication_api.model.LedMode
import com.novsu.core_ble_communication_api.model.NetworkSetting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import no.nordicsemi.android.ble.ktx.stateAsFlow
import javax.inject.Inject

class BluetoothCommunication : BluetoothCommunicationApi {

    @Inject
    lateinit var bleController: BleDataController

    @Inject
    lateinit var context: Context

    init {
        BluetoothCommunicationComponent.get().inject(this)
    }

    override suspend fun connectToDevice(mac: String) {
        val bluetoothManager: BluetoothManager? =
            ContextCompat.getSystemService(context, BluetoothManager::class.java)
        val bluetoothAdapter: BluetoothAdapter? = bluetoothManager?.adapter
        if(bluetoothAdapter != null){
            bleController.connectToDevice(bluetoothAdapter.getRemoteDevice(mac))
        }
    }

    override fun getConnectionStateFlow(): Flow<ConnectionState> {
        return flow {
            bleController.stateAsFlow().collect{
                when{
                    it.isReady -> emit(ConnectionState.CONNECTED)
                    it.isConnected -> emit(ConnectionState.CONNECTING)
                    else -> emit(ConnectionState.DISCONNECTED)
                }
            }
        }
    }

    override fun getModeFlow(): Flow<LedMode> {
        return bleController.modeFlow
    }

    override suspend fun setColor(color: BleColor) {
        bleController.writeColor(color)
    }

    override suspend fun setMode(mode: LedMode) {
        bleController.writeMode(mode)
    }

    override suspend fun setTonic(tonic: Int) {
        bleController.writeTonic(tonic)
    }

}