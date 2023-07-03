package com.novsu.feature_main_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.ui.graphics.Color

data class GradientColor(
    var isCheck: MutableState<Boolean>,
    var color: MutableState<Color>
)
