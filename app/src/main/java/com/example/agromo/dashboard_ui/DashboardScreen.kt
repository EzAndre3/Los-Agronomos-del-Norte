package com.example.agromo.dashboard_ui
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agromo.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToProfile: () -> Unit,
    onNavigateToAiChat: () -> Unit,
    onNavigateToFormulario: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary50)
    ) {

        TopBarSection(onAvatarClick = onNavigateToProfile)

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            WeatherCard()

            Spacer(modifier = Modifier.height(24.dp))

            MonitoringCard(onStartMonitoring = onNavigateToFormulario)

            Spacer(modifier = Modifier.height(24.dp))

            MyCropsSection()

            Spacer(modifier = Modifier.height(24.dp))

            QuickActionsSection()

            Spacer(modifier = Modifier.height(24.dp))

            PhotographySection(onStartAiChat = onNavigateToAiChat)

            Spacer(modifier = Modifier.height(24.dp))

            RecentReportsSection()

            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

@Composable
fun TopBarSection(onAvatarClick: () -> Unit) {
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
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Avatar",
                        tint = ColorBlackWhiteWhite,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Column {
                Text(
                    text = "¡Buenos días!",
                    fontSize = 14.sp,
                    color = ColorBlackWhiteBlack
                )
                Text(
                    text = "María Pia",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorBlackWhiteBlack
                )
            }
        }

        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = "Menu",
            tint = ColorBlackWhiteBlack,
            modifier = Modifier.size(24.dp)
        )
    }
}


@Composable
fun WeatherCard() {
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
                        text = "Cuenca, Colombia",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorBlackWhiteBlack
                    )
                    Text(
                        text = "Lunes, 12PM, Parcialmente soleado",
                        fontSize = 10.sp,
                        color = Neutral500
                    )
                    Text(
                        text = "23ºC",
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
                WeatherItem("Humedad", "48%", Icons.Filled.Water)
                WeatherItem("Viento", "10 Km/h", Icons.Filled.Air)
                WeatherItem("Lluvias", "22%", Icons.Filled.Cloud)
                WeatherItem("Pulverización", "Desfavorable", Icons.Filled.Warning, Alert600P)
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
                        imageVector = Icons.Filled.ArrowForward,
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
fun MyCropsSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Mis cultivos",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = ColorBlackWhiteBlack
            )
            Text(
                text = "Acceda y gestione tus cultivos",
                fontSize = 14.sp,
                color = Neutral700
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Primary800P.copy(alpha = 0.15f))
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            val crops = listOf("🫒", "🍆", "☕", "🌶️", "🎃", "🌽", "🍅", "🥕")
            crops.take(8).forEach { emoji ->
                CropItem(emoji)
            }
        }
    }
}

@Composable
fun CropItem(emoji: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.width(64.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .border(4.dp, Secondary700P, CircleShape)
                .background(ColorBlackWhiteWhite, CircleShape)
                .padding(6.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun QuickActionsSection() {
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
                text = "Registre diagnósticos de plagas, malezas y otros datos en el momento.",
                fontSize = 14.sp,
                color = Neutral700
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard("Añada nuevo\ncultivo", Icons.Filled.Add, true)
            QuickActionCard("Estado\nfenológico", Icons.Filled.Science)
            QuickActionCard("Plagas y\nenfermedades", Icons.Filled.BugReport)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard("Malezas y\ncompetencia", Icons.Filled.Grass)
            QuickActionCard("Humedad\ndel suelo", Icons.Filled.Water)
            QuickActionCard("PH del\nsuelo", Icons.Filled.Biotech)
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionCard("Altura de\nplantas", Icons.Filled.Height)
            QuickActionCard("Fertilidad\ndel suelo", Icons.Filled.Landscape)
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun RowScope.QuickActionCard(title: String, icon: androidx.compose.ui.graphics.vector.ImageVector, isAdd: Boolean = false) {
    Card(
        modifier = Modifier.weight(1f),
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(38.dp)
                ) {
                    PhotoStepCard("1", "Tomar foto\nde cultivo", Icons.Filled.CameraAlt)
                    PhotoStepCard("2", "Añádala a\nsu informe", Icons.Filled.Description)
                }

                Button(
                    onClick = {onStartAiChat() },
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
fun PhotoStepCard(number: String, title: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
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
            Box(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(10.dp, 8.dp)
                    .size(32.dp)
                    .background(Primary800P, RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = number,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = ColorBlackWhiteWhite
                )
            }

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
fun RecentReportsSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Resumen de informes\nrecientes",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = ColorBlackWhiteBlack
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Ver todos",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Primary800P
                )
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Ver todos",
                    tint = Primary800P,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        ReportCard(
            date = "12 sept",
            cropType = "Pimiento",
            cropIcon = "🌶️",
            title = "Informe integral",
            status = "Completo",
            statusColor = Primary600Intermediary
        )

        ReportCard(
            date = "23 sept",
            cropType = "Café",
            cropIcon = "☕",
            title = "pH del suelo",
            status = "Atender",
            statusColor = Color(0xFFAE9721)
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
    statusColor: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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

                        Row(
                            modifier = Modifier
                                .border(1.dp, Primary600Intermediary, RoundedCornerShape(16.dp))
                                .background(Neutral50, RoundedCornerShape(16.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = cropIcon, fontSize = 8.sp)
                            Text(
                                text = cropType,
                                fontSize = 12.sp,
                                color = Primary800P
                            )
                        }
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
