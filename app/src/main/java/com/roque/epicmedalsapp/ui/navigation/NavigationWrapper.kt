package com.roque.epicmedalsapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roque.epicmedalsapp.ui.screens.medals.MedalsScreen
import com.roque.epicmedalsapp.ui.screens.medals.MedalsViewModel

@Composable
fun NavigationWrapper() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Medals) {

        composable<Medals> {
            val medalsViewModel: MedalsViewModel = hiltViewModel()
            MedalsScreen(medalsViewModel = medalsViewModel)
        }
    }
}