package com.example.palapp.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.palapp.data.LoginResponse

@Composable
fun MyApp() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LogInPage(navController) }
        composable("create_profile") { UserProfileCreateScreen(navController) }
        composable("home_page_view") {
            ProfileHomePageView(navController) }
    }
}