package com.juegosdemesa.saltaconejo.ui.home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.alexstyl.swipeablecard.Direction
import com.alexstyl.swipeablecard.ExperimentalSwipeableCardApi
import com.alexstyl.swipeablecard.SwipeableCardState
import com.alexstyl.swipeablecard.rememberSwipeableCardState
import com.alexstyl.swipeablecard.swipableCard
import com.juegosdemesa.saltaconejo.R
import com.juegosdemesa.saltaconejo.data.Card
import com.juegosdemesa.saltaconejo.data.CountDownViewModel
import com.juegosdemesa.saltaconejo.ui.navigation.NavigationDestination
import com.juegosdemesa.saltaconejo.util.Utility
import com.juegosdemesa.saltaconejo.util.Utility.formatTime
import com.juegosdemesa.saltaconejo.util.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object QuestionsDestination: NavigationDestination {
    override val route = "cardsDisplay"
    override val titleRes = R.string.app_name
}

private lateinit var countDownViewModel: CountDownViewModel
private lateinit var cardStates: List<Pair<Card, SwipeableCardState>>
private lateinit var cardViewModel: CardViewModel
private lateinit var scope: CoroutineScope

@OptIn(ExperimentalSwipeableCardApi::class, ExperimentalCoilApi::class)
@Preview(showBackground = true)
@Composable
fun CardsDisplayScreen(
//    cardViewModel: CardViewModel = hiltViewModel()
){
    cardViewModel = viewModel(factory = AppViewModelProvider.Factory)
    countDownViewModel = viewModel()

    val context = LocalContext.current
    val cardUiState by cardViewModel.cardUiState.collectAsState()

    cardStates = cardUiState.itemList.reversed()
        .map { it to rememberSwipeableCardState() }
    scope = rememberCoroutineScope()


    val time by countDownViewModel.time.observeAsState(Utility.TIME_COUNTDOWN.formatTime())
    val progress by countDownViewModel.progress.observeAsState(1.00F)
    val isPlaying by countDownViewModel.isPlaying.observeAsState(false)
    val timeIsUp by countDownViewModel.isTimeUp.observeAsState(false)

    AnimatedVisibility(
        visible = !timeIsUp,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .paint( // Background image
                    painter = rememberImagePainter(R.drawable.double_bubble),
                    contentScale = ContentScale.Crop

                ),
            verticalArrangement = Arrangement.SpaceEvenly
        ){
            Box(
                Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ){
                cardStates.forEach{ (card, state) ->
                    if (state.swipedDirection == null){
                        // En caso de que no se de al botón, no se puede quitar la primera tarjeta
                        val modifier = if (isPlaying){
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
                        CardView(
                            modifier = modifier,
                            text = card.text
                        )
                    }
                    LaunchedEffect(card, state.swipedDirection) {
                        if (state.swipedDirection != null) {
                            if (state.swipedDirection == Direction.Right){
                                cardViewModel.addPointsToScore(card.points)
                            } else if (state.swipedDirection == Direction.Left){
                                cardViewModel.addMissCard()
                            }

                            //Check if there are more cards
                            if (cardUiState.itemList.last().id == card.id){
                                countDownViewModel.noMoreCards()
                            }
                        }
                    }
                }
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (timeIsUp){
                    context.toast("Se ha acabado el tiempo")
                }

                CountDownIndicator(
                    Modifier.padding(top = 10.dp),
                    progress = progress,
                    time = time,
                    size = 100,
                    stroke = 12
                )
                Row(Modifier
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
                AnimatedVisibility(
                    visible = !timeIsUp,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {

                }
            }
        }
    }

    AnimatedVisibility(
        visible = timeIsUp,
        enter = fadeIn(),
        exit = fadeOut()) {
        SetTimeIsUpScreen()
    }
}
@Preview(showBackground = true)
@Composable
private fun SetTimeIsUpScreen(){
    val score by cardViewModel.score.collectAsState()
    val miss by cardViewModel.miss.collectAsState()
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
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Ir a la siguiente pantalla")
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


@Composable
private fun BigCircleButton(
    onClick: () -> Unit,
    icon:ImageVector
){
    FloatingActionButton(
        onClick = onClick,
        modifier = Modifier
            .padding(32.dp)
            .size(100.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(50.dp)
    ){
        Icon(imageVector = icon,
            modifier = Modifier.size(60.dp),
            contentDescription = "")

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
fun CardView(
    modifier: Modifier,
    text: String
){
    Card (modifier){
        Box {
            Text(
                text = text,
                fontSize = 40.sp,
                lineHeight = 50.sp,
                textAlign = TextAlign.Center,
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

