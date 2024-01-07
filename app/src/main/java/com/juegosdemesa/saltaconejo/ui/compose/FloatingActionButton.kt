package com.juegosdemesa.saltaconejo.ui.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.juegosdemesa.saltaconejo.R

@Composable
fun DefaultFloatingActionButton(
    onClick: () -> Unit,
    icon: ImageVector){
    FloatingActionButton(
        onClick = { onClick.invoke() },
        containerColor = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(50.dp)) {
        Icon(imageVector = icon,
            contentDescription = "")
    }
}


@Composable
fun ImageWithText(text: String){
    Image(
        painter = painterResource(id = R.drawable.background),
        contentDescription = "")

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp)
            .wrapContentWidth(Alignment.CenterHorizontally)
    )

}

@Composable
fun Test(){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        border = BorderStroke(2.dp, androidx.compose.ui.graphics.Color.Black),
        content = {
            Text("Card with border argument",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.labelLarge)
        }
    )
}

@Preview
@Composable
fun TestPreview(){
    Test()
}