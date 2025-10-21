package com.example.agromo.dashboard_ui

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agromo.data.AppDatabase
import com.example.agromo.data.FormularioEntity
import com.example.agromo.network.SessionManager
import com.example.agromo.ui.theme.*

private fun getCropIcon(cropType: String): String {
    return when (cropType.lowercase()) {
        "maíz" -> "🌽"
        "trigo" -> "🌾"
        "sorgo" -> "🌿"
        "cebada" -> "🥬"
        "avena" -> "🥬"
        "frijol" -> "🫘"
        "soya" -> "🌱"
        "Caña de azúcar" -> "🥬"
        "papa" -> "🥔"
        "tomate" -> "🍅"
        else -> "🧑‍🌾"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    refreshKey: String,
    onNavigateToProfile: () -> Unit,
    onNavigateToAiChat: () -> Unit,
    onNavigateToFormulario: () -> Unit,
    onNavigateToQuickActionEdit: (String) -> Unit,
    onNavigateToFormDetail: (String) -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val formularioDao = db.formularioDao()
// ViewModel con Factory, igual que ya tienes:
    val viewModel: DashboardViewModel = viewModel(factory = DashboardViewModelFactory(context.applicationContext as Application, formularioDao))

// Cambia a observar los valores del ViewModel:
    val nombre by viewModel.nombre.collectAsState()
    val username by viewModel.username.collectAsState()

    val saludo = "¡Buenos días!"
    val presentacion = if (!nombre.isNullOrBlank()) nombre else username
    val avatarText = initialsOf(nombre, username)

    val weatherState by viewModel.weatherState.collectAsState()
    val formularios by viewModel.formularios.collectAsState()



    LaunchedEffect(refreshKey) {
        viewModel.reloadUserInfo()
        viewModel.loadWeather()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary50)
    ) {
        // Pasa las variables al TopBarSection
        TopBarSection(
            onAvatarClick = onNavigateToProfile,
            saludo = saludo,
            presentacion = presentacion,
            avatarText = avatarText,
            onSyncClick = { viewModel.startSync(context, formularioDao) }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(24.dp))
                WeatherCard(
                    location = weatherState.locationName,
                    temperature = weatherState.temperature,
                    description = weatherState.description,
                    humidity = weatherState.humidity,
                    wind = weatherState.windSpeed,
                    rain = weatherState.precipitation,
                    sprayStatus = weatherState.sprayStatus
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                MonitoringCard(onStartMonitoring = onNavigateToFormulario)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                QuickActionsSection(onCardClick = onNavigateToQuickActionEdit)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                PhotographySection(onStartAiChat = onNavigateToAiChat)
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                 RecentReportsHeader()
                 Spacer(modifier = Modifier.height(10.dp))
            }

            if (formularios.isEmpty()) {
                item {
                    Text(
                        text = "No hay informes recientes.",
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Neutral700
                    )
                }
            } else {
                items(formularios) { formulario ->
                    val cropIcon = getCropIcon(formulario.cultivo)
                    ReportCard(
                        date = formulario.cultivo, // Using 'cultivo' for date for now
                        cropType = formulario.cultivo,
                        cropIcon = cropIcon,
                        title = "Informe de ${formulario.cultivo}",
                        status = "Completo", // Placeholder status
                        statusColor = Primary600Intermediary,
                        onClick = { onNavigateToFormDetail(formulario.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item {
                Spacer(modifier = Modifier.height(100.dp))
            }
        }
    }
}

@Composable
fun RecentReportsHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Resumen de informes recientes",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = ColorBlackWhiteBlack
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
        }
    }
}

@Composable
fun TopBarSection(
    onAvatarClick: () -> Unit,
    saludo: String,
    presentacion: String,
    avatarText: String,
    onSyncClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            IconButton(onClick = { onAvatarClick() }) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Neutral500),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = avatarText,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Column {
                Text(
                    text = saludo,
                    fontSize = 14.sp,
                    color = ColorBlackWhiteBlack
                )
                Text(
                    text = presentacion,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorBlackWhiteBlack
                )
            }
        }

        IconButton(onClick = { onSyncClick() }) {
            Icon(
                imageVector = Icons.Default.Sync,
                contentDescription = "Sincronizar",
                tint = ColorBlackWhiteBlack,
                modifier = Modifier.size(40.dp)
            )
        }

    }
}


@Composable
fun WeatherCard(
    location: String,
    temperature: String,
    description: String,
    humidity: String,
    wind: String,
    rain: String,
    sprayStatus: SprayStatus
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral50),
        border = androidx.compose.foundation.BorderStroke(2.dp, Primary800P)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = location,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorBlackWhiteBlack
                    )
                    Text(
                        text = description,
                        fontSize = 10.sp,
                        color = Neutral500
                    )
                    Text(
                        text = temperature,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }


                Box(
                    modifier = Modifier
                        .size(82.dp)
                        .background(Color.Yellow.copy(alpha = 0.8f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.WbSunny,
                        contentDescription = "Sun",
                        tint = Color(0xFFFF8C00),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WeatherItem("Humedad", humidity, Icons.Filled.Water)
                WeatherItem("Viento", wind, Icons.Filled.Air)
                WeatherItem("Lluvias", rain, Icons.Filled.Cloud)
                val (sprayText, sprayColor) = when(sprayStatus) {
                    SprayStatus.FAVORABLE -> "Favorable" to Primary800P
                    SprayStatus.UNFAVORABLE -> "Desfavorable" to Alert600P
                    SprayStatus.UNKNOWN -> "--" to Color.Black
                }
                WeatherItem("Pulverización", sprayText, Icons.Filled.Warning, sprayColor)
            }
        }
    }
}


