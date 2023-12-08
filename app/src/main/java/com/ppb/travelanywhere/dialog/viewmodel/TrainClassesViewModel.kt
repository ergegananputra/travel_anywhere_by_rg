package com.ppb.travelanywhere.dialog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ppb.travelanywhere.services.database.train_classes.TrainClassesTable

class TrainClassesViewModel : ViewModel() {
    var data = MutableLiveData<TrainClassesTable>()
}