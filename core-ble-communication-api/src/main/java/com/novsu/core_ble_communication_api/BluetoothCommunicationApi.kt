package com.novsu.core_ble_communication_api

import com.novsu.core_ble_communication_api.model.BleColor
import com.novsu.core_ble_communication_api.model.ConnectionState
import com.novsu.core_ble_communication_api.model.LedMode
import com.novsu.core_ble_communication_api.model.NetworkSetting
import kotlinx.coroutines.flow.Flow

interface BluetoothCommunicationApi {

    suspend fun connectToDevice(mac: String)

    fun getConnectionStateFlow(): Flow<ConnectionState>
    fun getModeFlow(): Flow<LedMode>

    suspend fun setColor(color: BleColor)
    suspend fun setMode(mode: LedMode)
    suspend fun setTonic(tonic: Int)
}