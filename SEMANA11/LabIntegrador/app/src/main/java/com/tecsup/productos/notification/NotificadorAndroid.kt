package com.tecsup.productos.notification

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.tecsup.productos.MainActivity

class NotificadorAndroid(private val context: Context) : Notificador {
    override fun mostrar(titulo: String, texto: String) {
        if (Build.VERSION.SDK_INT >= 33 &&
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val manager = context.getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= 26) {
            manager.createNotificationChannel(NotificationChannel("sync", "Sincronización", NotificationManager.IMPORTANCE_DEFAULT))
        }
        val abrir = PendingIntent.getActivity(context, 0,
            Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
        manager.notify(1, NotificationCompat.Builder(context, "sync")
            .setSmallIcon(android.R.drawable.stat_notify_sync)
            .setContentTitle(titulo).setContentText(texto)
            .setContentIntent(abrir).setAutoCancel(true).build())
    }
}