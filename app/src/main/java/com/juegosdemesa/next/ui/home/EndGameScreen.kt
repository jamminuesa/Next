package com.juegosdemesa.next.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juegosdemesa.next.NextTopAppBar
import com.juegosdemesa.next.R
import com.juegosdemesa.next.data.model.Team
import com.juegosdemesa.next.ui.navigation.NavigationDestination
import com.juegosdemesa.next.ui.theme.Typography

object EndGameDestination: NavigationDestination {
    override val route = "endGame"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndGameScreen(
    navigateToHome:() -> Unit,
    vewModel: GameViewModel,
){
    Scaffold(
        topBar = {
            NextTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false,
            )
        },
    ) { innerPadding ->
        val gameResult by vewModel.gameResult.collectAsState()
        val winner by vewModel.winner.collectAsState()

        EndGameBody(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            winner,
            gameResult,
            navigateToHome
        )
    }
}

@Composable
fun EndGameBody(
    modifier: Modifier = Modifier,
    winner: String,
    result: List<Team>,
    navigateToHome: () -> Unit
){
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = winner,
            style = Typography.headlineLarge,
            )
        TeamList(
            modifier = modifier,
            teamList = result)

        Button(
            onClick = navigateToHome,
            shape = RoundedCornerShape(30),
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(30.dp)
        ){
            Text(text = stringResource(R.string.back_to_home))
        }
    }
}

@Composable
private fun TeamList(
    teamList: List<Team>,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier) {
        items(items = teamList, key = { it.id }) { item ->
            TeamItem(item = item,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun TeamItem(
    item: Team,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =  Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ColoredDot(item.color, 10.dp)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = "Puntos: ${item.totalScore}",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = "Cartas omitidas: ${item.totalMiss}",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}


@Preview (showBackground = true)
@Composable
fun EndGameBodyPreview(){
    EndGameBody(winner = "El ganador es: Equipo 2",
        result = listOf(Team(1), Team(2)),
        navigateToHome = {}
    )
}