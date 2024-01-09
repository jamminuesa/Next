package com.juegosdemesa.saltaconejo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juegosdemesa.saltaconejo.ui.home.HomeDestination
import com.juegosdemesa.saltaconejo.ui.home.HomeScreen
import com.juegosdemesa.saltaconejo.ui.home.CardRoundDestination
import com.juegosdemesa.saltaconejo.ui.home.CardRoundScreen
import com.juegosdemesa.saltaconejo.ui.home.EndGameDestination
import com.juegosdemesa.saltaconejo.ui.home.GameViewModel
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
    val gameViewModel: GameViewModel = viewModel()
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
                navigateToCardRound = {navController.navigate(CardRoundDestination.route)},
                gameViewModel
            )
        }

        composable(route = CardRoundDestination.route){
            CardRoundScreen(
                navigateToNextRound = {navController.navigate(CardRoundDestination.route)},
                navigateToEndGame = {navController.navigate(EndGameDestination.route)},
                gameViewModel
            )
        }
    }
}