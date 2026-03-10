package com.juegosdemesa.next.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.juegosdemesa.next.ui.home.CardRoundDestination
import com.juegosdemesa.next.ui.home.CardRoundScreen
import com.juegosdemesa.next.ui.home.EndGameDestination
import com.juegosdemesa.next.ui.home.EndGameScreen
import com.juegosdemesa.next.ui.home.GameViewModel
import com.juegosdemesa.next.ui.home.HomeDestination
import com.juegosdemesa.next.ui.home.HomeScreen
import com.juegosdemesa.next.ui.home.NewGameDestination
import com.juegosdemesa.next.ui.home.NewGameScreen
import com.juegosdemesa.next.ui.theme.ThemeMode

/**
 * Provides Navigation graph for the application.
 */

@Composable
fun NextNavHost(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier
){
    val gameViewModel: GameViewModel = hiltViewModel()
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ){
        composable(route = HomeDestination.route){
            HomeScreen(
                themeMode = themeMode,
                onThemeChange = onThemeChange,
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
                navigateToHome = {navController.navigate(HomeDestination.route)},
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