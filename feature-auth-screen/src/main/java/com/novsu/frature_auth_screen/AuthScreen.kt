package com.novsu.frature_auth_screen

import android.util.Log
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.novsu.navigation_api.NavigationApi
import com.novsu.navigation_api.Screen
import dagger.Lazy
import javax.inject.Inject

class AuthScreen {

    @Inject
    lateinit var factory: Lazy<AuthScreenViewModel.Factory>

    @Inject
    lateinit var navigation: NavigationApi

    init {
        AuthScreenComponent.get().inject(this)
    }

    @Composable
    private fun DrawHeading() {
        val circleColor = remember {
            Animatable(Color.Red)
        }
        LaunchedEffect(Unit) {
            while (true) {
                circleColor.animateTo(Color.Red, animationSpec = tween(2000))
                circleColor.animateTo(Color.Yellow, animationSpec = tween(2000))
                circleColor.animateTo(Color.Green, animationSpec = tween(2000))
                circleColor.animateTo(Color.Magenta, animationSpec = tween(2000))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight(0.5F)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(), onDraw = {
                drawCircle(
                    circleColor.value,
                    radius = size.width,
                    center = Offset(size.width / 2, 0F)
                )
            })
            Text(
                text = "Led Remote",
                style = TextStyle(fontSize = 42.sp),
                fontWeight = FontWeight(600),
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 25.dp)
            )
        }
    }

    @Composable
    private fun DrawAuthField(viewModel: AuthScreenViewModel) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {

            Text(
                text = "Давай знакомиться!",
                style = TextStyle(fontSize = 24.sp),
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
            )

            Button(
                onClick = { viewModel.signIn() },
                elevation = ButtonDefaults.elevatedButtonElevation(
                    defaultElevation = 9.dp,
                    pressedElevation = 5.dp,
                    disabledElevation = 0.dp,
                ),
                colors = ButtonDefaults.buttonColors(Color.White),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .padding(bottom = 32.dp)
                    .height(50.dp)
            ) {
                Image(
                    bitmap = ImageBitmap.imageResource(id = R.drawable.google),
                    contentDescription = "singIn"
                )
            }

        }
    }


    @Composable
    fun DrawModeList(){

    }

    @Composable
    fun Draw() {
        val viewModel: AuthScreenViewModel = viewModel(factory = factory.get())
        LaunchedEffect(true) {
            viewModel.user.collect {
                Log.e("USER", it.toString())
                if (it != null) {
                    navigation.navigate(Screen.Main)
                }
            }
        }
        Column {
            DrawHeading()
            DrawAuthField(viewModel)
        }

    }
}