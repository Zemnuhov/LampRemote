package com.novsu.core_local_storage_impl.di

import android.content.Context
import com.novsu.core_local_storage_impl.LocalStorage
import dagger.Component
import kotlin.properties.Delegates.notNull
import dagger.Component.Builder
import javax.inject.Scope

@Component(dependencies = [LocalStorageComponentDependencies::class], modules = [LocalStorageModule::class])
@LocalStorageScope
interface LocalStorageComponent {
    fun inject(localStorage: LocalStorage)

    @Builder
    interface ComponentBuilder{
        fun provideDependencies(dependencies: LocalStorageComponentDependencies): ComponentBuilder
        fun build(): LocalStorageComponent
    }

    companion object{
        private var component: LocalStorageComponent? = null

        fun get(): LocalStorageComponent{
            if(component == null){
                component = DaggerLocalStorageComponent
                    .builder()
                    .provideDependencies(LocalStorageComponentDependenciesProvider.dependencies)
                    .build()
            }
            return component!!
        }
    }
}

interface LocalStorageComponentDependencies{
    val context: Context
}

interface LocalStorageComponentDependenciesProvider{
    val dependencies: LocalStorageComponentDependencies
    companion object: LocalStorageComponentDependenciesProvider by LocalStorageComponentDependenciesStore
}

object LocalStorageComponentDependenciesStore: LocalStorageComponentDependenciesProvider{
    override var dependencies: LocalStorageComponentDependencies by notNull()
}

@Scope
annotation class LocalStorageScope