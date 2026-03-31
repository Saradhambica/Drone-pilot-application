package com.superbee.aeronautics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FarmerViewModel : ViewModel() {
    private val _selectedFarmer = MutableLiveData<Farmer?>()
    val selectedFarmer: LiveData<Farmer?> get() = _selectedFarmer

    fun selectFarmer(farmer: Farmer) {
        _selectedFarmer.value = farmer
    }

    fun clearSelection() {
        _selectedFarmer.value = null
    }
}
