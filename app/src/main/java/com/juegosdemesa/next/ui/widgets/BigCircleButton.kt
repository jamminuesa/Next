package com.juegosdemesa.next.ui.widgets

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun BigCircleButton(
    onClick: () -> Unit,
    icon: ImageVector
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