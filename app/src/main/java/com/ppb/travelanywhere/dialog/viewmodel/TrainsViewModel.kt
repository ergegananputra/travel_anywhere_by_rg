package com.ppb.travelanywhere.dialog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ppb.travelanywhere.services.database.trains.TrainsTable

class TrainsViewModel : ViewModel() {
    var data = MutableLiveData<TrainsTable>()
}