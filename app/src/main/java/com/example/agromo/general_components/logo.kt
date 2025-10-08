package com.example.agromo.general_components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.agromo.R

@Composable
fun LogoAgromo(
    size: Dp = 120.dp // puedes cambiar el tamaño desde donde lo llames
) {
    Image(
        painter = painterResource(id = R.drawable.logo_agromo),
        contentDescription = "Logo Agromo",
        modifier = Modifier.size(size)
    )
}