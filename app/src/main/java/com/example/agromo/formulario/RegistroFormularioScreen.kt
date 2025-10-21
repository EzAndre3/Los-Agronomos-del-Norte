
package com.example.agromo.formulario

import android.content.ContentValues
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.agromo.data.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroFormularioScreen(
    onNext: () -> Unit,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current

    val db = AppDatabase.getDatabase(context)
    val dao = db.formularioDao()

    val repository = remember { FormularioRepository(dao) }

    val viewModel: RegistroFormularioViewModel = viewModel(
        factory = RegistroFormularioViewModelFactory(repository)
    )

    val totalSteps = 8
    var step by remember { mutableStateOf(0) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        containerColor = Color.White,
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
                    if (step < totalSteps - 1) {
                        step += 1
                    } else {
                        val finalForm = viewModel.guardarFormulario()
                        coroutineScope.launch {
                            repository.saveFormulario(finalForm)
                        }
                        Log.d("FORMULARIO", "Formulario guardado: $finalForm")
                        onNext()
                    }
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
                0 -> StepLocalizacion(viewModel)
                1 -> StepVariedad(viewModel)
                2 -> StepHumedad(viewModel)
                3 -> SteppH(viewModel)
                4 -> StepAltura(viewModel)
                5 -> StepFenologico(viewModel)
                6 -> StepFollaje(viewModel)
                7 -> StepImagen(viewModel) // nuevo paso final
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
fun StepLocalizacion(viewModel: RegistroFormularioViewModel) {
    var localizacion by remember { mutableStateOf(TextFieldValue("")) }
    var usarActual by remember { mutableStateOf(false) }

    Column {
        Text("Registro del Cultivo", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(4.dp))
        Text(
            "Complete los datos que disponga; el resto puede omitirse.",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(20.dp))

        Text("Indique la ubicación de su cultivo", fontSize = 16.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = {
                usarActual = !usarActual
                if (usarActual) {
                    // Simulación: aquí puedes luego integrar un servicio real de ubicación
                    val localizacionActual = "Localizacion actual detectada"
                    localizacion = TextFieldValue(localizacionActual)
                    viewModel.updateLocalizacion(localizacionActual)
                } else {
                    localizacion = TextFieldValue("")
                    viewModel.updateLocalizacion("")
                }
            },
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
            value = localizacion,
            onValueChange = {
                localizacion = it
                viewModel.updateLocalizacion(it.text) //
            },
            label = { Text("Busca por ciudad o localidad") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
    }
}


@Composable
fun StepVariedad(viewModel: RegistroFormularioViewModel) {
    var cultivoQuery by remember { mutableStateOf(TextFieldValue("")) }
    var cultivos = listOf("Maíz", "Trigo", "Sorgo", "Cebada", "Avena", "Frijol", "Soya", "Caña de azúcar", "Papa", "Tomate")
    var seleccionCultivos by remember { mutableStateOf(setOf<String>()) }

    var fecha_siembra by remember { mutableStateOf(TextFieldValue("")) }
    var errorFecha by remember { mutableStateOf(false) }

    Column {
        Text("Información general del cultivo", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(4.dp))
        Text("¿Qué cultivo y variedad tiene sembrado?", color = Color.Gray, fontSize = 14.sp)
        Spacer(Modifier.height(20.dp))

        // Card decorativa (puedes añadir más detalles aquí si lo necesitas)
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FCEB)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(Modifier.padding(16.dp)) {
                Text("Selecciona uno o varios cultivos de la lista, o búscalos manualmente.")
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Variedad Cultivada", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = cultivoQuery,
            onValueChange = {
                cultivoQuery = it
                viewModel.updateCultivo(it.text) //
            },
            label = { Text("Busca por cultivo") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )

        Spacer(Modifier.height(12.dp))

        val verdeBoton = Color(0xFF33691E)
        val cultivosFiltrados = cultivos.filter {
            it.contains(cultivoQuery.text, ignoreCase = true)
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            cultivosFiltrados.forEach { c ->
                val isSelected = seleccionCultivos.contains(c)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            seleccionCultivos =
                                if (isSelected) seleccionCultivos - c else seleccionCultivos + c
                            // ✅ Actualiza cultivo al seleccionar
                            viewModel.updateCultivo(c)
                        }
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isSelected,
                        onCheckedChange = {
                            seleccionCultivos =
                                if (isSelected) seleccionCultivos - c else seleccionCultivos + c
                            viewModel.updateCultivo(c)
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = verdeBoton,
                            uncheckedColor = Color.Gray,
                            checkmarkColor = Color.White
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(c)
                }
            }
        }

        Spacer(Modifier.height(20.dp))
        Text("Fecha de la siembra", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = fecha_siembra,
            onValueChange = { input ->
                // Filtrar solo números
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

                fecha_siembra = TextFieldValue(
                    text = formatted,
                    selection = TextRange(formatted.length)
                )


                if (!errorFecha) {
                    viewModel.updateFechaSiembra(formatted)
                }
            },
            label = { Text("Fecha de la siembra (dd/mm/aaaa)") },
            isError = errorFecha,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            supportingText = {
                if (errorFecha)
                    Text("Fecha inválida", color = Color.Red, fontSize = 12.sp)
            }
        )
    }
}

@Composable
fun StepHumedad(viewModel: RegistroFormularioViewModel) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("quickactions", Context.MODE_PRIVATE) }
    val initialHumedad = remember { sharedPreferences.getString("Humedad del suelo", "") ?: "" }

    var humedad by remember { mutableStateOf(TextFieldValue(initialHumedad)) }
    var errorHumedad by remember { mutableStateOf(false) }

    LaunchedEffect(initialHumedad) {
        if (initialHumedad.isNotEmpty()) {
            viewModel.updateHumedad(initialHumedad)
        }
    }

    Column {
        Text("Suelo y condiciones", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(4.dp))
        Text(
            "Ingrese la humedad en porcentaje (0–100). Si tiene un sensor, anote el valor obtenido; si no, estime manualmente.",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(20.dp))

        Text("Humedad del suelo", fontSize = 16.sp)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = humedad,
            onValueChange = { input ->
                // Solo aceptar dígitos y limitar a 3 caracteres
                val digits = input.text.filter { it.isDigit() }.take(3)
                val valor = digits.toIntOrNull() ?: 0

                if (valor in 0..100) {
                    // Cursor siempre al final
                    humedad = TextFieldValue(
                        text = digits,
                        selection = TextRange(digits.length)
                    )
                    errorHumedad = false
                    viewModel.updateHumedad(digits) // ✅ actualiza el ViewModel
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
                if (errorHumedad)
                    Text("El valor debe estar entre 0% y 100%", color = Color.Red, fontSize = 12.sp)
            }
        )
    }
}


@Composable
fun SteppH(viewModel: RegistroFormularioViewModel) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("quickactions", Context.MODE_PRIVATE) }
    val initialPh = remember { sharedPreferences.getString("PH del suelo", "") ?: "" }

    var ph by remember { mutableStateOf(TextFieldValue(initialPh)) }
    var selectedMedidor by remember { mutableStateOf<String?>(null) } // Guarda el tipo de medidor

    LaunchedEffect(initialPh) {
        if (initialPh.isNotEmpty()) {
            viewModel.updatePH(initialPh, selectedMedidor ?: "")
        }
    }

    Column {
        Text("Nivel del pH", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(8.dp))
        Text(
            "Indique el nivel de pH del suelo. Si tiene dudas, especifique el tipo de medición que utilizó y siga las instrucciones correspondientes.",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = ph,
            onValueChange = {
                ph = it
                viewModel.updatePH(it.text, selectedMedidor ?: "")
            },
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
                val isSelected = selectedMedidor == tipo

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedMedidor = tipo
                            viewModel.updatePH(ph.text, tipo)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF33691E) else Color(0xFFF4FBE8)
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = tipo,
                        modifier = Modifier.padding(16.dp),
                        color = if (isSelected) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepAltura(viewModel: RegistroFormularioViewModel) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("quickactions", Context.MODE_PRIVATE) }
    val initialAltura = remember { sharedPreferences.getString("Altura de plantas", "") ?: "" }

    var altura by remember { mutableStateOf(TextFieldValue(initialAltura)) }
    var metodo by remember { mutableStateOf("Seleccione un método") }
    var expandedMetodo by remember { mutableStateOf(false) }

    LaunchedEffect(initialAltura) {
        if (initialAltura.isNotEmpty()) {
            viewModel.updateAltura(initialAltura, metodo)
        }
    }

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
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(20.dp))

        // ------------------------
        // Altura registrada
        // ------------------------
        OutlinedTextField(
            value = altura,
            onValueChange = {
                altura = it
                viewModel.updateAltura(altura.text, metodo)
            },
            label = { Text("Altura registrada (cm)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
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
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedMetodo)
                }
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
                            viewModel.updateAltura(altura.text, it)
                        }
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepFenologico(viewModel: RegistroFormularioViewModel) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("quickactions", Context.MODE_PRIVATE) }
    val initialEstado = remember { sharedPreferences.getString("Estado fenológico", "Seleccione estado") ?: "Seleccione estado" }

    var expanded by remember { mutableStateOf(false) }
    var estado by remember { mutableStateOf(initialEstado) }
    var observaciones by remember { mutableStateOf("") }

    LaunchedEffect(initialEstado) {
        if (initialEstado != "Seleccione estado") {
            viewModel.updateFenologico(initialEstado, observaciones)
        }
    }

    Column {
        Text("Estado fenológico de la planta", fontSize = 20.sp, color = Color(0xFF1B1B1B))
        Spacer(Modifier.height(8.dp))
        Text(
            "Seleccione una opción en el menú desplegable.",
            color = Color.Gray,
            fontSize = 14.sp
        )
        Spacer(Modifier.height(20.dp))

        // ----------------------------
        // Menú desplegable (estado fenológico)
        // ----------------------------
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = estado,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado fenológico") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                }
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                listOf("Germinación", "Floración", "Maduración", "Cosecha").forEach {
                    DropdownMenuItem(
                        text = { Text(it) },
                        onClick = {
                            estado = it
                            expanded = false
                            viewModel.updateFenologico(it, observaciones) // pasa también las observaciones actuales
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ----------------------------
        // Campo de observaciones
        // ----------------------------
        OutlinedTextField(
            value = observaciones,
            onValueChange = {
                observaciones = it
                viewModel.updateFenologico(estado, it) // pasa también el estado actual
            },
            label = { Text("Observaciones") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepFollaje(viewModel: RegistroFormularioViewModel) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("quickactions", Context.MODE_PRIVATE) }
    val densFollaje = remember { sharedPreferences.getString("Densidad de follaje", "Seleccione estado") ?: "Seleccione estado" }
    val colFollaje = remember { sharedPreferences.getString("Color predominante", "Seleccione estado") ?: "Seleccione estado" }
    val estFollaje = remember { sharedPreferences.getString("Estado General de Follaje", "Seleccione estado") ?: "Seleccione estado" }


    var densidad by remember { mutableStateOf(densFollaje) }
    var color by remember { mutableStateOf(colFollaje) }
    var estado by remember { mutableStateOf(estFollaje) }

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

        // --------------------------
        // Densidad del follaje
        // --------------------------
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
                shape = RoundedCornerShape(10.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDensidad) }
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
                            viewModel.updateFollaje(densidad, color, estado)
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // --------------------------
        // Color del follaje
        // --------------------------
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
                shape = RoundedCornerShape(10.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedColor) }
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
                            viewModel.updateFollaje(densidad, color, estado)
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(16.dp))

        // --------------------------
        // Estado general del follaje
        // --------------------------
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
                shape = RoundedCornerShape(10.dp),
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) }
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
                            viewModel.updateFollaje(densidad, color, estado)
                        }
                    )
                }
            }
        }
    }
}

