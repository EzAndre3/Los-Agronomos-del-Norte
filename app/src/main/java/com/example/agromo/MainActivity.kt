package com.example.agromo

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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
import android.Manifest
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import com.example.agromo.dashboard_ui.quickvalues.EditValueScreen
import androidx.navigation.navArgument

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        setContent {
            AgromoTheme {
                Box(
                    Modifier
                        .fillMaxSize()
                        .padding(WindowInsets.safeDrawing.asPaddingValues())
                ) {
                    AppContent()
                }
            }
        }

    }
}


@Composable
fun AppContent() {
    val navController = rememberNavController()
    var refreshQuickActions by remember { mutableStateOf(0) }

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
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToQuickActionEdit = { key -> navController.navigate("editValue/$key") }
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
        composable(
            route = "editValue/{key}",
            arguments = listOf(navArgument("key") { type = NavType.StringType })
        ) { backStackEntry ->
            val key = backStackEntry.arguments?.getString("key") ?: ""
            EditValueScreen(
                key = key,
                onBack = {
                    navController.popBackStack() }
            )
        }

    }
}
