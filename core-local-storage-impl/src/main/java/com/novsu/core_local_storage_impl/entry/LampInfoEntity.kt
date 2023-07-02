package com.novsu.core_local_storage_impl.entry

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.novsu.core_local_storage_api.LampInfo

@Entity
data class LampInfoEntity (
    @PrimaryKey
    @ColumnInfo(name = "mac")
    val mac: String,
    @ColumnInfo(name = "name")
    val name: String?,
){
    fun toLampInfo(): LampInfo{
        return LampInfo(
            name, mac
        )
    }
}