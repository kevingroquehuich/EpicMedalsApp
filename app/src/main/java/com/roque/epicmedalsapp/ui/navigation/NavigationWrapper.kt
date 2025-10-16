package com.roque.epicmedalsapp.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.roque.epicmedalsapp.ui.composables.BottomNavigationBar
import com.roque.epicmedalsapp.ui.composables.TopBar
import com.roque.epicmedalsapp.ui.navigation.BottomNavigationDestination.NavItems.navigationItems
import com.roque.epicmedalsapp.ui.screens.album.AlbumScreen
import com.roque.epicmedalsapp.ui.screens.album.AlbumViewModel
import com.roque.epicmedalsapp.ui.screens.medals.MedalsScreen
import com.roque.epicmedalsapp.ui.screens.medals.MedalsViewModel
import com.roque.epicmedalsapp.ui.screens.missions.MissionsScreen
import com.roque.epicmedalsapp.ui.screens.missions.MissionsViewModel
import com.roque.epicmedalsapp.ui.screens.streaks.StreaksScreen
import com.roque.epicmedalsapp.ui.screens.streaks.StreaksViewModel

@Composable
fun NavigationWrapper() {

    val navController = rememberNavController()

    var currentScreen by remember{
        mutableStateOf<BottomNavigationDestination>(BottomNavigationDestination.Medals)
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar(screenTitle = currentScreen.label) },
        bottomBar = {
            BottomNavigationBar(
                currentScreenId = currentScreen.route,
                navController = navController,
                items = navigationItems,
                onItemSelected = {currentScreen = it}
            )
        }
    ) { innerPadding ->

        NavHost(
            modifier = Modifier.padding(innerPadding),
            navController = navController,
            startDestination = BottomNavigationDestination.Medals.route
        ) {

            composable(route = BottomNavigationDestination.Medals.route) {
                val medalsViewModel: MedalsViewModel = hiltViewModel()
                MedalsScreen(medalsViewModel = medalsViewModel)
            }

            composable(route = BottomNavigationDestination.Missions.route) {
                val missionsViewModel: MissionsViewModel = hiltViewModel()
                MissionsScreen(missionsViewModel)
            }

            composable(route = BottomNavigationDestination.Streaks.route) {
                val streaksViewModel: StreaksViewModel = hiltViewModel()
                StreaksScreen(viewModel = streaksViewModel)
            }

            composable(route = BottomNavigationDestination.Album.route) {
                val albumViewModel: AlbumViewModel = hiltViewModel()
                AlbumScreen(viewModel = albumViewModel)
            }
        }
    }


}