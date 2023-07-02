package com.novsu.core_ble_communication.di

import android.content.Context
import com.novsu.core_ble_communication.BluetoothCommunication
import dagger.Component
import dagger.Component.Builder
import javax.inject.Scope
import kotlin.properties.Delegates.notNull

@Component(dependencies = [BluetoothCommunicationDependencies::class], modules = [BluetoothCommunicationModule::class])
@BluetoothCommunicationScope
interface BluetoothCommunicationComponent {
    fun inject(bluetoothCommunication: BluetoothCommunication)

    @Builder
    interface ComponentBuilder{
        fun provideDependencies(dependencies: BluetoothCommunicationDependencies): ComponentBuilder
        fun build(): BluetoothCommunicationComponent
    }

    companion object{
        var component: BluetoothCommunicationComponent? = null

        fun get(): BluetoothCommunicationComponent{
            if(component == null){
                component = DaggerBluetoothCommunicationComponent
                    .builder()
                    .provideDependencies(BluetoothCommunicationDependenciesProvider.dependencies)
                    .build()
            }
            return component!!
        }
    }
}


interface BluetoothCommunicationDependencies{
    val context: Context
}

interface BluetoothCommunicationDependenciesProvider{
    val dependencies: BluetoothCommunicationDependencies
    companion object: BluetoothCommunicationDependenciesProvider by BluetoothCommunicationDependenciesStore
}

object BluetoothCommunicationDependenciesStore: BluetoothCommunicationDependenciesProvider{
    override var dependencies: BluetoothCommunicationDependencies by notNull()
}

@Scope
annotation class BluetoothCommunicationScope
