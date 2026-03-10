package com.juegosdemesa.next.ui.home


import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.juegosdemesa.next.R
import com.juegosdemesa.next.data.model.Card
import com.juegosdemesa.next.data.model.Round
import com.juegosdemesa.next.data.model.RoundWithTeamAndModifier
import com.juegosdemesa.next.data.model.Team
import com.juegosdemesa.next.ui.navigation.NavigationDestination
import com.juegosdemesa.next.ui.theme.LocalGameColors
import com.juegosdemesa.next.ui.theme.NextTheme
import com.juegosdemesa.next.ui.theme.RoundColors
import com.juegosdemesa.next.ui.theme.ThemeMode
import com.juegosdemesa.next.ui.theme.Typography
import com.juegosdemesa.next.ui.widgets.BigCircleButton
import com.juegosdemesa.next.ui.widgets.InformationDialog
import com.juegosdemesa.next.ui.widgets.KeepScreenOn
import com.juegosdemesa.next.util.Utility
import com.juegosdemesa.next.util.Utility.formatTime
import kotlinx.coroutines.launch

object CardRoundDestination: NavigationDestination {
    override val route = "cardsDisplay"
    override val titleRes = R.string.app_name
}

object GameTheme {
    val colors: RoundColors
        @Composable
        get() = LocalGameColors.current
}



@Composable
fun CardRoundScreen(
    navigateToHome: () -> Unit,
    navigateToNextRound: () -> Unit,
    navigateToEndGame: () -> Unit,
    viewModel: GameViewModel
) {
    var showExitDialog by remember { mutableStateOf(false) }

    // Intercept back press event
    // 'enabled' controls if handler is active.
    // If enabled is false, back press will behave as default
    BackHandler (enabled = true) {
        showExitDialog = true
    }

    KeepScreenOn()

    val gameViewModel: GameViewModel = viewModel
    val cardViewModel: CardViewModel = hiltViewModel()
    val countDownViewModel: CountDownViewModel = viewModel()

    val timeIsUp by countDownViewModel.isTimeUp.observeAsState(false)
    val round by gameViewModel.round.collectAsState()

    LaunchedEffect(round){
        if (round != null){
            cardViewModel.loadCardsCategory(round!!.round.type)
        }
    }


    AnimatedVisibility(
        visible = !timeIsUp && round != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        if (round != null){
            val time by countDownViewModel.time.observeAsState(Utility.TIME_COUNTDOWN.formatTime())
            val progress by countDownViewModel.progress.observeAsState(1.00F)
            val isPlaying by countDownViewModel.isPlaying.observeAsState(false)
            val cardUiState by cardViewModel.cardListState.collectAsState()

            SetTimeIsRunningScreen(
                round!!,
                time = time,
                progress = progress,
                isPlaying = isPlaying,
                cardList = cardUiState,
                addSeenCard = { card -> cardViewModel.addSeenCard(card) },
                addPointsToScore = { points -> cardViewModel.addPointsToScore(points) },
                addMissCard = { cardViewModel.addMissCard() },
                noMoreCards = { countDownViewModel.noMoreCards() },
                startTimer = { countDownViewModel.startTimer() },
            )
        }

    }

    AnimatedVisibility(
        visible = timeIsUp,
        enter = fadeIn(),
        exit = fadeOut()) {
        SetTimeIsUpScreen(
            navigateToNextRound,
            navigateToEndGame,
            cardViewModel,
            gameViewModel
        )
    }

    // Diálogo que se muestra al pulsar atrás
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false }, // Permitir cerrar el diálogo pulsando fuera o atrás
            title = { Text("¿Salir de la partida?") },
            text = { Text("¿Estás seguro de que quieres salir? La partida actual se eliminará") },
            confirmButton = {
                Button(onClick = {
                    gameViewModel.deleteGame()
                    navigateToHome.invoke()
                }) {
                    Text("Sí, salir")
                }
            },
            dismissButton = {
                Button(onClick = { showExitDialog = false }) {
                    Text("No, cancelar")
                }
            }
        )
    }
}

