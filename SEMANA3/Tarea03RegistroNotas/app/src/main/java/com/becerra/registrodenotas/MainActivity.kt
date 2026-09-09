package com.becerra.registrodenotas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.becerra.registrodenotas.ui.theme.Lab03RegistroProductoTheme
import kotlin.math.roundToInt

private val Purple = Color(0xFF6C52AD)
private val SoftLilac = Color(0xFFF8F4FF)
private val DeepGreen = Color(0xFF1B5E20)
private val Green = Color(0xFF2E7D32)
private val Amber = Color(0xFFF9A825)
private val Red = Color(0xFFC62828)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Lab03RegistroProductoTheme { RegistroNotasApp() } }
    }
}

@Composable
fun RegistroNotasApp() {
    var fundamentos by remember { mutableFloatStateOf(0f) }
    var poo by remember { mutableFloatStateOf(0f) }
    var moviles by remember { mutableFloatStateOf(0f) }
    var baseDatos by remember { mutableFloatStateOf(0f) }
    var redondear by remember { mutableStateOf(false) }
    var confirmar by remember { mutableStateOf(false) }
    var mostrarResultado by remember { mutableStateOf(false) }

    val promedioPonderado = fundamentos * 0.20 + poo * 0.25 + moviles * 0.30 + baseDatos * 0.25
    val promedioFinal = if (redondear) promedioPonderado.roundToInt().toDouble() else promedioPonderado
    val resultado = resultadoPara(promedioFinal)

    Column(modifier = Modifier.fillMaxSize().background(Brush.verticalGradient(listOf(Color(0xFFECE3FF), SoftLilac)))) {
        TopAppBar(
            title = { Text("Registro de Notas", fontWeight = FontWeight.Bold) },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple, titleContentColor = Color.White)
        )
        Column(
            modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()).padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Notas del ciclo", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
            Text("Desliza para asignar cada nota (0 a 20)", color = MaterialTheme.colorScheme.outline)

            FilaCurso("Fundamentos de Programación", "20%", fundamentos) { fundamentos = it; mostrarResultado = false }
            FilaCurso("Programación Orientada a Objetos", "25%", poo) { poo = it; mostrarResultado = false }
            FilaCurso("Programación en Móviles", "30%", moviles) { moviles = it; mostrarResultado = false }
            FilaCurso("Base de Datos", "25%", baseDatos) { baseDatos = it; mostrarResultado = false }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Redondear promedio final", modifier = Modifier.weight(1f))
                Switch(checked = redondear, onCheckedChange = { redondear = it; mostrarResultado = false })
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = confirmar, onCheckedChange = { confirmar = it })
                Text("Confirmo que las notas son correctas")
            }

            Button(
                onClick = { mostrarResultado = true }, enabled = confirmar,
                modifier = Modifier.fillMaxWidth().height(56.dp), shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Purple)
            ) { Text("CALCULAR PROMEDIO", fontWeight = FontWeight.Bold) }

            if (mostrarResultado) {
                TarjetaResultado(promedioPonderado, promedioFinal, redondear, resultado, fundamentos, poo, moviles, baseDatos)
                Text("✓ Promedio calculado correctamente", color = Green, fontWeight = FontWeight.SemiBold, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
            } else {
                Text("Asigna las notas y confirma para calcular", color = MaterialTheme.colorScheme.outline)
            }

            Button(
                onClick = { fundamentos = 0f; poo = 0f; moviles = 0f; baseDatos = 0f; redondear = false; confirmar = false; mostrarResultado = false },
                modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.outlinedButtonColors(contentColor = Purple)
            ) { Text("LIMPIAR") }
        }
        Text("Desarrollado por: Jery Becerra Ninaquispe", modifier = Modifier.fillMaxWidth().padding(16.dp), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.outline, fontSize = 12.sp)
    }
}

@Composable
private fun FilaCurso(nombre: String, peso: String, nota: Float, alCambiar: (Float) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(nombre, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Text("($peso)", color = Purple)
            Spacer(Modifier.width(12.dp))
            Text(nota.toInt().toString(), color = if (nota < 13f) Red else Green, fontWeight = FontWeight.Bold, modifier = Modifier.clip(RoundedCornerShape(10.dp)).background(Color(0xFFEDE4FF)).padding(horizontal = 12.dp, vertical = 5.dp))
        }
        Slider(value = nota, onValueChange = alCambiar, valueRange = 0f..20f, steps = 19, colors = androidx.compose.material3.SliderDefaults.colors(thumbColor = Purple, activeTrackColor = Purple))
    }
}

@Composable
private fun TarjetaResultado(ponderado: Double, final: Double, redondeado: Boolean, resultado: Resultado, fundamentos: Float, poo: Float, moviles: Float, baseDatos: Float) {
    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White), border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD0BFFF))) {
        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Promedio ponderado:  %.2f".format(ponderado), fontSize = 18.sp)
            Text("Promedio final:  ${if (redondeado) final.roundToInt() else "%.2f".format(final)}", fontSize = 20.sp, color = Purple, fontWeight = FontWeight.Bold)
            if (redondeado) Text("(redondeado)", color = MaterialTheme.colorScheme.outline, fontSize = 12.sp)
            ChipResultado(resultado)
            Text("Fundamentos: ${fundamentos.toInt()} × 20% = %.2f".format(fundamentos * .20))
            Text("POO: ${poo.toInt()} × 25% = %.2f".format(poo * .25))
            Text("Móviles: ${moviles.toInt()} × 30% = %.2f".format(moviles * .30))
            Text("Base de Datos: ${baseDatos.toInt()} × 25% = %.2f".format(baseDatos * .25))
        }
    }
}

@Composable
private fun ChipResultado(resultado: Resultado) {
    Box(modifier = Modifier.clip(RoundedCornerShape(18.dp)).background(resultado.color.copy(alpha = .16f)).padding(horizontal = 18.dp, vertical = 8.dp)) {
        Text(resultado.texto, color = resultado.color, fontWeight = FontWeight.Bold)
    }
}

private data class Resultado(val texto: String, val color: Color)

private fun resultadoPara(promedio: Double): Resultado = when {
    promedio >= 17 -> Resultado("EXCELENTE", DeepGreen)
    promedio >= 13 -> Resultado("APROBADO", Green)
    promedio >= 10 -> Resultado("EN RECUPERACIÓN", Amber)
    else -> Resultado("DESAPROBADO", Red)
}
