package com.ppb.travelanywhere.services.database

import android.util.Log
import com.ppb.travelanywhere.services.database.queue.DbQueueTable
import com.ppb.travelanywhere.services.database.queue.DbQueueTableDao
import com.ppb.travelanywhere.services.database.stations.StationsTable
import com.ppb.travelanywhere.services.database.stations.StationsTableDao
import com.ppb.travelanywhere.services.database.ticket_history.TicketHistoryTable
import com.ppb.travelanywhere.services.database.ticket_history.TicketHistoryTableDao
import com.ppb.travelanywhere.services.database.train_classes.TrainClassesTable
import com.ppb.travelanywhere.services.database.train_classes.TrainClassesTableDao
import com.ppb.travelanywhere.services.database.trains.TrainsTable
import com.ppb.travelanywhere.services.database.trains.TrainsTableDao
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class AppDatabaseRepository(
    private val trainsTableDao : TrainsTableDao,
    private val stationsTableDao : StationsTableDao,
    private val trainClassesTableDao : TrainClassesTableDao,
    private val queueTableDao: DbQueueTableDao,
    private val ticketHistoryTableDao: TicketHistoryTableDao
) {

    private val executorService : ExecutorService = Executors.newSingleThreadExecutor()

    // Train
    val listTrains = trainsTableDao.selectAll

    suspend fun searchTrains(keyword: String): List<TrainsTable> {
        Log.d("AppDatabaseRepository", "searchTrains: $keyword")
        return trainsTableDao.search(keyword)
    }

    suspend fun getTrainById(id: String): TrainsTable {
        Log.d("AppDatabaseRepository", "getTrainById: $id")
        return trainsTableDao.getById(id)
    }

    fun insertTrain(train: TrainsTable) {
        Log.i("AppDatabaseRepository", "insertTrain: $train")
        executorService.execute {
            trainsTableDao.insert(train)
        }
    }

    fun updateTrain(train: TrainsTable) {
        Log.i("AppDatabaseRepository", "updateTrain: $train")
        executorService.execute {
            trainsTableDao.update(train)
        }
    }

    fun deleteTrainById(id: String) {
        Log.i("AppDatabaseRepository", "deleteTrainById: $id")
        executorService.execute {
            trainsTableDao.deleteById(id)
        }
    }

    fun deleteAllTrains() {
        Log.i("AppDatabaseRepository", "deleteAllTrains")
        executorService.execute {
            trainsTableDao.deleteAll()
        }
    }


    // Station
    val listStations = stationsTableDao.selectAll

    suspend fun searchStations(keyword: String): List<StationsTable> {
        Log.d("AppDatabaseRepository", "searchStations: $keyword")
        return stationsTableDao.search(keyword)
    }

    suspend fun getStationById(id: String): StationsTable {
        Log.d("AppDatabaseRepository", "getStationById: $id")
        return stationsTableDao.getById(id)
    }

    fun insertStation(station: StationsTable) {
        Log.i("AppDatabaseRepository", "insertStation: $station")
        executorService.execute {
            stationsTableDao.insert(station)
        }
    }

    fun updateStation(station: StationsTable) {
        Log.i("AppDatabaseRepository", "updateStation: $station")
        executorService.execute {
            stationsTableDao.update(station)
        }
    }

    fun deleteStationById(id: String) {
        Log.i("AppDatabaseRepository", "deleteStationById: $id")
        executorService.execute {
            stationsTableDao.deleteById(id)
        }
    }

    fun deleteAllStations() {
        Log.i("AppDatabaseRepository", "deleteAllStations")
        executorService.execute {
            stationsTableDao.deleteAll()
        }
    }


    // Train Class

    val listTrainClasses = trainClassesTableDao.selectAll


    suspend fun searchTrainClasses(keyword: String): List<TrainClassesTable> {
        Log.d("AppDatabaseRepository", "searchTrainClasses: $keyword")
        return trainClassesTableDao.search(keyword)
    }

    suspend fun getTrainClassById(id: String): TrainClassesTable {
        Log.d("AppDatabaseRepository", "getTrainClassById: $id")
        return trainClassesTableDao.getById(id)
    }

    fun insertTrainClass(trainClass: TrainClassesTable) {
        Log.i("AppDatabaseRepository", "insertTrainClass: $trainClass")
        executorService.execute {
            trainClassesTableDao.insert(trainClass)
        }
    }

    fun updateTrainClass(trainClass: TrainClassesTable) {
        Log.i("AppDatabaseRepository", "updateTrainClass: $trainClass")
        executorService.execute {
            trainClassesTableDao.update(trainClass)
        }
    }

    fun deleteTrainClassById(id: String) {
        Log.i("AppDatabaseRepository", "deleteStationById: $id")
        executorService.execute {
            trainClassesTableDao.deleteById(id)
        }
    }

    fun deleteAllTrainClasses() {
        Log.i("AppDatabaseRepository", "deleteAllStations")
        executorService.execute {
            trainClassesTableDao.deleteAll()
        }
    }


    // Queue

    val listQueue = queueTableDao.selectAll
    val showListQueue = queueTableDao.showQueue
    val countQueue = queueTableDao.count

    fun insertQueue(queue: DbQueueTable) {
        Log.i("AppDatabaseRepository", "insertQueue: $queue")
        executorService.execute {
            queueTableDao.insert(queue)
        }
    }

    fun deleteQueueById(id: Int) {
        Log.i("AppDatabaseRepository", "deleteQueueById: $id")
        executorService.execute {
            queueTableDao.deleteById(id)
        }
    }

    fun deleteAllQueue() {
        Log.i("AppDatabaseRepository", "deleteAllQueue")
        executorService.execute {
            queueTableDao.deleteAll()
        }
    }



    // Ticket History
    val listTicketHistory = ticketHistoryTableDao.selectAll
    val upComingTicketHistory = ticketHistoryTableDao.unComing

    fun insertTicketHistory(ticketHistory: TicketHistoryTable) {
        Log.i("AppDatabaseRepository", "insertTicketHistory: $ticketHistory")
        executorService.execute {
            ticketHistoryTableDao.insert(ticketHistory)
        }
    }

    fun deleteAllTicketHistory() {
        Log.i("AppDatabaseRepository", "deleteAllTicketHistory")
        executorService.execute {
            ticketHistoryTableDao.deleteAll()
        }
    }



}