@Composable
@OptIn(ExperimentalSwipeableCardApi::class)
private fun SetTimeIsRunningScreen(
    round: RoundWithTeamAndModifier,
    time: String,
    progress: Float,
    isPlaying: Boolean,
    cardList: List<Card>,
    addSeenCard: (Card) -> Unit,
    addPointsToScore: (Int) -> Unit,
    addMissCard: () -> Unit,
    noMoreCards: () -> Unit,
    startTimer: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val openInformationDialog = remember { mutableStateOf(false)  }

    val cardStates = cardList
        .reversed()
        .map { it to rememberSwipeableCardState() }

    ConstraintLayout(
        constraintSet = constraint,
        Modifier
            .fillMaxSize()
            .paint( // Background image
                painter = rememberAsyncImagePainter(R.drawable.double_bubble),
                colorFilter = ColorFilter.tint(round.team.toColor()),
                contentScale = ContentScale.Crop
            ),
    ) {

        Text(
            modifier = Modifier
                .layoutId("headerRef")
                .padding(8.dp),
            textAlign = TextAlign.Center,
            style = Typography.titleLarge,
            text = "Turno de: ${round.team.name}",
        )

        //Display the information dialog button only if the round hasn't started
        AnimatedVisibility(
            modifier = Modifier
                .layoutId("topRightCornerRef"),
            visible = !isPlaying,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            IconButton(
                onClick = { openInformationDialog.value = true }
            ) {
                Image(
                    painterResource(R.drawable.help_24),
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                    contentDescription = null
                )
            }
        }

        Box(
            Modifier
                .layoutId("topRef")
                .padding(12.dp)
                .fillMaxSize()
        ) {
            cardStates.forEachIndexed { index, (card, state) ->
                if (state.swipedDirection == null) {
                    // En caso de que no se de al botón, no se puede quitar la primera tarjeta
                    val modifier = if (isPlaying) {
                        Modifier
                            .swipableCard(
                                state = state,
                                blockedDirections = listOf(Direction.Down),
                                onSwiped = {
                                    // swipes are handled by the LaunchedEffect
                                    // so that we track button clicks & swipes
                                    // from the same place
                                },
                                onSwipeCancel = {
                                    Log.d("Swipeable-Card", "Cancelled swipe")
                                }
                            )
                    } else {
                        Modifier
                    }
                        // If the cards are from finish the song use annotatedString
                        if (round.round.type.value == 9){
                            CardViewFinishSong(
                                modifier = modifier,
                                text = card.text,
                                color = Utility.getCardColor(round.round.type)
                            )
                        } else {
                            CardView(
                                modifier = modifier,
                                text = card.text,
                                color = Utility.getCardColor(round.round.type)
                            )
                        }
                }
                LaunchedEffect(card, state.swipedDirection) {
                    if (state.swipedDirection != null) {
                            addSeenCard(cardStates[index - 1].first) // List is reversed
                        if (state.swipedDirection == Direction.Right) {
                            addPointsToScore(card.points)
                        } else if (state.swipedDirection == Direction.Left) {
                            addMissCard()
                        }

                        //Check if there are more cards
                        if (cardList.last().id == card.id) {
                            noMoreCards()
                        }
                    }
                }
            }
        }
        ButtonsNTimer(
            roundModifierText = round.modifier?.text ?: "",
            progress = progress,
            time = time,
            isPlaying = isPlaying,
            startTimer = startTimer,
            swipeLeft = {
                scope.launch {
                    val last = cardStates.reversed()
                        .firstOrNull {
                            it.second.offset.value == Offset(0f, 0f)
                        }?.second

                    last?.swipe(Direction.Left)
                }
            },
            swipeRight = {
                scope.launch {
                    val last = cardStates.reversed()
                        .firstOrNull {
                            it.second.offset.value == Offset(0f, 0f)
                        }?.second

                    last?.swipe(Direction.Right)
                }
            }
        )

        if (openInformationDialog.value){
            InformationDialog(
                onDismissRequest = { openInformationDialog.value = false },
                title = round.round.type.text,
                text = round.round.type.helpInfo
            )
        }
    }
}

