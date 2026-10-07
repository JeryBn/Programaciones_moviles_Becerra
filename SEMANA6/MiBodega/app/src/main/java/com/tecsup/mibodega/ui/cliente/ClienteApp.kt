package com.tecsup.mibodega.ui.cliente

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.tecsup.mibodega.ui.cliente.modelo.*
import com.tecsup.mibodega.ui.cliente.screens.*
import com.tecsup.mibodega.ui.cliente.screens.bienvenida.BienvenidaScreen
import com.tecsup.mibodega.ui.cliente.screens.registro.RegistroScreen
import com.tecsup.mibodega.ui.cliente.screens.inicio.InicioScreen
import com.tecsup.mibodega.ui.cliente.screens.detalle.DetalleProductoScreen
import com.tecsup.mibodega.ui.cliente.screens.carrito.CarritoScreen
import com.tecsup.mibodega.ui.cliente.screens.entrega.DatosEntregaScreen
import com.tecsup.mibodega.ui.cliente.screens.confirmacion.ConfirmacionScreen
import com.tecsup.mibodega.ui.theme.BodegaTheme

@Composable
fun ClienteApp() {
    var oscuro by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("Jery · cuenta de práctica") }
    var carrito by remember { mutableStateOf<List<ItemCarrito>>(emptyList()) }
    var favoritos by remember { mutableStateOf(setOf<Int>()) }
    var pedidos by remember { mutableStateOf<List<Pedido>>(emptyList()) }
    var ultimoPedido by remember { mutableStateOf<Pedido?>(null) }
    var informacion by remember { mutableStateOf(false) }
    val agregar: (Producto, Int) -> Unit = { producto, cantidad ->
        val existente = carrito.any { it.producto.id == producto.id }
        carrito = if (existente) carrito.map { if (it.producto.id == producto.id) it.copy(cantidad = it.cantidad + cantidad) else it }
            else carrito + ItemCarrito(producto, cantidad)
    }
    val marcar: (Int) -> Unit = { id -> favoritos = if (id in favoritos) favoritos - id else favoritos + id }
    BodegaTheme(oscuro = oscuro) {
        if (!Practica.USAR_NAVEGACION) {
            Estructura("Mi Bodega · práctica sin navegación", "inicio", carrito.sumOf { it.cantidad }, {}) {
                InicioScreen(favoritos = favoritos, onFavorito = marcar, onProductoClick = {}, onAgregarProducto = { agregar(it, 1) })
            }
        } else {
            val nav = rememberNavController()
            val entrada by nav.currentBackStackEntryAsState()
            val ruta = entrada?.destination?.route ?: "bienvenida"
            val navegar: (String) -> Unit = { destino -> nav.navigate(destino) {
                if (destino in secciones) popUpTo("inicio") { inclusive = false }
                launchSingleTop = true
            } }
            Estructura("Mi Bodega · ${ruta.substringBefore('/')}", ruta, carrito.sumOf { it.cantidad }, navegar,
                if (ruta in secciones || ruta == "bienvenida") null else ({ nav.popBackStack(); Unit })) {
                NavHost(navController = nav, startDestination = "bienvenida",
                    enterTransition = { fadeIn() + slideInHorizontally { it / 8 } },
                    exitTransition = { fadeOut() }, popEnterTransition = { fadeIn() }, popExitTransition = { fadeOut() }) {
                    composable("bienvenida") {
                        BienvenidaScreen({ navegar("registro") }, { navegar("inicio") }, { informacion = true })
                    }
                    composable("registro") { RegistroScreen({ nav.popBackStack() }) { n, _, _, _ ->
                        nombre = n; nav.navigate("inicio") { popUpTo("bienvenida") { inclusive = true } }
                    } }
                    composable("inicio") { InicioScreen(favoritos = favoritos, onFavorito = marcar,
                        onProductoClick = { navegar("detalle/${it.id}") }, onAgregarProducto = { agregar(it, 1) }) }
                    composable("favoritos") {
                        if (favoritos.isEmpty()) Text("Aún no tienes favoritos")
                        else InicioScreen(productos = listaProductosFake.filter { it.id in favoritos }, favoritos = favoritos,
                            onFavorito = marcar, onProductoClick = { navegar("detalle/${it.id}") }, onAgregarProducto = { agregar(it, 1) })
                    }
                    composable("pedidos") { PedidosScreen(pedidos) }
                    composable("perfil") { PerfilScreen(nombre, oscuro, { oscuro = it }) {
                        carrito = emptyList(); favoritos = emptySet(); pedidos = emptyList(); ultimoPedido = null
                        nombre = "Jery · cuenta de práctica"
                        nav.navigate("bienvenida") { popUpTo(nav.graph.id) { inclusive = true } }
                    } }
                    composable("detalle/{productoId}", arguments = listOf(navArgument("productoId") { type = NavType.IntType })) { entry ->
                        val producto = listaProductosFake.find { it.id == entry.arguments?.getInt("productoId") }
                        if (producto == null) Text("Producto no encontrado") else DetalleProductoScreen(producto,
                            { nav.popBackStack() }, { p, cantidad -> agregar(p, cantidad); navegar("carrito") },
                            producto.id in favoritos, { marcar(producto.id) })
                    }
                    composable("carrito") { CarritoScreen(carrito, { nav.popBackStack() },
                        { agregar(it, 1) }, { p -> carrito = carrito.map { if (it.producto.id == p.id) it.copy(cantidad = (it.cantidad - 1).coerceAtLeast(1)) else it } },
                        { p -> carrito = carrito.filterNot { it.producto.id == p.id } }, { if (carrito.isNotEmpty()) navegar("entrega") }) }
                    composable("entrega") { DatosEntregaScreen(carrito, { nav.popBackStack() }) { n, direccion, recojo ->
                        val pedido = Pedido((pedidos.maxOfOrNull { it.id } ?: 0) + 1, carrito.toList(), n, direccion, recojo, totalEntrega(carrito, recojo))
                        pedidos = pedidos + pedido; ultimoPedido = pedido; carrito = emptyList()
                        nav.navigate("confirmacion") { popUpTo("inicio") { inclusive = false }; launchSingleTop = true }
                    } }
                    composable("confirmacion") {
                        // AnimatedContent anima el resumen si cambia el pedido; NavHost anima los cambios de destino.
                        AnimatedContent(targetState = ultimoPedido, label = "Resumen del pedido") { pedido ->
                            ConfirmacionScreen(total = pedido?.total ?: 0.0, onVolverInicio = {
                                nav.navigate("inicio") { popUpTo("inicio") { inclusive = true } }
                            })
                        }
                    }
                }
            }
        }
        if (informacion) AlertDialog(onDismissRequest = { informacion = false }, title = { Text("Demostración académica") },
            text = { Text("Productos y pedidos se guardan solo en memoria. No hay pagos ni cuentas reales.") },
            confirmButton = { TextButton(onClick = { informacion = false }) { Text("Entendido") } })
    }
}
