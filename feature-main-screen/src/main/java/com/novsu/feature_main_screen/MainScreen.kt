package com.novsu.feature_main_screen

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import com.novsu.core_ble_communication_api.model.LedMode
import com.novsu.feature_main_screen.di.MainScreenComponent
import dagger.Lazy
import javax.inject.Inject
import kotlin.math.roundToInt

class MainScreen {

    @Inject
    lateinit var factory: Lazy<MainScreenViewModel.Factory>

    private lateinit var currentMode: MutableState<LedMode?>
    private lateinit var viewModel: MainScreenViewModel
    private lateinit var gradientColors: MutableState<List<GradientColor>>

    init {
        MainScreenComponent.get().inject(this)
    }



    @Composable
    fun Draw() {
        currentMode = remember {
            mutableStateOf(LedMode.FILL)
        }
        gradientColors = remember {
            mutableStateOf(listOf(
                GradientColor(mutableStateOf(true), mutableStateOf(Color.Cyan)),
                GradientColor(mutableStateOf(false), mutableStateOf(Color.Red)),
                GradientColor(mutableStateOf(false), mutableStateOf(Color.Yellow)),
                GradientColor(mutableStateOf(false), mutableStateOf(Color.Cyan))
            ))
        }
        viewModel = viewModel(factory = factory.get())
        Column() {
            Surface(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxHeight(0.5F), shadowElevation = 10.dp, shape = RoundedCornerShape(16.dp)
            ) {
                when (currentMode.value) {
                    LedMode.FILL -> DrawColorPickers(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(16.dp)
                    )

                    else -> {
                        DrawGradientSetting()
                    }
                }

            }
            Surface(
                modifier = Modifier.padding(16.dp),
                shadowElevation = 10.dp,
                shape = RoundedCornerShape(16.dp)
            ) {
                DrawModeList()
            }
        }

    }

    @Composable
    fun DrawModeList() {
        val checkMap = remember {
            mutableMapOf<LedMode, MutableState<Boolean>>()
        }
        val initMode = viewModel.ledMode.collectAsState(initial = null)
        if (initMode.value != null) {
            checkMap[initMode.value]?.value = true
        }
        Log.e("AAAAAA", checkMap.toString())

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            items(LedMode.values()) { mode ->
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    val checkedState = remember { mutableStateOf(mode == initMode.value) }
                    checkMap[mode] = checkedState
                    Text(text = mode.toName(), modifier = Modifier.padding(16.dp), fontSize = 20.sp)
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = {
                            checkedState.value = !checkedState.value
                            currentMode.value = mode
                            viewModel.setMode(mode)
                            checkMap.map { checkItem ->
                                if (checkItem.key != mode) checkItem.value.value = false
                            }
                        }
                    )
                }

            }
        }
    }

    @Composable
    fun DrawColorPickers(modifier: Modifier) {
        val controller = rememberColorPickerController()
        Column(modifier = modifier) {
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85F)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    val color: Color = colorEnvelope.color
                    if (colorEnvelope.fromUser) {
                        if(currentMode.value == LedMode.GRADIENT){
                            gradientColors.value.forEach{
                                if(it.isCheck.value){
                                    it.color.value = color
                                }
                            }
                            //вызвать ViewModel
                        }else{
                            viewModel.setColor(
                                (color.red * 255).roundToInt(),
                                (color.green * 255).roundToInt(),
                                (color.blue * 255).roundToInt()
                            )
                        }

                    }
                }
            )
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 10.dp, bottom = 10.dp)
                    .fillMaxHeight(),
                controller = controller,
            )
        }
    }

    @Composable
    fun DrawGradientSetting() {
        Column(modifier = Modifier.fillMaxSize()) {
            DrawColorPickers(modifier = Modifier.fillMaxHeight(0.8F))
            LazyRow(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround
            ) {
                items(gradientColors.value) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .size(LocalConfiguration.current.screenWidthDp.dp / 5)
                            .padding(8.dp)
                            .background(Color.Gray, shape = RoundedCornerShape(16.dp))
                            .clickable {
                                gradientColors.value.map { it.isCheck.value = false }
                                it.isCheck.value = true
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(if(it.isCheck.value)2.dp else 0.dp)
                                .fillMaxSize()
                                .background(it.color.value, shape = RoundedCornerShape(16.dp))
                        )
                    }

                }
            }
        }


    }

}