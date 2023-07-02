package com.novsu.lampremote.di

import android.app.Application
import android.content.Context
import com.neurotech.core_ble_device_scan.impl.BluetoothScan
import com.neurotech.core_ble_device_scan_api.BluetoothScanAPI
import com.novsu.core_ble_communication.BluetoothCommunication
import com.novsu.core_ble_communication_api.BluetoothCommunicationApi
import com.novsu.core_local_storage_api.LocalStorageApi
import com.novsu.core_local_storage_impl.LocalStorage
import com.novsu.feature_device_screen.DeviceScreen
import com.novsu.feature_main_screen.MainScreen
import com.novsu.navigation.LampRemoteNavigation
import com.novsu.navigation_api.NavigationApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideContext(app: Application): Context {
        return app.applicationContext
    }

    @Provides
    @Singleton
    fun provideBluetoothScanner(): BluetoothScanAPI{
        return BluetoothScan()
    }


    @Provides
    @Singleton
    fun provideMainScreen(): MainScreen {
        return MainScreen()
    }

    @Provides
    @Singleton
    fun provideDeviceScreen(): DeviceScreen {
        return DeviceScreen()
    }

    @Provides
    @Singleton
    fun provideAppNavigation(): NavigationApi{
        return LampRemoteNavigation()
    }

    @Provides
    @Singleton
    fun provideLocalStorage(): LocalStorageApi {
        return LocalStorage()
    }

    @Provides
    @Singleton
    fun provideBluetoothCommunication(): BluetoothCommunicationApi {
        return BluetoothCommunication()
    }
}