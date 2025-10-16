package com.roque.epicmedalsapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.roque.epicmedalsapp.ui.navigation.NavigationWrapper
import com.roque.epicmedalsapp.ui.screens.splash.SplashScreen
import com.roque.epicmedalsapp.ui.theme.EpicMedalsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            EpicMedalsAppTheme {
                var isLoading by remember { mutableStateOf(true) }

                // Simulación de carga
                LaunchedEffect(Unit) {
                    delay(5000)
                    isLoading = false
                }
                if (isLoading) {
                    SplashScreen{}
                } else {
                    NavigationWrapper()
                }

            }
        }
    }
}