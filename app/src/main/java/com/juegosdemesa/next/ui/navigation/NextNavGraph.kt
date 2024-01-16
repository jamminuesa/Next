package com.juegosdemesa.next.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juegosdemesa.next.ui.home.AppViewModelProvider
import com.juegosdemesa.next.ui.home.CardRoundDestination
import com.juegosdemesa.next.ui.home.CardRoundScreen
import com.juegosdemesa.next.ui.home.EndGameDestination
import com.juegosdemesa.next.ui.home.EndGameScreen
import com.juegosdemesa.next.ui.home.GameViewModel
import com.juegosdemesa.next.ui.home.HomeDestination
import com.juegosdemesa.next.ui.home.HomeScreen
import com.juegosdemesa.next.ui.home.NewGameDestination
import com.juegosdemesa.next.ui.home.NewGameScreen

/**
 * Provides Navigation graph for the application.
 */

@Composable
fun NextNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val gameViewModel: GameViewModel = viewModel(factory = AppViewModelProvider.GameViewModelFactory)
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

        composable(route = EndGameDestination.route){
            EndGameScreen(
                navigateToHome = {navController.navigate(HomeDestination.route)},
                gameViewModel,
            )
        }
    }
}