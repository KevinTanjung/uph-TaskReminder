package edu.uph.learn.taskreminder.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import edu.uph.learn.taskreminder.ui.authentication.LoginScreen
import edu.uph.learn.taskreminder.ui.authentication.RegisterScreen
import edu.uph.learn.taskreminder.ui.authentication.ResetPasswordScreen
import edu.uph.learn.taskreminder.ui.dashboard.DashboardScreen
import edu.uph.learn.taskreminder.ui.splash.SplashScreen
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val RESET_PASSWORD = "reset_password"
    const val DASHBOARD = "dashboard"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val auth = remember { FirebaseAuth.getInstance() }

    var splashTimeElapsed by remember { mutableStateOf(false) }
    var authStateResolved by remember { mutableStateOf(false) }
    var isAuthenticated by remember { mutableStateOf(false) }

    DisposableEffect(auth) {
        val listener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            Log.d("AppNavigation", "Current user: ${user?.uid ?: "None"}")
            isAuthenticated = user != null
            authStateResolved = true
        }
        auth.addAuthStateListener(listener)
        onDispose {
            auth.removeAuthStateListener(listener)
        }
    }

    LaunchedEffect(Unit) {
        delay(1.seconds)
        splashTimeElapsed = true
    }

    LaunchedEffect(authStateResolved, splashTimeElapsed, isAuthenticated) {
        if (!authStateResolved || !splashTimeElapsed) {
            return@LaunchedEffect
        }

        val currentRoute = navController.currentDestination?.route
        Log.d("AppNavigation", "Current route: $currentRoute, isAuthenticated: $isAuthenticated")

        when {
            currentRoute == Routes.SPLASH -> {
                val destination = if (isAuthenticated) {
                    Routes.DASHBOARD
                } else {
                    Routes.LOGIN
                }
                navController.navigate(destination) {
                    popUpTo(Routes.SPLASH) { inclusive = true }
                    launchSingleTop = true
                }
            }
            !isAuthenticated && currentRoute == Routes.DASHBOARD -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.DASHBOARD) { inclusive = true }
                    launchSingleTop = true
                }
            }
            isAuthenticated && currentRoute == Routes.LOGIN -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(Routes.DASHBOARD) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH
    ) {
        composable(Routes.SPLASH) {
            SplashScreen()
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onClickRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onClickResetPassword = {
                    navController.navigate(Routes.RESET_PASSWORD)
                }
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Routes.DASHBOARD) {
                        popUpTo(Routes.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }
        composable(Routes.RESET_PASSWORD) {
            ResetPasswordScreen(
                onBackToLogin = {
                    navController.popBackStack()
                },
                onResetPassword = {
                    navController.navigate(Routes.LOGIN)
                }
            )
        }
        composable(Routes.DASHBOARD) {
            DashboardScreen(
                onLogout = {
                    auth.signOut()
                }
            )
        }
    }
}
