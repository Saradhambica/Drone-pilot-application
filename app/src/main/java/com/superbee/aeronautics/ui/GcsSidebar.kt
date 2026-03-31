package com.superbee.aeronautics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.superbee.aeronautics.R



@Composable
fun GcsSidebar(
    modifier: Modifier = Modifier,
    isPaused: Boolean,
    isWaypointMode: Boolean,
    onRtl: () -> Unit,
    onPauseToggle: () -> Unit,
    onWaypointToggle: () -> Unit
) {
    Column(
        modifier = modifier
            .width(72.dp)
            .fillMaxHeight()
            .background(Color(0x99000000)),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // RTL
        GcsButton(
            iconRes = R.drawable.rtl,
            label = "RTL",
            onClick = onRtl
        )

        // PAUSE / RESUME
        GcsButton(
            iconRes = if (isPaused) R.drawable.resume_button else R.drawable.pause_button,
            label = if (isPaused) "RESUME" else "PAUSE",
            isActive = isPaused,
            onClick = onPauseToggle
        )

        // WAYPOINT MODE
        GcsButton(
            iconRes = R.drawable.waypoint,
            label = "WP",
            isActive = isWaypointMode,
            onClick = onWaypointToggle
        )
    }
}