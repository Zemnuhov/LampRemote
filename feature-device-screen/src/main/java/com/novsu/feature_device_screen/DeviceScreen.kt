package com.novsu.feature_device_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.neurotech.core_ble_device_scan_api.Devices
import com.novsu.core_ble_communication_api.model.ConnectionState
import com.novsu.feature_device_screen.di.DeviceScreenComponent
import dagger.Lazy
import javax.inject.Inject

class DeviceScreen {

    @Inject
    lateinit var factory: Lazy<DeviceScreenViewModel.Factory>
    private lateinit var viewModel: DeviceScreenViewModel

    init {
        DeviceScreenComponent.get().inject(this)
    }

    @Composable
    fun DrawHeader() {
        val connectionState =
            viewModel.getConnectionState().collectAsState(initial = ConnectionState.DISCONNECTED)
        val savedDevice = viewModel.deviceInfo.collectAsState(initial = null)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            shadowElevation = 10.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            if (savedDevice.value != null) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Image(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Тут будет фотка лампы",
                        modifier = Modifier
                            .padding(2.dp)
                    )
                    Text(text = if (connectionState.value == ConnectionState.CONNECTED) "Вы соеденены с устройством" else "Нет соединения с устройством")

                    Button(onClick = { viewModel.connectToDevice(savedDevice.value!!.name ,savedDevice.value!!.mac) }) {
                        Image(
                            painter =
                            if (connectionState.value == ConnectionState.CONNECTED)
                                painterResource(id = R.drawable.round_bluetooth_24)
                            else
                                painterResource(id = R.drawable.round_bluetooth_disabled_24),
                            contentDescription = "Connection"
                        )
                    }

                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "У Вас нет подключенных ламп",
                        Modifier
                            .align(Alignment.Center)
                    )
                    Image(
                        imageVector = Icons.Default.Refresh, contentDescription = "Поиск устройств",
                        Modifier
                            .align(Alignment.CenterEnd)
                            .padding(16.dp)
                            .clickable { viewModel.startScan() }
                    )

                }

            }
        }
    }

    @Composable
    fun DrawScanScreen() {
        val devices = viewModel.deviceScanList.collectAsState(initial = Devices(listOf()))
        val scanState = viewModel.scanState.collectAsState(initial = false)

        SwipeRefresh(
            state = rememberSwipeRefreshState(scanState.value),
            onRefresh = { viewModel.startScan() },
            modifier = Modifier.fillMaxSize()
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(devices.value.list) {
                    DrawItemScan(name = it.name, mac = it.mac)
                }
            }
        }


    }

    @Composable
    fun DrawItemScan(name: String, mac: String) {
        Surface(
            shadowElevation = 10.dp, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
                .clickable {
                    viewModel.connectToDevice(name, mac)
                },
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Image",
                    modifier = Modifier
                        .fillMaxWidth(0.2f)
                        .align(Alignment.CenterVertically)
                        .fillMaxHeight()
                )
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Row() {
                        Text(text = "Имя:", modifier = Modifier.padding(16.dp))
                        Text(text = name, modifier = Modifier.padding(16.dp))
                    }
                    Row() {
                        Text(text = "MAC:", modifier = Modifier.padding(16.dp))
                        Text(text = mac, modifier = Modifier.padding(16.dp))
                    }
                }
            }
        }
    }

    @Composable
    fun DrawDeviceInfo() {
        val deviceSetting = viewModel.deviceInfo.collectAsState(initial = null)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(text = "Об устройстве", fontSize = 32.sp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "MAC:", modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(0.25F), fontWeight = FontWeight(600)
                )
                Text(
                    text = deviceSetting.value?.mac ?: "", modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }

    @Composable
    fun Draw() {
        viewModel = viewModel(factory = factory.get())
        val savedDevice = viewModel.deviceInfo.collectAsState(initial = null)
        Column() {
            DrawHeader()
            Surface(
                shadowElevation = 10.dp, modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                if (savedDevice.value == null) {
                    DrawScanScreen()
                } else {
                    DrawDeviceInfo()
                }
            }

        }
    }

    @Preview
    @Composable
    fun Prev() {
        Draw()
    }

}