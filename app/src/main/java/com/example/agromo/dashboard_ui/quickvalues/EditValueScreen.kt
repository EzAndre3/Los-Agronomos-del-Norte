package com.example.agromo.dashboard_ui.quickvalues

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
                TextFieldOutlined(
                    value = value,
                    onValueChange = { value = it },
                    label = key,
                    placeholder = "Nuevo valor"
                )
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

