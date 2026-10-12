package edu.uph.learn.taskreminder.ui.component

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import edu.uph.learn.taskreminder.ui.theme.Completed
import edu.uph.learn.taskreminder.ui.theme.Danger

enum class SnackbarType {
    SUCCESS,
    ERROR
}

@Composable
fun Snackbar(
    snackbarData: SnackbarData,
    type: SnackbarType
) {
    Snackbar(
        snackbarData = snackbarData,
        containerColor = when (type) {
            SnackbarType.SUCCESS -> Completed
            SnackbarType.ERROR -> Danger
        },
        contentColor = Color.White,
        actionColor = Color.White
    )
}
