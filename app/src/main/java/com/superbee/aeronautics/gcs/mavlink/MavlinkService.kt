package com.superbee.aeronautics.gcs.mavlink

import android.util.Log
import io.dronefleet.mavlink.MavlinkConnection
import io.dronefleet.mavlink.common.Attitude
import io.dronefleet.mavlink.common.CommandLong
import io.dronefleet.mavlink.common.GlobalPositionInt
import io.dronefleet.mavlink.common.MavCmd
import io.dronefleet.mavlink.common.SysStatus
import io.dronefleet.mavlink.minimal.Heartbeat
import java.io.EOFException
import java.io.OutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetSocketAddress
import kotlin.concurrent.thread


class MavlinkService {

    private var socket: DatagramSocket? = null
    private var running = false

    private var droneAddress: InetSocketAddress? = null
    private var mavConnection: MavlinkConnection? = null

    fun start() {
        if (running) return
        running = true

        thread(start = true) {
            try {
                socket = DatagramSocket(null).apply {
                    reuseAddress = true
                    bind(InetSocketAddress(14550))
                }

                thread(start = true) {
                    while (running) {

                        val now = System.currentTimeMillis()

                        if (now - TelemetryState.lastHeartbeatTime > 2000) {
                            TelemetryState.connected.value = false
                        }

                        Thread.sleep(1000)
                    }
                }


                Log.d("MavlinkService", "Listening on UDP 14550")

                val buffer = ByteArray(2048)

                while (running) {

                    val packet = DatagramPacket(buffer, buffer.size)
                    socket?.receive(packet)

                    // Save drone address
                    droneAddress = InetSocketAddress(packet.address, packet.port)

                    val inputStream = packet.data
                        .copyOf(packet.length)
                        .inputStream()

                    val outputStream = createOutputStream()

                    mavConnection = MavlinkConnection.create(
                        inputStream,
                        outputStream
                    )

                    try {
                        var message = mavConnection?.next()

                        while (message != null) {

                            when (val payload = message.payload) {

                                is Heartbeat -> {
                                    TelemetryState.lastHeartbeatTime = System.currentTimeMillis()
                                    TelemetryState.connected.value = true

                                    val baseModeValue = payload.baseMode().value()
                                    val isArmed = (baseModeValue and 128) != 0
                                    TelemetryState.armed.value = isArmed

                                    Log.d("MavlinkService", "HEARTBEAT received")
                                }

                                is SysStatus -> {
                                    TelemetryState.battery.value =
                                        (payload.batteryRemaining()).toInt()
                                }

                                is GlobalPositionInt -> {
                                    TelemetryState.altitude.value =
                                        payload.relativeAlt() / 1000.0
                                }

                                is Attitude -> {
                                    val roll = payload.roll()
                                    val pitch = payload.pitch()
                                    val yaw = payload.yaw()
                                    Log.d("MavlinkService", "Attitude R:$roll P:$pitch Y:$yaw")
                                }
                            }

                            message = mavConnection?.next()
                        }

                    } catch (e: EOFException) {
                        // NORMAL for UDP — just means end of packet
                    }
                }

            } catch (e: Exception) {
                Log.e("MavlinkService", "Fatal Error: ${e.message}", e)
            }
        }
    }

    private fun createOutputStream(): OutputStream {
        return object : OutputStream() {
            override fun write(b: Int) {
                val address = droneAddress ?: return
                val packet = DatagramPacket(
                    byteArrayOf(b.toByte()),
                    1,
                    address.address,
                    address.port
                )
                socket?.send(packet)
            }
        }
    }

    fun arm() {
        val command = CommandLong.builder()
            .command(MavCmd.MAV_CMD_COMPONENT_ARM_DISARM)
            .param1(1f)
            .targetSystem(0)
            .targetComponent(0)
            .build()

        mavConnection?.send1(255, 0, command)
        Log.d("MavlinkService", "ARM command sent (broadcast)")
    }

    fun disarm() {
        val command = CommandLong.builder()
            .command(MavCmd.MAV_CMD_COMPONENT_ARM_DISARM)
            .param1(0f)
            .targetSystem(0)
            .targetComponent(0)
            .build()

        mavConnection?.send1(255, 0, command)
        Log.d("MavlinkService", "DISARM command sent")
    }

    fun stop() {
        running = false
        TelemetryState.connected.value = false
        socket?.close()
        socket = null
    }
}
