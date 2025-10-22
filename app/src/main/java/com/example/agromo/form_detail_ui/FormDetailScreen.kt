package com.example.agromo.form_detail_ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agromo.data.AppDatabase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDetailScreen(
    formId: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val formularioDao = db.formularioDao()
    val viewModel: FormDetailViewModel = viewModel(factory = FormDetailViewModelFactory(formularioDao, formId))

    val formState by viewModel.formState.collectAsState()

    var cultivo by remember { mutableStateOf("") }
    var fecha_siembra by remember { mutableStateOf(TextFieldValue("")) }
    var observaciones by remember { mutableStateOf("") }
    var estado_fenologico by remember { mutableStateOf("") }
    var humedad by remember { mutableStateOf("") }
    var ph by remember { mutableStateOf("") }
    var altura_planta by remember { mutableStateOf("") }
    var densidad_follaje by remember { mutableStateOf("") }
    var color_follaje by remember { mutableStateOf("") }
    var estado_follaje by remember { mutableStateOf("") }

    var humedadError by remember { mutableStateOf<String?>(null) }
    var phError by remember { mutableStateOf<String?>(null) }
    var errorFecha by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(formState) {
        formState?.let {
            cultivo = it.cultivo
            fecha_siembra = TextFieldValue(it.fecha_siembra ?: "")
            observaciones = it.observaciones
            estado_fenologico = it.estado_fenologico
            humedad = it.humedad
            ph = it.ph
            altura_planta = it.altura_planta
            densidad_follaje = it.densidad_follaje
            color_follaje = it.color_follaje
            estado_follaje = it.estado_follaje
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Formulario") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                    }
                }
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        formState?.let {
                            val updatedForm = it.copy(
                                cultivo = cultivo,
                                fecha_siembra = fecha_siembra.text,
                                observaciones = observaciones,
                                estado_fenologico = estado_fenologico,
                                humedad = humedad,
                                ph = ph,
                                altura_planta = altura_planta,
                                densidad_follaje = densidad_follaje,
                                color_follaje = color_follaje,
                                estado_follaje = estado_follaje
                            )
                            viewModel.updateForm(updatedForm)
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Cambios")
                }
                Button(
                    onClick = { showDeleteDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF24B43)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Eliminar formulario", color = Color.White)
                }
            }
        }
    ) { padding ->
        if (formState == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp, 0.dp, 16.dp, 32.dp)
                ) {
                    OutlinedTextField(value = cultivo, onValueChange = { cultivo = it }, label = { Text("Cultivo") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = fecha_siembra,
                        onValueChange = { input ->
                            val digits = input.text.filter { it.isDigit() }.take(8)
                            val formatted = when (digits.length) {
                                in 1..2 -> digits
                                in 3..4 -> "${digits.take(2)}/${digits.drop(2)}"
                                in 5..8 -> "${digits.take(2)}/${digits.drop(2).take(2)}/${digits.drop(4)}"
                                else -> digits
                            }
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
                        },
                        label = { Text("Fecha de Siembra (dd/mm/aaaa)") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = errorFecha,
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        supportingText = {
                            if (errorFecha)
                                Text("Fecha inválida", color = Color.Red, fontSize = 12.sp)
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(value = observaciones, onValueChange = { observaciones = it }, label = { Text("Observaciones") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = estado_fenologico, onValueChange = { estado_fenologico = it }, label = { Text("Estado Fenológico") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = humedad,
                        onValueChange = { value ->
                            humedad = value
                            val num = value.toFloatOrNull()
                            humedadError = if (value.isNotBlank() && (num == null || num < 0 || num > 100)) {
                                "El valor debe estar entre 0% y 100%"
                            } else null
                        },
                        label = { Text("Humedad de la Tierra") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = humedadError != null
                    )
                    if (humedadError != null) {
                        Text(humedadError!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = ph,
                        onValueChange = { value ->
                            ph = value
                            val num = value.toFloatOrNull()
                            phError = if (value.isNotBlank() && (num == null || num < 0 || num > 14)) {
                                "El valor debe estar entre 0 y 14"
                            } else null
                        },
                        label = { Text("PH del Suelo") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = phError != null
                    )
                    if (phError != null) {
                        Text(phError!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(value = altura_planta, onValueChange = { altura_planta = it }, label = { Text("Altura de Plantas") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = densidad_follaje, onValueChange = { densidad_follaje = it }, label = { Text("Densidad de Follaje") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = color_follaje, onValueChange = { color_follaje = it }, label = { Text("Color del Follaje") }, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(value = estado_follaje, onValueChange = { estado_follaje = it }, label = { Text("Estado del Follaje") }, modifier = Modifier.fillMaxWidth())
                }

                if (showDeleteDialog) {
                    AlertDialog(
                        onDismissRequest = { showDeleteDialog = false },
                        title = { Text("Confirmar eliminación") },
                        text = { Text("¿Estás seguro de borrar este formulario? Esta acción no se puede deshacer.") },
                        confirmButton = {
                            Button(
                                onClick = {
                                    showDeleteDialog = false
                                    formState?.let { viewModel.deleteForm(it) }
                                    onBack()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF24B43))
                            ) {
                                Text("Eliminar", color = Color.White)
                            }
                        },
                        dismissButton = {
                            Button(onClick = { showDeleteDialog = false }) {
                                Text("Cancelar")
                            }
                        }
                    )
                }
            }
        }
    }
}
