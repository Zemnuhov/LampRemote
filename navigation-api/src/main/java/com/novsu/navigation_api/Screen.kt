package com.novsu.navigation_api

import android.graphics.drawable.Icon


sealed class Screen(val route: String, val label: String) {
    object Main : Screen("main", "Главная")
    object Device : Screen("device", "Устройство")
}