/* ---------------------------- NUEVO: Paso de Imagen ---------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepImagen(viewModel: RegistroFormularioViewModel) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageAspectRatio by remember { mutableFloatStateOf(1f) }

    val mainGreen = Color(0xFF317C42)
    val lightBackground = Color(0xFFF7FBEF)

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
        uri?.let {
            context.contentResolver.openInputStream(it)?.use { input ->
                val bmp = BitmapFactory.decodeStream(input)
                imageAspectRatio = bmp.width.toFloat() / bmp.height.toFloat()
            }
        }
    }

    val photoUriState = remember { mutableStateOf<Uri?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = photoUriState.value
            photoUriState.value?.let {
                context.contentResolver.openInputStream(it)?.use { input ->
                    val bmp = BitmapFactory.decodeStream(input)
                    imageAspectRatio = bmp.width.toFloat() / bmp.height.toFloat()
                }
            }
        }
    }

    fun createGalleryUri(): Uri? {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "Agromo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/Agromo")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(lightBackground)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageUri == null) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Button(
                    onClick = {
                        val newUri = createGalleryUri()
                        photoUriState.value = newUri
                        newUri?.let { cameraLauncher.launch(it) }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF33691E))
                ) {
                    Icon(Icons.Default.CameraAlt, contentDescription = "Tomar Foto", tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Tomar Foto", fontSize = 16.sp, color = Color.White)
                }

                Button(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF33691E))
                ) {
                    Icon(Icons.Default.Image, contentDescription = "Seleccionar", tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text("Seleccionar desde Galería", fontSize = 16.sp, color = Color.White)
                }
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {

                Image(
                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Preview Foto",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(imageAspectRatio)
                        .clip(RoundedCornerShape(16.dp))
                )

                Button(
                    onClick = { imageUri = null },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(55.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reintentar",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text("Reintentar", color = Color.White, fontSize = 15.sp)
                }
            }
        }
    }
}

fun uploadImageToApi(imageUri: Uri) {
    // Placeholder - Aquí puedes implementar la subida HTTPS si lo deseas
}
