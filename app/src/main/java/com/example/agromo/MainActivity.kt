package com.example.agromo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.agromo.dashboard_ui.DashboardScreen
import com.example.agromo.dashboard_ui.quickvalues.EditValueScreen
import com.example.agromo.formulario.FormularioListViewModel
import com.example.agromo.formulario.FormularioRepository
import com.example.agromo.formulario.RegistroFormularioScreen
import com.example.agromo.formulario_detail.FormularioDetailScreen
import com.example.agromo.login_ui.ForgotPasswordScreen
import com.example.agromo.login_ui.LoginScreen
import com.example.agromo.login_ui.RegisterScreen
import com.example.agromo.login_ui.WelcomeScreen
import com.example.agromo.picture_ui.PictureScreen
import com.example.agromo.profile_ui.ProfileScreen
import com.example.agromo.ui.theme.AgromoTheme

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "formularios_datastore")

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
    val context = LocalContext.current
    val formularioRepository = remember { FormularioRepository(context) }
    val formularioViewModel: FormularioListViewModel = viewModel(factory = FormularioListViewModel.Factory(formularioRepository))
    val formularios by formularioViewModel.formularios.collectAsState()

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
                formularios = formularios,
                onNavigateToAiChat = { navController.navigate("aichat") },
                onNavigateToFormulario = { navController.navigate("formulario") },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToQuickActionEdit = { key -> navController.navigate("editValue/$key") },
                onNavigateToFormularioDetalle = { formularioId -> navController.navigate("formularioDetalle/$formularioId") }
            )
        }
        composable("formulario") {
            RegistroFormularioScreen(
                formularioViewModel = formularioViewModel,
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
        composable("formularioDetalle/{formularioId}") { backStackEntry ->
            val formularioId = backStackEntry.arguments?.getString("formularioId") ?: ""
            val formulario = formularioViewModel.getFormularioById(formularioId)
            if (formulario != null) {
                FormularioDetailScreen(formulario = formulario)
            }
        }

    }
}
