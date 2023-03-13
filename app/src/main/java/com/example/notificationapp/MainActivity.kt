package com.example.notificationapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Creamos activity que se ejecutará al clickear la notificación
        val unIntent = Intent(this, SecondActivity::class.java)

        //El último parámetro indica que apps externas pueden realizar acciones sobre nuestra app,
        //como, por ejemplo, las herramientas de push notification, y también que podamos nosotros
        //actualizar o remover la notificación (EJ: remover notificación de whatsapp si el usuario
        //ya vio el mensaje)
        //Un PendingIntent permite que aplicaciones externas (EJ: Sistema operativo Android)
        //ejecutar procesos (EJ: Carga de activity) dentro de mi app como si fueran la app. El
        //Intent es la acción, en este caso, cargar activity SecondActivity
        val unPendingIntent =
            PendingIntent.getActivity(this, 1, unIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        //En la ayuda para autocompletar el método TaskStackBuilder.create(), elegimos la clase de soporte siempre que se pueda porque tiene más compatibilidad
        val unTaskStackBuilder = TaskStackBuilder.create(this)
        unTaskStackBuilder.addNextIntentWithParentStack(unIntent)

        //Crear pending intent desde task stack, que como ya tiene un intent asociado, no hace falta pasarlo por parámetro
        unTaskStackBuilder.getPendingIntent(1, PendingIntent.FLAG_UPDATE_CURRENT)

        //Creamos botón con redireccionamiento a SecondActivity
        val unActionButtonIntent = Intent(this, SecondActivity::class.java)
        val unActionButtonPendingIntent = PendingIntent.getActivity(
            this,
            1,
            unActionButtonIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        //Creamos la notificación
        val unNotificationBuilder = NotificationCompat.Builder(this, "canal_id_001")
            .setContentTitle("Título: Notificación Activity 1")
            .setContentText("Contenido de notificación Activity 1")
            .setSmallIcon(R.mipmap.ic_launcher_round) //Ícono
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(unPendingIntent) //Agregamos redireccionamiento a una activity al clickear
            .addAction(
                0,
                "Ir a 2° acty.",
                unActionButtonPendingIntent
            ) //Agregamos opción con acción en notificación
//            .setAutoCancel(true) //Para que la notificación se autoelimine al ser clickeada, pero para esta app, vamos a hacerlo manualmente, creando la notificación acá y la eliminamos en la activity 2
            .setProgress(
                0,
                0,
                true
            ) //Esta configuración es para mostrar una barra que actúa como la ruedita de carga, que se vea que está ejecutando algo pero sin mostrar el progreso
            .setOngoing(true) //Evita que el usuario pueda eliminar la notificación deslizándola, si setAutoCancel(true), al clickearla, se elimina igual

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

        //De esta manera podemos obtener la notificación por si queremos modificar otros atributos
        val unNotification = unNotificationBuilder.build()

        //Asignamos un ID a la notificación para que luego, al ser clickeada, la eliminemos en la activity 2
        unNotificationManager.notify(1, unNotification)
    }
}