package com.example.agromo.formulario

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroFormularioScreen(
    onNext: () -> Unit,
    onBack: () -> Unit = {}
) {
    val totalSteps = 7
    var step by remember { mutableStateOf(0) }

    // Estados
    var ubicacion by remember { mutableStateOf(TextFieldValue("")) }
    var usarUbicacionActual by remember { mutableStateOf(false) }
    var cultivoQuery by remember { mutableStateOf(TextFieldValue("")) }
    val cultivos = listOf("Maíz", "Algodón", "Arroz", "Café", "Cacao", "Frijol")
    var seleccionCultivos by remember { mutableStateOf(setOf<String>()) }
    var humedad by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        containerColor = Color(0xFFFFFFFF),
        topBar = {
            TopAppBar(
                title = { Text("Formulario de Monitoreo", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = {
                        if (step == 0) onBack() else step = (step - 1).coerceAtLeast(0)
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { onBack() }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color(0xFF1B1B1B),
                    navigationIconContentColor = Color(0xFF1B1B1B),
                    actionIconContentColor = Color(0xFF1B1B1B)
                )
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    if (step < totalSteps - 1) step += 1 else onNext()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF33691E))
            ) {
                Text(
                    if (step < totalSteps - 1) "Siguiente" else "Finalizar",
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            StepIndicator(current = step, total = totalSteps)
            Spacer(modifier = Modifier.height(16.dp))

            when (step) {
                0 -> StepUbicacion(
                    ubicacion = ubicacion,
                    onUbicacionChange = { ubicacion = it },
                    usarActual = usarUbicacionActual,
                    onToggleUsarActual = { usarUbicacionActual = it }
                )
                1 -> StepVariedad(
                    cultivoQuery = cultivoQuery,
                    onCultivoQueryChange = { cultivoQuery = it },
                    cultivos = cultivos,
                    seleccionCultivos = seleccionCultivos,
                    onToggleCultivo = {
                        seleccionCultivos = if (seleccionCultivos.contains(it)) {
                            seleccionCultivos - it
                        } else {
                            seleccionCultivos + it
                        }
                    }
                )
                2 -> StepHumedad(
                    humedad = humedad,
                    onHumedadChange = { humedad = it }
                )
                3 -> SteppH()
                4 -> StepAltura()
                5 -> StepFenologico()
                6 -> StepFollaje()
            }

            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun StepIndicator(current: Int, total: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        repeat(total) { i ->
            val active = i == current
            Box(
                modifier = Modifier
                    .padding(horizontal = 3.dp)
                    .height(8.dp)
                    .width(if (active) 28.dp else 12.dp)
                    .background(
                        color = if (active) Color(0xFF33691E) else Color(0xFFDCEBC4),
                        shape = RoundedCornerShape(6.dp)
                    )
            )
        }
    }
}

@Composable
fun StepUbicacion(
    ubicacion: TextFieldValue,
    onUbicacionChange: (TextFieldValue) -> Unit,
    usarActual: Boolean,
    onToggleUsarActual: (Boolean) -> Unit
) {
    Column {
        Text("Registro del Cultivo", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(4.dp))
        Text("Complete los datos que disponga; el resto puede omitirse.", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(20.dp))
        Text("Indique la ubicación de su cultivo", fontSize = 16.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = { onToggleUsarActual(!usarActual) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF33691E))
        ) {
            Text(if (usarActual) "Usando ubicación actual" else "Usar mi ubicación actual")
        }

        Spacer(Modifier.height(8.dp))
        Text("O complete manualmente", color = Color.Gray)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = ubicacion,
            onValueChange = onUbicacionChange,
            label = { Text("Busca por ciudad") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
fun StepVariedad(
    cultivoQuery: TextFieldValue,
    onCultivoQueryChange: (TextFieldValue) -> Unit,
    cultivos: List<String>,
    seleccionCultivos: Set<String>,
    onToggleCultivo: (String) -> Unit
) {
    Column {
        Text("Información general del cultivo", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(4.dp))
        Text("¿Qué cultivo y variedad tiene sembrado?", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FCEB)),
            shape = RoundedCornerShape(12.dp)
        ) {

        }

        Spacer(Modifier.height(20.dp))
        Text("Variedad Cultivada", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = cultivoQuery,
            onValueChange = onCultivoQueryChange,
            label = { Text("Busca por cultivo") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
        Spacer(Modifier.height(12.dp))

        val verdeBoton = Color(0xFF33691E)

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            cultivos.forEach { c ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onToggleCultivo(c) }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = seleccionCultivos.contains(c),
                        onCheckedChange = { onToggleCultivo(c) },
                        colors = CheckboxDefaults.colors(
                            checkedColor = verdeBoton,      // color del fondo del checkbox cuando está marcado
                            uncheckedColor = Color.Gray,    // color del borde cuando no está marcado
                            checkmarkColor = Color.White     // color del ✔️ dentro
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(c)
                }
            }
        }

        var fechaSiembra by remember { mutableStateOf(TextFieldValue("")) }
        var errorFecha by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = fechaSiembra,
            onValueChange = { input ->
                // Filtrar solo los números
                val digits = input.text.filter { it.isDigit() }.take(8)

                // Aplicar formato dd/mm/aaaa
                val formatted = when (digits.length) {
                    in 1..2 -> digits
                    in 3..4 -> "${digits.take(2)}/${digits.drop(2)}"
                    in 5..8 -> "${digits.take(2)}/${digits.drop(2).take(2)}/${digits.drop(4)}"
                    else -> digits
                }

                // Validar fecha
                val partes = formatted.split("/")
                errorFecha = if (partes.size == 3 && partes[2].length == 4) {
                    val dia = partes[0].toIntOrNull() ?: 0
                    val mes = partes[1].toIntOrNull() ?: 0
                    dia !in 1..31 || mes !in 1..12
                } else false

                // Actualizar con el cursor al final
                fechaSiembra = TextFieldValue(
                    text = formatted,
                    selection = TextRange(formatted.length)
                )
            },
            label = { Text("Fecha de la siembra (dd/mm/aaaa)") },
            isError = errorFecha,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = {
                if (errorFecha) Text("Fecha inválida", color = Color.Red, fontSize = 12.sp)
            }
        )
    }
}

@Composable
fun StepHumedad(
    humedad: TextFieldValue,
    onHumedadChange: (TextFieldValue) -> Unit
) {
    Column {
        Text("Suelo y condiciones", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(4.dp))
        Text("Ingresar la humedad en la escala si es manualmente o anote el valor si posee un sensor.", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(20.dp))

        Text("Humedad del suelo", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        var humedad by remember { mutableStateOf("") }
        var errorHumedad by remember { mutableStateOf(false) }

        OutlinedTextField(
            value = humedad,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }.take(3)
                val valor = digits.toIntOrNull() ?: 0

                if (valor in 0..100) {
                    humedad = digits
                    errorHumedad = false
                } else {
                    errorHumedad = true
                }
            },
            label = { Text("Humedad del suelo (%)") },
            isError = errorHumedad,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = {
                if (errorHumedad) Text("El valor no puede superar 100%", color = Color.Red, fontSize = 12.sp)
            }
        )
    }
}

@Composable
fun SteppH() {
    var ph by remember { mutableStateOf(TextFieldValue("")) }
    var selectedMedidor by remember { mutableStateOf<String?>(null) } // Estado para guardar el seleccionado

    Column {
        Text("Nivel del pH", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(8.dp))
        Text(
            "Indique el nivel de pH del suelo. Si tiene dudas, especifique el tipo de medición que utilizó y siga las instrucciones correspondientes.",
            color = Color.Gray, fontSize = 14.sp
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = ph,
            onValueChange = { ph = it },
            label = { Text("Nivel de pH (0-14)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(Modifier.height(20.dp))
        Text("¿Qué tipo de medidor utilizó?", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        val medidores = listOf("Medidor Digital", "Cinta Reactiva", "Medición Manual")

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            medidores.forEach { tipo ->
                val isSelected = tipo == selectedMedidor

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedMedidor = tipo },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF33691E) else Color(0xFFF4FBE8)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        tipo,
                        Modifier
                            .padding(16.dp),
                        color = if (isSelected) Color.White else Color.Black // texto blanco si está seleccionado
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepFertilidad() {
    var nivelFertilidadExpanded by remember { mutableStateOf(false) }
    var nivelFertilidad by remember { mutableStateOf("Nivel de fertilidad") }

    var nitrogeno by remember { mutableStateOf("") }
    var fosforo by remember { mutableStateOf("") }
    var potasio by remember { mutableStateOf("") }
    var materia by remember { mutableStateOf("") }
    var cic by remember { mutableStateOf("") }

    Column {
        Text("Fertilidad del suelo", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(8.dp))
        Text("Indique la fertilidad del suelo, ya sea mediante análisis digital o manual.", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(16.dp))

        Text("Registro manual", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = nivelFertilidadExpanded,
            onExpandedChange = { nivelFertilidadExpanded = !nivelFertilidadExpanded }
        ) {
            OutlinedTextField(
                value = nivelFertilidad,
                onValueChange = {},
                readOnly = true,
                label = { Text("Nivel de fertilidad") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            ExposedDropdownMenu(
                expanded = nivelFertilidadExpanded,
                onDismissRequest = { nivelFertilidadExpanded = false }
            ) {
                listOf("Bajo", "Medio", "Alto").forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            nivelFertilidad = it
                            nivelFertilidadExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Registro con sensor digital", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        listOf(
            "Nitrógeno (N)" to "Valor mg/kg",
            "Fósforo (P)" to "Valor mg/kg",
            "Potasio (K)" to "Valor mg/kg",
            "Materia Orgánica" to "Valor %",
            "CIC" to "Valor (cmol/kg)"
        ).forEach { (label, placeholder) ->
            OutlinedTextField(
                value = when (label) {
                    "Nitrógeno (N)" -> nitrogeno
                    "Fósforo (P)" -> fosforo
                    "Potasio (K)" -> potasio
                    "Materia Orgánica" -> materia
                    "CIC" -> cic
                    else -> ""
                },
                onValueChange = {
                    when (label) {
                        "Nitrógeno (N)" -> nitrogeno = it
                        "Fósforo (P)" -> fosforo = it
                        "Potasio (K)" -> potasio = it
                        "Materia Orgánica" -> materia = it
                        "CIC" -> cic = it
                    }
                },
                label = { Text(label) },
                placeholder = { Text(placeholder) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                shape = RoundedCornerShape(10.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepAltura() {
    var altura by remember { mutableStateOf(TextFieldValue("")) }
    var metodo by remember { mutableStateOf("Seleccione un método") }
    var expandedMetodo by remember { mutableStateOf(false) }

    val metodos = listOf(
        "Regla / Cinta Métrica",
        "Sensor Ultrasónico",
        "Medición Visual Estimada"
    )

    Column {
        Text("Medición de la Planta", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(8.dp))
        Text(
            "Mida la altura promedio de las plantas, tomando como referencia un valor promedio (no la más baja ni la más alta).",
            color = Color.Gray, fontSize = 14.sp
        )
        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = altura,
            onValueChange = { altura = it },
            label = { Text("Altura registrada (cm)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(Modifier.height(16.dp))
        Text("Método de medición", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expandedMetodo,
            onExpandedChange = { expandedMetodo = !expandedMetodo }
        ) {
            OutlinedTextField(
                value = metodo,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccione un método") },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            ExposedDropdownMenu(
                expanded = expandedMetodo,
                onDismissRequest = { expandedMetodo = false }
            ) {
                metodos.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            metodo = it
                            expandedMetodo = false
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepFenologico() {
    var expanded by remember { mutableStateOf(false) }
    var estado by remember { mutableStateOf("Seleccione estado") }
    var observaciones by remember { mutableStateOf("") }

    Column {
        Text("Estado fenológico de la planta", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(8.dp))
        Text("Seleccione una opción en el menú desplegable.", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(20.dp))

        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
            OutlinedTextField(
                value = estado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado fenológico") },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                listOf("Germinación", "Floración", "Maduración", "Cosecha").forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            estado = it
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        OutlinedTextField(value = observaciones, onValueChange = { observaciones = it }, label = { Text("Observaciones") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepFollaje() {
    var densidad by remember { mutableStateOf("Seleccione una opción") }
    var color by remember { mutableStateOf("Seleccione una opción") }
    var estado by remember { mutableStateOf("Seleccione una opción") }

    var expandedDensidad by remember { mutableStateOf(false) }
    var expandedColor by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) }

    val opcionesDensidad = listOf("Mucha", "Media", "Poca")
    val opcionesColor = listOf("Verde oscuro - sano", "Amarillo - débil", "Marrón - seco")
    val opcionesEstado = listOf("Uniformes", "Irregulares")

    Column {
        Text("Densidad del follaje", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(8.dp))
        Text(
            "Observe el follaje: ¿cuánto suelo se ve poco, mitad o nada? ¿De qué color son las hojas y cuán uniforme es el follaje?",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(20.dp))

        // Densidad
        Text("Densidad del follaje", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expandedDensidad,
            onExpandedChange = { expandedDensidad = !expandedDensidad }
        ) {
            OutlinedTextField(
                value = densidad,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccione una opción") },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            ExposedDropdownMenu(
                expanded = expandedDensidad,
                onDismissRequest = { expandedDensidad = false }
            ) {
                opcionesDensidad.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            densidad = it
                            expandedDensidad = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Color
        Text("Color predominante del follaje", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expandedColor,
            onExpandedChange = { expandedColor = !expandedColor }
        ) {
            OutlinedTextField(
                value = color,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccione una opción") },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            ExposedDropdownMenu(
                expanded = expandedColor,
                onDismissRequest = { expandedColor = false }
            ) {
                opcionesColor.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            color = it
                            expandedColor = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // Estado general
        Text("Estado general del follaje", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(
            expanded = expandedEstado,
            onExpandedChange = { expandedEstado = !expandedEstado }
        ) {
            OutlinedTextField(
                value = estado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccione una opción") },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(10.dp)
            )
            ExposedDropdownMenu(
                expanded = expandedEstado,
                onDismissRequest = { expandedEstado = false }
            ) {
                opcionesEstado.forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            estado = it
                            expandedEstado = false
                        }
                    )
                }
            }
        }
    }
}