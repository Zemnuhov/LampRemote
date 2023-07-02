package com.novsu.core_local_storage_impl

import androidx.room.Database
import androidx.room.RoomDatabase
import com.novsu.core_local_storage_impl.dao.LampInfoDao
import com.novsu.core_local_storage_impl.entry.LampInfoEntity

@Database(
    entities = [
        LampInfoEntity::class,
    ],
    version = 1
)
abstract class LampRemoteDatabase: RoomDatabase() {
    abstract fun lampInfoDao(): LampInfoDao
}