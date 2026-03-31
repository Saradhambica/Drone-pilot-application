package com.superbee.aeronautics

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.superbee.aeronautics.databinding.ActivityMissionBinding
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import org.json.JSONObject
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon
import java.io.File
import java.io.FileOutputStream
import android.view.ViewGroup
import android.view.WindowManager
import android.preference.PreferenceManager
import com.superbee.aeronautics.gcs.mavlink.MavlinkService

data class Mission(
    val farmerId: String = "",
    val crop: String = "",
    val acres: String = "",
    val soilType: String = "",
    val droneUIN: String = "",
    val altitude: Double = 0.0,
    val speed: Double = 0.0,
    val polygon: List<Map<String, Double>> = emptyList(),
    val waypoints: List<Map<String, Double>> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

class MissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMissionBinding
    private lateinit var db: FirebaseFirestore
    private lateinit var mapView: MapView

    private val boundaryPoints = mutableListOf<GeoPoint>()
    private val waypoints = mutableListOf<GeoPoint>()
    private val boundaryMarkers = mutableListOf<Marker>()
    private val waypointMarkers = mutableListOf<Marker>()

    private val mavlinkService = MavlinkService()

    private var boundaryPolygon: Polygon? = null
    private var mainMarker: Marker? = null

    private enum class Mode { BOUNDARY, WAYPOINT }
    private var currentMode = Mode.BOUNDARY
    private var isFullscreen = false
    private var originalMapHeight = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        mavlinkService.start()

        // Initialize Map
        Configuration.getInstance().load(
            applicationContext,
            PreferenceManager.getDefaultSharedPreferences(applicationContext)
        )
        // Initialize Map
        Configuration.getInstance().load(this, getSharedPreferences("osmdroid", MODE_PRIVATE))
        mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Initial location
        val lat = binding.etLatitude.text.toString().toDoubleOrNull() ?: 20.5937
        val lng = binding.etLongitude.text.toString().toDoubleOrNull() ?: 78.9629
        val initialLocation = GeoPoint(lat, lng)
        mapView.controller.setZoom(16.0)
        mapView.controller.setCenter(initialLocation)
        addInitialMarker(initialLocation)

        // Buttons
        binding.btnGoToLocation.setOnClickListener {
            val latInput = binding.etLatitude.text.toString().toDoubleOrNull()
            val lngInput = binding.etLongitude.text.toString().toDoubleOrNull()
            if (latInput != null && lngInput != null) {
                val loc = GeoPoint(latInput, lngInput)
                mapView.controller.setCenter(loc)
                mapView.controller.setZoom(16.0)
                addInitialMarker(loc)
            } else {
                Toast.makeText(this, "Enter valid latitude/longitude", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBoundaryMode.setOnClickListener { currentMode = Mode.BOUNDARY }
        binding.btnWaypointMode.setOnClickListener { currentMode = Mode.WAYPOINT }
        binding.btnUndo.setOnClickListener { undoLastPoint() }
        binding.btnClear.setOnClickListener { clearAllPoints() }

        // Save original map height
        binding.mapView.post {
            originalMapHeight = binding.mapView.height
        }

        // Fullscreen toggle
        binding.btnFullscreenMap.setOnClickListener { toggleFullscreen() }

        // 3-Dots Menu Button
        binding.btnMoreOptions.setOnClickListener { view ->
            val popup = PopupMenu(this, view)
            popup.menuInflater.inflate(R.menu.map_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_standard -> mapView.setTileSource(TileSourceFactory.MAPNIK)
                    R.id.action_terrain -> {
                        val terrain = XYTileSource(
                            "Stamen Terrain",
                            0, 20, 256, ".png",
                            arrayOf("https://tile.stamen.com/terrain/")
                        )
                        mapView.setTileSource(terrain)
                    }
                    R.id.action_satellite -> {
                        val satellite = XYTileSource(
                            "Stamen Toner",
                            0, 20, 256, ".png",
                            arrayOf("https://tile.stamen.com/toner/")
                        )
                        mapView.setTileSource(satellite)
                    }
                }
                true
            }
            popup.show()
        }

        // Map touch for adding points
        mapView.setOnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_UP) {
                val geo = mapView.projection.fromPixels(event.x.toInt(), event.y.toInt()) as GeoPoint
                if (currentMode == Mode.BOUNDARY) {
                    boundaryPoints.add(geo)
                    addBoundaryMarker(geo)
                    redrawPolygon()
                } else {
                    waypoints.add(geo)
                    addWaypointMarker(geo, waypoints.size)
                }
            }
            false
        }

        // Create mission
        binding.btnCreateMission.setOnClickListener {
            if (boundaryPoints.size < 3) {
                Toast.makeText(this, "Draw at least 3 boundary points", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            downloadMission()
        }
    }

    // ----------------- Marker & Polygon functions -----------------
    private fun addInitialMarker(point: GeoPoint) {
        if (mainMarker == null) {
            mainMarker = Marker(mapView).apply {
                position = point
                title = "Target Location"
                icon = ContextCompat.getDrawable(this@MissionActivity, R.drawable.marker_green)
            }
            mapView.overlays.add(mainMarker)
        } else {
            mainMarker!!.position = point
        }
        mapView.invalidate()
    }

    private fun addBoundaryMarker(point: GeoPoint) {
        val marker = Marker(mapView).apply {
            position = point
            icon = ContextCompat.getDrawable(this@MissionActivity, R.drawable.marker_orange)
        }
        mapView.overlays.add(marker)
        boundaryMarkers.add(marker)
        mapView.invalidate()
    }

    private fun addWaypointMarker(point: GeoPoint, number: Int) {
        val marker = Marker(mapView).apply {
            position = point
            icon = createNumberedMarker(number)
        }
        mapView.overlays.add(marker)
        waypointMarkers.add(marker)
        mapView.invalidate()
    }

    private fun createNumberedMarker(number: Int): BitmapDrawable {
        val size = 40
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = android.graphics.Paint().apply { color = android.graphics.Color.BLUE }
        canvas.drawCircle(size / 2f, size / 2f, size / 2f, paint)
        paint.color = android.graphics.Color.WHITE
        paint.textSize = 20f
        paint.textAlign = android.graphics.Paint.Align.CENTER
        canvas.drawText(number.toString(), size / 2f, size / 2f + 7f, paint)
        return BitmapDrawable(resources, bmp)
    }

    private fun redrawPolygon() {
        boundaryPolygon?.let { mapView.overlays.remove(it) }
        boundaryPolygon = Polygon(mapView).apply {
            points = boundaryPoints
            fillColor = 0x30FF8800
            strokeColor = 0xFFFF8800.toInt()
            strokeWidth = 3f
        }
        mapView.overlays.add(boundaryPolygon)
        mapView.invalidate()
    }

    private fun undoLastPoint() {
        if (currentMode == Mode.BOUNDARY && boundaryPoints.isNotEmpty()) {
            boundaryPoints.removeAt(boundaryPoints.lastIndex)
            mapView.overlays.remove(boundaryMarkers.removeAt(boundaryMarkers.lastIndex))
            redrawPolygon()
        } else if (currentMode == Mode.WAYPOINT && waypoints.isNotEmpty()) {
            waypoints.removeAt(waypoints.lastIndex)
            mapView.overlays.remove(waypointMarkers.removeAt(waypointMarkers.lastIndex))
        }
        mapView.invalidate()
    }

    private fun clearAllPoints() {
        boundaryPoints.clear()
        waypoints.clear()
        boundaryMarkers.forEach { mapView.overlays.remove(it) }
        waypointMarkers.forEach { mapView.overlays.remove(it) }
        boundaryMarkers.clear()
        waypointMarkers.clear()
        boundaryPolygon?.let { mapView.overlays.remove(it) }
        boundaryPolygon = null
        mapView.invalidate()
    }

    private fun downloadMission() {
        try {
            val missionJson = JSONObject().apply {
                put("crop", binding.etCrop.text.toString())
                put("acres", binding.etAcres.text.toString())
                put("soilType", binding.etSoilType.text.toString())
                put("droneUIN", binding.etDroneUIN.text.toString())
                put("altitude", binding.etAltitude.text.toString().toDoubleOrNull() ?: 0.0)
                put("speed", binding.etSpeed.text.toString().toDoubleOrNull() ?: 0.0)
                val polygonArray = JSONArray().apply {
                    boundaryPoints.forEach { put(JSONObject().apply { put("lat", it.latitude); put("lng", it.longitude) }) }
                }
                put("polygon", polygonArray)
                val waypointsArray = JSONArray().apply {
                    waypoints.forEach { put(JSONObject().apply { put("lat", it.latitude); put("lng", it.longitude) }) }
                }
                put("waypoints", waypointsArray)
            }

            val file = File(getExternalFilesDir(null), "Mission_${System.currentTimeMillis()}.json")
            FileOutputStream(file).use { it.write(missionJson.toString().toByteArray()) }
            Toast.makeText(this, "Mission saved to ${file.absolutePath}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this, "Error saving mission: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // ----------------- Fullscreen toggle -----------------
    private fun toggleFullscreen() {
        val params = mapView.layoutParams
        if (!isFullscreen) {
            // Expand
            originalMapHeight = mapView.height
            params.height = ViewGroup.LayoutParams.MATCH_PARENT
            mapView.layoutParams = params
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            // Shrink
            params.height = originalMapHeight
            mapView.layoutParams = params
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }
        isFullscreen = !isFullscreen
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }
}
