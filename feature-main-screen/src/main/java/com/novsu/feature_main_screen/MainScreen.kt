package com.novsu.feature_main_screen

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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

    init {
        MainScreenComponent.get().inject(this)
    }

    @Composable
    fun Draw(){
        val viewModel: MainScreenViewModel = viewModel(factory = factory.get())
        Column() {
            Surface(modifier = Modifier.padding(16.dp), shadowElevation = 10.dp, shape = RoundedCornerShape(16.dp)) {
                DrawColorPickers(
                    viewModel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.5F)
                        .padding(16.dp)
                )
            }
            Surface(modifier = Modifier.padding(16.dp), shadowElevation = 10.dp, shape = RoundedCornerShape(16.dp)) {
                DrawModeList(viewModel)
            }
        }

    }

    @Composable
    fun DrawModeList(viewModel: MainScreenViewModel){
        val checkMap = remember {
            mutableMapOf<LedMode, MutableState<Boolean>>()
        }
        val initMode = viewModel.ledMode.collectAsState(initial = null)
        if(initMode.value != null){
            checkMap[initMode.value]?.value = true
        }
        Log.e("AAAAAA",checkMap.toString())

        LazyColumn(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()){
            items(LedMode.values()){ mode ->
                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    val checkedState = remember { mutableStateOf(mode == initMode.value) }
                    checkMap[mode] = checkedState
                    Text(text = mode.toName(), modifier = Modifier.padding(16.dp), fontSize = 20.sp)
                    Checkbox(
                        checked = checkedState.value,
                        onCheckedChange = {
                            checkedState.value = !checkedState.value
                            viewModel.setMode(mode)
                            checkMap.map { checkItem -> if(checkItem.key != mode) checkItem.value.value = false }
                        }
                    )
                }

            }
        }
    }
    
    @Composable
    fun DrawColorPickers(viewModel: MainScreenViewModel, modifier: Modifier){
        val controller = rememberColorPickerController()
        Column(modifier = modifier) {
            HsvColorPicker(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.7F)
                    .padding(10.dp),
                controller = controller,
                onColorChanged = { colorEnvelope: ColorEnvelope ->
                    val color: Color = colorEnvelope.color
                    if(colorEnvelope.fromUser){
                        viewModel.setColor(
                            (color.red * 255).roundToInt(),
                            (color.green * 255).roundToInt(),
                            (color.blue * 255).roundToInt()
                        )
                    }
                }
            )
            BrightnessSlider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 24.dp, top = 10.dp, bottom = 10.dp)
                    .fillMaxHeight(0.3F),
                controller = controller,
            )
        }
    }

}