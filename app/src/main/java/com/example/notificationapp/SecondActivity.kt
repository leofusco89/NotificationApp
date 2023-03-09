    package com.example.notificationapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat

class SecondActivity : AppCompatActivity(), AsyncNotif.OnAsyncNotif {
    private lateinit var unNotificationBuilder: NotificationCompat.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        //Creamos la notificación
        unNotificationBuilder = NotificationCompat.Builder(this, "canal_id_001")
            .setContentTitle("Notif acty 2")
            .setContentText("Este es el contenido")
            .setSmallIcon(R.mipmap.ic_launcher_round) //Ícono
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true) //Para que la notificación se elimine al ser clickeada
            .setProgress(
                0,
                0,
                true
            ) //Esta configuración es para mostrar una barra que actúa como la ruedita de carga, que se vea que está corriendo algo pero sin mostrar el progreso

        //Obtiene la referencia del servicio del sistema que maneja las notificaciones
        val unNotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        //Crear canal si no fue creado previamente y si es versión superior a Android 8.0 (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val unNotificationChannel = NotificationChannel(
                "canal_id_001",
                "Canal para nuestra notif",
                NotificationManager.IMPORTANCE_HIGH
            )
            unNotificationChannel.description = "Descripción del canal"
            unNotificationChannel.lockscreenVisibility =
                NotificationCompat.VISIBILITY_PRIVATE //Para este canal, en pantalla de bloqueo, solo muestra título e ícono
            unNotificationManager.createNotificationChannel(unNotificationChannel)
        }

        unNotificationManager.notify(2, unNotificationBuilder.build())

        //Ejecutamos en este thread el proceso de actualización de la progress bar de la notificación que acabamos de mostrar
        val async = AsyncNotif(this)
        async.execute()
    }

    override fun onAsyncNotifDoInBackground() {
        //Crear canal si no fue creado previamente y si es versión superior a Android 8.0 (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Del segundo 0 al 3 progress bar funciona de manera indeterminada
            Thread.sleep(3000)

            //Del segundo 3 al 7, indicamos que la progress bar pasa a ser determinada vaya de 0% a 100% de a 25% por segundo
            unNotificationBuilder.setProgress(100, 0, false)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC) //Para este canal, en pantalla de bloqueo, vemos todo el contenido

            //Gatillamos el cambio que hicimos en la notificación
            val unNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            unNotificationManager.notify(2, unNotificationBuilder.build())

            //Llenamos la barra de 25% en 25%
            var progress = 0
            while (progress < 100) {
                Thread.sleep(1000)
                progress += 25
                unNotificationBuilder.setProgress(100, progress, false)
                unNotificationManager.notify(2, unNotificationBuilder.build())
            }
        }
    }

    override fun onAsyncNotifPostExecute() {
        //Crear canal si no fue creado previamente y si es versión superior a Android 8.0 (Oreo)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Del segundo 7 al 10, indicamos que la progress bar estará llena
            unNotificationBuilder.setProgress(0, 0, false)
            val unNotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            unNotificationManager.notify(2, unNotificationBuilder.build())
            Thread.sleep(5000)

            //En el segundo 10, eliminamos la notificación
            unNotificationManager.cancel(2)
        }
    }
}
