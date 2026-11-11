package com.tecsup.productos.data.auth

import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.productos.data.repository.resultado
import com.tecsup.productos.domain.model.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository : AuthRepository {
    private val auth get() = FirebaseAuth.getInstance()

    override suspend fun iniciarSesion(correo: String, clave: String) = resultado {
        check(FirebaseApp.getInstance().name.isNotBlank())
        val usuario = requireNotNull(auth.signInWithEmailAndPassword(correo, clave).await().user)
        try {
            leerSesion(usuario.uid, usuario.email.orEmpty())
        } catch (cancelacion: CancellationException) {
            throw cancelacion
        } catch (error: Exception) {
            auth.signOut()
            throw error
        }
    }

    override suspend fun sesionActual(): Sesion? {
        val usuario = auth.currentUser ?: return null
        return leerSesion(usuario.uid, usuario.email.orEmpty())
    }

    override fun cerrarSesion() = auth.signOut()

    private suspend fun leerSesion(uid: String, correo: String): Sesion {
        val doc = FirebaseFirestore.getInstance().collection("usuarios").document(uid).get().await()
        val rol = when (doc.getString("rol")) {
            "admin" -> Rol.ADMIN
            "usuario" -> Rol.USUARIO
            else -> error("La cuenta no tiene un rol válido en Firestore.")
        }
        return Sesion(uid, correo, rol)
    }
}