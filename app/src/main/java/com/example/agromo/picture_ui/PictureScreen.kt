package com.example.agromo.picture_ui

import android.content.ContentValues
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Image as ImageIcon
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PictureScreen(
    onNavigateBackToDashboard: () -> Unit
) {
    val context = LocalContext.current

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageAspectRatio by remember { mutableFloatStateOf(1f) } // relación ancho/alto

    val mainGreen = Color(0xFF317C42)
    val lightBackground = Color(0xFFF7FBEF)

    // Galería
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

    // Cámara
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

    // Crear URI en galería
    fun createGalleryUri(): Uri? {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "Agromo_${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/Agromo")
        }
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Captura o Selecciona una Foto", fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { onNavigateBackToDashboard() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    navigationIconContentColor = mainGreen,
                    titleContentColor = mainGreen
                )
            )
        },
        containerColor = lightBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (imageUri == null) {
                // Pantalla inicial
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
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
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Tomar Foto",
                            tint = Color.White,
                            modifier = Modifier.padding(end = 8.dp)
                        )
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
                        Icon(
                            imageVector = Icons.Default.ImageIcon,
                            contentDescription = "Seleccionar desde Galería",
                            tint = Color.White,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Seleccionar desde Galería", fontSize = 16.sp, color = Color.White)
                    }
                }
            } else {
                // Preview con proporción dinámica
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    modifier = Modifier.fillMaxWidth()
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

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = { imageUri = null },
                            modifier = Modifier
                                .weight(1f)
                                .height(55.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Volver a Tomar",
                                tint = Color.White,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Reintentar", color = Color.White, fontSize = 15.sp)
                        }

                        Button(
                            onClick = {
                                imageUri?.let { uploadImageToApi(it) }
                                onNavigateBackToDashboard()
                                      },
                            modifier = Modifier
                                .weight(1f)
                                .height(55.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF33691E))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Confirmar Foto",
                                tint = Color.White,
                                modifier = Modifier.padding(end = 8.dp)
                            )
                            Text("Confirmar", color = Color.White, fontSize = 15.sp)
                        }
                    }
                }
            }
        }
    }
}


// Placeholder de función para enviar imagen a la API
fun uploadImageToApi(imageUri: Uri) {
    // Implementar llamada HTTPs
}