@Composable
private fun ButtonsNTimer(
    roundModifierText: String,
    progress: Float,
    time: String,
    isPlaying: Boolean,
    startTimer: () -> Unit,
    swipeRight:() -> Unit,
    swipeLeft:() -> Unit
){
    Column(Modifier
        .layoutId("bottomRef"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BasicText(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(5.dp),
            text = roundModifierText,
            autoSize = TextAutoSize.StepBased(),
            style = MaterialTheme.typography.bodyMedium.copy(
                textAlign = TextAlign.Center // Apply text alignment
            ),

        )

        CountDownIndicator(
            Modifier.padding(top = 1.dp),
            progress = progress,
            time = time,
            size = 100,
            stroke = 12
        )
        Row(
            Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BigCircleButton(
                onClick = {
                    if (isPlaying){
                        swipeLeft()
                    }
                          },
                icon = Icons.Default.Close
            )
            BigCircleButton(
                onClick = {
                    if (!isPlaying){
                        startTimer()
                    }
                    swipeRight()
                },
                icon = Icons.Default.Check
            )
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun SetTimeIsUpScreen(
    navigateToNextRound: () -> Unit,
    navigateToEndGame: () -> Unit,
    cardViewModel: CardViewModel,
    gameViewModel: GameViewModel
){
    val score by cardViewModel.score.collectAsState()
    val miss by cardViewModel.miss.collectAsState()
    val nextTeam by gameViewModel.nextRoundTeam.collectAsState()

    val isGameOver by gameViewModel.isGameOver.collectAsState()

    Column(Modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        Text(text = "Se ha acabado el tiempo")
        val text = if (score == 1) "acierto" else "aciertos"
        Text(text = "Habéis conseguido $score $text")

        val text1 = if (miss == 1) "carta" else "cartas"
        Text(text = "Habéis pasado $miss $text1")

        if (isGameOver){
            Text(text = "Se acabó lo que se daba")
            Button(onClick = {
                gameViewModel.markRoundAsCompleted(score, miss)
                navigateToEndGame.invoke()
            }) {
                Text(text = "Ver puntuaciones finales")
            }
        } else {
            Button(onClick = {
                gameViewModel.markRoundAsCompleted(score, miss)
                navigateToNextRound()
            }
            ) {
                Text(text = "Turno de ${nextTeam.team.name}")
            }
        }
    }
}

@Preview
@Composable
private fun TimeRunningScreenPreviewLight(){
    TimeRunningScreenPreview(themeMode = ThemeMode.Light)
}

@Preview
@Composable
private fun TimeRunningScreenPreviewDark(){
    TimeRunningScreenPreview(themeMode = ThemeMode.Dark)
}

@Composable
private fun TimeRunningScreenPreview(themeMode: ThemeMode){
    NextTheme (themeMode = themeMode){
        val cards = listOf(
            Card(text = "Texto 1"),
            Card(text = "Texto 2"),
            Card(text = "Texto 3"),
            Card(text = "Texto 4")
        )
        Surface {
            SetTimeIsRunningScreen(
                round = RoundWithTeamAndModifier(
                    round = Round(),
                    team = Team(name = "Equipo A")
                ),
                time = "00:05",
                progress = 0.5f,
                isPlaying = true,
                cardList = cards,
                addSeenCard = {},
                addPointsToScore = {},
                addMissCard = {},
                noMoreCards = {},
                startTimer = {}
            ) 
        }
    }
}

val constraint = ConstraintSet {
    val headerRef = createRefFor("headerRef")
    val topRightCornerRef = createRefFor("topRightCornerRef")
    val topRef = createRefFor("topRef")
    val bottomRef = createRefFor("bottomRef")

    constrain(headerRef) {
        top.linkTo(parent.top)
        width = Dimension.matchParent
        height = Dimension.wrapContent
    }

    constrain(topRightCornerRef){
        top.linkTo(parent.top)
        end.linkTo(parent.end)
        width = Dimension.wrapContent
        height = Dimension.wrapContent
    }

    constrain(topRef) {
        top.linkTo(headerRef.bottom)
        bottom.linkTo(bottomRef.top)
        width = Dimension.matchParent
        height = Dimension.fillToConstraints
    }

    constrain(bottomRef){
        bottom.linkTo(parent.bottom)
        height = Dimension.wrapContent
        width = Dimension.matchParent
    }
}

@Composable
fun CountDownIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    time: String,
    size: Int,
    stroke: Int
){

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec,
        label = "",
    )

    Column(modifier = modifier) {
        Box {

            CircularProgressIndicatorBackGround(
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                stroke
            )

            CircularProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                strokeWidth = stroke.dp,
            )

            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = time,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun CircularProgressIndicatorBackGround(
    modifier: Modifier = Modifier,
    color: Color,
    stroke: Int
) {
    val style = with(LocalDensity.current) { Stroke(stroke.dp.toPx()) }

    Canvas(modifier = modifier, onDraw = {

        val innerRadius = (size.minDimension - style.width)/2

        drawArc(
            color = color,
            startAngle = 0f,
            sweepAngle = 360f,
            topLeft = Offset(
                (size / 2.0f).width - innerRadius,
                (size / 2.0f).height - innerRadius
            ),
            size = Size(innerRadius * 2, innerRadius * 2),
            useCenter = false,
            style = style
        )

    })
}

@Composable
private fun CardViewFinishSong(
    modifier: Modifier,
    text: String,
    color: Color
){
    val annotatedString = buildAnnotatedString {
        if (text.contains(" ... ") && text.contains(" - ")){
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                append(text.substring(0, text.indexOf(" ... ") + 1) )
            }
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 60.sp)) {
                append(text.substring(text.indexOf(" ... ") + 1,
                    text.indexOf(" ... ") + 4 )
                )
            }

            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface,
                textDecoration = TextDecoration.Underline)
            ) {
                append(text.substring(text.indexOf(" ... ") + 4, text.indexOf(" - ")))
            }

            withStyle(style = SpanStyle(fontSize = 20.sp, color = MaterialTheme.colorScheme.onSurface)) {
                append("\n" + text.substring(text.indexOf(" - ") + 3))
            }
        } else {
            append(text)
        }
    }

    CardView(
        modifier = modifier,
        text = annotatedString,
        color = color )
}

