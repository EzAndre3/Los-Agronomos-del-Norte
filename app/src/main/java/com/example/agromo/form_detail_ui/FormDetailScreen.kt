package com.example.agromo.form_detail_ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.agromo.data.AppDatabase
import com.example.agromo.data.FormularioEntity

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

    // States for editable fields, matching FormularioEntity
    var cultivo by remember { mutableStateOf("") }
    var fecha_siembra by remember { mutableStateOf("") }
    var observaciones by remember { mutableStateOf("") }
    var estado_fenologico by remember { mutableStateOf("") }
    var humedad by remember { mutableStateOf("") }
    var ph by remember { mutableStateOf("") }
    var altura_planta by remember { mutableStateOf("") }

    // Update local states when formState is loaded
    LaunchedEffect(formState) {
        formState?.let {
            cultivo = it.cultivo
            fecha_siembra = it.fecha_siembra
            observaciones = it.observaciones
            estado_fenologico = it.estado_fenologico
            humedad = it.humedad
            ph = it.ph
            altura_planta = it.altura_planta
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
        }
    ) { padding ->
        if (formState == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(value = cultivo, onValueChange = { cultivo = it }, label = { Text("Cultivo") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = fecha_siembra, onValueChange = { fecha_siembra = it }, label = { Text("Fecha de Siembra") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = observaciones, onValueChange = { observaciones = it }, label = { Text("Observaciones") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = estado_fenologico, onValueChange = { estado_fenologico = it }, label = { Text("Estado Fenológico") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = humedad, onValueChange = { humedad = it }, label = { Text("Humedad de la Tierra") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = ph, onValueChange = { ph = it }, label = { Text("PH del Suelo") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = altura_planta, onValueChange = { altura_planta = it }, label = { Text("Altura de Plantas") }, modifier = Modifier.fillMaxWidth())

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        formState?.let {
                            val updatedForm = it.copy(
                                cultivo = cultivo,
                                fecha_siembra = fecha_siembra,
                                observaciones = observaciones,
                                estado_fenologico = estado_fenologico,
                                humedad = humedad,
                                ph = ph,
                                altura_planta = altura_planta
                            )
                            viewModel.updateForm(updatedForm)
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar Cambios")
                }
            }
        }
    }
}