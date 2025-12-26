package com.example.myplayer.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.myplayer.navigation.BottomNavItem

@Composable
fun HomeScreen(navController: NavController) {
    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        HomeCard(title = "Music", icon = Icons.Default.MusicNote) {
            navController.navigate(BottomNavItem.Music.route)
        }
        HomeCard(title = "Videos", icon = Icons.Default.Videocam) {
            navController.navigate(BottomNavItem.Videos.route)
        }
    }
}

@Composable
fun HomeCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(150.dp)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = title, modifier = Modifier.size(70.dp))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = title)
        }
    }
}