package com.roque.epicmedalsapp.ui.navigation

import com.roque.epicmedalsapp.R

sealed class BottomNavigationDestination(
    val route: String,
    val icon: Int,
    val label: String
) {
    object Medals : BottomNavigationDestination("medals", R.drawable.ic_outline_trophy, "Medallas")
    object Missions : BottomNavigationDestination("missions", R.drawable.ic_outline_target, "Misiones")
    object Streaks : BottomNavigationDestination("streaks", R.drawable.ic_outline_fire, "Rachas")
    object Album : BottomNavigationDestination("album", R.drawable.ic_outline_photo_prints, "Album")

    object NavItems {
        val navigationItems = listOf(
            Medals,
            Missions,
            Streaks,
            Album
        )
    }
}