package com.tecsup.cafecampus.ui.cliente
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
@Composable fun SplashScreen() { Box(Modifier.fillMaxSize(),contentAlignment=Alignment.Center) { Column { Text("CafeCampus",style=MaterialTheme.typography.headlineLarge); CircularProgressIndicator() } } }

