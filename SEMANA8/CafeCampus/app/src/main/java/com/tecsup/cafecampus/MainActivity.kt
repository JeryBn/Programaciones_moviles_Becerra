package com.tecsup.cafecampus
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tecsup.cafecampus.data.repository.CafeRepositoryRoom
import com.tecsup.cafecampus.data.prefs.PreferenciasStore
import com.tecsup.cafecampus.viewmodel.*
import com.tecsup.cafecampus.model.Rol
import com.tecsup.cafecampus.ui.theme.CafeTheme
import com.tecsup.cafecampus.ui.cliente.*
import com.tecsup.cafecampus.ui.navigation.NavCafe
class MainActivity:ComponentActivity() {
 override fun onCreate(savedInstanceState:Bundle?) {super.onCreate(savedInstanceState)
  val app=application as CafeApp
  val repo=CafeRepositoryRoom(app.db);val prefs=PreferenciasStore(app)
  setContent {
   var inicializado by remember {mutableStateOf(false)}
   LaunchedEffect(Unit) {app.inicializacion.await();inicializado=true}
   if(!inicializado) CafeTheme {SplashScreen()} else {
    val sesion:SesionViewModel=viewModel(factory=CafeViewModelFactory {SesionViewModel(repo,prefs)})
    val usuario by sesion.usuarioActual.collectAsState();val oscuro by sesion.modoOscuro.collectAsState();val listo by sesion.listo.collectAsState()
    CafeTheme(if(usuario?.rol=="ADMIN") Rol.ADMIN else Rol.CLIENTE,oscuro) {
     val background=MaterialTheme.colorScheme.background
     SideEffect {window.statusBarColor=background.toArgb();WindowCompat.getInsetsController(window,window.decorView).isAppearanceLightStatusBars=!oscuro}
     if(!listo) SplashScreen() else if(usuario==null) LoginScreen(sesion) else NavCafe(repo,prefs,sesion)
    }
   }
  }
 }
}

