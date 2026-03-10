package com.juegosdemesa.next.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.BrightnessAuto
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juegosdemesa.next.R
import com.juegosdemesa.next.ui.navigation.NavigationDestination
import com.juegosdemesa.next.ui.theme.NextTheme
import com.juegosdemesa.next.ui.theme.ThemeMode

object HomeDestination: NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
    navigateToNewGame:() -> Unit,
){
    Scaffold(
        topBar = {
            Box(modifier = Modifier.statusBarsPadding()){
                ThemeSwitcher(
                    themeMode = themeMode,
                    onThemeChange = onThemeChange,
                )
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(32.dp) // Añadimos más padding interno
                .fillMaxHeight() // Ocupa toda la altura
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            // Esto centrará el texto y espaciará el botón
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Text(
                text = "Next",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )

            // El texto de bienvenida que pediste
            Text(
                text = "El juego en el que tendrás que describir, hacer mímica y hacer sonidos para ganar",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )

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

/**
 * Un Composable para el selector de tema (Día / Noche / Sistema).
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ThemeSwitcher(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
) {
    val options = listOf(
        Pair(ThemeMode.Light, Icons.Default.Brightness7),
        Pair(ThemeMode.System, Icons.Default.BrightnessAuto),
        Pair(ThemeMode.Dark, Icons.Default.Brightness4)
    )

    Box (
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .padding(top = 16.dp),
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {

        }
        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.align(Alignment.Center),
        ) {
            options.forEachIndexed { index, (mode, icon) ->
                SegmentedButton (
                    shape = SegmentedButtonDefaults.itemShape(index = index, count = options.size),
                    onClick = { onThemeChange(mode) },
                    selected = themeMode == mode
                ) {
                    Icon(imageVector = icon, contentDescription = mode.name)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewHomeScreenLight(){
    NextTheme(themeMode = ThemeMode.Light) {
        HomeScreen (
            themeMode = ThemeMode.Light,
            onThemeChange = {},
            navigateToNewGame = {}
        )
    }
}

@Preview
@Composable
fun PreviewHomeScreenDark(){
    NextTheme(themeMode = ThemeMode.Dark) {
        HomeScreen (
            themeMode = ThemeMode.Dark,
            onThemeChange = {},
            navigateToNewGame = {}
        )
    }
}