@Preview
@Composable
private fun TimeRunningScreenPreviewSongLight(){
    TimeRunningScreenPreviewCardSong(themeMode = ThemeMode.Light)
}

@Preview
@Composable
private fun TimeRunningScreenPreviewSongDark(){
    TimeRunningScreenPreviewCardSong(themeMode = ThemeMode.Dark)
}

@Composable
private fun TimeRunningScreenPreviewCardSong(themeMode: ThemeMode){
    NextTheme (themeMode = themeMode){
        val text = "Sufre , mamón, devuélveme a mi chica ... O te retorcerás entre polvos picapica - Hombres G"

        val cards = listOf(
            Card(text = text),
            Card(text = text),
        )
        Surface {
            SetTimeIsRunningScreen(
                round = RoundWithTeamAndModifier(
                    round = Round(type= Card.Category.FINISH_THE_SONG),
                    team = Team(name = "Equipo A")
                ),
                time = "00:05",
                progress = 0.5f,
                isPlaying = true,
                cardList = cards,
                addSeenCard = {},
                addPointsToScore = {},
                addMissCard = {},
                noMoreCards = {},
                startTimer = {}
            )
        }
    }
}

@Composable
fun CardView(
    modifier: Modifier,
    text: String,
    color: Color
){
    Card (modifier){
        Box (
            Modifier.background(color)
        ){
            BasicText(
                text = text,
                autoSize = TextAutoSize.StepBased(
                    maxFontSize = 40.sp
                ),
                style = MaterialTheme.typography.bodyMedium.copy(
                    textAlign = TextAlign.Center, // Apply text alignment
                    lineHeight = 50.sp,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 6.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
fun CardView(
    modifier: Modifier,
    text: AnnotatedString,
    color: Color
){
    Card (modifier){
        Box (
            Modifier.background(color)
        ){
            BasicText(
                text = text,
                autoSize = TextAutoSize.StepBased(
                    maxFontSize = 40.sp
                ),
                style = MaterialTheme.typography.bodyMedium.copy(
                    lineHeight = 50.sp,
                    textAlign = TextAlign.Center // Apply text alignment
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .padding(horizontal = 6.dp)
                    .wrapContentHeight(Alignment.CenterVertically)
                    .wrapContentWidth(Alignment.CenterHorizontally)
            )
        }
    }
}

