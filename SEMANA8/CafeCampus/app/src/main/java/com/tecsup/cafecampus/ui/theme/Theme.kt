package com.tecsup.cafecampus.ui.theme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.tecsup.cafecampus.model.Rol
@Composable fun CafeTheme(rol:Rol=Rol.CLIENTE,oscuro:Boolean=false,content:@Composable ()->Unit) {
 val admin=rol==Rol.ADMIN
 val primary=Color(if(admin) 0xFF4F46E5 else 0xFFEA580C)
 val scheme=if(oscuro) darkColorScheme(primary=Color(if(admin) 0xFFC7D2FE else 0xFFFDBA74))
 else lightColorScheme(primary=primary,primaryContainer=Color(if(admin) 0xFFE0E7FF else 0xFFFFEDD5),background=Color(if(admin) 0xFFF7F7FD else 0xFFFFFBF7))
 MaterialTheme(colorScheme=scheme,content=content)
}

