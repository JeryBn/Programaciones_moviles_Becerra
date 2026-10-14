package com.tecsup.cafecampus.data.prefs
import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import com.tecsup.cafecampus.model.Rol
val Context.dataStore by preferencesDataStore("ajustes")
class PreferenciasStore(context:Context) {
 private val store=context.applicationContext.dataStore
 private val perfil=stringPreferencesKey("perfilActivo")
 private val oscuro=booleanPreferencesKey("modoOscuro")
 private val recibir=booleanPreferencesKey("recibirPedidos")
 val perfilActivo=store.data.map { it[perfil]?:"" }
 val modoOscuro=store.data.map { it[oscuro]?:false }
 val recibirPedidos=store.data.map { it[recibir]?:true }
 suspend fun guardarPerfil(rol:Rol)=store.edit { it[perfil]=rol.name }
 suspend fun guardarModoOscuro(b:Boolean)=store.edit { it[oscuro]=b }
 suspend fun guardarRecibirPedidos(b:Boolean)=store.edit { it[recibir]=b }
 suspend fun cerrarSesion()=store.edit { it.remove(perfil) }
}
