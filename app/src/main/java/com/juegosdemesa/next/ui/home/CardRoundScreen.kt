package com.juegosdemesa.next.ui.home


import android.annotation.SuppressLint
import android.util.Log
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.res.colorResource
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.SwipeableCardState
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.juegosdemesa.next.R
import com.juegosdemesa.next.data.model.Card
import com.juegosdemesa.next.data.model.RoundWithTeamAndModifier
import com.juegosdemesa.next.ui.navigation.NavigationDestination
import com.juegosdemesa.next.ui.theme.Typography
import com.juegosdemesa.next.ui.widgets.BigCircleButton
import com.juegosdemesa.next.ui.widgets.InformationDialog
import com.juegosdemesa.next.ui.widgets.KeepScreenOn
import com.juegosdemesa.next.ui.widgets.autotextsize.AutoSizeText
import com.juegosdemesa.next.util.Utility
import com.juegosdemesa.next.util.Utility.formatTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object CardRoundDestination: NavigationDestination {
    override val route = "cardsDisplay"
    override val titleRes = R.string.app_name
}

private lateinit var countDownViewModel: CountDownViewModel
private lateinit var cardStates: List<Pair<Card, SwipeableCardState>>
private lateinit var cardViewModel: CardViewModel
private lateinit var gameViewModel: GameViewModel
private lateinit var scope: CoroutineScope

@Composable
fun CardRoundScreen(
    navigateToNextRound: () -> Unit,
    navigateToEndGame: () -> Unit,
    viewModel: GameViewModel
) {
    KeepScreenOn()
    gameViewModel = viewModel
    cardViewModel = hiltViewModel()
    countDownViewModel = viewModel()
    scope = rememberCoroutineScope()

    val timeIsUp by countDownViewModel.isTimeUp.observeAsState(false)
    val round by gameViewModel.round.collectAsState()
    if (round != null){
        cardViewModel.setCardCategory(round!!.round.type)
    }


    AnimatedVisibility(
        visible = !timeIsUp && round != null,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        if (round != null){
            SetTimeIsRunningScreen(
                round!!
            )
        }

    }

    AnimatedVisibility(
        visible = timeIsUp,
        enter = fadeIn(),
        exit = fadeOut()) {
        SetTimeIsUpScreen(
            navigateToNextRound,
            navigateToEndGame
        )
    }
}

@Composable
@OptIn(ExperimentalSwipeableCardApi::class)
private fun SetTimeIsRunningScreen(
    round: RoundWithTeamAndModifier
) {
    val time by countDownViewModel.time.observeAsState(Utility.TIME_COUNTDOWN.formatTime())
    val progress by countDownViewModel.progress.observeAsState(1.00F)
    val isPlaying by countDownViewModel.isPlaying.observeAsState(false)
    val cardUiState by cardViewModel.cardUiState.collectAsState()
    val openInformationDialog = remember { mutableStateOf(false)  }

    cardStates = cardUiState.itemList
        .toMutableList()
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
            text = round.team.name,
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
            cardStates.forEach { (card, state) ->
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
                                color = round.round.type.color
                            )
                        } else {
                            CardView(
                                modifier = modifier,
                                text = card.text,
                                color = round.round.type.color
                            )
                        }
                }
                LaunchedEffect(card, state.swipedDirection) {
                    if (state.swipedDirection != null) {
                            cardViewModel.addSeenCard(card)
                        if (state.swipedDirection == Direction.Right) {
                            cardViewModel.addPointsToScore(card.points)
                        } else if (state.swipedDirection == Direction.Left) {
                            cardViewModel.addMissCard()
                        }

                        //Check if there are more cards
                        if (cardUiState.itemList.last().id == card.id) {
                            countDownViewModel.noMoreCards()
                        }
                    }
                }
            }
        }
        ButtonsNTimer(
            roundModifierText = round.modifier?.text ?: "",
            progress = progress,
            time = time,
            isPlaying = isPlaying
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
    isPlaying: Boolean
){
    Column(Modifier
        .layoutId("bottomRef"),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AutoSizeText(
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
                .padding(5.dp),
            text = roundModifierText,
            alignment = Alignment.Center
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
                onClick = { onErrorButton(isPlaying) },
                icon = Icons.Default.Close
            )
            BigCircleButton(
                onClick = { onAcceptButton(isPlaying) },
                icon = Icons.Default.Check
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonSNTimerPreview(){
    ButtonsNTimer(
        roundModifierText = "",
        progress = 0.5f,
        time = "00:45",
        isPlaying = true
    )
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
private fun SetTimeIsUpScreen(
    navigateToNextRound: () -> Unit,
    navigateToEndGame: () -> Unit
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
                cardViewModel.increaseTimesSeenCards()
                navigateToNextRound.invoke()
            }
            ) {
                Text(text = "Turno de ${nextTeam.team.name}")
            }
        }
    }
}


private fun onAcceptButton(isPlaying: Boolean){
    if (!isPlaying){
        countDownViewModel.startTimer()
    }
    scope.launch {
        val last = cardStates.reversed()
            .firstOrNull {
                it.second.offset.value == Offset(0f, 0f)
            }?.second

        last?.swipe(Direction.Right)
    }
}

private fun onErrorButton(isPlaying: Boolean){
    if (isPlaying){
        scope.launch {
            val last = cardStates.reversed()
                .firstOrNull {
                    it.second.offset.value == Offset(0f, 0f)
                }?.second
            last?.swipe(Direction.Left)
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
                color = colorResource(R.color.black),
                stroke
            )

            CircularProgressIndicator(
                progress = animatedProgress,
                modifier = Modifier
                    .height(size.dp)
                    .width(size.dp),
                strokeWidth = stroke.dp,
            )

            Column(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = time,
                    color = Color.Black,
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
            withStyle(style = SpanStyle(color = Color.Black)) {
                append(text.substring(0, text.indexOf(" ... ") + 1) )
            }
            withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontSize = 60.sp)) {
                append(text.substring(text.indexOf(" ... ") + 1,
                    text.indexOf(" ... ") + 4 )
                )
            }

            withStyle(style = SpanStyle(color = Color.Black,
                textDecoration = TextDecoration.Underline)
            ) {
                append(text.substring(text.indexOf(" ... ") + 4, text.indexOf(" - ")))
            }

            withStyle(style = SpanStyle(fontSize = 20.sp)) {
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

@Preview(showBackground = true)
@Composable
private fun CardViewSongPreview(){
    val text = "Sufre , mamón, devuélveme a mi chica ... O te retorcerás entre polvos picapica - Hombres G"
    CardViewFinishSong(
        modifier =  Modifier,
        text = text,
        Color.White
    )
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
            AutoSizeText(
                text = text,
                maxTextSize = 40.sp,
//                lineHeight = 50.sp,
//                textAlign = TextAlign.Center,
                alignment = Alignment.Center,
                style = MaterialTheme.typography.bodyMedium,
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
            AutoSizeText(
                text = text,
                maxTextSize = 40.sp,
                alignment = Alignment.Center,
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

