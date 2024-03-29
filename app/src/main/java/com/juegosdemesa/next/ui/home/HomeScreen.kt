package com.juegosdemesa.next.ui.home

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
import com.juegosdemesa.next.R
import com.juegosdemesa.next.NextTopAppBar
import com.juegosdemesa.next.ui.navigation.NavigationDestination
import com.juegosdemesa.next.ui.theme.NextTheme

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToNewGame:() -> Unit,
){
    Scaffold(
        topBar = {
            NextTopAppBar(
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
                onClick = navigateToNewGame,

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
fun PreviewHomeScreen(){
    NextTheme {
        HomeScreen {}
    }
}
