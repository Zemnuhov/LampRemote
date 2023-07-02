package com.novsu.core_local_storage_impl.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.novsu.core_local_storage_impl.entry.LampInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LampInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLamp(vararg device: LampInfoEntity)

    @Delete
    fun deleteLamp(vararg device: LampInfoEntity)

    @Query("SELECT mac FROM LampInfoEntity")
    fun getMac(): String?

    @Query("SELECT * FROM LampInfoEntity")
    fun getLampInfo(): Flow<LampInfoEntity?>
}