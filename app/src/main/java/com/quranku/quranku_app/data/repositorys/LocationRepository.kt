package com.quranku.quranku_app.data.repositorys

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationRepository (
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    @ApplicationContext private val context: Context // Inject context to check permissions
) {

    suspend fun getCurrentLocation(): Result<Pair<Double, Double>> {
        // Check if location permissions are granted
        if (!hasLocationPermission()) {
            return Result.failure(SecurityException("Location permission not granted"))
        }

        return try {
            val location = fusedLocationProviderClient.lastLocation.await()
            if (location != null) {
                Result.success(Pair(location.latitude, location.longitude))
            } else {
                Result.failure(Exception("Location not found"))
            }
        } catch (e: SecurityException) {
            Result.failure(Exception("Location permission error"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED || coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }
}