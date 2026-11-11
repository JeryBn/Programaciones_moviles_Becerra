package com.tecsup.productos

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import com.tecsup.productos.ui.navigation.AppNav
import com.tecsup.productos.ui.theme.CatalogoTheme

class MainActivity : ComponentActivity() {
    private val permiso = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = AppContainer(applicationContext)
        setContent { CatalogoTheme { AppNav(container) } }
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= 33 &&
            !getPreferences(MODE_PRIVATE).getBoolean("permiso_solicitado", false)) {
            getPreferences(MODE_PRIVATE).edit().putBoolean("permiso_solicitado", true).apply()
            permiso.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}