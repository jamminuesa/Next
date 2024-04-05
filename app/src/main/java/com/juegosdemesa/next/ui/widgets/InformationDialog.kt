package com.juegosdemesa.next.ui.widgets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.juegosdemesa.next.data.model.Card

@Composable
fun InformationDialog(
    title: String,
    text: String,
    onDismissRequest: () -> Unit
){
    AlertDialog(
        icon = {
            Icon(
                Icons.Filled.Info,
                tint = MaterialTheme.colorScheme.primary,
                contentDescription = "Info Icon")
        },
        title = {
            Text(text = title)
        },
        text = {
            Text(
                text = text,
                textAlign = TextAlign.Justify
            )
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {},
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Cerrar")
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun InformationDialogPreview(){
    InformationDialog(title = Card.Category.IMITATE.text, text = Card.Category.IMITATE.helpInfo) {

    }
}