package com.example.lab05
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import kotlinx.coroutines.launch

@Composable
fun TecsupStoreApp() {
    val drawer = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val contexto = LocalContext.current
    var favoritos by remember { mutableStateOf(setOf<Int>()) }
    var pedidos by remember { mutableStateOf<List<ProductoStore>>(emptyList()) }
    var reportado by remember { mutableStateOf<ProductoStore?>(null) }
    var mensaje by remember { mutableStateOf("") }
    val marcar: (Int) -> Unit = { favoritos = toggleFavorito(favoritos, it) }
    val compartir: (ProductoStore) -> Unit = { producto ->
        val intento = Intent(Intent.ACTION_SEND).apply { type = "text/plain"; putExtra(Intent.EXTRA_TEXT, "${producto.nombre} · S/ %.2f".format(producto.precio)) }
        contexto.startActivity(Intent.createChooser(intento, "Compartir producto"))
    }
    val reportar: (ProductoStore) -> Unit = { reportado = it }
    if (!Practica.USAR_NAVEGACION) {
        ModalNavigationDrawer(drawerState = drawer, drawerContent = { AppDrawer("inicio", favoritos.size, {}) }) {
            StoreEstructura("TECSUP Store · práctica sin navegación", { scope.launch { drawer.open() } }) {
                StoreLista(productosStore, favoritos, marcar, compartir, reportar, {})
            }
        }
    } else {
        val nav = rememberNavController()
        val entrada by nav.currentBackStackEntryAsState()
        val ruta = entrada?.destination?.route ?: "inicio"
        val navegar: (String) -> Unit = { destino ->
            nav.navigate(destino) { if (destino in rutasStore) popUpTo("inicio") { inclusive = false }; launchSingleTop = true }
            scope.launch { drawer.close() }
        }
        ModalNavigationDrawer(drawerState = drawer, drawerContent = { AppDrawer(ruta, favoritos.size, navegar) }) {
            StoreEstructura("TECSUP Store · ${ruta.substringBefore('/')}", { scope.launch { drawer.open() } }) {
                NavHost(navController = nav, startDestination = "inicio") {
                    composable("inicio") { StoreLista(productosStore, favoritos, marcar, compartir, reportar, { navegar("detalle/$it") }) }
                    composable("favoritos") { StoreLista(productosStore.filter { it.id in favoritos }, favoritos, marcar, compartir, reportar, { navegar("detalle/$it") }) }
                    composable("pedidos") { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Mis pedidos de práctica", style = MaterialTheme.typography.headlineMedium)
                        if (pedidos.isEmpty()) Text("Aún no tienes pedidos")
                        pedidos.forEachIndexed { indice, producto -> Text("Pedido #${indice + 1}: ${producto.nombre} · Confirmado") }
                    } }
                    composable("perfil") { Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Jery Becerra", style = MaterialTheme.typography.headlineMedium)
                        Text("Estudiante TECSUP · Programación en Móviles")
                        Text("${favoritos.size} favoritos · ${pedidos.size} pedidos de práctica")
                        Text("Datos en memoria, sin servidor ni pagos reales")
                    } }
                    composable("salir") { Column(Modifier.padding(20.dp)) {
                        Text("¿Cerrar la sesión de demostración y limpiar sus datos?")
                        Button(onClick = { favoritos = emptySet(); pedidos = emptyList(); nav.navigate("inicio") { popUpTo("inicio") { inclusive = true } } }) { Text("Cerrar y reiniciar") }
                        TextButton(onClick = { nav.popBackStack() }) { Text("Cancelar") }
                    } }
                    composable("detalle/{productoId}", arguments = listOf(navArgument("productoId") { type = NavType.IntType })) { entry ->
                        val producto = productosStore.find { it.id == entry.arguments?.getInt("productoId") }
                        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            if (producto == null) Text("Producto no encontrado") else {
                                Text(producto.nombre, style = MaterialTheme.typography.headlineMedium)
                                Text("ID: ${producto.id} · ${producto.categoria}")
                                Text("S/ %.2f".format(producto.precio))
                                Button(onClick = { pedidos = pedidos + producto; navegar("pedidos") }) { Text("Confirmar pedido de práctica") }
                            }
                            TextButton(onClick = { nav.popBackStack() }) { Text("Volver") }
                        }
                    }
                }
            }
        }
    }
    reportado?.let { producto -> AlertDialog(onDismissRequest = { reportado = null },
        title = { Text("Reportar ${producto.nombre}") }, text = { Text("Registrar una observación local de demostración para este producto") },
        confirmButton = { TextButton(onClick = { mensaje = "Reporte local registrado para ${producto.nombre}"; reportado = null }) { Text("Confirmar reporte") } },
        dismissButton = { TextButton(onClick = { reportado = null }) { Text("Cancelar") } }) }
    if (mensaje.isNotEmpty()) AlertDialog(onDismissRequest = { mensaje = "" }, title = { Text("Resultado") }, text = { Text(mensaje) },
        confirmButton = { TextButton(onClick = { mensaje = "" }) { Text("Aceptar") } })
}
