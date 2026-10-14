package com.tecsup.cafecampus.ui.navigation
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.*
import com.tecsup.cafecampus.viewmodel.*
import com.tecsup.cafecampus.ui.cliente.*
import com.tecsup.cafecampus.ui.admin.*
@OptIn(ExperimentalMaterial3Api::class)
@Composable fun NavCafe(sesion:SesionViewModel,menu:MenuViewModel,cliente:PedidosClienteViewModel,panel:PanelAdminViewModel,pedidos:PedidosAdminViewModel,adminMenu:MenuAdminViewModel,recreo:MiRecreoViewModel) {
 val usuario by sesion.usuarioActual.collectAsState(); val u=usuario?:return;val admin=u.rol=="ADMIN"
 // Perfil cambia la identidad del grafo y limpia su back stack. Menú sobrevive rotación.
 key(u.id) {
 val nav=rememberNavController()
 val abierto by sesion.recibirPedidos.collectAsState()
 val entry by nav.currentBackStackEntryAsState();val ruta=entry?.destination?.route
 val tabs=if(admin) listOf("panel" to "Panel","admin_pedidos" to "Pedidos","admin_menu" to "Menú","ajustes" to "Ajustes") else listOf("menu" to "Menú","pedidos" to "Pedidos","perfil" to "Perfil")
 Scaffold(topBar={TopAppBar(title={Text(if(admin) "CafeCampus · Admin" else "CafeCampus · Estudiante")})},bottomBar={NavigationBar {tabs.forEachIndexed {i,(r,t)->NavigationBarItem(selected=ruta==r,onClick={nav.navigate(r) {popUpTo(nav.graph.startDestinationId);launchSingleTop=true}},icon={Icon(when(i) {0->Icons.Default.Home;1->Icons.Default.ReceiptLong;2->Icons.Default.RestaurantMenu;else->Icons.Default.Settings},null)},label={Text(t)})}}}) {padding->
  NavHost(nav,startDestination=if(admin) "panel" else "menu",modifier=Modifier.padding(padding)) {
   composable("menu") {MenuScreen(menu,recreo,{nav.navigate("detalle/$it")},{nav.navigate("confirmar")})}
   composable("pedidos") {MisPedidosScreen(cliente,menu,{nav.navigate("confirmar")}) {nav.navigate("seguimiento/$it")}}
   composable("perfil") {AjustesScreen(sesion)}
   composable("detalle/{id}",arguments=listOf(navArgument("id") {type=NavType.IntType})) {e->DetalleProductoScreen(menu,e.arguments!!.getInt("id")) {nav.popBackStack()}}
   composable("confirmar") {ConfirmarPedidoScreen(menu,cliente,abierto,{nav.popBackStack()},{id->nav.navigate("seguimiento/$id") {popUpTo("menu");launchSingleTop=true}})}
   composable("seguimiento/{id}",arguments=listOf(navArgument("id") {type=NavType.IntType})) {e->SeguimientoScreen(cliente,e.arguments!!.getInt("id")) {nav.navigate("pedidos") {popUpTo("menu");launchSingleTop=true}}}
   composable("panel") {PanelScreen(panel,pedidos) {nav.navigate("admin_detalle/$it")}}
   composable("admin_pedidos") {PedidosAdminScreen(pedidos) {nav.navigate("admin_detalle/$it")}}
   composable("admin_menu") {MenuAdminScreen(adminMenu) {nav.navigate("formulario/$it")}}
   composable("admin_detalle/{id}",arguments=listOf(navArgument("id") {type=NavType.IntType})) {e->DetallePedidoAdminScreen(pedidos,e.arguments!!.getInt("id")) {nav.popBackStack()}}
   composable("formulario/{id}",arguments=listOf(navArgument("id") {type=NavType.IntType})) {e->FormularioProductoScreen(adminMenu,e.arguments!!.getInt("id")) {nav.popBackStack()}}
   composable("ajustes") {AjustesScreen(sesion)}
  }
 }
 }
}

