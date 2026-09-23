package com.example.lab05

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object List : Screen("list")
    object Profile : Screen("profile")
    object DetailScreen : Screen(route ="detail/{itemId}") {
        fun createRoute(texto: String): String = "detail/$texto"
    }
}