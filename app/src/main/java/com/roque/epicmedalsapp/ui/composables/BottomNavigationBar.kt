package com.roque.epicmedalsapp.ui.composables

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.roque.epicmedalsapp.ui.navigation.BottomNavigationDestination

@Composable
fun BottomNavigationBar(
    currentScreenId: String,
    onItemSelected: (BottomNavigationDestination) -> Unit,
    navController: NavController,
    items: List<BottomNavigationDestination>
) {

    Row(
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            CustomBottomNavigationItem(
                item = item,
                isSelected = item.route == currentScreenId,
                onClick = {
                    onItemSelected(item)
                    navController.popBackStack()
                    navController.navigate(item.route) {

                        navController.graph.startDestinationRoute?.let { screen_route ->
                            popUpTo(screen_route) {
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun CustomBottomNavigationItem(
    item: BottomNavigationDestination,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val background = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
    val scale by animateFloatAsState(if (isSelected) 1.2f else 1f)

    Box(modifier = Modifier
        .clip(CircleShape)
        .background(background)
        .clickable { onClick() }
    ) {
        Icon(
            modifier = Modifier.scale(scale).padding(vertical = 12.dp, horizontal = 32.dp),
            painter = painterResource(id = item.icon),
            contentDescription = null,
            tint = contentColor
        )
    }
}
