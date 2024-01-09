package com.juegosdemesa.saltaconejo.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juegosdemesa.saltaconejo.R
import com.juegosdemesa.saltaconejo.SaltaConejoTopAppBar
import com.juegosdemesa.saltaconejo.ui.navigation.NavigationDestination
import com.juegosdemesa.saltaconejo.ui.theme.SaltaConejoTheme

object EndGameDestination: NavigationDestination {
    override val route = "endGame"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndGameScreen(
    navigateToHome:() -> Unit,
){
    Scaffold(
        topBar = {
            SaltaConejoTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = navigateToHome,

                shape = RoundedCornerShape(30),
                modifier = Modifier
                    .padding(innerPadding)
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(30.dp)
            ){
                Text(text = stringResource(R.string.new_game))
            }
        }
    }
}

@Preview
@Composable
fun PreviewEndGameScreen(){
    SaltaConejoTheme {
        EndGameScreen {
        }
    }
}
