package com.superbee.aeronautics

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.superbee.aeronautics.gcs.mavlink.MavlinkService
import com.superbee.aeronautics.gcs.mavlink.TelemetryState
import com.superbee.aeronautics.ui.GcsSidebar
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.ColorFilter
import com.superbee.aeronautics.ui.OsmdroidMap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview


class DroneDashboardActivity : ComponentActivity() {

    private val mavlinkService = MavlinkService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mavlinkService.start()

        setContent {
            MaterialTheme {
                DroneDashboardScreen()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mavlinkService.stop()
    }
}

@Preview(showBackground = true)
@Composable
fun DroneDashboardScreen() {

    val battery = TelemetryState.battery.value
    val altitude = TelemetryState.altitude.value
    val connected = TelemetryState.connected.value
    val armed = TelemetryState.armed.value
    var isWaypointMode by remember { mutableStateOf(false) }
    var isPaused by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize()) {

        /* ================= TOOLBAR ================= */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF16324F))
                .statusBarsPadding()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier.offset(y = (-4).dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                     //logo
                    Image(
                        painter = painterResource(id = R.drawable.sagcs_logo),
                        contentDescription = "SAGCS Logo",
                        modifier = Modifier.size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        if (connected) "● CONNECTED" else "● DISCONNECTED",
                        color = if (connected) Color.Green else Color.Red,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.battery),
                            contentDescription = "Battery",
                            modifier = Modifier.size(16.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("$battery%", color = Color.White, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.alt),
                            contentDescription = "Altitude",
                            modifier = Modifier.size(16.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Alt: %.1f m".format(altitude), color = Color.White, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(R.drawable.speed),
                            contentDescription = "Speed",
                            modifier = Modifier.size(16.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Speed: --", color = Color.White, fontSize = 14.sp)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        if (armed) "ARMED" else "DISARMED",
                        color = if (armed) Color.Red else Color.Gray,
                        fontSize = 14.sp
                    )
                }
            }
        }

        /* ================= MAIN SCREEN ================= */
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkGray)
        ) {

            // 🗺 MAP (bottom layer)
            OsmdroidMap(
                modifier = Modifier.fillMaxSize(),
                context = LocalContext.current,
                isWaypointMode = isWaypointMode
            )

            // FLOATING SIDEBAR (on top of map)
            GcsSidebar(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 8.dp),

                isPaused = isPaused,
                isWaypointMode = isWaypointMode,

                onRtl = {
                    // TODO: RTL logic
                },

                onPauseToggle = {
                    isPaused = !isPaused
                },

                onWaypointToggle = {
                    isWaypointMode = !isWaypointMode
                }
            )
        }
    }
}
