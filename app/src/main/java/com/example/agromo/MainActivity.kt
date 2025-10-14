package com.example.agromo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.agromo.picture_ui.PictureScreen
import com.example.agromo.login_ui.ForgotPasswordScreen
import com.example.agromo.login_ui.LoginScreen
import com.example.agromo.login_ui.RegisterScreen
import com.example.agromo.ui.theme.AgromoTheme
import com.example.agromo.dashboard_ui.DashboardScreen
import com.example.agromo.profile_ui.ProfileScreen
import com.example.agromo.login_ui.WelcomeScreen
import com.example.agromo.formulario.RegistroFormularioScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AgromoTheme {
                AppContent()
            }
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "welcome") {
        composable("welcome") {
            WelcomeScreen(
                onRegisterClick = { navController.navigate("register") },
                onLoginClick = { navController.navigate("login") }
            )
        }

        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onNavigateToForgotPassword = { navController.navigate("forgot_password") },
                onNavigateToDashboard = { navController.navigate("dashboard") }
            )
        }
        composable("register") {
            RegisterScreen(
                onBackToLogin = { navController.popBackStack() }
            )
        }
        composable("forgot_password") {
            ForgotPasswordScreen(
                onBackToLogin = { navController.popBackStack() }
            )
        }
        composable("dashboard") {
            DashboardScreen(
                onNavigateToAiChat = { navController.navigate("aichat") },
                onNavigateToFormulario = { navController.navigate("formulario") },
                onNavigateToProfile = { navController.navigate("profile") }
            )
        }
        composable("formulario") {
            RegistroFormularioScreen(
                onNext = { navController.popBackStack() }, // al finalizar
                onBack = { navController.popBackStack() }  // si presiona la X o flecha en paso 0
            )
        }
        composable("profile") {
            ProfileScreen(
                onBack = {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onSave = { /* guardar */ },
                onLogout = {
                    navController.navigate("login") {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }
        composable("aichat") {
            PictureScreen(
                onNavigateBackToDashboard = { navController.navigate("dashboard") }
            )
        }
    }
}
