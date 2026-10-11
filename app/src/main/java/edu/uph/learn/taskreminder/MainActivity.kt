package edu.uph.learn.taskreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import edu.uph.learn.taskreminder.navigation.AppNavigation
import edu.uph.learn.taskreminder.ui.theme.TaskReminderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            TaskReminderTheme {
                AppNavigation()
            }
        }
    }
}
