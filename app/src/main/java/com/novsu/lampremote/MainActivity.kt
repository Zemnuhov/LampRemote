package com.novsu.lampremote

import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.novsu.feature_device_screen.DeviceScreen
import com.novsu.feature_main_screen.MainScreen
import com.novsu.lampremote.di.RemoteAppComponent
import com.novsu.lampremote.ui.theme.LampRemoteTheme
import com.novsu.navigation_api.NavigationApi
import com.novsu.navigation_api.Screen
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainScreen: MainScreen

    @Inject
    lateinit var deviceScreen: DeviceScreen

    @Inject
    lateinit var navigation: NavigationApi

    private lateinit var broadcastReceiver: TonicReceiver

    override fun onResume() {
        super.onResume()
        val intentFilter = IntentFilter("com.neurotech.TONIC_BROADCAST_ACTION")
        registerReceiver(broadcastReceiver, intentFilter)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RemoteAppComponent.get().inject(this)
        broadcastReceiver = TonicReceiver()

        setContent {
            LampRemoteTheme(dynamicColor = false) {
                val navController = rememberNavController()
                navigation.bindNavController(navController)
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        bottomBar = {

                            val screen: State<NavBackStackEntry?> =
                                navController.currentBackStackEntryFlow
                                    .collectAsState(
                                        initial = null
                                    )
                            if (screen.value != null) {
                                BottomNavigation(backgroundColor = MaterialTheme.colorScheme.primary){
                                    val screens = listOf(Screen.Main, Screen.Device)
                                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                                    val currentDestination = navBackStackEntry?.destination
                                    screens.forEach { screen ->
                                        BottomNavigationItem(
                                            icon = {
                                                Icon(
                                                    Icons.Filled.Favorite,
                                                    contentDescription = null
                                                )
                                            },
                                            label = { Text(screen.label) },
                                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                            onClick = {
                                                navController.navigate(screen.route) {
                                                    popUpTo(navController.graph.findStartDestination().id) {
                                                        saveState = true
                                                    }
                                                    launchSingleTop = true
                                                    restoreState = true
                                                }
                                            }
                                        )
                                    }
                                }

                            }

                        }
                    ) { innerPadding ->
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Device.route,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable(Screen.Main.route) { mainScreen.Draw() }
                            composable(Screen.Device.route) { deviceScreen.Draw() }
                        }
                    }

                }
            }
        }
    }


}
