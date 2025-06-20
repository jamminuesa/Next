package com.juegosdemesa.next.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.juegosdemesa.next.NextTopAppBar
import com.juegosdemesa.next.R
import com.juegosdemesa.next.data.model.Round
import com.juegosdemesa.next.data.model.Team
import com.juegosdemesa.next.ui.navigation.NavigationDestination
import com.juegosdemesa.next.ui.theme.NextTheme
import com.juegosdemesa.next.util.Utility

object NewGameDestination: NavigationDestination {
    override val route = "new_game"
    override val titleRes = R.string.new_game
}

lateinit var gViewModel: GameViewModel
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewGameScreen(
    navigateToCardRound: () -> Unit,
    viewModel: GameViewModel,
){
    gViewModel  = viewModel
    val teamList by gViewModel.teamList.collectAsState()
    val roundList by gViewModel.simpleRoundList.collectAsState()
    val checked by gViewModel.withModifiedRounds.collectAsState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            NextTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = true,
                scrollBehavior = scrollBehavior
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = roundList.isNotEmpty(),
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                FloatingActionButton(
                    onClick = {
                        viewModel.createNewGame()
                        navigateToCardRound.invoke()
                    },
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        },
    ) { innerPadding ->
        NewGameBody(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            teamList = teamList,
            roundList = roundList,
            modificationsChecked = checked
            )

    }
}

//Composable

@Composable
fun NewGameBody(
    modifier: Modifier = Modifier,
    teamList: List<Team>,
    roundList: List<Round>,
    modificationsChecked: Boolean
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        RoundModifierCheckbox(modificationsChecked)
        TeamHeader(teamList = teamList)
        RoundHeader(roundList = roundList)
    }
}

@Preview(showBackground = true)
@Composable
fun NewGameBodyPreview(){
    val list = mutableListOf<Team>()
    for (i in 1..4){
        list.add(Team(i))
    }

    val list1 = mutableListOf<Round>()
    for (i in 1..4){
        list1.add(Round())
    }
    NewGameBody(
        teamList = list,
        roundList = list1,
        modificationsChecked = true
    )
}

@Composable
fun TeamHeader(
    modifier: Modifier = Modifier,
    teamList: List<Team>
){
    val showDialog =  remember { mutableStateOf(false) }

    if(showDialog.value)
        TextInputDialog(
            title = "Equipo",
            hint = "Escribe el nombre del equipo",
            setShowDialog = {
                showDialog.value = it
            }
        ) { result ->
            gViewModel.addTeam(result)
        }

    Column(
        modifier = modifier ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(8.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Equipos",
                style = MaterialTheme.typography.titleLarge
            )
            Button(
                onClick = {
                    showDialog.value = true
                          },
                // Uses ButtonDefaults.ContentPadding by default
                contentPadding = PaddingValues(
                    start = 20.dp,
                    top = 12.dp,
                    end = 20.dp,
                    bottom = 12.dp
                )
            ) {
                // Inner content including an icon and a text label
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Favorite",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text("Añadir")
            }
        }
        TeamList(teamList = teamList)

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
private fun RoundModifierCheckbox(
    checked: Boolean
) {
    val text = remember { mutableStateOf("Jugar sin modificadores de ronda") }

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ){

        Text(
            text = text.value,
            style = MaterialTheme.typography.titleMedium
        )

        Checkbox(
            checked = checked,
            onCheckedChange = {
                gViewModel.isRoundModificationsChecked(it)
                text.value = if (it) {
                    "Jugar con modificadores de ronda"
                } else {
                    "Jugar sin modificadores de ronda"
                }
            }
        )
    }
}

@Preview (showBackground = true)
@Composable
fun CheckBoxPreview(){
    RoundModifierCheckbox(false)
}

@Preview
@Composable
fun TeamListPreview(){
    val list = mutableListOf<Team>()
    for (i in 1..4){
        list.add(Team(i))
    }
    TeamList(list)
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
                ColoredDot(item.toColor(), 10.dp)
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    text = item.name,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview
@Composable
fun TeamItemPreview(){
    TeamItem(Team(1))
}

@Composable
fun ColoredDot(
    color: Color,
    size: Dp
){
    Column(modifier = Modifier
        .wrapContentSize()) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(color)
        )
    }
}

@Preview
@Composable
fun ColoredDotPreview(){
    ColoredDot(
        color = Utility.hexColorToColor("#076943"),
        size = 10.dp)
}


@Preview(showBackground = true)
@Composable
fun TeamHeaderPreview(){
    NextTheme {
        val list = mutableListOf<Team>()
        for (i in 1..4){
            list.add(Team(i))
        }
        TeamHeader(teamList = list)
    }
}

@Composable
fun RoundHeader(
    modifier: Modifier = Modifier,
    roundList: List<Round>
){
    Column(
        modifier = modifier ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Row(
            modifier
                .fillMaxWidth()
                .padding(8.dp),

            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Rondas",
                style = MaterialTheme.typography.titleLarge
            )
//            Button(
//                onClick = { newGameViewModel.addNewAutoGenerateTeam() },
//                // Uses ButtonDefaults.ContentPadding by default
//                contentPadding = PaddingValues(
//                    start = 20.dp,
//                    top = 12.dp,
//                    end = 20.dp,
//                    bottom = 12.dp
//                )
//            ) {
//                // Inner content including an icon and a text label
//                Icon(
//                    Icons.Filled.Add,
//                    contentDescription = "Favorite",
//                    modifier = Modifier.size(ButtonDefaults.IconSize)
//                )
//                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
//                Text("Añadir")
//            }
        }
        RoundList(roundList = roundList)

    }
}

@Composable
private fun RoundList(
    roundList: List<Round>,
    modifier: Modifier = Modifier
){
    LazyColumn(modifier = modifier) {
        items(items = roundList, key = { it.id }) { item ->
            RoundItem(item = item,
                modifier = Modifier
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun RoundItem(
    item: Round,
    modifier: Modifier = Modifier
){
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = item.type.color
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .background(item.type.color),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(item.type.color),
                horizontalArrangement =  Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = item.type.text,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoundHeaderPreview(){
    NextTheme {
        val list = mutableListOf<Round>()
        for (i in 1..4){
            list.add(Round())
        }
        RoundHeader( roundList = list)
    }
}

@Preview
@Composable
fun RoundListPreview(){
    val list = mutableListOf<Round>()
    for (i in 1..4){
        list.add(Round())
    }
    RoundList(list)
}

@Preview(showBackground = true)
@Composable
fun RoundItemPreview(){
    RoundItem(Round())
}
