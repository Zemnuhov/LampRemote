package com.novsu.core_ble_communication.di

import android.content.Context
import com.novsu.core_ble_communication.BleDataController
import com.novsu.core_ble_communication.LampBluetoothManager
import dagger.Module
import dagger.Provides

@Module
class BluetoothCommunicationModule {

    @Provides
    @BluetoothCommunicationScope
    fun provideBleDataController(context: Context): BleDataController{
        return BleDataController(context)
    }

}