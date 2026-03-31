package com.superbee.aeronautics.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SopViewModel : ViewModel() {
    val environment = MutableLiveData<String>()
    val droneModel = MutableLiveData<String>()
    val nozzleType = MutableLiveData<String>()
    val pesticideName = MutableLiveData<String>()
    val flightSpeed = MutableLiveData<String>()
    val sprayRate = MutableLiveData<String>()
    val operatorName = MutableLiveData<String>()
}
