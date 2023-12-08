package com.ppb.travelanywhere

import android.app.Application
import com.ppb.travelanywhere.services.database.AppDatabaseRepository
import com.ppb.travelanywhere.services.database.queue.DbQueueDatabase
import com.ppb.travelanywhere.services.database.stations.StationsDatabase
import com.ppb.travelanywhere.services.database.ticket_history.TicketHistoryDatabase
import com.ppb.travelanywhere.services.database.train_classes.TrainClassesDatabase
import com.ppb.travelanywhere.services.database.trains.TrainsDatabase

class TravelAnywhereApps : Application() {

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

}