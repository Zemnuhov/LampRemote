package com.novsu.frature_auth_screen

import com.novsu.core_firebase_auth_api.FirebaseAuthApi
import com.novsu.core_local_storage_api.LocalStorageApi
import com.novsu.navigation_api.NavigationApi
import dagger.Component
import dagger.Component.Builder
import javax.inject.Provider
import kotlin.properties.Delegates.notNull

@Component(dependencies = [AuthScreenDependencies::class])
internal interface AuthScreenComponent {
    fun inject(authScreen: AuthScreen)

    @Builder
    interface AuthScreenComponentBuilder{
        fun provideDependencies(dependencies: AuthScreenDependencies): AuthScreenComponentBuilder
        fun build(): AuthScreenComponent
    }

    companion object{
        var component: AuthScreenComponent? = null

        fun get(): AuthScreenComponent {
            if(component == null){
                component = DaggerAuthScreenComponent
                    .builder()
                    .provideDependencies(AuthScreenDependenciesProvider.dependencies)
                    .build()
            }
            return component!!
        }
    }
}

interface AuthScreenDependencies{
    val firebaseAuthApi: FirebaseAuthApi
    val navigationApi: NavigationApi
    val localStorageApi: LocalStorageApi
}

interface AuthScreenDependenciesProvider{
    val dependencies: AuthScreenDependencies
    companion object: AuthScreenDependenciesProvider by AuthScreenDependenciesStore
}

object AuthScreenDependenciesStore: AuthScreenDependenciesProvider{
    override var dependencies: AuthScreenDependencies by notNull()
}
