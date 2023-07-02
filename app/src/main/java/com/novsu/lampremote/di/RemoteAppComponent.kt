package com.novsu.lampremote.di

import android.app.Application
import com.novsu.core_ble_communication.di.BluetoothCommunicationDependencies
import com.novsu.core_ble_communication.di.BluetoothCommunicationDependenciesStore
import com.novsu.core_local_storage_impl.di.LocalStorageComponentDependencies
import com.novsu.core_local_storage_impl.di.LocalStorageComponentDependenciesStore
import com.novsu.feature_device_screen.di.DeviceScreenComponentDependencies
import com.novsu.feature_device_screen.di.DeviceScreenDependenciesStore
import com.novsu.feature_main_screen.di.MainScreenDependencies
import com.novsu.feature_main_screen.di.MainScreenDependenciesStore
import com.novsu.lampremote.MainActivity
import com.novsu.lampremote.TonicReceiver
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface RemoteAppComponent:
    MainScreenDependencies,
    BluetoothCommunicationDependencies,
    DeviceScreenComponentDependencies,
    LocalStorageComponentDependencies {

    fun inject(mainActivity: MainActivity)
    fun inject(mainActivity: TonicReceiver)

    @Component.Builder
    interface AppBuilder{
        @BindsInstance
        fun application(application: Application): AppBuilder
        fun build(): RemoteAppComponent
    }

    companion object{
        private var component: RemoteAppComponent? = null

        fun init(app: Application){
            component = DaggerRemoteAppComponent.builder().application(app).build()
        }

        fun get(): RemoteAppComponent{
            return checkNotNull(component)
        }

        fun provideDependencies(){
            MainScreenDependenciesStore.dependencies = component!!
            DeviceScreenDependenciesStore.dependencies = component!!
            BluetoothCommunicationDependenciesStore.dependencies = component!!
            LocalStorageComponentDependenciesStore.dependencies = component!!
        }
    }
}