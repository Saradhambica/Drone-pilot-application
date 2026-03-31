package com.superbee.aeronautics.gcs.mavlink

import androidx.compose.runtime.mutableStateOf

object TelemetryState {

    val battery = mutableStateOf(0)
    val altitude = mutableStateOf(0.0)
    val roll = mutableStateOf(0f)
    val pitch = mutableStateOf(0f)
    val yaw = mutableStateOf(0f)
    val latitude = mutableStateOf(0.0)
    val longitude = mutableStateOf(0.0)
    val connected = mutableStateOf(false)
    val armed = mutableStateOf(false)
    var lastHeartbeatTime: Long = 0L

    val systemId = mutableStateOf(-1)
    val componentId = mutableStateOf(-1)

    val heading = mutableStateOf(0f)


}