@Composable
fun WeatherItem(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, valueColor: Color = Color.Black) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Neutral500,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 10.sp,
            color = Neutral500
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = valueColor
        )
    }
}

@Composable
fun MonitoringCard(
    onStartMonitoring: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(196.dp),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Primary800P)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.align(Alignment.TopStart),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Monitoreo de Cultivo",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorBlackWhiteWhite
                )
                Text(
                    text = "Complete los datos clave de tu cultivo paso a paso.",
                    fontSize = 14.sp,
                    color = ColorBlackWhiteWhite,
                    modifier = Modifier.width(200.dp)
                )
            }


            Button(
                onClick = { onStartMonitoring() },
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .background(Color.Transparent, RoundedCornerShape(100.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = ColorBlackWhiteWhite)
            ) {
                Text(
                    text = "Comenzar",
                    fontSize = 14.sp,
                    color = Primary900Dark
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .background(Primary800P, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Arrow",
                        tint = ColorBlackWhiteWhite,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }


            Box(
                modifier = Modifier
                    .size(80.dp)
                    .align(Alignment.BottomEnd)
                    .background(ColorBlackWhiteWhite.copy(alpha = 0.2f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Agriculture,
                    contentDescription = "Agriculture",
                    tint = ColorBlackWhiteWhite,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
    }
}

@Composable
fun QuickActionsSection(
    onCardClick: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Ingreso rápido",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ColorBlackWhiteBlack
            )
            Text(
                text = "Registre diagnósticos y otros datos clave en el momento.",
                fontSize = 14.sp,
                color = Neutral700
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard("Estado fenológico", Icons.Filled.Science, onClick = { onCardClick("Estado fenológico") })
            QuickActionCard("Densidad de follaje", Icons.Filled.DensityMedium, onClick = { onCardClick("Densidad de follaje") })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard("Color predominante", Icons.Filled.Palette, onClick = { onCardClick("Color predominante") })
            QuickActionCard("Humedad del suelo", Icons.Filled.Water, onClick = { onCardClick("Humedad del suelo") })
            QuickActionCard("PH del suelo", Icons.Filled.Biotech, onClick = { onCardClick("PH del suelo") })
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard("Altura de plantas", Icons.Filled.Height, onClick = { onCardClick("Altura de plantas") })
            QuickActionCard("Estado General de Follaje", Icons.Filled.Eco, onClick = { onCardClick("Estado General de Follaje") })
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun RowScope.QuickActionCard(
    title: String,
    icon: ImageVector,
    isAdd: Boolean = false,
    onClick: () -> Unit
    ) {


    Card(
        modifier = Modifier.weight(1f).
        clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Primary50),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .background(
                        if (isAdd) Primary800P else Color.Transparent,
                        RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (isAdd) ColorBlackWhiteWhite else Primary800P,
                    modifier = Modifier.size(48.dp)
                )
            }
            Text(
                text = title,
                fontSize = 14.sp,
                color = ColorBlackWhiteBlack,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun PhotographySection(
    onStartAiChat: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Fotografía de tus cultivos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ColorBlackWhiteBlack
            )
            Text(
                text = "Captura fotos y súmala al informe de tu cultivo.",
                fontSize = 14.sp,
                color = Neutral700
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Primary300)
        ) {
            Column(
                modifier = Modifier.padding(22.dp, 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally // ✅ centramos todo el contenido
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // ✅ mantenemos la estructura, pero comentamos el número
                    PhotoStepCard("1", "Tomar foto de cultivo", Icons.Filled.CameraAlt)
                }

                Button(
                    onClick = { onStartAiChat() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary900Dark),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Tomar foto",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Primary50
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoStepCard(number: String, title: String, icon: ImageVector) {
    Column(
        modifier = Modifier.width(113.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .background(Primary100, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Primary800P,
                modifier = Modifier.size(40.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            fontSize = 14.sp,
            color = Primary900Dark,
            lineHeight = 16.sp
        )
    }
}


@Composable
fun ReportCard(
    date: String,
    cropType: String,
    cropIcon: String,
    title: String,
    status: String,
    statusColor: Color,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Neutral50),
        border = androidx.compose.foundation.BorderStroke(1.dp, Primary800P)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(Primary300, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = cropIcon,
                        fontSize = 32.sp
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(42.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = date,
                            fontSize = 12.sp,
                            color = ColorBlackWhiteBlack
                        )
                    }

                    Text(
                        text = title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorBlackWhiteBlack
                    )

                    Box(
                        modifier = Modifier
                            .background(statusColor.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                            .border(1.dp, statusColor, RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = status,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = statusColor
                        )
                    }
                }
            }

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Ir al informe",
                tint = ColorBlackWhiteBlack,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
private fun initialsOf(nombre: String?, username: String): String {
    val parts = nombre?.trim()?.split(" ")?.filter { it.isNotBlank() } ?: emptyList()
    return if (parts.size >= 2) {
        parts.take(2).joinToString("") { it.first().uppercase() }
    } else if (parts.size == 1) {
        parts.first().first().uppercase().toString()
    } else {
        username.firstOrNull()?.uppercase()?.toString() ?: "?"
    }
}
