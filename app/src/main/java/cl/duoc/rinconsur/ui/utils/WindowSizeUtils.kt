package cl.duoc.rinconsur.ui.utils

import android.app.Activity
import android.content.ContextWrapper
import androidx.activity.compose.LocalActivity
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun obtenerWindowSizeClass(): WindowSizeClass {
    val context = LocalContext.current
    val activity = LocalActivity.current ?: generateSequence(context) {
        (it as? ContextWrapper)?.baseContext
    }.filterIsInstance<Activity>().firstOrNull() ?: (context as Activity)

    return calculateWindowSizeClass(activity)
}
