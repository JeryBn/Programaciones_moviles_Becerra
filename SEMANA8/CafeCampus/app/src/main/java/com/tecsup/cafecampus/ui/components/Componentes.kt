package com.tecsup.cafecampus.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.tecsup.cafecampus.data.local.entity.Producto
import com.tecsup.cafecampus.data.local.relation.PedidoConDetalle
@Composable fun CafeButton(texto:String,enabled:Boolean=true,onClick:()->Unit) { Button(onClick=onClick,enabled=enabled,shape=RoundedCornerShape(10.dp),modifier=Modifier.fillMaxWidth()) { Text(texto) } }
@Composable fun CafeTextField(texto:String,label:String,password:Boolean=false,onChange:(String)->Unit) { OutlinedTextField(value=texto,onValueChange=onChange,label={Text(label)},singleLine=true,visualTransformation=if(password) PasswordVisualTransformation() else VisualTransformation.None,modifier=Modifier.fillMaxWidth()) }
@Composable fun ProductoIcono(icono:String) { Text(when(icono) {"cafe"->"☕";"plato"->"🍽";"jugo"->"🍊";"pan"->"🥪";"botella"->"🥤";else->"🥟"},style=MaterialTheme.typography.headlineLarge) }
@Composable fun ProductoCard(p:Producto,onClick:()->Unit) { OutlinedCard(onClick=onClick,shape=RoundedCornerShape(12.dp),modifier=Modifier.fillMaxWidth()) { Column(Modifier.padding(16.dp),verticalArrangement=Arrangement.spacedBy(8.dp)) { ProductoIcono(p.icono); Text(p.nombre,style=MaterialTheme.typography.titleMedium); Text("S/ %.2f".format(p.precio),color=MaterialTheme.colorScheme.primary); Text(if(p.disponible) "Disponible" else "Agotado") } } }
@Composable fun EstadoChip(estado:String) { SuggestionChip(onClick={},label={Text(estado)},colors=SuggestionChipDefaults.suggestionChipColors(labelColor=when(estado) {"RECIBIDO"->androidx.compose.ui.graphics.Color(0xFF2563EB);"PREPARANDO"->androidx.compose.ui.graphics.Color(0xFFB45309);"LISTO"->androidx.compose.ui.graphics.Color(0xFF15803D);"CANCELADO"->MaterialTheme.colorScheme.error;else->MaterialTheme.colorScheme.onSurfaceVariant})) }
@Composable fun PedidoCard(p:PedidoConDetalle,onClick:()->Unit) { OutlinedCard(onClick=onClick,modifier=Modifier.fillMaxWidth(),shape=RoundedCornerShape(12.dp)) { Column(Modifier.padding(16.dp)) { Text("Pedido #${p.pedido.id} · ${p.cliente.nombre}",style=MaterialTheme.typography.titleMedium); EstadoChip(p.pedido.estado); Text("Recojo ${p.pedido.horaRecojo} · S/ %.2f".format(p.pedido.total)); if(p.pedido.nota.isNotBlank()) Text(p.pedido.nota) } } }
@Composable fun StepperEstado(estado:String) { Column { listOf("RECIBIDO","PREPARANDO","LISTO","ENTREGADO").forEach { Text((if(it==estado) "● " else "○ ")+it,modifier=Modifier.padding(8.dp),color=if(it==estado) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant) }; if(estado=="CANCELADO") EstadoChip(estado) } }

