package com.tecsup.productos.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.tecsup.productos.data.auth.AuthRepository
import com.tecsup.productos.data.repository.resultado
import com.tecsup.productos.domain.model.Sesion
import com.tecsup.productos.ui.UiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class LoginViewModel(private val repo: AuthRepository) : ViewModel() {
    private val mutable = MutableStateFlow<UiState<Sesion?>>(UiState.Cargando)
    val estado = mutable.asStateFlow()

    init { restaurar() }

    private fun restaurar() {
        viewModelScope.launch {
            mutable.value = resultado { repo.sesionActual() }.fold(
                { UiState.Exito(it) },
                { UiState.Error(mensaje(it)) })
        }
    }

    fun ingresar(correo: String, clave: String) {
        if (mutable.value == UiState.Cargando) return
        if (correo.isBlank() || clave.isBlank()) {
            mutable.value = UiState.Error("Completa el correo y la contraseña.")
            return
        }
        mutable.value = UiState.Cargando
        viewModelScope.launch {
            mutable.value = repo.iniciarSesion(correo.trim(), clave).fold(
                { UiState.Exito(it) },
                { UiState.Error(mensaje(it)) })
        }
    }

    fun cerrarSesion() {
        repo.cerrarSesion()
        mutable.value = UiState.Exito(null)
    }

    private fun mensaje(error: Throwable) = when (error) {
        is FirebaseAuthInvalidCredentialsException, is FirebaseAuthInvalidUserException -> "Correo o contraseña incorrectos."
        is IllegalStateException -> "Comprueba la configuración de Firebase y el rol en Firestore."
        else -> "No se pudo validar la sesión. Comprueba tu conexión."
    }
}