package com.novsu.core_local_storage_api

import kotlinx.coroutines.flow.Flow

interface LocalStorageApi {
    suspend fun saveLampInfo(lampInfo: LampInfo)
    fun getLampInfoFlow(): Flow<LampInfo?>
}