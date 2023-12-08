package com.ppb.travelanywhere.dialog.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ppb.travelanywhere.services.database.stations.StationsTable

class StationsKedatanganViewModel : ViewModel() {
    var data = MutableLiveData<StationsTable>()
}