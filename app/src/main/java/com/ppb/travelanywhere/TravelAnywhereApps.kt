package com.ppb.travelanywhere

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.ppb.travelanywhere.services.database.AppDatabaseRepository
import com.ppb.travelanywhere.services.database.queue.DbQueueDatabase
import com.ppb.travelanywhere.services.database.stations.StationsDatabase
import com.ppb.travelanywhere.services.database.ticket_history.TicketHistoryDatabase
import com.ppb.travelanywhere.services.database.train_classes.TrainClassesDatabase
import com.ppb.travelanywhere.services.database.trains.TrainsDatabase

class TravelAnywhereApps : Application() {
    private val CHANNEL_ID = "TRAVEL_ANYWHERE_REMINDER"
    private var _notifId = 0
    private val notifId: Int
        get() = _notifId++

    private val trainDb by lazy {
        TrainsDatabase.getDatabase(this)
    }
    private val stationDb by lazy {
        StationsDatabase.getDatabase(this)
    }
    private val trainClassDb by lazy {
        TrainClassesDatabase.getDatabase(this)
    }
    private val queueDb by lazy {
        DbQueueDatabase.getDatabase(this)
    }
    private val ticketHistoryDb by lazy {
        TicketHistoryDatabase.getDatabase(this)
    }

    val appRepository by lazy {
        AppDatabaseRepository(
            trainDb.trainsTableDao(),
            stationDb.stationsTableDao(),
            trainClassDb.trainClassesTableDao(),
            queueDb.dbQueueTableDao(),
            ticketHistoryDb.ticketHistoryTableDao()
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        val channel = NotificationChannel(
            CHANNEL_ID,
            "High priority notifications",
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(title : String, message : String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder =
            NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.icon_travel_anywhere)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)

        notificationManager.notify(666, notificationBuilder.build())
    }

}