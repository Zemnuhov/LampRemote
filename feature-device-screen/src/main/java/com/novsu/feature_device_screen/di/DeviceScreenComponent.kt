package com.novsu.feature_device_screen.di

import com.neurotech.core_ble_device_scan_api.BluetoothScanAPI
import com.novsu.core_ble_communication_api.BluetoothCommunicationApi
import com.novsu.core_local_storage_api.LocalStorageApi
import com.novsu.feature_device_screen.DeviceScreen
import dagger.Component
import dagger.Component.Builder
import javax.inject.Scope
import kotlin.properties.Delegates.notNull

@Component(dependencies = [DeviceScreenComponentDependencies::class])
@DeviceScreenScope
interface DeviceScreenComponent {
    fun inject(deviceScreen: DeviceScreen)

    @Builder
    interface ComponentBuilder{
        fun provideDependencies(dependencies: DeviceScreenComponentDependencies):ComponentBuilder
        fun build():DeviceScreenComponent
    }

    companion object{
        private var component: DeviceScreenComponent? = null

        fun get(): DeviceScreenComponent{
            if(component == null){
                component = DaggerDeviceScreenComponent
                    .builder()
                    .provideDependencies(DeviceScreenDependenciesProvider.dependencies)
                    .build()
            }
            return component!!
        }
    }

}

interface DeviceScreenComponentDependencies{
    val bluetoothScanAPI: BluetoothScanAPI
    val bluetoothCommunicationApi: BluetoothCommunicationApi
    val localStorageApi: LocalStorageApi
}

interface DeviceScreenDependenciesProvider{
    val dependencies: DeviceScreenComponentDependencies
    companion object: DeviceScreenDependenciesProvider by DeviceScreenDependenciesStore
}

object DeviceScreenDependenciesStore: DeviceScreenDependenciesProvider{
    override var dependencies: DeviceScreenComponentDependencies by notNull()
}

@Scope
annotation class DeviceScreenScope


