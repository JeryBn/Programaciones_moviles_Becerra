package com.tecsup.campusgps.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import androidx.core.location.LocationManagerCompat
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.tecsup.campusgps.model.Posicion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class UbicacionRepositoryGoogle(private val context: Context) : UbicacionRepository {
    override fun playServicesDisponible() =
        GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) ==
            ConnectionResult.SUCCESS

    @SuppressLint("MissingPermission") // El llamador solicita permiso y se maneja su revocación.
    override suspend fun obtenerPosicion(): Result<Posicion> {
        val token = CancellationTokenSource()
        return try {
            val manager = context.getSystemService(LocationManager::class.java)
            if (!LocationManagerCompat.isLocationEnabled(manager)) throw UbicacionNoDisponible()
            val request = CurrentLocationRequest.Builder()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setDurationMillis(12_000)
                .setMaxUpdateAgeMillis(0)
                .build()
            val fix = LocationServices.getFusedLocationProviderClient(context)
                .getCurrentLocation(request, token.token)
                .await() ?: throw UbicacionNoDisponible()
            Result.success(Posicion(fix.latitude, fix.longitude))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        } finally {
            token.cancel()
        }
    }
}
