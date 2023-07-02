package com.novsu.navigation_api

import androidx.navigation.NavHostController



interface NavigationApi {

    fun bindNavController(navController: NavHostController)
    fun navigate(targetScreen: Screen)
}