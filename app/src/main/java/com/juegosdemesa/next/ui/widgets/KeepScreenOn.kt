package com.juegosdemesa.next.ui.widgets

import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import com.juegosdemesa.next.util.findActivity

@Composable
fun KeepScreenOn() {
    val context = LocalContext.current
    // Find the Activity from the current Composable's context
    val activity = context.findActivity()

    // DisposableEffect is used to perform side effects that need cleanup.
    // It's ideal for managing lifecycle-aware operations like adding/removing Window flags.
    DisposableEffect(activity) {
        // When the Composable enters the composition (is displayed)
        // Add the FLAG_KEEP_SCREEN_ON to prevent the screen from turning off.
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // When the Composable leaves the composition (is hidden, navigated away from, or destroyed)
        // This 'onDispose' block is called to clean up resources.
        // Remove the FLAG_KEEP_SCREEN_ON to allow the screen to turn off normally.
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}