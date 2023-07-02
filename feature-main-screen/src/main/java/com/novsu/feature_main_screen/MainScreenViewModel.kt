package com.novsu.feature_main_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.novsu.core_ble_communication_api.BluetoothCommunicationApi
import com.novsu.core_ble_communication_api.model.BleColor
import com.novsu.core_ble_communication_api.model.LedMode
import com.novsu.navigation_api.NavigationApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Provider

class MainScreenViewModel(
    private val bluetoothCommunication: BluetoothCommunicationApi,
    private val navigation: NavigationApi
): ViewModel() {

    val ledMode get() =  bluetoothCommunication.getModeFlow()

    fun setColor(R: Int, G: Int, B: Int){
        viewModelScope.launch {
            bluetoothCommunication.setColor(BleColor(R,G,B))
        }
    }

    fun setMode(mode: LedMode){
        viewModelScope.launch {
            bluetoothCommunication.setMode(mode)
        }
    }

    @Suppress("UNCHECKED_CAST")
    class Factory @Inject constructor(
        private val bluetoothCommunicationApi: Provider<BluetoothCommunicationApi>,
        private val navigationApi: Provider<NavigationApi>
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainScreenViewModel(
                bluetoothCommunicationApi.get(),
                navigationApi.get()
            ) as T
        }
    }
}