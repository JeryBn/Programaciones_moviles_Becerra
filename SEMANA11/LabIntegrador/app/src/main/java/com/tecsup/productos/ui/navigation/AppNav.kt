package com.tecsup.productos.ui.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.*
import com.tecsup.productos.AppContainer
import com.tecsup.productos.domain.model.*
import com.tecsup.productos.ui.UiState
import com.tecsup.productos.ui.admin.AdminScreen
import com.tecsup.productos.ui.detalle.DetalleScreen
import com.tecsup.productos.ui.favoritos.FavoritosScreen
import com.tecsup.productos.ui.lista.ListaScreen
import com.tecsup.productos.ui.login.*

@Composable
fun AppNav(container: AppContainer) {
    val login: LoginViewModel = viewModel(factory = container.factory)
    val estado by login.estado.collectAsStateWithLifecycle()
    val sesion = (estado as? UiState.Exito)?.datos
    if (sesion == null) {
        Scaffold { padding ->
            Box(Modifier.padding(padding)) {
                LoginScreen(estado == UiState.Cargando, (estado as? UiState.Error)?.mensaje, login::ingresar)
            }
        }
    } else {
        key(sesion.uid) { CatalogoNav(container, sesion, login::cerrarSesion) }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CatalogoNav(container: AppContainer, sesion: Sesion, cerrar: () -> Unit) {
    val nav = rememberNavController()
    val entrada by nav.currentBackStackEntryAsState()
    val ruta = entrada?.destination?.route
    val titulo = when (ruta) {
        "favoritos" -> "Favoritos"
        "admin" -> "Panel Admin"
        "detalle/{id}" -> "Detalle"
        else -> "Smartphones"
    }
    val abrir: (Int) -> Unit = { nav.navigate("detalle/$it") }
    Scaffold(topBar = {
        TopAppBar(title = { Text(titulo) }, navigationIcon = {
            if (ruta == "detalle/{id}") TextButton(onClick = { nav.popBackStack() }) { Text("Volver") }
        }, actions = { TextButton(onClick = cerrar) { Text("Cerrar sesión") } })
    }, bottomBar = {
        if (ruta != "detalle/{id}") NavigationBar {
            NavigationBarItem(ruta == "lista", { nav.popBackStack("lista", false) },
                icon = { Text("≡") }, label = { Text("Catálogo") })
            NavigationBarItem(ruta == "favoritos", { nav.navigate("favoritos") {
                popUpTo("lista"); launchSingleTop = true
            } }, icon = { Text("★") }, label = { Text("Favoritos") })
            if (sesion.rol == Rol.ADMIN) NavigationBarItem(ruta == "admin", { nav.navigate("admin") {
                popUpTo("lista"); launchSingleTop = true
            } }, icon = { Text("⚙") }, label = { Text("Admin") })
        }
    }) { padding ->
        NavHost(nav, startDestination = "lista", Modifier.padding(padding).padding(horizontal = 16.dp)) {
            composable("lista") { ListaScreen(viewModel(factory = container.factory), abrir) }
            composable("favoritos") { FavoritosScreen(viewModel(factory = container.factory), abrir) }
            composable("detalle/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) {
                val id = requireNotNull(it.arguments).getInt("id")
                DetalleScreen(viewModel(factory = container.detalle(id)))
            }
            if (sesion.rol == Rol.ADMIN) composable("admin") {
                AdminScreen(viewModel(factory = container.admin(sesion.rol)))
            }
        }
    }
}