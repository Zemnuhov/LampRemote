package com.novsu.feature_device_screen

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.neurotech.core_ble_device_scan_api.BluetoothScanAPI
import com.novsu.core_ble_communication_api.BluetoothCommunicationApi
import com.novsu.core_ble_communication_api.model.ConnectionState
import com.novsu.core_ble_communication_api.model.NetworkSetting
import com.novsu.core_local_storage_api.LampInfo
import com.novsu.core_local_storage_api.LocalStorageApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Provider

class DeviceScreenViewModel(
    private val bluetoothScan: BluetoothScanAPI,
    private val bluetoothCommunication: BluetoothCommunicationApi,
    private val localStorage: LocalStorageApi
) : ViewModel() {

    val deviceInfo = runBlocking(Dispatchers.IO) { localStorage.getLampInfoFlow() }
    val deviceScanList = runBlocking(Dispatchers.IO) { bluetoothScan.getDevicesFlow() }
    val scanState = runBlocking(Dispatchers.IO) { bluetoothScan.getScanState()}

    fun getConnectionState(): Flow<ConnectionState> {
        return bluetoothCommunication.getConnectionStateFlow()
    }

    fun connectToDevice(name:String?, mac: String){
        viewModelScope.launch {
            bluetoothCommunication.connectToDevice(mac)
            bluetoothCommunication.getConnectionStateFlow().collect{
                if(it == ConnectionState.CONNECTED){
                    val localDevice = localStorage.getLampInfoFlow().first()
                    if(localDevice?.mac != mac){
                        localStorage.saveLampInfo(LampInfo(name, mac))
                    }
                }
            }
        }
    }

    fun startScan(){
        viewModelScope.launch {
            bluetoothScan.startScan()
        }
    }


    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val bluetoothScanAPI: Provider<BluetoothScanAPI>,
        private val bluetoothCommunicationApi: Provider<BluetoothCommunicationApi>,
        private val localStorageApi: Provider<LocalStorageApi>
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            require(modelClass == DeviceScreenViewModel::class.java)
            return DeviceScreenViewModel(
                bluetoothScanAPI.get(),
                bluetoothCommunicationApi.get(),
                localStorageApi.get()
            ) as T
        }
    }
}