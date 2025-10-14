package com.example.agromo.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
suspend fun getLastKnownLocation(context: Context): Location? {
    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    return suspendCancellableCoroutine { cont ->
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                cont.resume(location)
            }
            .addOnFailureListener {
                cont.resume(null)
            }
    }
}
