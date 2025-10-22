package com.example.agromo.dashboard_ui.quickvalues

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.agromo.login_ui.components.TextFieldOutlined
import com.example.agromo.login_ui.components.PrimaryButton
import com.example.agromo.ui.theme.Primary50

@Composable
fun EditValueScreen(
    key: String,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("quickactions", Context.MODE_PRIVATE)
    var value by remember { mutableStateOf(prefs.getString(key, "") ?: "") }

    val options: List<String>? = when (key) {
        "Densidad de follaje" -> listOf("Mucha", "Media", "Poca")
        "Color predominante" -> listOf("Verde oscuro - sano", "Amarillo - débil", "Marrón - seco")
        "Estado General de Follaje" -> listOf("Uniformes", "Irregulares")
        "Estado fenológico" -> listOf("Germinación", "Floración", "Maduración", "Cosecha")
        else -> null
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Primary50),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Primary50),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Editar $key",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(22.dp))

                if (options != null) {
                    if (value.isEmpty() && options.isNotEmpty()) {
                        value = options.first()
                    }
                    RadioGroup(options = options, selectedOption = value, onOptionSelected = { value = it })
                } else {
                    val isNumericField = key in listOf("PH del suelo", "Humedad del suelo", "Altura de plantas")

                    TextFieldOutlined(
                        value = value,
                        onValueChange = { newValue ->
                            if (isNumericField) {
                                if (newValue.matches(Regex("^\\d*\\.?\\d*\$"))) {
                                    val numeric = newValue.toFloatOrNull()
                                    val isValid = when (key) {
                                        "PH del suelo" -> numeric == null || (numeric in 1f..14f)
                                        "Humedad del suelo" -> numeric == null || (numeric in 0f..100f)
                                        "Altura de plantas" -> true
                                        else -> true
                                    }
                                    if (isValid) value = newValue
                                }
                            } else {
                                value = newValue
                            }
                        },
                        label = key,
                        placeholder = when (key) {
                            "PH del suelo" -> "Ingresa un valor entre 1 y 14"
                            "Humedad del suelo" -> "Ingresa un valor entre 0 y 100"
                            "Altura de plantas" -> "Ingresa una altura numérica"
                            else -> "Nuevo valor"
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = if (isNumericField) KeyboardType.Number else KeyboardType.Text
                        ),
                        isError = when (key) {
                            "PH del suelo" -> value.toFloatOrNull()?.let { it < 1 || it > 14 } ?: false
                            "Humedad del suelo" -> value.toFloatOrNull()?.let { it < 0 || it > 100 } ?: false
                            else -> false
                        }
                    )

                    when (key) {
                        "PH del suelo" -> if (value.toFloatOrNull()?.let { it < 1 || it > 14 } == true)
                            Text("El valor debe ser entre 1 y 14", color = Color.Red, fontSize = 12.sp)
                        "Humedad del suelo" -> if (value.toFloatOrNull()?.let { it < 0 || it > 100 } == true)
                            Text("El valor debe ser entre 0 y 100", color = Color.Red, fontSize = 12.sp)
                    }
                }

                Spacer(Modifier.height(18.dp))
                PrimaryButton(
                    text = "Guardar"
                ) {
                    prefs.edit().putString(key, value).apply()
                    onBack()
                }
            }
        }
    }
}

@Composable
fun RadioGroup(
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column {
        options.forEach { option ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .selectable(
                        selected = (option == selectedOption),
                        onClick = { onOptionSelected(option) }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (option == selectedOption),
                    onClick = { onOptionSelected(option) }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = option)
            }
        }
    }
}
