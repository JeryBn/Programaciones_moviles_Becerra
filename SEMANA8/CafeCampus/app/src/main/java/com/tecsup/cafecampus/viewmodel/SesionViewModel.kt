package com.tecsup.cafecampus.viewmodel
import androidx.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import com.tecsup.cafecampus.data.repository.CafeRepository
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.data.local.entity.Usuario
import com.tecsup.cafecampus.model.Rol
class SesionViewModel(private val repo:CafeRepository,private val prefs:PreferenciasStore):ViewModel() {
 private val _usuario=MutableStateFlow<Usuario?>(null); val usuarioActual=_usuario.asStateFlow()
 private val _error=MutableStateFlow<String?>(null); val error=_error.asStateFlow()
 val listo=MutableStateFlow(false)
 val modoOscuro=prefs.modoOscuro.stateIn(viewModelScope,SharingStarted.Eagerly,false)
 val recibirPedidos=prefs.recibirPedidos.stateIn(viewModelScope,SharingStarted.Eagerly,true)
 init { viewModelScope.launch { val rol=prefs.perfilActivo.first(); if(rol.isNotBlank()) _usuario.value=repo.usuarioPorRol(Rol.valueOf(rol)); listo.value=true } }
 fun login(correo:String,clave:String,rolSeleccionado:Rol) { viewModelScope.launch {
  _error.value=null
  if(correo.isBlank()||clave.isBlank()) { _error.value="Completa correo y contraseña"; return@launch }
  val u=repo.login(correo,clave)
  if(u==null) _error.value="Correo o contraseña incorrectos"
  else if(u.rol!=rolSeleccionado.name) _error.value="El usuario no corresponde al perfil seleccionado"
  else { prefs.guardarPerfil(rolSeleccionado); _usuario.value=u }
 } }
 fun cambiarPerfil() { viewModelScope.launch { val rol=if(_usuario.value?.rol=="ADMIN") Rol.CLIENTE else Rol.ADMIN; _usuario.value=repo.usuarioPorRol(rol); prefs.guardarPerfil(rol) } }
 fun cerrarSesion() { viewModelScope.launch { prefs.cerrarSesion(); _usuario.value=null; _error.value=null } }
 fun oscuro(b:Boolean) { viewModelScope.launch { prefs.guardarModoOscuro(b) } }
 fun recibir(b:Boolean) { viewModelScope.launch { prefs.guardarRecibirPedidos(b) } }
}

