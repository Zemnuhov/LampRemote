package com.novsu.core_local_storage_impl

import com.novsu.core_local_storage_api.LampInfo
import com.novsu.core_local_storage_api.LocalStorageApi
import com.novsu.core_local_storage_impl.dao.LampInfoDao
import com.novsu.core_local_storage_impl.di.LocalStorageComponent
import com.novsu.core_local_storage_impl.entry.LampInfoEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocalStorage: LocalStorageApi {

    @Inject
    lateinit var database: LampRemoteDatabase

    private val lampInfoDao: LampInfoDao

    init {
        LocalStorageComponent.get().inject(this)
        lampInfoDao = database.lampInfoDao()
    }

    override suspend fun saveLampInfo(lampInfo: LampInfo) {
        withContext(Dispatchers.IO){
            lampInfoDao.insertLamp(LampInfoEntity(lampInfo.mac, lampInfo.name))
        }
    }

    override fun getLampInfoFlow(): Flow<LampInfo?> {
        return lampInfoDao.getLampInfo().map { if (it != null) LampInfo(it.name, it.mac) else null }
    }
}