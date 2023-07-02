package com.novsu.feature_main_screen.di

import com.novsu.core_ble_communication_api.BluetoothCommunicationApi
import com.novsu.feature_main_screen.MainScreen
import com.novsu.navigation_api.NavigationApi
import dagger.Component
import dagger.Component.Builder
import kotlin.properties.Delegates.notNull

@Component(dependencies = [MainScreenDependencies::class])
interface MainScreenComponent {
    fun inject(mainScreen: MainScreen)

    @Builder
    interface MainScreenComponentBuilder{
        fun provideDependencies(dependencies: MainScreenDependencies):MainScreenComponentBuilder
        fun build():MainScreenComponent
    }

    companion object{
        private var component: MainScreenComponent? = null

        fun get(): MainScreenComponent{
            if (component == null){
                component = DaggerMainScreenComponent
                    .builder()
                    .provideDependencies(MainScreenDependenciesProvider.dependencies)
                    .build()
            }
            return component!!
        }
    }

}

interface MainScreenDependencies{
    val bluetoothCommunicationApi: BluetoothCommunicationApi
    val navigationApi: NavigationApi
}

interface MainScreenDependenciesProvider{
    val dependencies: MainScreenDependencies
    companion object: MainScreenDependenciesProvider by MainScreenDependenciesStore
}

object MainScreenDependenciesStore: MainScreenDependenciesProvider{
    override var dependencies: MainScreenDependencies by notNull()
}