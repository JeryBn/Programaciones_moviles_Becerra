package com.tecsup.cafecampus
import android.app.Application
import kotlinx.coroutines.*
import com.tecsup.cafecampus.data.local.*
class CafeApp:Application() {
 val db by lazy { CafeDatabase.obtener(this) }
 val inicializacion by lazy { CoroutineScope(SupervisorJob()+Dispatchers.IO).async { DatosIniciales.sembrar(db) } }
 override fun onCreate() { super.onCreate(); inicializacion.start() }
}

