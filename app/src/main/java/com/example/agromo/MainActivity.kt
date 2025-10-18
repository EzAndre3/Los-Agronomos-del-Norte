package com.example.agromo

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.agromo.dashboard_ui.DashboardScreen
import com.example.agromo.dashboard_ui.quickvalues.EditValueScreen
import com.example.agromo.form_detail_ui.FormDetailScreen
import com.example.agromo.formulario.RegistroFormularioScreen
import com.example.agromo.login_ui.ForgotPasswordScreen
import com.example.agromo.login_ui.LoginScreen
import com.example.agromo.login_ui.RegisterScreen
import com.example.agromo.login_ui.WelcomeScreen
import com.example.agromo.picture_ui.PictureScreen
import com.example.agromo.profile_ui.ProfileScreen
import com.example.agromo.ui.theme.AgromoTheme
import com.example.agromo.network.SessionManager
import kotlinx.coroutines.launch
import androidx.core.view.WindowCompat

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Solicitar permisos de ubicación
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }

        // ------ FORZAR status bar icons EN NEGRO ------
        WindowCompat.setDecorFitsSystemWindows(window, true)
        window.statusBarColor = Color.White.toArgb() // fondo blanco status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
            )
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
        // ----------------------------------------------

        setContent {
            AgromoTheme {
                // aseguramos status bar color BLANCO en cada recomposición
                SideEffect {
                    window.statusBarColor = Color.White.toArgb()
                }
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

    @Composable
    fun AppContent() {
        val navController = rememberNavController()
        var startDestination by remember { mutableStateOf<String?>(null) }

        // 🔹 Verificar sesión guardada
        LaunchedEffect(Unit) {
            val sessionManager = SessionManager(applicationContext)
            val token = sessionManager.getToken()
            startDestination = if (token != null && token.isNotEmpty()) {
                "dashboard"
            } else {
                "welcome"
            }
        }

        // 🔸 Solo cargamos el NavHost cuando ya sabemos a dónde ir
        if (startDestination != null) {
            NavHost(navController = navController, startDestination = startDestination!!) {
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
                        onNavigateToDashboard = {
                            navController.navigate("dashboard") {
                                popUpTo("welcome") { inclusive = true }
                                launchSingleTop = true
                            }
                        }
                    )
                }

                composable("register") {
                    RegisterScreen(
                        onBackToLogin = { navController.popBackStack() },
                        navController = navController
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
                        onNavigateToQuickActionEdit = { key ->
                            navController.navigate("editValue/$key")
                        },
                        onNavigateToFormDetail = { formId ->
                            navController.navigate("form_detail/$formId")
                        }
                    )
                }

                composable("formulario") {
                    RegistroFormularioScreen(
                        onNext = { navController.popBackStack() },
                        onBack = { navController.popBackStack() }
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
                        onSave = { /* guardar cambios */ },
                        onLogout = {
                            val sessionManager = SessionManager(applicationContext)
                            lifecycleScope.launch {
                                sessionManager.clearSession()
                            }
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
                        onBack = { navController.popBackStack() }
                    )
                }
                
                composable(
                    route = "form_detail/{formId}",
                    arguments = listOf(navArgument("formId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val formId = backStackEntry.arguments?.getString("formId")
                    if (formId != null) {
                        FormDetailScreen(
                            formId = formId,
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
