package com.novsu.lampremote

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.novsu.core_ble_communication_api.BluetoothCommunicationApi
import com.novsu.lampremote.di.RemoteAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TonicReceiver: BroadcastReceiver()  {
    @Inject
    lateinit var bluetoothCommunicationApi: BluetoothCommunicationApi

    init {
        RemoteAppComponent.get().inject(this)
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = intent.getIntExtra("value", 0)
        CoroutineScope(Dispatchers.IO).launch{
            bluetoothCommunicationApi.setTonic(message)
        }
    }
}