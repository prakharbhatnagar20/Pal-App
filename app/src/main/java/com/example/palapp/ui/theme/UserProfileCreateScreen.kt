package com.example.palapp.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun UserProfileCreateScreen(navController: NavController, modifier: Modifier=Modifier) {
    Column(modifier = modifier.fillMaxWidth(),verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "User Profile")
        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Preview
@Composable
fun UserProfileCreateScreenPreview(){
    PalAppTheme {
        UserProfileCreateScreen(navController = rememberNavController())
    }
}