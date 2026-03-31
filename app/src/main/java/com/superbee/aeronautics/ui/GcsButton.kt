package com.superbee.aeronautics.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image

@Composable
fun GcsButton(
    iconRes: Int,
    label: String,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    val bgColor = if (isActive) Color(0xFF374151) else Color(0xFF1E293B)
    val iconColor = if (isActive) Color(0xFFFF9800) else Color.White

    Box(
        modifier = Modifier
            .size(56.dp)
            .background(Color(0xFF1E293B))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(22.dp),
                colorFilter = ColorFilter.tint(Color.White)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = label,
                color = iconColor,
                fontSize = 10.sp
            )
        }
    }
}