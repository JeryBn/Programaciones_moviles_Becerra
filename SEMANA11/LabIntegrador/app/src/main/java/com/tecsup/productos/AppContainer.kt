package com.tecsup.productos

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.room.Room
import com.tecsup.productos.data.auth.FirebaseAuthRepository
import com.tecsup.productos.data.local.AppDatabase
import com.tecsup.productos.data.remote.RetrofitClient
import com.tecsup.productos.data.repository.ItemRepositoryImpl
import com.tecsup.productos.domain.model.Rol
import com.tecsup.productos.notification.NotificadorAndroid
import com.tecsup.productos.ui.admin.AdminViewModel
import com.tecsup.productos.ui.detalle.DetalleViewModel
import com.tecsup.productos.ui.favoritos.FavoritosViewModel
import com.tecsup.productos.ui.lista.ListaViewModel
import com.tecsup.productos.ui.login.LoginViewModel

class AppContainer(context: Context) {
    private val app = context.applicationContext
    private val db = Room.databaseBuilder(app, AppDatabase::class.java, "catalogo.db").build()
    private val items = ItemRepositoryImpl(RetrofitClient.crear(), db)
    private val auth = FirebaseAuthRepository()
    private val notificador = NotificadorAndroid(app)

    val factory: ViewModelProvider.Factory = viewModelFactory {
        initializer { LoginViewModel(auth) }
        initializer { ListaViewModel(items, notificador) }
        initializer { FavoritosViewModel(items) }
    }

    fun detalle(id: Int) = viewModelFactory { initializer { DetalleViewModel(id, items) } }
    fun admin(rol: Rol) = viewModelFactory { initializer { AdminViewModel(items, notificador, rol) } }
}