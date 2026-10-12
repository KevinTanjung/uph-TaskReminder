package edu.uph.learn.taskreminder.navigation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import edu.uph.learn.taskreminder.data.auth.AuthService
import edu.uph.learn.taskreminder.ui.auth.EditPasswordScreen
import edu.uph.learn.taskreminder.ui.auth.LoginScreen
import edu.uph.learn.taskreminder.ui.auth.RegisterScreen
import edu.uph.learn.taskreminder.ui.auth.ResetPasswordScreen
import edu.uph.learn.taskreminder.ui.component.TaskBottomBar
import edu.uph.learn.taskreminder.ui.dashboard.DashboardScreen
import edu.uph.learn.taskreminder.ui.profile.ProfileScreen
import edu.uph.learn.taskreminder.ui.splash.SplashScreen
import edu.uph.learn.taskreminder.ui.theme.WarmBackground
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val RESET_PASSWORD = "reset_password"
    const val DASHBOARD = "dashboard"
    const val CALENDAR = "calendar"
    const val PROFILE = "profile"
    const val EDIT_PASSWORD = "edit_password"
    const val EDIT_SCHEDULE = "edit_schedule"
    const val CREATE_ASSIGNMENT = "create_assignment"
    const val VIEW_ASSIGNMENT = "view_assignment"
    const val EDIT_ASSIGNMENT = "edit_assignment"
}

private val mainRoutes = setOf(Routes.DASHBOARD, Routes.CALENDAR, Routes.PROFILE)
private val protectedRoutes = mainRoutes + setOf(
    Routes.CREATE_ASSIGNMENT,
    Routes.VIEW_ASSIGNMENT,
    Routes.EDIT_ASSIGNMENT
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    var splashTimeElapsed by remember { mutableStateOf(false) }
    var authStateResolved by remember { mutableStateOf(false) }
    var isAuthenticated by remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    DisposableEffect(Unit) {
        val listener = AuthService.addAuthStateListener { user ->
            Log.d("AppNavigation", "Current user: ${user?.uid ?: "None"}")
            isAuthenticated = user != null
            authStateResolved = true
        }
        onDispose {
            AuthService.removeAuthStateListener(listener)
        }
    }

    LaunchedEffect(Unit) {
        delay(1.seconds)
        splashTimeElapsed = true
    }

    LaunchedEffect(
        authStateResolved,
        splashTimeElapsed,
        isAuthenticated,
        currentRoute
    ) {
        if (!authStateResolved || !splashTimeElapsed) {
            return@LaunchedEffect
        }

        Log.d("AppNavigation", "Current route: $currentRoute, isAuthenticated: $isAuthenticated")

        when {
            // Startup destination.
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
            // Successful login or registration.
            isAuthenticated && currentRoute in setOf(Routes.LOGIN, Routes.REGISTER) -> {
                navController.navigate(Routes.DASHBOARD) {
                    popUpTo(Routes.LOGIN) { inclusive = true }
                    launchSingleTop = true
                }
            }
            // Logout: return to Login from any protected screen
            !isAuthenticated && currentRoute in protectedRoutes -> {
                navController.navigate(Routes.LOGIN) {
                    popUpTo(navController.graph.id) { inclusive = true }
                    launchSingleTop = true
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute in mainRoutes) {
                TaskBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    onAddClick = {
                        navController.navigate(Routes.CREATE_ASSIGNMENT)
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SPLASH,
            modifier = Modifier
                .padding(innerPadding)
                .background(WarmBackground)
        ) {
            composable(Routes.SPLASH) {
                SplashScreen()
            }
            composable(Routes.LOGIN) {
                LoginScreen(
                    onLoginSuccess = {
                        // Navigation is handled by AuthStateListener.
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
                        // Navigation is handled by AuthStateListener.
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
                    }
                )
            }
            composable(Routes.DASHBOARD) {
                DashboardScreen()
            }
            composable(Routes.CALENDAR) {
                PlaceholderScreen("Calendar")
            }
            composable(Routes.PROFILE) {
                ProfileScreen(
                    email = AuthService.getCurrentUser()?.email.orEmpty(),
                    onClickEditPassword = {
                        navController.navigate(Routes.EDIT_PASSWORD)
                    },
                    onClickReminderSettings = {
                        navController.navigate(Routes.EDIT_SCHEDULE)
                    },
                    onLogout = {
                        AuthService.signOut()
                    }
                )
            }
            composable(Routes.EDIT_PASSWORD) {
                EditPasswordScreen(
                    onBackToProfile = {
                        navController.popBackStack()
                    }
                )
            }
            composable(Routes.EDIT_SCHEDULE) {
                PlaceholderScreen("Edit Schedule")
            }
            composable(Routes.CREATE_ASSIGNMENT) {
                PlaceholderScreen("Create Assignment")
            }
            composable(Routes.VIEW_ASSIGNMENT) {
                PlaceholderScreen("View Assignment")
            }
            composable(Routes.EDIT_ASSIGNMENT) {
                PlaceholderScreen("Edit Assignment")
            }
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(WarmBackground)
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("$title Screen")
        }
    }
}
