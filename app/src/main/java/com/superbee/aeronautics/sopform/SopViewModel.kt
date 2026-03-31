package com.superbee.aeronautics.sopform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SopViewModel : ViewModel() {
    // Step 1 data
    val location = MutableLiveData<String>()
    val latLong = MutableLiveData<String>()
    val altitude = MutableLiveData<String>()
    val surrounding = MutableLiveData<String>()

    // Step 2 data
    val cropName = MutableLiveData<String>()
    val variety = MutableLiveData<String>()
    val type = MutableLiveData<String>()
    val spacing = MutableLiveData<String>()
    val dateSowing = MutableLiveData<String>()
    val cropStage = MutableLiveData<String>()
    val shadedArea = MutableLiveData<String>()

    //step3
    val classification = MutableLiveData<String>()
    val category = MutableLiveData<String>()
    val maxWeight = MutableLiveData<String>()
    val powerSource = MutableLiveData<String>()
    val otherSpecs = MutableLiveData<String>()
    val photoUri = MutableLiveData<String>()



    //step4
    val tankCapacity = MutableLiveData<String>()
    val nozzleMounting = MutableLiveData<String>()
    val boomLength = MutableLiveData<String>()
    val nozzleCount = MutableLiveData<String>()
    val nozzleType = MutableLiveData<String>()
    val coneAngle = MutableLiveData<String>()
    val dropletSize = MutableLiveData<String>()
    val flowRate = MutableLiveData<String>()
    val operatingPressure = MutableLiveData<String>()


    //step 5A
    val targetDisease = MutableLiveData<String>()
    val formulationType = MutableLiveData<String>()
    val pesticideName = MutableLiveData<String>()
    val concentration = MutableLiveData<String>()
    val dosage = MutableLiveData<String>()
    val waterVolume = MutableLiveData<String>()


    //step 5B
    val targetDeficiency2 = MutableLiveData<String>()
    val formulationType2 = MutableLiveData<String>()
    val cropNutrientName2 = MutableLiveData<String>()
    val nutrientType2 = MutableLiveData<String>()
    val concentration2 = MutableLiveData<String>()
    val dosage2 = MutableLiveData<String>()
    val waterVolume2 = MutableLiveData<String>()

    //step 6A
    val temperature = MutableLiveData<String>()
    val relativeHumidity = MutableLiveData<String>()
    val wind = MutableLiveData<String>()

    //step 6B
    val flightModes = MutableLiveData<String>()
    val heightCanopy = MutableLiveData<String>()
    val swath = MutableLiveData<String>()
    val overlap = MutableLiveData<String>()
    val sprayWidth = MutableLiveData<String>()
    val flightDirection = MutableLiveData<String>()
    val timeOfSpray = MutableLiveData<String>()
    val multipleFlight = MutableLiveData<String>()

    //step 6C
    val fieldCapacityTheoretical = MutableLiveData<String>()
    val fieldCapacityActual = MutableLiveData<String>()
    val vmd = MutableLiveData<String>()
    val nmd = MutableLiveData<String>()
    val dropletDensity = MutableLiveData<String>()
    val sprayUniformity = MutableLiveData<String>()
    val controlEfficiency = MutableLiveData<String>()
    val phytoToxicity = MutableLiveData<String>()
    val efficacy = MutableLiveData<String>()
    val otherObservations = MutableLiveData<String>()



    // to store Firestore submission ID
    val submissionId = MutableLiveData<String>()



}