package com.example.agromo.profile_ui

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.agromo.R
import com.example.agromo.network.SessionManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/* ---------- Modelo ---------- */

data class ProfileUiState(
    val fullName: String = "María González",
    val email: String = "",
    val phone: String = "",
    val company: String = "AWAQ",
    val role: String = "CEO",
    val location: String = ""
)

/* ---------- DataStore ---------- */

private val Context.dataStore by preferencesDataStore("profile_prefs")

private object ProfileKeys {
    val FULL_NAME = stringPreferencesKey("full_name")
    val EMAIL     = stringPreferencesKey("email")
    val PHONE     = stringPreferencesKey("phone")
    val COMPANY   = stringPreferencesKey("company")
    val ROLE      = stringPreferencesKey("role")
    val LOCATION  = stringPreferencesKey("location")
}

private suspend fun saveProfile(context: Context, profile: ProfileUiState) {
    context.dataStore.edit { p ->
        p[ProfileKeys.FULL_NAME] = profile.fullName
        p[ProfileKeys.EMAIL]     = profile.email
        p[ProfileKeys.PHONE]     = profile.phone
        p[ProfileKeys.COMPANY]   = profile.company
        p[ProfileKeys.ROLE]      = profile.role
        p[ProfileKeys.LOCATION]  = profile.location
    }
}

private fun readProfile(context: Context) = context.dataStore.data.map { p ->
    ProfileUiState(
        fullName = p[ProfileKeys.FULL_NAME] ?: "María González",
        email    = p[ProfileKeys.EMAIL]     ?: "",
        phone    = p[ProfileKeys.PHONE]     ?: "",
        company  = p[ProfileKeys.COMPANY]   ?: "AWAQ",
        role     = p[ProfileKeys.ROLE]      ?: "CEO",
        location = p[ProfileKeys.LOCATION]  ?: ""
    )
}

/* ---------- Header reutilizable ---------- */

@Composable
fun AgromoHeader() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo_agromo),
            contentDescription = "Logo",
            tint = Color(0xFF2E7D32),
            modifier = Modifier.size(80.dp)
        )
    }
}

/* ---------- UI ---------- */

private enum class ProfileTab(val label: String) {
    PERSONAL("Información personal"),
    SETTINGS("Configuración"),
    NOTIFICATIONS("Notificaciones")
}

private enum class ThemeOption { SYSTEM, LIGHT, DARK }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBack: () -> Unit,
    onSave: (ProfileUiState) -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val uiState = remember { mutableStateOf(ProfileUiState()) }
    var tab by remember { mutableStateOf(ProfileTab.PERSONAL) }

    LaunchedEffect(Unit) {
        uiState.value = readProfile(context).first()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { AgromoHeader() },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Atrás"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2E7D32)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initialsOf(uiState.value.fullName),
                            fontSize = 28.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(Modifier.height(12.dp))

                    Text(uiState.value.fullName, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Text(uiState.value.role, color = Color.Gray)
                    Text(uiState.value.company, color = Color.Gray)

                    Spacer(Modifier.height(16.dp))

                    // Tabs
                    ScrollableTabRow(
                        selectedTabIndex = tab.ordinal,
                        containerColor = Color.Transparent,
                        contentColor = Color(0xFF2E7D32),
                        edgePadding = 0.dp,
                        divider = {}
                    ) {
                        ProfileTab.values().forEach { t ->
                            Tab(
                                selected = t == tab,
                                onClick = { tab = t },
                                selectedContentColor = Color(0xFF2E7D32),
                                unselectedContentColor = Color.Gray,
                                text = { Text(t.label, maxLines = 1, overflow = TextOverflow.Ellipsis, fontSize = 13.sp) }
                            )
                        }
                    }

                    Spacer(Modifier.height(8.dp))

                    when (tab) {
                        ProfileTab.PERSONAL -> {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                PersonalForm(
                                    state = uiState.value,
                                    onChange = { uiState.value = it }
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            val current = uiState.value
                                            scope.launch {
                                                saveProfile(context, current)
                                            }
                                            onSave(current)
                                            onBack()
                                        },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                                    ) { Text("Guardar Cambios", color = Color.White) }

                                    OutlinedButton(
                                        onClick = onBack,
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(12.dp)
                                    ) { Text("Cancelar") }
                                }

                                // 🔹 Botón de Cerrar Sesión
                                OutlinedButton(
                                    onClick = {
                                        val sessionManager = SessionManager(context)
                                        scope.launch(Dispatchers.IO) {
                                            sessionManager.clearSession()
                                            snackbarHostState.showSnackbar("Sesión cerrada correctamente")
                                        }
                                        onLogout()
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                ) { Text("Cerrar sesión") }
                            }
                        }

                        ProfileTab.SETTINGS -> {
                            SettingsSection()
                        }

                        ProfileTab.NOTIFICATIONS -> {
                            NotificationsSection()
                        }
                    }
                }
            }
        }
    }
}

/* ---------- Subvistas ---------- */

@Composable
private fun PersonalForm(state: ProfileUiState, onChange: (ProfileUiState) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        LabeledField("Nombre Completo", state.fullName) { onChange(state.copy(fullName = it)) }
        LabeledField("Correo Electrónico", state.email) { onChange(state.copy(email = it)) }
        LabeledField("Teléfono", state.phone) { onChange(state.copy(phone = it)) }
        LabeledField("Empresa", state.company) { onChange(state.copy(company = it)) }
        LabeledField("Cargo", state.role) { onChange(state.copy(role = it)) }
        LabeledField("Ubicación", state.location) { onChange(state.copy(location = it)) }
    }
}

@Composable
private fun LabeledField(label: String, value: String, onValue: (String) -> Unit) {
    Column {
        Text(label, fontSize = 13.sp, color = Color.Gray)
        OutlinedTextField(
            value = value,
            onValueChange = onValue,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )
    }
}

// Tema con Sistema / Claro / Oscuro + GPS switch
@Composable
private fun SettingsSection() {
    var theme by remember { mutableStateOf(ThemeOption.SYSTEM) }
    var gpsEnabled by remember { mutableStateOf(false) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Text("Preferencias", fontWeight = FontWeight.SemiBold)

        AssistChip(
            onClick = {
                theme = when (theme) {
                    ThemeOption.SYSTEM -> ThemeOption.LIGHT
                    ThemeOption.LIGHT -> ThemeOption.DARK
                    ThemeOption.DARK -> ThemeOption.SYSTEM
                }
            },
            label = {
                Text(
                    when (theme) {
                        ThemeOption.SYSTEM -> "Tema: Sistema"
                        ThemeOption.LIGHT -> "Tema: Claro"
                        ThemeOption.DARK -> "Tema: Oscuro"
                    }
                )
            }
        )

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("GPS")
            Spacer(Modifier.weight(1f))
            Switch(checked = gpsEnabled, onCheckedChange = { gpsEnabled = it })
        }
    }
}

// Notificaciones con un switch
@Composable
private fun NotificationsSection() {
    var enabled by remember { mutableStateOf(true) }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        Text("Notificaciones", fontWeight = FontWeight.SemiBold)
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text("Activado")
            Spacer(Modifier.weight(1f))
            Switch(checked = enabled, onCheckedChange = { enabled = it })
        }
    }
}

/* ---------- Helpers ---------- */

private fun initialsOf(name: String): String =
    name.trim().split(" ").filter { it.isNotBlank() }.take(2)
        .joinToString("") { it.first().uppercase() }
        .ifBlank { "MG" }
