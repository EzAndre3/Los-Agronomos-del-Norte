package com.example.agromo.login_ui.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp


@Composable
fun PasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        textStyle = TextStyle(
            color = Color.Black,
            fontSize = 16.sp
        ),
        trailingIcon = {
            val icon = if (passwordVisible) "👁️" else "👁️"
            TextButton(onClick = { passwordVisible = !passwordVisible }) {
                Text(icon)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
