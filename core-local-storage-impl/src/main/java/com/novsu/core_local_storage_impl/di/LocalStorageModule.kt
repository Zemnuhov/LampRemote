package com.novsu.core_local_storage_impl.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.novsu.core_local_storage_impl.LampRemoteDatabase
import dagger.Module
import dagger.Provides


@Module
class LocalStorageModule {

    @Provides
    @LocalStorageScope
    fun provideMainDatabase(context: Context): LampRemoteDatabase{
        return Room.databaseBuilder(
            context,
            LampRemoteDatabase::class.java,
            name = "lamp_remote_database"
        ).build()
    }
}