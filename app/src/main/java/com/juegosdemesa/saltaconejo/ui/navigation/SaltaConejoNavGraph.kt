package com.juegosdemesa.saltaconejo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juegosdemesa.saltaconejo.ui.home.HomeDestination
import com.juegosdemesa.saltaconejo.ui.home.HomeScreen
import com.juegosdemesa.saltaconejo.ui.home.QuestionsDestination
import com.juegosdemesa.saltaconejo.ui.home.CardsDisplayScreen
import com.juegosdemesa.saltaconejo.ui.home.NewGameDestination
import com.juegosdemesa.saltaconejo.ui.home.NewGameScreen

/**
 * Provides Navigation graph for the application.
 */

@Composable
fun SaltaConejoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ){
        composable(route = HomeDestination.route){
            HomeScreen(
                navigateToNewGame = {navController.navigate(NewGameDestination.route)}
            )
        }

        composable(route = NewGameDestination.route){
            NewGameScreen(
                navigateToQuestions = {navController.navigate(QuestionsDestination.route)}
            )
        }

        composable(route = QuestionsDestination.route){
            CardsDisplayScreen()
        }
    }
}