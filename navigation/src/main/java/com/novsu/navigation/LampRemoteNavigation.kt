package com.novsu.navigation

import androidx.navigation.NavHostController
import com.novsu.navigation_api.NavigationApi
import com.novsu.navigation_api.Screen

class LampRemoteNavigation: NavigationApi {

    private var navController: NavHostController? = null

    override fun bindNavController(navController: NavHostController) {
        this.navController = navController
    }

    override fun navigate(targetScreen: Screen) {
        if(navController != null){
            navController!!.navigate(targetScreen.route)
        }
    }
}