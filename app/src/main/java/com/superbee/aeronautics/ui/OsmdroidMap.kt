package com.superbee.aeronautics.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.superbee.aeronautics.R
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.*
import java.util.concurrent.CopyOnWriteArrayList
import org.osmdroid.views.overlay.Marker
import com.superbee.aeronautics.gcs.mavlink.TelemetryState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState

@Composable
fun OsmdroidMap(
    modifier: Modifier = Modifier,
    context: Context,
    isWaypointMode: Boolean
) {

    // Get telemetry values (this triggers recomposition)
    val lat = TelemetryState.latitude.value
    val lon = TelemetryState.longitude.value
    val heading = TelemetryState.heading.value
    val currentWaypointMode by rememberUpdatedState(isWaypointMode)


    AndroidView(
        modifier = modifier,

        factory = {

            /* ---------- CONFIG ---------- */
            Configuration.getInstance().userAgentValue = context.packageName
            Configuration.getInstance().tileFileSystemCacheMaxBytes =
                1024L * 1024L * 1024L

            val mapView = MapView(context)

            /* ---------- MAP SETUP ---------- */
            mapView.setTileSource(TileSourceFactory.MAPNIK)
            mapView.setMultiTouchControls(true)
            mapView.minZoomLevel = 3.0
            mapView.maxZoomLevel = 20.0
            mapView.controller.setZoom(17.0)
            mapView.controller.setCenter(GeoPoint(17.3850, 78.4867))

            /* ---------- DATA ---------- */
            val boundaryPoints = CopyOnWriteArrayList<GeoPoint>()
            var boundaryPolygon: Polygon? = null

            val pathLine = Polyline()
            pathLine.outlinePaint.color = android.graphics.Color.YELLOW
            pathLine.outlinePaint.strokeWidth = 5f

            mapView.overlays.add(pathLine)

            /* ---------- TAP HANDLER ---------- */
            val mapEventsOverlay = MapEventsOverlay(object : MapEventsReceiver {

                override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                    p ?: return false

                    if (!currentWaypointMode) return false

                    // waypoint marker
                    val waypoint = Marker(mapView).apply {
                        position = p
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        title = "WP ${boundaryPoints.size + 1}"
                    }
                    mapView.overlays.add(waypoint)

                    boundaryPoints.add(p)

                    // 🔥 DRAW LINE
                    pathLine.setPoints(boundaryPoints.toList())

                    // polygon
                    if (boundaryPoints.size >= 3) {
                        boundaryPolygon?.let { mapView.overlays.remove(it) }

                        boundaryPolygon = Polygon().apply {
                            points = boundaryPoints.toList()
                            fillColor = 0x3300FF00
                            strokeColor = 0xFF00FF00.toInt()
                            strokeWidth = 3f
                        }
                        mapView.overlays.add(boundaryPolygon)
                    }

                    mapView.invalidate()
                    return true
                }

                override fun longPressHelper(p: GeoPoint?): Boolean = false
            })

            mapView.overlays.add(mapEventsOverlay)

            /* ---------- DRONE MARKER ---------- */
            val droneMarker = Marker(mapView).apply {
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)

                // 🔥 SCALE ICON HERE
                val drawable = context.getDrawable(R.drawable.pointer)
                val scaledIcon = drawable?.let {
                    val bitmap = (it as android.graphics.drawable.BitmapDrawable).bitmap
                    android.graphics.drawable.BitmapDrawable(
                        context.resources,
                        android.graphics.Bitmap.createScaledBitmap(bitmap, 70, 70, false)
                    )
                }

                icon = scaledIcon
            }


            mapView.overlays.add(droneMarker)

            // store reference
            mapView.tag = droneMarker

            mapView
        },

        update = { mapView ->

            val droneMarker = mapView.tag as? Marker




            if (droneMarker != null && lat != 0.0 && lon != 0.0) {

                val position = GeoPoint(lat, lon)

                droneMarker.position = position
                droneMarker.rotation = heading

                mapView.controller.setCenter(position)
                mapView.invalidate()
            }
        }
    )
}